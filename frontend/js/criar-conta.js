/* criar-conta.js — cadastro de responsável: POST /api/auth/registro (já deixa a pessoa logada). */
const form = document.getElementById("form-conta");
const alertaErro = document.getElementById("erro-envio");
const botao = form.querySelector("button[type=submit]");
const senha = document.getElementById("senha");
const confirmar = document.getElementById("confirmar");

if (usuarioLogado()) location.replace("painel.html");

// Marca o campo "confirmar" como inválido enquanto as senhas forem diferentes
function conferirSenhas() {
  confirmar.setCustomValidity(senha.value === confirmar.value ? "" : "As senhas precisam ser iguais.");
}
senha.addEventListener("input", conferirSenhas);
confirmar.addEventListener("input", conferirSenhas);

form.addEventListener("submit", async e => {
  e.preventDefault();
  alertaErro.classList.add("d-none");
  conferirSenhas();
  form.classList.add("was-validated");
  if (!form.checkValidity()) return;

  botao.disabled = true; botao.textContent = "Criando conta…";
  try {
    salvarSessao(await criarConta({
      nome: document.getElementById("nome").value.trim(),
      email: document.getElementById("email").value.trim(),
      telefone: document.getElementById("telefone").value.trim(),
      senha: senha.value
    }));
    location.href = "painel.html";
  } catch (erro) {
    exibirErroForm(alertaErro, erro);
    botao.disabled = false; botao.textContent = "Criar conta";
  }
});
