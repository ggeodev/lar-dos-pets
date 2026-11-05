/* interesse.js — formulário "Quero adotar": valida e envia para POST /api/interesses. */
const form = document.getElementById("form-interesse");
const select = document.getElementById("animal-escolhido");
const confirmacao = document.getElementById("confirmacao");
const alertaErro = document.getElementById("erro-envio");
const botao = form.querySelector("button[type=submit]");
const valor = id => document.getElementById(id).value.trim();

// Lista de animais disponíveis; pré-seleciona se vier ?id= na URL
listarAnimais().then(animais => {
  animais.forEach(a => select.insertAdjacentHTML("beforeend",
    `<option value="${a.id}">${esc(a.nome)} (${esc(a.especie)}, ${esc(a.cidade)})</option>`));
  const idUrl = new URLSearchParams(location.search).get("id");
  if (idUrl) select.value = idUrl;
}).catch(e => exibirErroForm(alertaErro, e));

form.addEventListener("submit", async e => {
  e.preventDefault();
  alertaErro.classList.add("d-none");
  form.classList.add("was-validated");      // mensagens de validação do Bootstrap
  if (!form.checkValidity()) return;

  botao.disabled = true; botao.textContent = "Enviando…";
  try {
    await enviarInteresse({
      animalId: Number(select.value),
      nome: valor("nome"),
      cidade: valor("cidade"),
      email: valor("email"),
      telefone: valor("telefone"),
      moradia: paraEnum(valor("moradia")),                       // "Casa com quintal" -> CASA_COM_QUINTAL
      possuiOutrosAnimais: valor("outros-animais") === "Sim",
      motivo: valor("motivo"),
      aceitouTermo: document.getElementById("termo").checked
    });
    form.classList.add("d-none");
    confirmacao.classList.remove("d-none");
  } catch (erro) {
    exibirErroForm(alertaErro, erro);
    botao.disabled = false; botao.textContent = "Enviar interesse";
  }
});
