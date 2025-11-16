package com.veridia.gestao.plataformacursos.dto;

import java.math.BigDecimal;

public class CursoComAlunosDTO {
    private Long cursoId;
    private String cursoNome;
    private BigDecimal cursoPreco;
    private Long totalAlunos;

    public CursoComAlunosDTO() {}

    public CursoComAlunosDTO(Long cursoId, String cursoNome, BigDecimal cursoPreco, Long totalAlunos) {
        this.cursoId = cursoId;
        this.cursoNome = cursoNome;
        this.cursoPreco = cursoPreco;
        this.totalAlunos = totalAlunos;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public String getCursoNome() {
        return cursoNome;
    }

    public void setCursoNome(String cursoNome) {
        this.cursoNome = cursoNome;
    }

    public BigDecimal getCursoPreco() {
        return cursoPreco;
    }

    public void setCursoPreco(BigDecimal cursoPreco) {
        this.cursoPreco = cursoPreco;
    }

    public Long getTotalAlunos() {
        return totalAlunos;
    }

    public void setTotalAlunos(Long totalAlunos) {
        this.totalAlunos = totalAlunos;
    }
}
