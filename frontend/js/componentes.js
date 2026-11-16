/* ==========================================================
   componentes.js — peças reutilizáveis em todas as páginas
   Cada <body> define data-base: "" (index) ou "../" (pages/)
   e data-pagina para marcar o item ativo do menu.
   ========================================================== */
const BASE = document.body.dataset.base || "";
const PAGINA = document.body.dataset.pagina || "";

/** Patinha usada na logo (herda a cor do texto). */
const PATA_SVG = `<svg viewBox="0 0 64 64" width="20" height="20" fill="currentColor" aria-hidden="true"><ellipse cx="32" cy="42" rx="13" ry="10.5"/><circle cx="16.5" cy="29" r="5.5"/><circle cx="26.5" cy="19.5" r="5.5"/><circle cx="37.5" cy="19.5" r="5.5"/><circle cx="47.5" cy="29" r="5.5"/></svg>`;

/* Menu superior: muda conforme a pessoa esteja logada ou não */
function renderNavbar() {
  const usuario = usuarioLogado();
  const item = (chave, href, rotulo) =>
    `<li class="nav-item"><a class="nav-link ${PAGINA === chave ? "active" : ""}" href="${BASE}${href}">${rotulo}</a></li>`;
  const comuns = item("inicio", "index.html", "Início") + item("animais", "pages/animais.html", "Animais");
  const links = usuario
    ? comuns
      + item("painel", "pages/painel.html", "Meus animais")
      + item("cadastro", "pages/cadastro.html", "Cadastrar animal")
      + `<li class="nav-item"><button id="btn-sair" type="button" class="btn btn-link nav-link">Sair (${esc(usuario.nome.split(" ")[0])})</button></li>`
    : comuns
      + item("entrar", "pages/entrar.html", "Entrar")
      + `<li class="nav-item ms-lg-2"><a class="btn btn-mar btn-sm" href="${BASE}pages/criar-conta.html">Criar conta</a></li>`;

  document.getElementById("navbar").innerHTML = `
    <a class="pular-conteudo" href="#conteudo">Pular para o conteúdo</a>
    <nav class="navbar navbar-expand-lg navbar-adocao">
      <div class="container">
        <a class="navbar-brand" href="${BASE}index.html"><span class="logo-marca">${PATA_SVG}</span>Lar de Patas</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menu" aria-controls="menu" aria-expanded="false" aria-label="Abrir menu">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="menu">
          <ul class="navbar-nav ms-auto align-items-lg-center gap-lg-1">${links}</ul>
        </div>
      </div>
    </nav>`;

  const principal = document.querySelector("main");   // alvo do link "pular para o conteúdo"
  if (principal && !principal.id) principal.id = "conteudo";

  document.getElementById("btn-sair")?.addEventListener("click", () => {
    sair();
    location.href = BASE + "index.html";
  });
}

/* Rodapé */
function renderRodape() {
  document.getElementById("rodape").innerHTML = `
    <footer class="rodape">
      <div class="container">
        <div class="row g-4">
          <div class="col-lg-5">
            <p class="d-flex align-items-center gap-2 mb-2 text-white fs-5"><span class="logo-marca">${PATA_SVG}</span><strong>Lar de Patas</strong></p>
            <p class="mb-0">Plataforma de adoção responsável. Aproximamos animais que precisam de um lar de pessoas dispostas a cuidar deles para sempre.</p>
          </div>
          <div class="col-6 col-lg-3">
            <h2>Para quem quer adotar</h2>
            <ul>
              <li><a href="${BASE}pages/animais.html">Ver animais</a></li>
              <li><a href="${BASE}index.html#antes-de-adotar">Antes de adotar</a></li>
              <li><a href="${BASE}index.html#duvidas">Dúvidas frequentes</a></li>
            </ul>
          </div>
          <div class="col-6 col-lg-4">
            <h2>Para responsáveis</h2>
            <ul>
              <li><a href="${BASE}pages/criar-conta.html">Criar conta</a></li>
              <li><a href="${BASE}pages/entrar.html">Entrar</a></li>
              <li><a href="${BASE}pages/cadastro.html">Cadastrar um animal</a></li>
            </ul>
          </div>
        </div>
        <hr class="my-4">
        <p class="small mb-0">Projeto acadêmico de Ciência da Computação, feito com HTML5, CSS3, Bootstrap, Java e PostgreSQL.</p>
      </div>
    </footer>`;
}

/* Cartão de animal (home e catálogo). `colunas` define a largura em cada tela.
   Todo texto vindo da API passa por esc() para evitar injeção de HTML. */
function criarCardAnimal(a, colunas = "col-sm-6 col-xl-4") {
  const iconeSexo = a.sexo === "Fêmea" ? "bi-gender-female" : "bi-gender-male";
  return `
    <div class="${colunas}">
      <article class="card card-animal">
        ${fotoAnimal(a)}
        <div class="card-body">
          <div class="d-flex justify-content-between align-items-start gap-2">
            <h3 class="card-title h5 mb-1"><a href="${BASE}pages/animal.html?id=${a.id}" class="stretched-link">${esc(a.nome)}</a></h3>
            <i class="bi ${iconeSexo} icone-sexo" role="img" aria-label="${esc(a.sexo)}" title="${esc(a.sexo)}"></i>
          </div>
          <p class="local"><i class="bi bi-geo-alt-fill"></i> ${esc(a.cidade)}</p>
          <div class="d-flex flex-wrap gap-1">
            <span class="etiqueta">${esc(a.especie)}</span>
            <span class="etiqueta">${esc(a.porte)}</span>
            <span class="etiqueta">${esc(a.idade)}</span>
          </div>
        </div>
      </article>
    </div>`;
}

renderNavbar();
renderRodape();
