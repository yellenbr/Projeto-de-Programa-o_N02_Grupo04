package com.veridia.gestao.plataformacursos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "inscricao_id", nullable = false)
    private Inscricao inscricao;
    
    @Column(nullable = false)
    private BigDecimal valor;
    
    @Column(nullable = false)
    private LocalDateTime dataPagamento;
    
    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;
    
    @Enumerated(EnumType.STRING)
    private StatusPagamento status;
    
    public enum MetodoPagamento {
        CARTAO_CREDITO,
        CARTAO_DEBITO,
        PIX,
        BOLETO,
        TRANSFERENCIA
    }
    
    public enum StatusPagamento {
        PENDENTE,
        PROCESSANDO,
        APROVADO,
        RECUSADO,
        CANCELADO
    }
    
    public Pagamento() {
        this.dataPagamento = LocalDateTime.now();
        this.status = StatusPagamento.PENDENTE;
    }
    
    public Pagamento(Inscricao inscricao, BigDecimal valor, MetodoPagamento metodoPagamento) {
        this.inscricao = inscricao;
        this.valor = valor;
        this.metodoPagamento = metodoPagamento;
        this.dataPagamento = LocalDateTime.now();
        this.status = StatusPagamento.PENDENTE;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Inscricao getInscricao() {
        return inscricao;
    }
    
    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }
    
    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
    
    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }
    
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
    
    public StatusPagamento getStatus() {
        return status;
    }
    
    public void setStatus(StatusPagamento status) {
        this.status = status;
    }
}
