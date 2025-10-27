package com.veridia.gestao.plataformacursos.service;

import com.veridia.gestao.plataformacursos.dto.ReembolsoDTO;
import com.veridia.gestao.plataformacursos.dto.TransferenciaDTO;
import com.veridia.gestao.plataformacursos.exception.NegocioException;
import com.veridia.gestao.plataformacursos.exception.RecursoNaoEncontradoException;
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
            .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + alunoId));
        
        // Buscar curso
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado com ID: " + cursoId));
        
        // Verificar se o aluno pode se inscrever
        if (!aluno.podeSeInscrever(curso)) {
            if (inscricaoRepository.existsByAluno_IdAndCurso_IdAndStatusNot(
                alunoId, cursoId, Inscricao.StatusInscricao.CANCELADA)) {
                throw new NegocioException("Aluno já está inscrito neste curso");
            }
            throw new NegocioException("Curso sem vagas disponíveis ou inativo");
        }
        
        // Reservar vaga no curso (valida automaticamente)
        curso.reservarVaga();
        
        // Criar e salvar inscrição
        Inscricao inscricao = new Inscricao(aluno, curso);
        
        return inscricaoRepository.save(inscricao);
    }
    
    /**
     * REGRA: Processar pagamento da inscrição
     * - Pagamento confirmado altera status para PAGO
     */
    @Transactional
    public Inscricao processarPagamento(Long inscricaoId, Pagamento.MetodoPagamento metodoPagamento) {
        Inscricao inscricao = inscricaoRepository.findById(inscricaoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Inscrição não encontrada com ID: " + inscricaoId));
        
        // Criar pagamento
        BigDecimal valor = inscricao.getCurso().getPreco();
        Pagamento pagamento = new Pagamento(inscricao, valor, metodoPagamento);
        
        // Processar e aprovar pagamento
        pagamento.processar();
        pagamento.aprovar();
        pagamentoRepository.save(pagamento);
        
        // Confirmar pagamento na inscrição (usa método do model)
        inscricao.confirmarPagamento(pagamento);
        
        return inscricaoRepository.save(inscricao);
    }
    
    /**
     * REGRA: Cancelar inscrição
     * - Se curso não começou, gera reembolso total
     */
    @Transactional
    public ReembolsoDTO cancelarInscricao(Long inscricaoId) {
        Inscricao inscricao = inscricaoRepository.findById(inscricaoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Inscrição não encontrada com ID: " + inscricaoId));
        
        // Cancelar usando método do model (encapsula lógica)
        boolean temReembolso = inscricao.cancelar();
        
        Curso curso = inscricao.getCurso();
        BigDecimal valorReembolso = BigDecimal.ZERO;
        
        // Se houve reembolso, obter valor
        if (temReembolso && inscricao.getPagamento() != null) {
            valorReembolso = inscricao.getPagamento().getValor();
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
            .orElseThrow(() -> new RecursoNaoEncontradoException("Inscrição não encontrada com ID: " + inscricaoId));
        
        Curso novoCurso = cursoRepository.findById(novoCursoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Novo curso não encontrado com ID: " + novoCursoId));
        
        // Validar se pode transferir usando método do curso
        novoCurso.reservarVaga();
        
        String cursoAntigoNome = inscricaoAtual.getCurso().getNome();
        
        // Cancelar inscrição atual (sem gerar reembolso neste caso)
        inscricaoAtual.setStatus(Inscricao.StatusInscricao.CANCELADA);
        inscricaoRepository.save(inscricaoAtual);
        
        // Criar nova inscrição
        Inscricao novaInscricao = new Inscricao(inscricaoAtual.getAluno(), novoCurso);
        
        // Transferir status de pagamento se existir
        if (inscricaoAtual.getPagamento() != null && 
            inscricaoAtual.getPagamento().isAprovado()) {
            // Criar novo pagamento para a nova inscrição
            Pagamento novoPagamento = new Pagamento(
                novaInscricao, 
                novoCurso.getPreco(), 
                inscricaoAtual.getPagamento().getMetodoPagamento()
            );
            novoPagamento.aprovar();
            pagamentoRepository.save(novoPagamento);
            
            novaInscricao.confirmarPagamento(novoPagamento);
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
