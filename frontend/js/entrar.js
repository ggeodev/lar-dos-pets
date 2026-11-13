/* entrar.js — login: envia e-mail e senha para POST /api/auth/login e guarda o token. */
const form = document.getElementById("form-entrar");
const alertaErro = document.getElementById("erro-envio");
const botao = form.querySelector("button[type=submit]");

if (usuarioLogado()) location.replace("painel.html");   // já está logado

form.addEventListener("submit", async e => {
  e.preventDefault();
  alertaErro.classList.add("d-none");
  form.classList.add("was-validated");
  if (!form.checkValidity()) return;

  botao.disabled = true; botao.textContent = "Entrando…";
  try {
    salvarSessao(await entrarNaConta({
      email: document.getElementById("email").value.trim(),
      senha: document.getElementById("senha").value
    }));
    // Volta para a página que a pessoa queria abrir (?voltar=cadastro.html) ou vai ao painel
    location.href = destinoSeguro(new URLSearchParams(location.search).get("voltar"));
  } catch (erro) {
    exibirErroForm(alertaErro, erro);
    botao.disabled = false; botao.textContent = "Entrar";
  }
});
