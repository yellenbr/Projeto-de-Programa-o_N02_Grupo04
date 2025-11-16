package com.veridia.gestao.plataformacursos.dto;

import java.math.BigDecimal;

public class DashboardInstrutorDTO {
    private Long totalCursos;
    private Long totalAlunos;
    private BigDecimal receitaTotal;

    public DashboardInstrutorDTO() {}

    public DashboardInstrutorDTO(Long totalCursos, Long totalAlunos, BigDecimal receitaTotal) {
        this.totalCursos = totalCursos;
        this.totalAlunos = totalAlunos;
        this.receitaTotal = receitaTotal;
    }

    public Long getTotalCursos() {
        return totalCursos;
    }

    public void setTotalCursos(Long totalCursos) {
        this.totalCursos = totalCursos;
    }

    public Long getTotalAlunos() {
        return totalAlunos;
    }

    public void setTotalAlunos(Long totalAlunos) {
        this.totalAlunos = totalAlunos;
    }

    public BigDecimal getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(BigDecimal receitaTotal) {
        this.receitaTotal = receitaTotal;
    }
}
