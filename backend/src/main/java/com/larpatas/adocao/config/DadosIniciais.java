package com.larpatas.adocao.config;

import com.larpatas.adocao.model.*;
import com.larpatas.adocao.repository.AnimalRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Carrega os mesmos animais fictícios do frontend, só no perfil "dev" e só se a tabela estiver vazia. */
@Configuration
@Profile("dev")
public class DadosIniciais {

    @Bean
    CommandLineRunner carregarAnimais(AnimalRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            repo.saveAll(List.of(
                a("Thor", Especie.CACHORRO, Porte.GRANDE, Sexo.MACHO, "3 anos", "Araraquara, SP",
                  "Thor é brincalhão e muito dócil com crianças. Adora passeios longos e aprende comandos rápido.",
                  "Vacinado e castrado", "Dócil, ativo, sociável", "ONG Patas Amigas", true),
                a("Luna", Especie.GATO, Porte.PEQUENO, Sexo.FEMEA, "8 meses", "São Carlos, SP",
                  "Luna é curiosa e carinhosa. Convive bem com outros gatos e já usa a caixa de areia.",
                  "Vacinada, castração pendente", "Curiosa, carinhosa", "Maria Souza", true),
                a("Mel", Especie.CACHORRO, Porte.MEDIO, Sexo.FEMEA, "2 anos", "Araraquara, SP",
                  "Mel foi resgatada das ruas e hoje é calma e muito apegada. Ideal para apartamento com passeios diários.",
                  "Vacinada e castrada", "Calma, apegada", "ONG Patas Amigas", true),
                a("Bolinha", Especie.COELHO, Porte.PEQUENO, Sexo.MACHO, "1 ano", "Ribeirão Preto, SP",
                  "Bolinha é tranquilo e gosta de cenoura. Precisa de espaço para correr e acompanhamento veterinário.",
                  "Saudável, vermifugado", "Tranquilo, tímido", "João Pereira", false),
                a("Simba", Especie.GATO, Porte.MEDIO, Sexo.MACHO, "4 anos", "Matão, SP",
                  "Simba é independente, mas adora um colo no fim do dia. Já é adulto e tem hábitos domésticos.",
                  "Vacinado e castrado", "Independente, afetuoso", "Clínica VetCerto", true),
                a("Pipoca", Especie.CACHORRO, Porte.PEQUENO, Sexo.FEMEA, "5 anos", "São Carlos, SP",
                  "Pipoca é uma senhorinha sociável que busca um lar tranquilo. Ótima companhia para idosos.",
                  "Vacinada e castrada", "Sociável, tranquila", "Maria Souza", false),
                a("Rex", Especie.CACHORRO, Porte.GRANDE, Sexo.MACHO, "6 meses", "Ribeirão Preto, SP",
                  "Filhote cheio de energia. Precisa de quintal e de um tutor disposto a treinar com paciência.",
                  "Vacinação em andamento", "Enérgico, curioso", "ONG Patas Amigas", false),
                a("Nina", Especie.GATO, Porte.PEQUENO, Sexo.FEMEA, "2 anos", "Araraquara, SP",
                  "Nina é reservada no início, mas se torna muito companheira. Prefere casas sem crianças pequenas.",
                  "Vacinada e castrada", "Reservada, companheira", "João Pereira", false)));
        };
    }

    private static Animal a(String nome, Especie esp, Porte porte, Sexo sexo, String idade, String cidade,
                            String desc, String saude, String temp, String resp, boolean destaque) {
        Animal x = new Animal();
        x.setNome(nome); x.setEspecie(esp); x.setPorte(porte); x.setSexo(sexo); x.setIdade(idade);
        x.setCidade(cidade); x.setDescricao(desc); x.setSaude(saude); x.setTemperamento(temp);
        x.setResponsavelNome(resp);
        x.setResponsavelEmail("contato@exemplo.com");   // fictício
        x.setResponsavelTelefone("(16) 90000-0000");    // fictício
        x.setDestaque(destaque);
        return x;
    }
}
