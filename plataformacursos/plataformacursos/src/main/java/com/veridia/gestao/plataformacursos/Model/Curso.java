package com.veridia.gestao.plataformacursos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer limiteVagas;
    private Integer cargaHoraria;
    private Boolean ativo = true;
    private LocalDateTime dataInicio;
    private Integer numeroInscritos = 0;

    @ManyToOne
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<Inscricao> inscricoes = new ArrayList<>();

    public Curso() {}

    public Curso(String nome, String descricao, BigDecimal preco, Integer limiteVagas, Instrutor instrutor) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.limiteVagas = limiteVagas;
        this.instrutor = instrutor;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getLimiteVagas() {
        return limiteVagas;
    }

    public void setLimiteVagas(Integer limiteVagas) {
        this.limiteVagas = limiteVagas;
    }

    public Instrutor getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(Instrutor instrutor) {
        this.instrutor = instrutor;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Integer getNumeroInscritos() {
        return numeroInscritos;
    }

    public void setNumeroInscritos(Integer numeroInscritos) {
        this.numeroInscritos = numeroInscritos;
    }

    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }

    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }

    // Métodos de negócio
    public boolean isCursoComecou() {
        return dataInicio != null && LocalDateTime.now().isAfter(dataInicio);
    }

    public boolean temVagasDisponiveis() {
        return limiteVagas == null || numeroInscritos < limiteVagas;
    }

    public boolean reservarVaga() {
        if (!temVagasDisponiveis()) {
            return false;
        }
        numeroInscritos++;
        return true;
    }
}
