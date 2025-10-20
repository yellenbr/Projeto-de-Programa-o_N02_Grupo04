package com.veridia.gestao.plataformacursos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(length = 1000)
    private String descricao;
    
    @Column(nullable = false)
    private BigDecimal preco;
    
    private Integer cargaHoraria;
    
    @Column(nullable = false)
    private Integer vagas = 60; // Limite máximo de vagas
    
    @Column(nullable = false)
    private Boolean ativo = true; // Curso ativo ou cancelado
    
    @ManyToOne
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;
    
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<Inscricao> inscricoes;
    
    // Construtores
    public Curso() {
    }
    
    public Curso(String nome, String descricao, BigDecimal preco, Integer cargaHoraria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.cargaHoraria = cargaHoraria;
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
    
    public Integer getCargaHoraria() {
        return cargaHoraria;
    }
    
    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }
    
    public Instrutor getInstrutor() {
        return instrutor;
    }
    
    public void setInstrutor(Instrutor instrutor) {
        this.instrutor = instrutor;
    }
    
    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }
    
    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }
    
    public Integer getVagas() {
        return vagas;
    }
    
    public void setVagas(Integer vagas) {
        this.vagas = vagas;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public long getNumeroInscritos() {
        if (inscricoes == null) return 0;
        return inscricoes.stream()
            .filter(i -> i.getStatus() != Inscricao.StatusInscricao.CANCELADA)
            .count();
    }
    
    public boolean temVagasDisponiveis() {
        return getNumeroInscritos() < vagas;
    }
    
    public boolean isCursoComecou() {
        return false;
    }
}
