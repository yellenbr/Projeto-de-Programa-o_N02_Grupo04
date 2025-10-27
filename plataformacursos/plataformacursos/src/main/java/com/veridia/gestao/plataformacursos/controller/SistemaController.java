package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.model.Inscricao;
import com.veridia.gestao.plataformacursos.model.Pagamento;
import com.veridia.gestao.plataformacursos.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.veridia.gestao.plataformacursos.dto.InscricaoDTO;


@RestController
@RequestMapping("/api/sistema")
public class SistemaController {

    @Autowired
    private InscricaoService inscricaoService;

    @PostMapping("/inscrever")
    public ResponseEntity<Inscricao> inscrever(@RequestBody InscricaoDTO dto) {
        Pagamento pagamento = new Pagamento();
        pagamento.setMetodo(dto.getMetodoPagamento());
        pagamento.setValor(dto.getValor());

        Inscricao inscricao = new Inscricao();
        inscricao.setIdAluno(dto.getIdAluno());
        inscricao.setIdCurso(dto.getIdCurso());
        inscricao.setTipoInscricao(dto.getTipoInscricao());

        Inscricao novaInscricao = inscricaoService.realizarInscricao(inscricao, pagamento);
        return ResponseEntity.ok(novaInscricao);
    }
}
