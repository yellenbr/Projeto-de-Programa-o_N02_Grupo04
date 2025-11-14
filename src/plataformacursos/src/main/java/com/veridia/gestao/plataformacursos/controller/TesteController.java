package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.model.Aluno;
import com.veridia.gestao.plataformacursos.model.Curso;
import com.veridia.gestao.plataformacursos.model.Inscricao;
import com.veridia.gestao.plataformacursos.model.Instrutor;
import com.veridia.gestao.plataformacursos.model.Pagamento;
import com.veridia.gestao.plataformacursos.repository.AlunoRepository;
import com.veridia.gestao.plataformacursos.repository.CursoRepository;
import com.veridia.gestao.plataformacursos.repository.InstrutorRepository;
import com.veridia.gestao.plataformacursos.repository.InscricaoRepository;
import com.veridia.gestao.plataformacursos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teste")
@CrossOrigin(origins = "*")
public class TesteController {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("alunos", alunoRepository.count());
        status.put("cursos", cursoRepository.count());
        status.put("instrutores", instrutorRepository.count());
        status.put("inscricoes", inscricaoRepository.count());
        status.put("status", "online");
        return status;
    }

    @GetMapping("/pagamentos")
    public List<Pagamento> listarPagamentos() {
        return pagamentoRepository.findAll();
    }

    @GetMapping("/inscricoes")
    public List<Inscricao> listarInscricoes() {
        return inscricaoRepository.findAll();
    }

    @GetMapping("/aluno/{id}/detalhes")
    public ResponseEntity<Map<String, Object>> getDetalhesAluno(@PathVariable Long id) {
        Aluno aluno = alunoRepository.findById(id).orElse(null);
        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }

        List<Inscricao> inscricoes = inscricaoRepository.findAll().stream()
                .filter(i -> i.getAluno().getId().equals(id))
                .collect(Collectors.toList());

        long cursosAtivos = inscricoes.stream()
                .filter(i -> i.getStatus() == Inscricao.StatusInscricao.ATIVA)
                .count();

        long cursosPendentes = inscricoes.stream()
                .filter(i -> i.getStatus() == Inscricao.StatusInscricao.PENDENTE)
                .count();

        long cursosConcluidos = inscricoes.stream()
                .filter(i -> i.getStatus() == Inscricao.StatusInscricao.CONCLUIDA)
                .count();

        Map<String, Object> response = new HashMap<>();
        response.put("aluno", aluno);
        response.put("inscricoes", inscricoes);
        response.put("numeroCursosAtivos", cursosAtivos);
        response.put("cursosPendentes", cursosPendentes);
        response.put("cursosConcluidos", cursosConcluidos);
        response.put("temInscricoesPendentes", cursosPendentes > 0);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/curso/{id}/detalhes")
    public ResponseEntity<Map<String, Object>> getDetalhesCurso(@PathVariable Long id) {
        Curso curso = cursoRepository.findById(id).orElse(null);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        Instrutor instrutor = curso.getInstrutor();
        
        long numeroInscritos = inscricaoRepository.findAll().stream()
                .filter(i -> i.getCurso().getId().equals(id))
                .filter(i -> i.getStatus() != Inscricao.StatusInscricao.CANCELADA)
                .count();

        boolean vagasDisponiveis = curso.getLimiteVagas() == null || numeroInscritos < curso.getLimiteVagas();

        Map<String, Object> response = new HashMap<>();
        response.put("curso", curso);
        response.put("instrutor", instrutor);
        response.put("numeroInscritos", numeroInscritos);
        response.put("vagasDisponiveis", vagasDisponiveis);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/inscricao/{id}/detalhes")
    public ResponseEntity<Map<String, Object>> getDetalhesInscricao(@PathVariable Long id) {
        Inscricao inscricao = inscricaoRepository.findById(id).orElse(null);
        if (inscricao == null) {
            return ResponseEntity.notFound().build();
        }

        Pagamento pagamento = pagamentoRepository.findAll().stream()
                .filter(p -> p.getInscricao().getId().equals(id))
                .findFirst()
                .orElse(null);

        boolean isAtiva = inscricao.getStatus() == Inscricao.StatusInscricao.ATIVA;
        boolean podeCancelar = inscricao.getStatus() != Inscricao.StatusInscricao.CANCELADA 
                && inscricao.getStatus() != Inscricao.StatusInscricao.CONCLUIDA;
        boolean temDireitoReembolso = pagamento != null && pagamento.getStatus() == Pagamento.StatusPagamento.CONFIRMADO;

        Map<String, Object> response = new HashMap<>();
        response.put("inscricao", inscricao);
        response.put("aluno", inscricao.getAluno());
        response.put("curso", inscricao.getCurso());
        response.put("pagamento", pagamento);
        response.put("status", inscricao.getStatus().toString());
        response.put("isAtiva", isAtiva);
        response.put("podeCancelar", podeCancelar);
        response.put("temDireitoReembolso", temDireitoReembolso);

        return ResponseEntity.ok(response);
    }
}
