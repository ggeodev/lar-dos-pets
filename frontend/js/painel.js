/* painel.js — área do responsável: lista os animais da conta, mostra os interessados
   e permite marcar o animal como adotado (ou reabrir). Exige login. */
if (exigirLogin()) iniciarPainel();

function iniciarPainel() {
  const lista = document.getElementById("lista-painel");
  document.getElementById("saudacao").textContent = `Olá, ${usuarioLogado().nome}!`;

  /** Sessão vencida: volta ao login. Outros erros aparecem na tela. */
  function tratarErro(e) {
    if (e.status === 401) { location.replace("entrar.html?voltar=painel.html"); return; }
    exibirErro(lista, e);
  }

  function cartaoAnimal(a) {
    const adotado = a.status === "ADOTADO";
    const proximo = adotado ? "DISPONIVEL" : "ADOTADO";
    return `
      <div class="col-12">
        <article class="cartao-form" data-nome="${esc(a.nome)}">
          <div class="d-flex flex-wrap justify-content-between align-items-start gap-2">
            <div>
              <h2 class="h5 mb-1">
                <a href="animal.html?id=${a.id}" class="text-decoration-none">${esc(a.nome)}</a>
                <span class="badge ${adotado ? "text-bg-secondary" : "text-bg-success"} ms-1">${adotado ? "Adotado" : "Disponível"}</span>
              </h2>
              <div class="meta-linha mb-0"><span><i class="bi bi-tag"></i> ${esc(ROTULOS.especie[a.especie] || a.especie)}</span><span><i class="bi bi-geo-alt-fill"></i> ${esc(a.cidade)}</span></div>
            </div>
            <div class="d-flex flex-wrap gap-2">
              <button type="button" class="btn btn-mar btn-sm" data-acao="interesses" data-id="${a.id}">Ver interessados (${a.totalInteresses})</button>
              <button type="button" class="btn btn-outline-dark btn-sm" data-acao="status" data-id="${a.id}" data-status="${proximo}">${adotado ? "Reabrir adoção" : "Marcar como adotado"}</button>
            </div>
          </div>
          <div class="mt-3 d-none" id="interesses-${a.id}"></div>
        </article>
      </div>`;
  }

  function cartaoInteresse(i, nomeAnimal) {
    const digitos = i.telefone.replace(/\D/g, "");
    const numero = (digitos.length === 10 || digitos.length === 11) ? "55" + digitos : digitos; // assume Brasil
    const msg = `Olá, ${i.nome}! Recebi seu interesse em adotar ${nomeAnimal} pelo Lar de Patas.`;
    const data = new Date(i.criadoEm).toLocaleDateString("pt-BR");
    return `
      <div class="border rounded-3 p-3 mb-2">
        <div class="d-flex justify-content-between flex-wrap gap-1">
          <strong>${esc(i.nome)}</strong><span class="text-muted small">${data}</span>
        </div>
        <div class="meta-linha"><span><i class="bi bi-geo-alt-fill"></i> ${esc(i.cidade)}</span><span><i class="bi bi-house-heart-fill"></i> ${esc(ROTULOS.moradia[i.moradia] || i.moradia)}</span><span>${i.possuiOutrosAnimais ? "Já tem outros animais" : "Não tem outros animais"}</span></div>
        <p class="mb-2">${esc(i.motivo)}</p>
        <div class="d-flex flex-wrap gap-2">
          <a class="btn btn-sm btn-sol" target="_blank" rel="noopener" href="https://wa.me/${numero}?text=${encodeURIComponent(msg)}">WhatsApp</a>
          <a class="btn btn-sm btn-outline-dark" href="mailto:${esc(i.email)}?subject=${encodeURIComponent("Adoção de " + nomeAnimal)}">E-mail</a>
        </div>
      </div>`;
  }

  async function carregar() {
    lista.innerHTML = CARREGANDO;
    try {
      const animais = await meusAnimais();
      lista.innerHTML = animais.length
        ? animais.map(cartaoAnimal).join("")
        : `<div class="col-12"><div class="cartao-form text-center">
             <p class="mb-3">Você ainda não cadastrou nenhum animal.</p>
             <a class="btn btn-sol" href="cadastro.html">Cadastrar o primeiro animal</a>
           </div></div>`;
    } catch (e) { tratarErro(e); }
  }

  async function alternarInteresses(id, nomeAnimal) {
    const area = document.getElementById("interesses-" + id);
    if (!area.classList.contains("d-none")) { area.classList.add("d-none"); return; }   // fecha
    area.classList.remove("d-none");
    area.textContent = "Carregando…";
    try {
      const itens = await interessadosDoAnimal(id);
      area.innerHTML = itens.length
        ? itens.map(i => cartaoInteresse(i, nomeAnimal)).join("")
        : '<p class="text-muted mb-0">Ninguém demonstrou interesse ainda.</p>';
    } catch (e) {
      if (e.status === 401) return tratarErro(e);
      area.innerHTML = `<div class="alert alert-danger mb-0">${esc(mensagemDeErro(e))}</div>`;
    }
  }

  async function mudarStatus(id, novoStatus, botao, nomeAnimal) {
    const texto = novoStatus === "ADOTADO"
      ? `Marcar ${nomeAnimal} como adotado? Ele deixará de aparecer no catálogo.`
      : `Reabrir a adoção de ${nomeAnimal}? Ele voltará ao catálogo.`;
    if (!confirm(texto)) return;
    botao.disabled = true;
    try {
      await alterarStatusAnimal(id, novoStatus);
      await carregar();
    } catch (e) { botao.disabled = false; tratarErro(e); }
  }

  // Um único listener para todos os botões da lista
  lista.addEventListener("click", ev => {
    const botao = ev.target.closest("button[data-acao]");
    if (!botao) return;
    const nome = botao.closest("article").dataset.nome;
    if (botao.dataset.acao === "interesses") alternarInteresses(botao.dataset.id, nome);
    if (botao.dataset.acao === "status") mudarStatus(botao.dataset.id, botao.dataset.status, botao, nome);
  });

  carregar();
}
