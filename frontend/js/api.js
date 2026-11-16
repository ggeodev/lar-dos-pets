/* ==========================================================
   api.js — comunicação com o backend Spring Boot e sessão de login
   Todas as chamadas à API ficam aqui. Para trocar o endereço
   do servidor (ex.: ao publicar o site), altere só API_PRODUCAO.
   ========================================================== */

// Local (localhost ou arquivo aberto direto) usa a API na sua máquina;
// no GitHub Pages usa a API publicada. Troque API_PRODUCAO pelo endereço do seu backend.
const EM_DESENVOLVIMENTO = ["localhost", "127.0.0.1", ""].includes(location.hostname);
const API_PRODUCAO = "https://SEU-BACKEND.onrender.com/api";
const API_URL = EM_DESENVOLVIMENTO ? "http://localhost:8080/api" : API_PRODUCAO;

/* ---------- Tradução entre a API (enums) e o que o usuário vê ---------- */
const ROTULOS = {
  especie: { CACHORRO: "Cachorro", GATO: "Gato", COELHO: "Coelho", OUTRA: "Outra" },
  porte:   { PEQUENO: "Pequeno", MEDIO: "Médio", GRANDE: "Grande" },
  sexo:    { MACHO: "Macho", FEMEA: "Fêmea" },
  moradia: { CASA_COM_QUINTAL: "Casa com quintal", CASA_SEM_QUINTAL: "Casa sem quintal", APARTAMENTO: "Apartamento" }
};
const EMOJIS_ESPECIE = { CACHORRO: "🐕", GATO: "🐈", COELHO: "🐇", OUTRA: "🐾" };

/** "Médio" -> "MEDIO", "Casa com quintal" -> "CASA_COM_QUINTAL" (formato esperado pela API) */
function paraEnum(rotulo) {
  return rotulo.normalize("NFD").replace(/[\u0300-\u036f]/g, "").trim().toUpperCase().replace(/\s+/g, "_");
}

/** Escapa texto antes de colocar em innerHTML. Os dados vêm de usuários: nunca confie neles. */
function esc(texto) {
  return String(texto ?? "").replace(/[&<>"']/g, c => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
}

/** Converte a resposta da API para o formato usado pelas telas. */
function adaptarAnimal(r) {
  return {
    id: r.id,
    nome: r.nome,
    especieCodigo: r.especie,
    fotoUrl: r.fotoUrl,
    especie: ROTULOS.especie[r.especie] || r.especie,
    porte: ROTULOS.porte[r.porte] || r.porte,
    sexo: ROTULOS.sexo[r.sexo] || r.sexo,
    idade: r.idade,
    cidade: r.cidade,
    emoji: EMOJIS_ESPECIE[r.especie] || "🐾",
    descricao: r.descricao,
    saude: r.saude,
    temperamento: r.temperamento || "Não informado",
    responsavel: r.responsavelNome,
    destaque: r.destaque,
    status: r.status
  };
}

/* ---------- Sessão de login (token guardado no navegador) ---------- */
const CHAVE_SESSAO = "lar_sessao";
function lerSessao() { try { return JSON.parse(localStorage.getItem(CHAVE_SESSAO)); } catch { return null; } }
function salvarSessao(resposta) {
  try { localStorage.setItem(CHAVE_SESSAO, JSON.stringify({ token: resposta.token, usuario: resposta.usuario })); } catch { /* storage bloqueado */ }
}
function sair() { try { localStorage.removeItem(CHAVE_SESSAO); } catch { /* ignora */ } }
function usuarioLogado() { return lerSessao()?.usuario || null; }

/** Páginas restritas chamam isto no início: sem login, vão para a tela de entrar e voltam depois. */
function exigirLogin() {
  if (usuarioLogado()) return true;
  location.replace("entrar.html?voltar=" + encodeURIComponent(location.pathname.split("/").pop()));
  return false;
}
/** Aceita só nomes simples de página (ex.: "painel.html"), evitando redirecionar para sites externos. */
function destinoSeguro(voltar) { return /^[a-z-]+\.html$/.test(voltar || "") ? voltar : "painel.html"; }

/* ---------- Chamada genérica ---------- */
class ErroApi extends Error {
  constructor(status, mensagem, erros) { super(mensagem); this.status = status; this.erros = erros || null; }
}

async function chamarApi(caminho, { metodo = "GET", corpo, autenticado = false } = {}) {
  const opcoes = { method: metodo, headers: {} };
  if (corpo !== undefined) {              // só enviamos Content-Type quando há corpo (evita preflight em GET)
    opcoes.headers["Content-Type"] = "application/json";
    opcoes.body = JSON.stringify(corpo);
  }
  if (autenticado) {
    const token = lerSessao()?.token;
    if (!token) throw new ErroApi(401, "Você precisa entrar para continuar.");
    opcoes.headers.Authorization = "Bearer " + token;
  }
  let resposta;
  try {
    resposta = await fetch(API_URL + caminho, opcoes);
  } catch {
    throw new ErroApi(0, "Não foi possível conectar ao servidor. Se ele estiver em modo de espera, aguarde cerca de 1 minuto e recarregue a página.");
  }
  if (resposta.status === 401 && autenticado) {   // token vencido ou inválido
    sair();
    throw new ErroApi(401, "Sua sessão expirou. Entre novamente.");
  }
  const dados = await resposta.json().catch(() => null);
  if (!resposta.ok) throw new ErroApi(resposta.status, dados?.detail || "Erro inesperado no servidor.", dados?.erros);
  return dados;
}

/* ---------- Operações públicas ---------- */
async function listarAnimais(filtros = {}) {
  const params = new URLSearchParams();
  Object.entries(filtros).forEach(([k, v]) => { if (v !== undefined && v !== "") params.set(k, v); });
  const consulta = params.toString() ? "?" + params : "";
  return (await chamarApi("/animais" + consulta)).map(adaptarAnimal);
}
async function buscarAnimal(id)       { return adaptarAnimal(await chamarApi(`/animais/${encodeURIComponent(id)}`)); }
async function listarCidades()        { return chamarApi("/animais/cidades"); }
async function estatisticas()         { return chamarApi("/animais/estatisticas"); }
async function enviarInteresse(dados) { return chamarApi("/interesses", { metodo: "POST", corpo: dados }); }

/* ---------- Conta ---------- */
async function criarConta(dados)     { return chamarApi("/auth/registro", { metodo: "POST", corpo: dados }); }
async function entrarNaConta(dados)  { return chamarApi("/auth/login", { metodo: "POST", corpo: dados }); }

/* ---------- Operações que exigem login ---------- */
async function cadastrarAnimal(dados) { return adaptarAnimal(await chamarApi("/animais", { metodo: "POST", corpo: dados, autenticado: true })); }
async function meusAnimais()          { return chamarApi("/meus-animais", { autenticado: true }); }
async function interessadosDoAnimal(id) { return chamarApi(`/meus-animais/${id}/interesses`, { autenticado: true }); }
async function alterarStatusAnimal(id, status) {
  return chamarApi(`/meus-animais/${id}/status`, { metodo: "PATCH", corpo: { status }, autenticado: true });
}

/* ---------- Mensagens de erro e carregamento ---------- */
function mensagemDeErro(e) {
  if (e instanceof ErroApi && e.erros) {
    return "Corrija os campos: " + Object.entries(e.erros).map(([campo, msg]) => `${campo} (${msg})`).join("; ");
  }
  return e instanceof ErroApi ? e.message : "Ocorreu um erro inesperado.";
}
/** Mostra o erro dentro de uma lista/área da página. */
function exibirErro(container, e) {
  container.innerHTML = `<div class="col-12"><div class="alert alert-danger" role="alert">${esc(mensagemDeErro(e))}</div></div>`;
}
/** Mostra o erro no alerta de um formulário. */
function exibirErroForm(alerta, e) { alerta.textContent = mensagemDeErro(e); alerta.classList.remove("d-none"); }
const CARREGANDO = `<div class="col-12 text-center py-5"><div class="spinner-border text-secondary" role="status"><span class="visually-hidden">Carregando…</span></div></div>`;
