package com.veridia.gestao.plataformacursos.service;

import com.veridia.gestao.plataformacursos.dto.ReembolsoDTO;
import com.veridia.gestao.plataformacursos.dto.TransferenciaDTO;
import com.veridia.gestao.plataformacursos.model.Aluno;
import com.veridia.gestao.plataformacursos.model.Curso;
import com.veridia.gestao.plataformacursos.model.Inscricao;
import com.veridia.gestao.plataformacursos.model.Pagamento;
import com.veridia.gestao.plataformacursos.repository.AlunoRepository;
import com.veridia.gestao.plataformacursos.repository.CursoRepository;
import com.veridia.gestao.plataformacursos.repository.InscricaoRepository;
import com.veridia.gestao.plataformacursos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class InscricaoService {
    
    @Autowired
    private InscricaoRepository inscricaoRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private PagamentoRepository pagamentoRepository;
    
    public List<Inscricao> listarTodas() {
        return inscricaoRepository.findAll();
    }
    
    public Optional<Inscricao> buscarPorId(Long id) {
        return inscricaoRepository.findById(id);
    }
    
    public List<Inscricao> buscarPorAluno(Long alunoId) {
        return inscricaoRepository.findByAluno_Id(alunoId);
    }
    
    public List<Inscricao> buscarPorCurso(Long cursoId) {
        return inscricaoRepository.findByCurso_Id(cursoId);
    }
    
    /**
     * REGRA: Aluno se inscreve em um curso
     * - Verifica se tem vagas (max 60)
     * - Verifica se aluno já está inscrito
     * - Cria inscrição com status PENDENTE
     */
    @Transactional
    public Inscricao inscrever(Long alunoId, Long cursoId) {
        // Buscar aluno
        Aluno aluno = alunoRepository.findById(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        // Buscar curso
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        
        // Verificar se o curso está ativo
        if (!curso.getAtivo()) {
            throw new RuntimeException("Curso cancelado ou inativo");
        }
        
        // REGRA: Verificar se já está inscrito (não pode inscrever duas vezes)
        if (inscricaoRepository.existsByAluno_IdAndCurso_IdAndStatusNot(
            alunoId, cursoId, Inscricao.StatusInscricao.CANCELADA)) {
            throw new RuntimeException("Aluno já está inscrito neste curso");
        }
        
        // REGRA: Verificar vagas disponíveis (max 60)
        if (!curso.temVagasDisponiveis()) {
            throw new RuntimeException("Curso sem vagas disponíveis (limite: " + curso.getVagas() + " alunos)");
        }
        
        // Criar e salvar inscrição
        Inscricao inscricao = new Inscricao(aluno, curso);
        inscricao.setStatus(Inscricao.StatusInscricao.PENDENTE);
        
        return inscricaoRepository.save(inscricao);
    }
    
    /**
     * REGRA: Processar pagamento da inscrição
     * - Pagamento confirmado altera status para PAGO
     */
    @Transactional
    public Inscricao processarPagamento(Long inscricaoId, Pagamento.MetodoPagamento metodoPagamento) {
        Inscricao inscricao = inscricaoRepository.findById(inscricaoId)
            .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
        
        if (inscricao.getStatus() != Inscricao.StatusInscricao.PENDENTE) {
            throw new RuntimeException("Inscrição não está pendente de pagamento");
        }
        
        // Criar pagamento
        BigDecimal valor = inscricao.getCurso().getPreco();
        Pagamento pagamento = new Pagamento(inscricao, valor, metodoPagamento);
        pagamento.setStatus(Pagamento.StatusPagamento.APROVADO);
        pagamentoRepository.save(pagamento);
        
        // REGRA: Alterar status para PAGO após pagamento confirmado
        inscricao.setStatus(Inscricao.StatusInscricao.PAGO);
        inscricao.setPagamento(pagamento);
        
        return inscricaoRepository.save(inscricao);
    }
    
    /**
     * REGRA: Cancelar inscrição
     * - Se curso não começou, gera reembolso total
     */
    @Transactional
    public ReembolsoDTO cancelarInscricao(Long inscricaoId) {
        Inscricao inscricao = inscricaoRepository.findById(inscricaoId)
            .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
        
        if (inscricao.getStatus() == Inscricao.StatusInscricao.CANCELADA) {
            throw new RuntimeException("Inscrição já está cancelada");
        }
        
        Curso curso = inscricao.getCurso();
        BigDecimal valorReembolso = BigDecimal.ZERO;
        
        // REGRA: Reembolso total se curso não começou
        if (!curso.isCursoComecou() && inscricao.getPagamento() != null) {
            valorReembolso = inscricao.getPagamento().getValor();
            inscricao.getPagamento().setStatus(Pagamento.StatusPagamento.CANCELADO);
            inscricao.setStatus(Inscricao.StatusInscricao.REEMBOLSADA);
        } else {
            inscricao.setStatus(Inscricao.StatusInscricao.CANCELADA);
        }
        
        inscricaoRepository.save(inscricao);
        
        return new ReembolsoDTO(
            inscricao.getId(),
            inscricao.getPagamento() != null ? inscricao.getPagamento().getId() : null,
            inscricao.getAluno().getNome(),
            curso.getNome(),
            valorReembolso,
            "Cancelamento solicitado pelo aluno"
        );
    }
    
    /**
     * REGRA: Transferir para outro curso
     * - Só permitido se houver vaga no novo curso
     */
    @Transactional
    public TransferenciaDTO transferirCurso(Long inscricaoId, Long novoCursoId) {
        Inscricao inscricaoAtual = inscricaoRepository.findById(inscricaoId)
            .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
        
        Curso novoCurso = cursoRepository.findById(novoCursoId)
            .orElseThrow(() -> new RuntimeException("Novo curso não encontrado"));
        
        // REGRA: Verificar vaga no novo curso
        if (!novoCurso.temVagasDisponiveis()) {
            throw new RuntimeException("Curso de destino sem vagas disponíveis");
        }
        
        // Verificar se não está ativo no novo curso
        if (!novoCurso.getAtivo()) {
            throw new RuntimeException("Curso de destino está inativo");
        }
        
        String cursoAntigoNome = inscricaoAtual.getCurso().getNome();
        
        // Cancelar inscrição atual
        inscricaoAtual.setStatus(Inscricao.StatusInscricao.CANCELADA);
        inscricaoRepository.save(inscricaoAtual);
        
        // Criar nova inscrição
        Inscricao novaInscricao = new Inscricao(inscricaoAtual.getAluno(), novoCurso);
        
        // Transferir pagamento se existir
        if (inscricaoAtual.getPagamento() != null && 
            inscricaoAtual.getPagamento().getStatus() == Pagamento.StatusPagamento.APROVADO) {
            novaInscricao.setStatus(Inscricao.StatusInscricao.PAGO);
        } else {
            novaInscricao.setStatus(Inscricao.StatusInscricao.PENDENTE);
        }
        
        inscricaoRepository.save(novaInscricao);
        
        return new TransferenciaDTO(
            inscricaoAtual.getId(),
            novoCurso.getId(),
            inscricaoAtual.getAluno().getNome(),
            cursoAntigoNome,
            novoCurso.getNome()
        );
    }
    
    @Transactional
    public void deletar(Long id) {
        inscricaoRepository.deleteById(id);
    }
}
