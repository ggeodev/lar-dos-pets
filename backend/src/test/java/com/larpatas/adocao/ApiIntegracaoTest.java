package com.larpatas.adocao;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * Testes de integração: sobem a aplicação inteira com banco H2 em memória e chamam a API de verdade.
 * Cada teste cria os próprios dados (e-mails e cidades únicos), então um não depende do outro.
 */
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class ApiIntegracaoTest {

    private static final String SENHA = "senha-segura-123";

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;

    // ---------- utilitários ----------

    private static String unico(String prefixo) { return prefixo + UUID.randomUUID().toString().substring(0, 8); }

    private ResultActions postJson(String url, Object corpo, String token) throws Exception {
        MockHttpServletRequestBuilder req = post(url).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(corpo));
        if (token != null) req.header("Authorization", "Bearer " + token);
        return mvc.perform(req);
    }

    private ResultActions patchJson(String url, Object corpo, String token) throws Exception {
        MockHttpServletRequestBuilder req = patch(url).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(corpo));
        if (token != null) req.header("Authorization", "Bearer " + token);
        return mvc.perform(req);
    }

    private ResultActions getAutenticado(String url, String token) throws Exception {
        return mvc.perform(get(url).header("Authorization", "Bearer " + token));
    }

    private String campo(MvcResult r, String nome) throws Exception {
        return json.readTree(r.getResponse().getContentAsString(StandardCharsets.UTF_8)).get(nome).asText();
    }

    private Map<String, Object> corpoRegistro(String email) {
        return Map.of("nome", "Responsável Teste", "email", email, "telefone", "(16) 90000-0000", "senha", SENHA);
    }

    /** Cria uma conta e devolve o token. */
    private String registrar(String email) throws Exception {
        MvcResult r = postJson("/api/auth/registro", corpoRegistro(email), null)
                .andExpect(status().isCreated()).andReturn();
        return campo(r, "token");
    }

    private Map<String, Object> animalValido(String cidade) {
        return Map.of("nome", "Pipoca", "especie", "GATO", "porte", "PEQUENO", "sexo", "FEMEA",
                "idade", "1 ano", "cidade", cidade, "descricao", "Gata tranquila e carinhosa.",
                "saude", "Vacinada e castrada");
    }

    private Map<String, Object> interesse(long animalId, String email) {
        return Map.of("animalId", animalId, "nome", "Carlos Teste", "cidade", "Araraquara",
                "email", email, "telefone", "(16) 98888-0000", "moradia", "APARTAMENTO",
                "possuiOutrosAnimais", false, "motivo", "Tenho espaço e tempo para cuidar.",
                "aceitouTermo", true);
    }

    // ---------- leitura pública ----------

    @Test
    @DisplayName("catálogo é público")
    void catalogoEhPublico() throws Exception {
        mvc.perform(get("/api/animais")).andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("animal inexistente responde 404")
    void animalInexistente() throws Exception {
        mvc.perform(get("/api/animais/999999")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("filtro com valor inválido responde 400")
    void filtroInvalido() throws Exception {
        mvc.perform(get("/api/animais").param("especie", "DRAGAO")).andExpect(status().isBadRequest());
    }

    // ---------- conta e login ----------

    @Test
    @DisplayName("criar conta devolve token e dados do usuário")
    void criarConta() throws Exception {
        postJson("/api/auth/registro", corpoRegistro(unico("ana-") + "@exemplo.com"), null)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.usuario.nome").value("Responsável Teste"));
    }

    @Test
    @DisplayName("e-mail repetido (mesmo em maiúsculas) responde 409")
    void emailDuplicado() throws Exception {
        String email = unico("dup-") + "@exemplo.com";
        registrar(email);
        postJson("/api/auth/registro", corpoRegistro(email.toUpperCase()), null).andExpect(status().isConflict());
    }

    @Test
    @DisplayName("senha curta responde 400 com o campo apontado")
    void senhaCurta() throws Exception {
        Map<String, Object> corpo = Map.of("nome", "Ana", "email", unico("c-") + "@exemplo.com",
                "telefone", "(16) 90000-0000", "senha", "123");
        postJson("/api/auth/registro", corpo, null).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.senha").exists());
    }

    @Test
    @DisplayName("login correto devolve token")
    void loginCorreto() throws Exception {
        String email = unico("ok-") + "@exemplo.com";
        registrar(email);
        postJson("/api/auth/login", Map.of("email", email, "senha", SENHA), null)
                .andExpect(status().isOk()).andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("senha errada e e-mail inexistente respondem 401 com a mesma mensagem")
    void loginInvalido() throws Exception {
        String email = unico("err-") + "@exemplo.com";
        registrar(email);
        postJson("/api/auth/login", Map.of("email", email, "senha", "senha-errada-1"), null)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("E-mail ou senha incorretos."));
        postJson("/api/auth/login", Map.of("email", unico("nao-") + "@exemplo.com", "senha", SENHA), null)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("E-mail ou senha incorretos."));
    }

    // ---------- rotas protegidas ----------

    @Test
    @DisplayName("cadastrar animal sem login responde 401")
    void cadastroSemLogin() throws Exception {
        postJson("/api/animais", animalValido("Qualquer"), null).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("painel sem login ou com token inválido responde 401")
    void painelSemLogin() throws Exception {
        mvc.perform(get("/api/meus-animais")).andExpect(status().isUnauthorized());
        getAutenticado("/api/meus-animais", "token-invalido").andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("campos obrigatórios do animal são validados")
    void animalInvalido() throws Exception {
        String token = registrar(unico("v-") + "@exemplo.com");
        postJson("/api/animais", Map.of("nome", ""), token).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("contato do responsável não aparece na API pública")
    void contatoNaoVazaNaApiPublica() throws Exception {
        String email = unico("dono-") + "@exemplo.com";
        String token = registrar(email);
        MvcResult r = postJson("/api/animais", animalValido(unico("Cidade-")), token)
                .andExpect(status().isCreated()).andReturn();
        mvc.perform(get("/api/animais/" + campo(r, "id")))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString(email))))
                .andExpect(content().string(not(containsString("90000-0000"))));
    }

    // ---------- fluxo completo de adoção ----------

    @Test
    @DisplayName("fluxo: cadastrar, receber interesse, isolar contas, adotar")
    void fluxoCompleto() throws Exception {
        String cidade = unico("Cidade-");
        String tokenDono = registrar(unico("dono-") + "@exemplo.com");
        String tokenOutro = registrar(unico("outro-") + "@exemplo.com");

        // 1) o dono cadastra e o animal aparece no catálogo público
        MvcResult criado = postJson("/api/animais", animalValido(cidade), tokenDono)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("DISPONIVEL"))
                .andExpect(jsonPath("$.destaque").value(false))
                .andReturn();
        long id = Long.parseLong(campo(criado, "id"));
        mvc.perform(get("/api/animais").param("cidade", cidade))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));

        // 2) qualquer pessoa manifesta interesse; repetir o mesmo e-mail é barrado
        String emailInteressado = unico("int-") + "@exemplo.com";
        postJson("/api/interesses", interesse(id, emailInteressado), null).andExpect(status().isCreated());
        postJson("/api/interesses", interesse(id, emailInteressado.toUpperCase()), null)
                .andExpect(status().isConflict());

        // 3) o dono vê o pedido com contato e o total no painel
        getAutenticado("/api/meus-animais/" + id + "/interesses", tokenDono)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value(emailInteressado))
                .andExpect(jsonPath("$[0].telefone").value("(16) 98888-0000"));
        getAutenticado("/api/meus-animais", tokenDono)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalInteresses", is(1)));

        // 4) outra conta não enxerga nem altera o animal (404, como se não existisse)
        getAutenticado("/api/meus-animais/" + id + "/interesses", tokenOutro).andExpect(status().isNotFound());
        patchJson("/api/meus-animais/" + id + "/status", Map.of("status", "ADOTADO"), tokenOutro)
                .andExpect(status().isNotFound());
        getAutenticado("/api/meus-animais", tokenOutro).andExpect(jsonPath("$", hasSize(0)));

        // 5) o dono marca como adotado: sai do catálogo, perfil mostra ADOTADO, novos pedidos são barrados
        patchJson("/api/meus-animais/" + id + "/status", Map.of("status", "ADOTADO"), tokenDono)
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ADOTADO"));
        mvc.perform(get("/api/animais").param("cidade", cidade)).andExpect(jsonPath("$", hasSize(0)));
        mvc.perform(get("/api/animais/" + id)).andExpect(jsonPath("$.status").value("ADOTADO"));
        postJson("/api/interesses", interesse(id, unico("novo-") + "@exemplo.com"), null)
                .andExpect(status().isConflict());

        // 6) reabrir devolve o animal ao catálogo
        patchJson("/api/meus-animais/" + id + "/status", Map.of("status", "DISPONIVEL"), tokenDono)
                .andExpect(status().isOk());
        mvc.perform(get("/api/animais").param("cidade", cidade)).andExpect(jsonPath("$", hasSize(1)));
    }
}
