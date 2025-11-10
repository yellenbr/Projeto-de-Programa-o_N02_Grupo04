package com.veridia.gestao.plataformacursos.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String cpf;
    private Integer numeroCursosAtivos = 0;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Inscricao> inscricoes = new ArrayList<>();

    public Aluno() {}

    public Aluno(String nome, String email, String cpf) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getNumeroCursosAtivos() {
        return numeroCursosAtivos != null ? numeroCursosAtivos : 0;
    }

    public void setNumeroCursosAtivos(Integer numeroCursosAtivos) {
        this.numeroCursosAtivos = numeroCursosAtivos;
    }

    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }

    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }

    // Métodos de negócio
    public boolean podeSeInscrever(Curso curso) {
        if (curso == null || !curso.getAtivo()) {
            return false;
        }
        if (!curso.temVagasDisponiveis()) {
            return false;
        }
        int cursosAtivos = (numeroCursosAtivos != null) ? numeroCursosAtivos : 0;
        return cursosAtivos < 5;
    }

    public boolean temInscricoesPendentes() {
        return inscricoes.stream()
                .anyMatch(i -> i.getStatus() == Inscricao.StatusInscricao.PENDENTE);
    }
}

