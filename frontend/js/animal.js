/* animal.js — perfil: busca o animal na API usando o parâmetro ?id= da URL. */
const area = document.getElementById("perfil");
const id = new URLSearchParams(location.search).get("id");

function montarPerfil(a) {
  document.title = `${a.nome} — Lar de Patas`;
  const adotado = a.status === "ADOTADO";
  const fato = (icone, rotulo, valor) =>
    `<div class="col-6"><div class="fato"><i class="bi ${icone}"></i><div><span>${rotulo}</span><strong>${esc(valor)}</strong></div></div></div>`;

  area.innerHTML = `
    <nav aria-label="breadcrumb"><ol class="breadcrumb">
      <li class="breadcrumb-item"><a href="animais.html">Animais</a></li>
      <li class="breadcrumb-item active" aria-current="page">${esc(a.nome)}</li>
    </ol></nav>
    <div class="row g-4 g-lg-5">
      <div class="col-lg-6">${fotoAnimal(a, "perfil-foto")}</div>
      <div class="col-lg-6">
        <div class="d-flex flex-wrap align-items-center gap-3 mb-1">
          <h1 class="mb-0">${esc(a.nome)}</h1>
          <span class="badge ${adotado ? "text-bg-secondary" : "text-bg-success"}">${adotado ? "Adotado" : "Disponível"}</span>
        </div>
        <p class="local fs-6"><i class="bi bi-geo-alt-fill"></i> ${esc(a.cidade)}</p>
        <p class="fs-5 texto-livre limite">${esc(a.descricao)}</p>

        <div class="row g-2 mb-4">
          ${fato("bi-tag", "Espécie", a.especie)}
          ${fato("bi-calendar3", "Idade", a.idade)}
          ${fato("bi-rulers", "Porte", a.porte)}
          ${fato(a.sexo === "Fêmea" ? "bi-gender-female" : "bi-gender-male", "Sexo", a.sexo)}
        </div>

        <div class="bloco-info"><h2>Temperamento</h2><p class="mb-0">${esc(a.temperamento)}</p></div>
        <div class="bloco-info mb-4"><h2>Saúde</h2><p class="mb-0">${esc(a.saude)}</p></div>

        <!-- Informações sobre adoção (some quando o animal já foi adotado) -->
        ${adotado ? `
        <div class="alert alert-secondary mb-0">
          <strong>${esc(a.nome)} já encontrou um lar.</strong> Veja outros animais que ainda esperam por uma família.
          <div class="mt-2"><a class="btn btn-mar btn-sm" href="animais.html">Ver animais disponíveis</a></div>
        </div>` : `
        <div class="caixa-adocao">
          <h2 class="h5">Quer adotar ${esc(a.nome)}?</h2>
          <p class="mb-2">Quem cuida dele: <strong>${esc(a.responsavel)}</strong></p>
          <ul class="mb-3">
            <li>Envie o formulário de interesse.</li>
            <li>O responsável analisa os pedidos e entra em contato para combinar uma visita.</li>
            <li>A adoção é feita com assinatura de termo de responsabilidade.</li>
          </ul>
          <a class="btn btn-sol btn-lg" href="interesse.html?id=${a.id}">Quero adotar ${esc(a.nome)}</a>
        </div>`}
      </div>
    </div>`;
}

function montarMensagem(titulo, texto) {
  area.innerHTML = `
    <div class="text-center py-5">
      <h1 class="h3">${esc(titulo)}</h1>
      <p class="text-muted">${esc(texto)}</p>
      <a class="btn btn-mar" href="animais.html">Ver todos os animais</a>
    </div>`;
}

(async () => {
  if (!id) return montarMensagem("Animal não encontrado", "Escolha um animal no catálogo para ver o perfil.");
  area.innerHTML = CARREGANDO;
  try {
    montarPerfil(await buscarAnimal(id));
  } catch (e) {
    if (e.status === 404 || e.status === 400) montarMensagem("Animal não encontrado", "Ele pode ter sido removido ou o endereço está incorreto.");
    else montarMensagem("Não foi possível carregar o perfil", mensagemDeErro(e));
  }
})();
