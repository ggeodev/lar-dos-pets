/* animais.js — catálogo: busca os animais na API aplicando os filtros escolhidos. */
const formFiltros = document.getElementById("form-filtros");
const listaAnimais = document.getElementById("lista-animais");
const contador = document.getElementById("contador");
const vazio = document.getElementById("sem-resultados");
const selectCidade = document.getElementById("filtro-cidade");
let ultimaBusca = 0; // evita que uma resposta antiga sobrescreva uma mais recente

// Opções de localização vindas do banco
listarCidades()
  .then(cidades => cidades.forEach(c => selectCidade.insertAdjacentHTML("beforeend", `<option>${esc(c)}</option>`)))
  .catch(() => { /* se falhar, o erro principal aparece na lista de animais */ });

async function aplicarFiltros() {
  const busca = ++ultimaBusca;
  const f = Object.fromEntries(new FormData(formFiltros));
  listaAnimais.innerHTML = CARREGANDO;
  vazio.classList.add("d-none");
  try {
    const animais = await listarAnimais({
      especie: f.especie && paraEnum(f.especie),
      porte: f.porte && paraEnum(f.porte),
      sexo: f.sexo && paraEnum(f.sexo),
      cidade: f.cidade
    });
    if (busca !== ultimaBusca) return;
    listaAnimais.innerHTML = animais.map(a => criarCardAnimal(a)).join("");
    contador.textContent = `${animais.length} ${animais.length === 1 ? "animal encontrado" : "animais encontrados"}`;
    vazio.classList.toggle("d-none", animais.length > 0);
  } catch (e) {
    if (busca !== ultimaBusca) return;
    contador.textContent = "";
    exibirErro(listaAnimais, e);
  }
}

formFiltros.addEventListener("change", aplicarFiltros);
formFiltros.addEventListener("reset", () => setTimeout(aplicarFiltros));
aplicarFiltros();
