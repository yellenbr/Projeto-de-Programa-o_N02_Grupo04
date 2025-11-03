package com.veridia.gestao.plataformacursos.service;

import com.veridia.gestao.plataformacursos.model.Inscricao;
import com.veridia.gestao.plataformacursos.model.Pagamento;
import com.veridia.gestao.plataformacursos.repository.InscricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private PagamentoService pagamentoService;

    public Inscricao realizarInscricao(Inscricao inscricao, Pagamento pagamento) {

        Pagamento pagamentoProcessado = pagamentoService.processarPagamento(pagamento);

        if (pagamentoProcessado.getStatus().equals("APROVADO")) {
            inscricao.setAcessoLiberado(true);
        }

        return inscricaoRepository.save(inscricao);
    }
}
