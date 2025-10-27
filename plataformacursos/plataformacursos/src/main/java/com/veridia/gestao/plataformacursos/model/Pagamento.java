package com.veridia.gestao.plataformacursos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@Entity
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataPagamento;
    private Double valor;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Pagamento() {

    }


    public MetodoPagamento getMetodo() {
        return metodoPagamento;
    }

    public void setMetodo(MetodoPagamento metodo) {
        this.metodoPagamento = metodo;
    }

    public Pagamento(Double valor, MetodoPagamento metodoPagamento) {
        this.valor = valor;
        this.metodoPagamento = metodoPagamento;
        this.status = Status.PENDENTE;
    }

}