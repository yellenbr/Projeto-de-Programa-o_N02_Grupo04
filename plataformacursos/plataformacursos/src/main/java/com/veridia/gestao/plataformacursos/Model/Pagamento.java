package com.veridia.gestao.plataformacursos.model;

import com.veridia.gestao.plataformacursos.exception.NegocioException;
import com.veridia.gestao.plataformacursos.exception.ValidacaoException;
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
        validarInscricao(inscricao);
        validarValor(valor);
        validarMetodoPagamento(metodoPagamento);

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
        validarInscricao(inscricao);
        this.inscricao = inscricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        validarValor(valor);
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
        validarMetodoPagamento(metodoPagamento);
        this.metodoPagamento = metodoPagamento;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    // Métodos de validação
    private void validarInscricao(Inscricao inscricao) {
        if (inscricao == null) {
            throw new ValidacaoException("Inscrição não pode ser nula");
        }
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new ValidacaoException("Valor do pagamento não pode ser nulo");
        }
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("Valor do pagamento deve ser maior que zero");
        }
    }

    private void validarMetodoPagamento(MetodoPagamento metodoPagamento) {
        if (metodoPagamento == null) {
            throw new ValidacaoException("Método de pagamento não pode ser nulo");
        }
    }

    // Métodos de negócio
    /**
     * Processa o pagamento (simula processamento)
     * @throws NegocioException se o pagamento não estiver pendente
     */
    public void processar() {
        if (this.status != StatusPagamento.PENDENTE) {
            throw new NegocioException(
                    "Apenas pagamentos pendentes podem ser processados. Status atual: " + this.status);
        }
        this.status = StatusPagamento.PROCESSANDO;
    }

    /**
     * Aprova o pagamento
     * @throws NegocioException se não estiver em processamento
     */
    public void aprovar() {
        if (this.status != StatusPagamento.PROCESSANDO &&
                this.status != StatusPagamento.PENDENTE) {
            throw new NegocioException(
                    "Apenas pagamentos em processamento ou pendentes podem ser aprovados");
        }
        this.status = StatusPagamento.APROVADO;
    }

    /**
     * Recusa o pagamento
     * @throws NegocioException se não estiver em processamento ou pendente
     */
    public void recusar() {
        if (this.status != StatusPagamento.PROCESSANDO &&
                this.status != StatusPagamento.PENDENTE) {
            throw new NegocioException(
                    "Apenas pagamentos em processamento ou pendentes podem ser recusados");
        }
        this.status = StatusPagamento.RECUSADO;
    }

    /**
     * Cancela o pagamento
     * @throws NegocioException se já estiver cancelado ou recusado
     */
    public void cancelar() {
        if (this.status == StatusPagamento.CANCELADO) {
            throw new NegocioException("Pagamento já está cancelado");
        }
        if (this.status == StatusPagamento.RECUSADO) {
            throw new NegocioException("Pagamento recusado não pode ser cancelado");
        }
        this.status = StatusPagamento.CANCELADO;
    }

    /**
     * Verifica se o pagamento foi aprovado
     */
    public boolean isAprovado() {
        return this.status == StatusPagamento.APROVADO;
    }

    /**
     * Verifica se o pagamento está pendente
     */
    public boolean isPendente() {
        return this.status == StatusPagamento.PENDENTE;
    }

    /**
     * Verifica se o pagamento pode ser processado
     */
    public boolean podeSerProcessado() {
        return this.status == StatusPagamento.PENDENTE;
    }

    /**
     * Verifica se o pagamento pode ser cancelado
     */
    public boolean podeCancelar() {
        return this.status != StatusPagamento.CANCELADO &&
                this.status != StatusPagamento.RECUSADO;
    }
}