package com.larpatas.adocao.model;

import jakarta.persistence.*;
import java.time.Instant;

/** Manifestação de interesse em adotar um animal. Tabela: interesses. */
@Entity
@Table(name = "interesses")
public class Interesse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 30)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private TipoMoradia moradia;

    @Column(name = "possui_outros_animais", nullable = false)
    private boolean possuiOutrosAnimais;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @PrePersist
    void antesDeSalvar() {
        criadoEm = Instant.now();
    }

    public Long getId() { return id; }
    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public TipoMoradia getMoradia() { return moradia; }
    public void setMoradia(TipoMoradia moradia) { this.moradia = moradia; }
    public boolean isPossuiOutrosAnimais() { return possuiOutrosAnimais; }
    public void setPossuiOutrosAnimais(boolean v) { this.possuiOutrosAnimais = v; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Instant getCriadoEm() { return criadoEm; }
}
