package com.veridia.plataforma.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private BigDecimal preco;
    private Integer limiteVagas;


    public Curso() {}

    public Curso(String titulo, String descricao, BigDecimal preco, Integer limiteVagas) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.limiteVagas = limiteVagas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
}