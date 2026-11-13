/* ==========================================================
   componentes.js — peças reutilizáveis em todas as páginas
   Cada <body> define data-base: "" (index) ou "../" (pages/)
   e data-pagina para marcar o item ativo do menu.
   ========================================================== */
const BASE = document.body.dataset.base || "";
const PAGINA = document.body.dataset.pagina || "";

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
      + `<li class="nav-item"><a class="btn btn-sol btn-sm" href="${BASE}pages/criar-conta.html">Criar conta</a></li>`;

  document.getElementById("navbar").innerHTML = `
    <nav class="navbar navbar-expand-lg navbar-dark navbar-adocao">
      <div class="container">
        <a class="navbar-brand" href="${BASE}index.html">🐾 Lar de Patas</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menu" aria-controls="menu" aria-expanded="false" aria-label="Abrir menu">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="menu">
          <ul class="navbar-nav ms-auto align-items-lg-center gap-lg-2">${links}</ul>
        </div>
      </div>
    </nav>`;

  document.getElementById("btn-sair")?.addEventListener("click", () => {
    sair();
    location.href = BASE + "index.html";
  });
}

/* Rodapé */
function renderRodape() {
  document.getElementById("rodape").innerHTML = `
    <footer class="rodape">
      <div class="container d-flex flex-column flex-md-row justify-content-between gap-2">
        <span>🐾 Lar de Patas — projeto acadêmico de Ciência da Computação.</span>
        <span>Adotar é um compromisso para toda a vida do animal.</span>
      </div>
    </footer>`;
}

/* Card de animal (usado na home e no catálogo).
   Todo texto vindo da API passa por esc() para evitar injeção de HTML. */
function criarCardAnimal(a) {
  return `
    <div class="col-sm-6 col-xl-4">
      <article class="card card-animal">
        <div class="foto-animal tom-${a.id % 4}" role="img" aria-label="Foto ilustrativa de ${esc(a.nome)}">${a.emoji}</div>
        <div class="card-body d-flex flex-column">
          <h3 class="card-title h5"><a href="${BASE}pages/animal.html?id=${a.id}" class="stretched-link">${esc(a.nome)}</a></h3>
          <p class="text-muted small mb-2">${esc(a.cidade)}</p>
          <div class="d-flex flex-wrap gap-1">
            <span class="etiqueta">${esc(a.especie)}</span>
            <span class="etiqueta">${esc(a.porte)}</span>
            <span class="etiqueta">${esc(a.sexo)}</span>
            <span class="etiqueta">${esc(a.idade)}</span>
          </div>
        </div>
      </article>
    </div>`;
}

renderNavbar();
renderRodape();
