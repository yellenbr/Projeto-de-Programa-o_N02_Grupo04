package com.veridia.gestao.plataformacursos.service;

import com.veridia.gestao.plataformacursos.model.Pagamento;
import com.veridia.gestao.plataformacursos.model.Status;
import com.veridia.gestao.plataformacursos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    public Pagamento processarPagamento(Pagamento pagamento) {
        // Validação simples
        if (pagamento.getValor() == null || pagamento.getValor() <= 0) {
            throw new IllegalArgumentException("Valor inválido para pagamento.");
        }

        // Simula aprovação de pagamento
        pagamento.setStatus(Status.APROVADO);
        pagamento.setDataPagamento(LocalDateTime.now());

        // Salva no banco
        return pagamentoRepository.save(pagamento);
    }
}
