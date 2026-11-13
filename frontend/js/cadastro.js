/* cadastro.js — cadastro de animais (exige login): valida e envia para POST /api/animais.
   O responsável é a conta logada, então não há campos de contato aqui.
   A foto ainda NÃO existe (upload será uma etapa futura). */
if (exigirLogin()) iniciarCadastro();

function iniciarCadastro() {
  const form = document.getElementById("form-cadastro");
  const confirmacao = document.getElementById("confirmacao");
  const alertaErro = document.getElementById("erro-envio");
  const botao = form.querySelector("button[type=submit]");
  const valor = id => document.getElementById(id).value.trim();

  form.addEventListener("submit", async e => {
    e.preventDefault();
    alertaErro.classList.add("d-none");
    form.classList.add("was-validated");
    if (!form.checkValidity()) return;

    botao.disabled = true; botao.textContent = "Cadastrando…";
    try {
      const criado = await cadastrarAnimal({
        nome: valor("nome-animal"),
        especie: paraEnum(valor("especie")),
        porte: paraEnum(valor("porte")),
        sexo: paraEnum(valor("sexo")),
        idade: valor("idade"),
        cidade: valor("cidade"),
        descricao: valor("descricao"),
        saude: valor("saude")
      });
      document.getElementById("link-novo-animal").href = `animal.html?id=${criado.id}`;
      form.classList.add("d-none");
      confirmacao.classList.remove("d-none");
    } catch (erro) {
      if (erro.status === 401) { location.replace("entrar.html?voltar=cadastro.html"); return; }
      exibirErroForm(alertaErro, erro);
      botao.disabled = false; botao.textContent = "Cadastrar animal";
    }
  });
}
