/* ==========================================================
   api.js — comunicação com o backend Spring Boot
   Todas as chamadas à API ficam aqui. Para trocar o endereço
   do servidor (ex.: ao publicar o site), altere só API_URL.
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
  sexo:    { MACHO: "Macho", FEMEA: "Fêmea" }
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
    destaque: r.destaque
  };
}

/* ---------- Chamada genérica ---------- */
class ErroApi extends Error {
  constructor(status, mensagem, erros) { super(mensagem); this.status = status; this.erros = erros || null; }
}

async function chamarApi(caminho, { metodo = "GET", corpo } = {}) {
  const opcoes = { method: metodo };
  if (corpo !== undefined) {              // só enviamos Content-Type quando há corpo (evita preflight em GET)
    opcoes.headers = { "Content-Type": "application/json" };
    opcoes.body = JSON.stringify(corpo);
  }
  let resposta;
  try {
    resposta = await fetch(API_URL + caminho, opcoes);
  } catch {
    throw new ErroApi(0, "Não foi possível conectar ao servidor. Se ele estiver em modo de espera, aguarde cerca de 1 minuto e recarregue a página.");
  }
  const dados = await resposta.json().catch(() => null);
  if (!resposta.ok) throw new ErroApi(resposta.status, dados?.detail || "Erro inesperado no servidor.", dados?.erros);
  return dados;
}

/* ---------- Operações usadas pelas páginas ---------- */
async function listarAnimais(filtros = {}) {
  const params = new URLSearchParams();
  Object.entries(filtros).forEach(([k, v]) => { if (v !== undefined && v !== "") params.set(k, v); });
  const consulta = params.toString() ? "?" + params : "";
  return (await chamarApi("/animais" + consulta)).map(adaptarAnimal);
}
async function buscarAnimal(id)       { return adaptarAnimal(await chamarApi(`/animais/${encodeURIComponent(id)}`)); }
async function listarCidades()        { return chamarApi("/animais/cidades"); }
async function cadastrarAnimal(dados) { return adaptarAnimal(await chamarApi("/animais", { metodo: "POST", corpo: dados })); }
async function enviarInteresse(dados) { return chamarApi("/interesses", { metodo: "POST", corpo: dados }); }

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
