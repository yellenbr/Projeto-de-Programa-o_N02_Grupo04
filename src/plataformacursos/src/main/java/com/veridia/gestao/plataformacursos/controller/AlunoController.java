package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.model.Aluno;
import com.veridia.gestao.plataformacursos.model.Curso;
import com.veridia.gestao.plataformacursos.model.Inscricao;
import com.veridia.gestao.plataformacursos.model.Pagamento;
import com.veridia.gestao.plataformacursos.repository.AlunoRepository;
import com.veridia.gestao.plataformacursos.repository.CursoRepository;
import com.veridia.gestao.plataformacursos.repository.InscricaoRepository;
import com.veridia.gestao.plataformacursos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @GetMapping
    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        return alunoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Aluno aluno) {
        Map<String, String> resposta = new HashMap<>();

        if (aluno.getCpf() == null || aluno.getCpf().isBlank()) {
            resposta.put("mensagem", "CPF é obrigatório");
            return ResponseEntity.badRequest().body(resposta);
        }

        String cpfNormalizado = aluno.getCpf().replaceAll("\\D", "");
        if (cpfNormalizado.length() != 11) {
            resposta.put("mensagem", "CPF inválido");
            return ResponseEntity.badRequest().body(resposta);
        }

        if (alunoRepository.existsByCpf(cpfNormalizado)) {
            resposta.put("mensagem", "ja tem uma conta nesse CPF");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
        }

        if (aluno.getEmail() != null && alunoRepository.existsByEmailIgnoreCase(aluno.getEmail())) {
            resposta.put("mensagem", "Este email já está cadastrado");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
        }

        aluno.setCpf(cpfNormalizado);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoRepository.save(aluno));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(@PathVariable Long id, @RequestBody Aluno alunoAtualizado) {
        return alunoRepository.findById(id)
                .map(aluno -> {
                    aluno.setNome(alunoAtualizado.getNome());
                    aluno.setEmail(alunoAtualizado.getEmail());
                    aluno.setCpf(alunoAtualizado.getCpf());
                    return ResponseEntity.ok(alunoRepository.save(aluno));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{alunoId}/inscrever/{cursoId}")
    public ResponseEntity<Inscricao> inscreverEmCurso(@PathVariable Long alunoId, @PathVariable Long cursoId) {
        Aluno aluno = alunoRepository.findById(alunoId).orElse(null);
        Curso curso = cursoRepository.findById(cursoId).orElse(null);

        if (aluno == null || curso == null) {
            return ResponseEntity.notFound().build();
        }

        if (!curso.getAtivo()) {
            return ResponseEntity.badRequest().build();
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setAluno(aluno);
        inscricao.setCurso(curso);
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setStatus(Inscricao.StatusInscricao.PENDENTE);

        return ResponseEntity.ok(inscricaoRepository.save(inscricao));
    }

    @PostMapping("/{alunoId}/pagamento/{inscricaoId}")
    public ResponseEntity<?> processarPagamento(
            @PathVariable Long alunoId, 
            @PathVariable Long inscricaoId,
            @RequestParam String metodoPagamento) {
        
        Inscricao inscricao = inscricaoRepository.findById(inscricaoId).orElse(null);
        if (inscricao == null || !inscricao.getAluno().getId().equals(alunoId)) {
            Map<String, String> erro = new HashMap<>();
            erro.put("mensagem", "Inscrição não encontrada ou não pertence ao aluno");
            return ResponseEntity.badRequest().body(erro);
        }

        if (inscricao.getStatus() != Inscricao.StatusInscricao.PENDENTE) {
            Map<String, String> erro = new HashMap<>();
            erro.put("mensagem", "Esta inscrição já foi processada");
            return ResponseEntity.badRequest().body(erro);
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setInscricao(inscricao);
        pagamento.setValor(BigDecimal.valueOf(inscricao.getCurso().getPreco()));
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setStatus(Pagamento.StatusPagamento.CONFIRMADO);
        pagamento.setMetodoPagamento(metodoPagamento);

        inscricao.setStatus(Inscricao.StatusInscricao.ATIVA);
        inscricaoRepository.save(inscricao);

        return ResponseEntity.ok(pagamentoRepository.save(pagamento));
    }

    @PostMapping("/{alunoId}/inscricoes/{inscricaoId}/pagamento")
    public ResponseEntity<?> processarPagamentoInscricao(
            @PathVariable Long alunoId, 
            @PathVariable Long inscricaoId,
            @RequestParam String metodoPagamento) {
        
        return processarPagamento(alunoId, inscricaoId, metodoPagamento);
    }

    @PostMapping("/{alunoId}/inscricoes/{inscricaoId}/cancelar")
    public ResponseEntity<Map<String, Object>> cancelarInscricao(
            @PathVariable Long alunoId, 
            @PathVariable Long inscricaoId) {
        
        Inscricao inscricao = inscricaoRepository.findById(inscricaoId).orElse(null);
        if (inscricao == null || !inscricao.getAluno().getId().equals(alunoId)) {
            return ResponseEntity.notFound().build();
        }

        inscricao.setStatus(Inscricao.StatusInscricao.CANCELADA);
        inscricaoRepository.save(inscricao);

        Map<String, Object> response = new HashMap<>();
        response.put("valorReembolsado", 0.0);
        response.put("mensagem", "Inscrição cancelada com sucesso");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{alunoId}/inscricoes/{inscricaoId}/concluir")
    public ResponseEntity<Inscricao> concluirInscricao(
            @PathVariable Long alunoId, 
            @PathVariable Long inscricaoId) {
        
        Inscricao inscricao = inscricaoRepository.findById(inscricaoId).orElse(null);
        if (inscricao == null) {
            return ResponseEntity.notFound().build();
        }

        inscricao.setStatus(Inscricao.StatusInscricao.CONCLUIDA);
        return ResponseEntity.ok(inscricaoRepository.save(inscricao));
    }
}
