package com.larpatas.adocao.config;

import com.larpatas.adocao.model.*;
import com.larpatas.adocao.repository.*;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Dados FICTÍCIOS de demonstração, só no perfil "dev" e só se não houver animais.
 * Contas de teste (senha de todas: demo1234):
 *   patas@exemplo.com (ONG Patas Amigas) · maria@exemplo.com · joao@exemplo.com · vet@exemplo.com
 */
@Configuration
@Profile("dev")
public class DadosIniciais {

    private static final String SENHA_DEMO = "demo1234";

    @Bean
    CommandLineRunner carregarDados(AnimalRepository animais, UsuarioRepository usuarios,
                                    InteresseRepository interesses, PasswordEncoder encoder) {
        return args -> {
            if (animais.count() > 0) return;

            Usuario ong   = usuario(usuarios, encoder, "ONG Patas Amigas", "patas@exemplo.com", "(16) 90000-0001");
            Usuario maria = usuario(usuarios, encoder, "Maria Souza", "maria@exemplo.com", "(16) 90000-0002");
            Usuario joao  = usuario(usuarios, encoder, "João Pereira", "joao@exemplo.com", "(16) 90000-0003");
            Usuario vet   = usuario(usuarios, encoder, "Clínica VetCerto", "vet@exemplo.com", "(16) 90000-0004");

            List<Animal> salvos = animais.saveAll(List.of(
                a("Thor", Especie.CACHORRO, Porte.GRANDE, Sexo.MACHO, "3 anos", "Araraquara, SP",
                  "Thor é brincalhão e muito dócil com crianças. Adora passeios longos e aprende comandos rápido.",
                  "Vacinado e castrado", "Dócil, ativo, sociável", ong, true),
                a("Luna", Especie.GATO, Porte.PEQUENO, Sexo.FEMEA, "8 meses", "São Carlos, SP",
                  "Luna é curiosa e carinhosa. Convive bem com outros gatos e já usa a caixa de areia.",
                  "Vacinada, castração pendente", "Curiosa, carinhosa", maria, true),
                a("Mel", Especie.CACHORRO, Porte.MEDIO, Sexo.FEMEA, "2 anos", "Araraquara, SP",
                  "Mel foi resgatada das ruas e hoje é calma e muito apegada. Ideal para apartamento com passeios diários.",
                  "Vacinada e castrada", "Calma, apegada", ong, true),
                a("Bolinha", Especie.COELHO, Porte.PEQUENO, Sexo.MACHO, "1 ano", "Ribeirão Preto, SP",
                  "Bolinha é tranquilo e gosta de cenoura. Precisa de espaço para correr e acompanhamento veterinário.",
                  "Saudável, vermifugado", "Tranquilo, tímido", joao, false),
                a("Simba", Especie.GATO, Porte.MEDIO, Sexo.MACHO, "4 anos", "Matão, SP",
                  "Simba é independente, mas adora um colo no fim do dia. Já é adulto e tem hábitos domésticos.",
                  "Vacinado e castrado", "Independente, afetuoso", vet, true),
                a("Pipoca", Especie.CACHORRO, Porte.PEQUENO, Sexo.FEMEA, "5 anos", "São Carlos, SP",
                  "Pipoca é uma senhorinha sociável que busca um lar tranquilo. Ótima companhia para idosos.",
                  "Vacinada e castrada", "Sociável, tranquila", maria, false),
                a("Rex", Especie.CACHORRO, Porte.GRANDE, Sexo.MACHO, "6 meses", "Ribeirão Preto, SP",
                  "Filhote cheio de energia. Precisa de quintal e de um tutor disposto a treinar com paciência.",
                  "Vacinação em andamento", "Enérgico, curioso", ong, false),
                a("Nina", Especie.GATO, Porte.PEQUENO, Sexo.FEMEA, "2 anos", "Araraquara, SP",
                  "Nina é reservada no início, mas se torna muito companheira. Prefere casas sem crianças pequenas.",
                  "Vacinada e castrada", "Reservada, companheira", joao, false)));

            // Alguns pedidos fictícios para o painel do responsável não começar vazio
            interesses.saveAll(List.of(
                i(salvos.get(0), "Carlos Dias", "Araraquara, SP", "carlos@exemplo.com", "(16) 98888-0001",
                  TipoMoradia.CASA_COM_QUINTAL, false, "Sempre tive cães de grande porte e tenho quintal amplo."),
                i(salvos.get(0), "Fernanda Lima", "Américo Brasiliense, SP", "fernanda@exemplo.com", "(16) 98888-0002",
                  TipoMoradia.CASA_SEM_QUINTAL, true, "Meus filhos pedem um cachorro há anos e já temos experiência com animais."),
                i(salvos.get(2), "Paulo Ramos", "Araraquara, SP", "paulo@exemplo.com", "(16) 98888-0003",
                  TipoMoradia.APARTAMENTO, false, "Moro em apartamento, trabalho em casa e posso passear várias vezes ao dia.")));
        };
    }

    private static Usuario usuario(UsuarioRepository repo, PasswordEncoder encoder,
                                   String nome, String email, String telefone) {
        return repo.findByEmail(email).orElseGet(() -> {
            Usuario u = new Usuario();
            u.setNome(nome); u.setEmail(email); u.setTelefone(telefone);
            u.setSenhaHash(encoder.encode(SENHA_DEMO));
            return repo.save(u);
        });
    }

    private static Animal a(String nome, Especie esp, Porte porte, Sexo sexo, String idade, String cidade,
                            String desc, String saude, String temp, Usuario resp, boolean destaque) {
        Animal x = new Animal();
        x.setNome(nome); x.setEspecie(esp); x.setPorte(porte); x.setSexo(sexo); x.setIdade(idade);
        x.setCidade(cidade); x.setDescricao(desc); x.setSaude(saude); x.setTemperamento(temp);
        x.setResponsavel(resp); x.setDestaque(destaque);
        return x;
    }

    private static Interesse i(Animal animal, String nome, String cidade, String email, String tel,
                               TipoMoradia moradia, boolean outros, String motivo) {
        Interesse x = new Interesse();
        x.setAnimal(animal); x.setNome(nome); x.setCidade(cidade); x.setEmail(email); x.setTelefone(tel);
        x.setMoradia(moradia); x.setPossuiOutrosAnimais(outros); x.setMotivo(motivo);
        return x;
    }
}
