/* animal.js — perfil: busca o animal na API usando o parâmetro ?id= da URL. */
const area = document.getElementById("perfil");
const id = new URLSearchParams(location.search).get("id");

function montarPerfil(animal) {
  document.title = `${animal.nome} — Lar de Patas`;
  area.innerHTML = `
    <nav aria-label="breadcrumb"><ol class="breadcrumb">
      <li class="breadcrumb-item"><a href="animais.html">Animais</a></li>
      <li class="breadcrumb-item active" aria-current="page">${esc(animal.nome)}</li>
    </ol></nav>
    <div class="row g-4">
      <div class="col-lg-6">
        <div class="foto-animal perfil-foto tom-${animal.id % 4}" role="img" aria-label="Foto ilustrativa de ${esc(animal.nome)}">${animal.emoji}</div>
      </div>
      <div class="col-lg-6">
        <h1 class="display-5">${esc(animal.nome)}</h1>
        <p class="text-muted">${esc(animal.cidade)}</p>
        <p class="fs-5">${esc(animal.descricao)}</p>
        <dl class="row ficha">
          <dt class="col-5">Espécie</dt><dd class="col-7">${esc(animal.especie)}</dd>
          <dt class="col-5">Idade</dt><dd class="col-7">${esc(animal.idade)}</dd>
          <dt class="col-5">Porte</dt><dd class="col-7">${esc(animal.porte)}</dd>
          <dt class="col-5">Sexo</dt><dd class="col-7">${esc(animal.sexo)}</dd>
          <dt class="col-5">Temperamento</dt><dd class="col-7">${esc(animal.temperamento)}</dd>
          <dt class="col-5">Saúde</dt><dd class="col-7">${esc(animal.saude)}</dd>
        </dl>
        <!-- Informações sobre adoção (some quando o animal já foi adotado) -->
        ${animal.status === "ADOTADO" ? `
        <div class="alert alert-secondary mb-0">
          <strong>${esc(animal.nome)} já encontrou um lar.</strong>
          Veja outros animais que ainda esperam por uma família.
          <div class="mt-2"><a class="btn btn-mar btn-sm" href="animais.html">Ver animais disponíveis</a></div>
        </div>` : `
        <div class="caixa-adocao">
          <h2 class="h5">Como adotar ${esc(animal.nome)}</h2>
          <p class="mb-2">Responsável: <strong>${esc(animal.responsavel)}</strong></p>
          <ul class="mb-3">
            <li>Envie o formulário de interesse.</li>
            <li>O responsável analisa os pedidos e entra em contato para combinar uma visita.</li>
            <li>A adoção é feita com assinatura de termo de responsabilidade.</li>
          </ul>
          <a class="btn btn-sol btn-lg" href="interesse.html?id=${animal.id}">Tenho interesse em adotar</a>
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
    if (e.status === 404 || e.status === 400) montarMensagem("Animal não encontrado", "Ele pode ter sido adotado ou o endereço está incorreto.");
    else montarMensagem("Não foi possível carregar o perfil", mensagemDeErro(e));
  }
})();
