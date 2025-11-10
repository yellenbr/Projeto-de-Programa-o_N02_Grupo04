package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.dto.ReembolsoDTO;
import com.veridia.gestao.plataformacursos.dto.TransferenciaDTO;
import com.veridia.gestao.plataformacursos.model.Inscricao;
import com.veridia.gestao.plataformacursos.model.Pagamento;
import com.veridia.gestao.plataformacursos.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alunos/{alunoId}")
public class AlunoFluxoController {

    @Autowired
    private InscricaoService inscricaoService;

    @PostMapping("/inscrever/{cursoId}")
    public ResponseEntity<?> inscrever(@PathVariable Long alunoId, @PathVariable Long cursoId) {
        try {
            Inscricao inscricao = inscricaoService.inscrever(alunoId, cursoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(inscricao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        }
    }


    @PostMapping("/pagamento/{inscricaoId}")
    public ResponseEntity<?> realizarPagamento(
            @PathVariable Long alunoId,
            @PathVariable Long inscricaoId,
            @RequestParam Pagamento.MetodoPagamento metodoPagamento) {
        try {
            Inscricao inscricao = inscricaoService.processarPagamento(inscricaoId, metodoPagamento);
            return ResponseEntity.ok(inscricao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        }
    }


    @PostMapping("/cancelar/{inscricaoId}")
    public ResponseEntity<?> cancelarInscricao(
            @PathVariable Long alunoId,
            @PathVariable Long inscricaoId) {
        try {
            ReembolsoDTO reembolso = inscricaoService.cancelarInscricao(inscricaoId);
            return ResponseEntity.ok(reembolso);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        }
    }

    @PostMapping("/transferir/{inscricaoId}")
    public ResponseEntity<?> transferirCurso(
            @PathVariable Long alunoId,
            @PathVariable Long inscricaoId,
            @RequestParam Long novoCursoId) {
        try {
            TransferenciaDTO transferencia = inscricaoService.transferirCurso(inscricaoId, novoCursoId);
            return ResponseEntity.ok(transferencia);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        }
    }

    @GetMapping("/inscricoes")
    public ResponseEntity<List<Inscricao>> listarInscricoes(@PathVariable Long alunoId) {
        List<Inscricao> inscricoes = inscricaoService.buscarPorAluno(alunoId);
        return ResponseEntity.ok(inscricoes);
    }
}