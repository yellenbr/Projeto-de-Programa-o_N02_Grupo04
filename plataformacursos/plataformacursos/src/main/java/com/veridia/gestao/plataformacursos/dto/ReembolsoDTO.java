package com.veridia.gestao.plataformacursos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReembolsoDTO {
    
    private Long inscricaoId;
    private Long pagamentoId;
    private String nomeAluno;
    private String nomeCurso;
    private BigDecimal valorReembolsado;
    private LocalDateTime dataReembolso;
    private String motivo;
    private String protocolo;
    
    public ReembolsoDTO() {
    }
    
    public ReembolsoDTO(Long inscricaoId, Long pagamentoId, String nomeAluno, 
                       String nomeCurso, BigDecimal valorReembolsado, String motivo) {
        this.inscricaoId = inscricaoId;
        this.pagamentoId = pagamentoId;
        this.nomeAluno = nomeAluno;
        this.nomeCurso = nomeCurso;
        this.valorReembolsado = valorReembolsado;
        this.dataReembolso = LocalDateTime.now();
        this.motivo = motivo;
        this.protocolo = "REEMB-" + System.currentTimeMillis();
    }
    
    public Long getInscricaoId() {
        return inscricaoId;
    }
    
    public void setInscricaoId(Long inscricaoId) {
        this.inscricaoId = inscricaoId;
    }
    
    public Long getPagamentoId() {
        return pagamentoId;
    }
    
    public void setPagamentoId(Long pagamentoId) {
        this.pagamentoId = pagamentoId;
    }
    
    public String getNomeAluno() {
        return nomeAluno;
    }
    
    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }
    
    public String getNomeCurso() {
        return nomeCurso;
    }
    
    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }
    
    public BigDecimal getValorReembolsado() {
        return valorReembolsado;
    }
    
    public void setValorReembolsado(BigDecimal valorReembolsado) {
        this.valorReembolsado = valorReembolsado;
    }
    
    public LocalDateTime getDataReembolso() {
        return dataReembolso;
    }
    
    public void setDataReembolso(LocalDateTime dataReembolso) {
        this.dataReembolso = dataReembolso;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public String getProtocolo() {
        return protocolo;
    }
    
    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }
}
