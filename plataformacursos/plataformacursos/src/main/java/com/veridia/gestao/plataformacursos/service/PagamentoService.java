package com.veridia.gestao.plataformacursos.service;

import com.veridia.gestao.plataformacursos.model.Pagamento;
import com.veridia.gestao.plataformacursos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {
    
    @Autowired
    private PagamentoRepository pagamentoRepository;
    
    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }
    
    public Optional<Pagamento> buscarPorId(Long id) {
        return pagamentoRepository.findById(id);
    }
    
    public Optional<Pagamento> buscarPorInscricao(Long inscricaoId) {
        return pagamentoRepository.findByInscricao_Id(inscricaoId);
    }
    
    public List<Pagamento> buscarPorStatus(Pagamento.StatusPagamento status) {
        return pagamentoRepository.findByStatus(status);
    }
    
    public Pagamento salvar(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }
    
    public Pagamento processar(Long id) {
        return pagamentoRepository.findById(id)
            .map(pagamento -> {
                pagamento.setStatus(Pagamento.StatusPagamento.PROCESSANDO);
                return pagamentoRepository.save(pagamento);
            })
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
    }
    
    public Pagamento aprovar(Long id) {
        return pagamentoRepository.findById(id)
            .map(pagamento -> {
                pagamento.setStatus(Pagamento.StatusPagamento.APROVADO);
                return pagamentoRepository.save(pagamento);
            })
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
    }
    
    public Pagamento recusar(Long id) {
        return pagamentoRepository.findById(id)
            .map(pagamento -> {
                pagamento.setStatus(Pagamento.StatusPagamento.RECUSADO);
                return pagamentoRepository.save(pagamento);
            })
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
    }
    
    public void deletar(Long id) {
        pagamentoRepository.deleteById(id);
    }
}
