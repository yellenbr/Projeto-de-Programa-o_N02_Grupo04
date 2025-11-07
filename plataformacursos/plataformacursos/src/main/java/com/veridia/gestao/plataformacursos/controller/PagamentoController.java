package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.model.Pagamento;
import com.veridia.gestao.plataformacursos.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping
    public ResponseEntity<List<Pagamento>> listarTodos() {
        return ResponseEntity.ok(pagamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPorId(@PathVariable Long id) {
        return pagamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/inscricao/{inscricaoId}")
    public ResponseEntity<Pagamento> buscarPorInscricao(@PathVariable Long inscricaoId) {
        return pagamentoService.buscarPorInscricao(inscricaoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pagamento>> buscarPorStatus(@PathVariable Pagamento.StatusPagamento status) {
        return ResponseEntity.ok(pagamentoService.buscarPorStatus(status));
    }

    @PostMapping
    public ResponseEntity<Pagamento> criar(@RequestBody Pagamento pagamento) {
        Pagamento novoPagamento = pagamentoService.salvar(pagamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPagamento);
    }

    @PatchMapping("/{id}/processar")
    public ResponseEntity<Pagamento> processar(@PathVariable Long id) {
        try {
            Pagamento pagamento = pagamentoService.processar(id);
            return ResponseEntity.ok(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<Pagamento> aprovar(@PathVariable Long id) {
        try {
            Pagamento pagamento = pagamentoService.aprovar(id);
            return ResponseEntity.ok(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/recusar")
    public ResponseEntity<Pagamento> recusar(@PathVariable Long id) {
        try {
            Pagamento pagamento = pagamentoService.recusar(id);
            return ResponseEntity.ok(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}