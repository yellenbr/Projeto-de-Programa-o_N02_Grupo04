package com.veridia.gestao.plataformacursos.service;

import com.veridia.gestao.plataformacursos.dto.ReembolsoDTO;
import com.veridia.gestao.plataformacursos.model.Curso;
import com.veridia.gestao.plataformacursos.model.Inscricao;
import com.veridia.gestao.plataformacursos.model.Instrutor;
import com.veridia.gestao.plataformacursos.model.Pagamento;
import com.veridia.gestao.plataformacursos.repository.CursoRepository;
import com.veridia.gestao.plataformacursos.repository.InscricaoRepository;
import com.veridia.gestao.plataformacursos.repository.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstrutorService {
    
    @Autowired
    private InstrutorRepository instrutorRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private InscricaoRepository inscricaoRepository;
    
    public List<Instrutor> listarTodos() {
        return instrutorRepository.findAll();
    }
    
    public Optional<Instrutor> buscarPorId(Long id) {
        return instrutorRepository.findById(id);
    }
    
    public Optional<Instrutor> buscarPorEmail(String email) {
        return instrutorRepository.findByEmail(email);
    }
    
    public Instrutor salvar(Instrutor instrutor) {
        if (instrutorRepository.existsByEmail(instrutor.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        return instrutorRepository.save(instrutor);
    }
    
    public Instrutor atualizar(Long id, Instrutor instrutorAtualizado) {
        return instrutorRepository.findById(id)
            .map(instrutor -> {
                instrutor.setNome(instrutorAtualizado.getNome());
                instrutor.setEmail(instrutorAtualizado.getEmail());
                instrutor.setEspecialidade(instrutorAtualizado.getEspecialidade());
                return instrutorRepository.save(instrutor);
            })
            .orElseThrow(() -> new RuntimeException("Instrutor não encontrado"));
    }
    
    /**
     * REGRA: Instrutor escolhe administrar um curso
     * - Um curso só pode ter 1 instrutor
     * - Um instrutor pode administrar vários cursos
     */
    @Transactional
    public Curso vincularCurso(Long instrutorId, Long cursoId) {
        Instrutor instrutor = instrutorRepository.findById(instrutorId)
            .orElseThrow(() -> new RuntimeException("Instrutor não encontrado"));
        
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        
        // REGRA: Um curso só tem 1 instrutor
        if (curso.getInstrutor() != null) {
            throw new RuntimeException("Este curso já possui um instrutor vinculado");
        }
        
        curso.setInstrutor(instrutor);
        return cursoRepository.save(curso);
    }
    
    /**
     * REGRA: Cancelar curso
     * - Notifica alunos (via console)
     * - Gera reembolso automático para todos os alunos inscritos
     */
    @Transactional
    public List<ReembolsoDTO> cancelarCurso(Long instrutorId, Long cursoId) {
        Instrutor instrutor = instrutorRepository.findById(instrutorId)
            .orElseThrow(() -> new RuntimeException("Instrutor não encontrado"));
        
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        
        // REGRA: Verificar se o instrutor é o responsável pelo curso
        if (curso.getInstrutor() == null || !curso.getInstrutor().getId().equals(instrutorId)) {
            throw new RuntimeException("Instrutor não está vinculado a este curso");
        }
        
        if (!curso.getAtivo()) {
            throw new RuntimeException("Curso já está cancelado");
        }
        
        // Buscar todas as inscrições ativas do curso
        List<Inscricao> inscricoes = inscricaoRepository.findByCurso_Id(cursoId);
        List<ReembolsoDTO> reembolsos = new ArrayList<>();
        
        // REGRA: Reembolso automático para todos os alunos
        for (Inscricao inscricao : inscricoes) {
            if (inscricao.getStatus() != Inscricao.StatusInscricao.CANCELADA) {
                
                // Notificar aluno (via console)
                System.out.println("========================================");
                System.out.println("NOTIFICAÇÃO: Cancelamento de Curso");
                System.out.println("========================================");
                System.out.println("Aluno: " + inscricao.getAluno().getNome());
                System.out.println("Email: " + inscricao.getAluno().getEmail());
                System.out.println("Curso: " + curso.getNome());
                System.out.println("Motivo: Cancelamento pelo instrutor");
                System.out.println("Status: Reembolso será processado");
                System.out.println("========================================\n");
                
                // Processar reembolso
                if (inscricao.getPagamento() != null) {
                    Pagamento pagamento = inscricao.getPagamento();
                    pagamento.setStatus(Pagamento.StatusPagamento.CANCELADO);
                    
                    ReembolsoDTO reembolso = new ReembolsoDTO(
                        inscricao.getId(),
                        pagamento.getId(),
                        inscricao.getAluno().getNome(),
                        curso.getNome(),
                        pagamento.getValor(),
                        "Curso cancelado pelo instrutor"
                    );
                    reembolsos.add(reembolso);
                }
                
                // Atualizar status da inscrição
                inscricao.setStatus(Inscricao.StatusInscricao.REEMBOLSADA);
                inscricaoRepository.save(inscricao);
            }
        }
        
        // Desativar o curso
        curso.setAtivo(false);
        cursoRepository.save(curso);
        
        System.out.println("========================================");
        System.out.println("CURSO CANCELADO COM SUCESSO");
        System.out.println("========================================");
        System.out.println("Curso: " + curso.getNome());
        System.out.println("Instrutor: " + instrutor.getNome());
        System.out.println("Total de alunos reembolsados: " + reembolsos.size());
        System.out.println("========================================\n");
        
        return reembolsos;
    }
    
    /**
     * Listar cursos de um instrutor
     */
    public List<Curso> listarCursosDoInstrutor(Long instrutorId) {
        Instrutor instrutor = instrutorRepository.findById(instrutorId)
            .orElseThrow(() -> new RuntimeException("Instrutor não encontrado"));
        
        return cursoRepository.findByInstrutor_Id(instrutorId);
    }
    
    public void deletar(Long id) {
        instrutorRepository.deleteById(id);
    }
}
