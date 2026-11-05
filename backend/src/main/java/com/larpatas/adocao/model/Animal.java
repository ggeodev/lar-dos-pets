package com.larpatas.adocao.model;

import jakarta.persistence.*;
import java.time.Instant;

/** Animal cadastrado para adoção. Tabela: animais. */
@Entity
@Table(name = "animais")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Especie especie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Porte porte;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Sexo sexo;

    /** Texto livre, como no formulário (ex.: "3 anos", "8 meses"). */
    @Column(nullable = false, length = 40)
    private String idade;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, length = 200)
    private String saude;

    @Column(length = 200)
    private String temperamento;

    /** Reservado para quando houver upload de fotos. */
    @Column(name = "foto_url", length = 300)
    private String fotoUrl;

    @Column(name = "responsavel_nome", nullable = false, length = 100)
    private String responsavelNome;

    // Contato do responsável: guardado, mas NÃO exposto na API pública (ver AnimalResponse).
    @Column(name = "responsavel_email", nullable = false, length = 150)
    private String responsavelEmail;

    @Column(name = "responsavel_telefone", nullable = false, length = 30)
    private String responsavelTelefone;

    @Column(nullable = false)
    private boolean destaque = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatusAnimal status = StatusAnimal.DISPONIVEL;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @PrePersist
    void antesDeSalvar() {
        criadoEm = Instant.now();
    }

    // ----- getters e setters -----
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Especie getEspecie() { return especie; }
    public void setEspecie(Especie especie) { this.especie = especie; }
    public Porte getPorte() { return porte; }
    public void setPorte(Porte porte) { this.porte = porte; }
    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }
    public String getIdade() { return idade; }
    public void setIdade(String idade) { this.idade = idade; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getSaude() { return saude; }
    public void setSaude(String saude) { this.saude = saude; }
    public String getTemperamento() { return temperamento; }
    public void setTemperamento(String temperamento) { this.temperamento = temperamento; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public String getResponsavelNome() { return responsavelNome; }
    public void setResponsavelNome(String responsavelNome) { this.responsavelNome = responsavelNome; }
    public String getResponsavelEmail() { return responsavelEmail; }
    public void setResponsavelEmail(String responsavelEmail) { this.responsavelEmail = responsavelEmail; }
    public String getResponsavelTelefone() { return responsavelTelefone; }
    public void setResponsavelTelefone(String responsavelTelefone) { this.responsavelTelefone = responsavelTelefone; }
    public boolean isDestaque() { return destaque; }
    public void setDestaque(boolean destaque) { this.destaque = destaque; }
    public StatusAnimal getStatus() { return status; }
    public void setStatus(StatusAnimal status) { this.status = status; }
    public Instant getCriadoEm() { return criadoEm; }
}
