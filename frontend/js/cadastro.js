/* cadastro.js — cadastro de animais: valida e envia para POST /api/animais.
   A foto ainda NÃO é enviada (upload será uma etapa futura). */
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
      cidade: valor("cidade-resp"),          // cidade onde o animal está (mesma do responsável)
      descricao: valor("descricao"),
      saude: valor("saude"),
      responsavelNome: valor("responsavel"),
      responsavelEmail: valor("email-resp"),
      responsavelTelefone: valor("tel-resp")
    });
    document.getElementById("link-novo-animal").href = `animal.html?id=${criado.id}`;
    form.classList.add("d-none");
    confirmacao.classList.remove("d-none");
  } catch (erro) {
    exibirErroForm(alertaErro, erro);
    botao.disabled = false; botao.textContent = "Cadastrar animal";
  }
});
