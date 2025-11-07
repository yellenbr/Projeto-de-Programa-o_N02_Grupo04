package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.model.*;
import com.veridia.gestao.plataformacursos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para validação de persistência e relacionamentos JPA
 * Semana 3 - Testes de integração com banco de dados
 *
 * Endpoints disponíveis:
 * - GET /api/teste/status - Verifica status do banco
 * - GET /api/teste/dados - Lista todos os dados
 * - GET /api/teste/instrutores - Lista instrutores
 * - GET /api/teste/cursos - Lista cursos
 * - GET /api/teste/alunos - Lista alunos
 * - GET /api/teste/inscricoes - Lista inscrições
 * - GET /api/teste/pagamentos - Lista pagamentos
 * - GET /api/teste/curso/{id}/detalhes - Detalhes do curso com relacionamentos
 * - GET /api/teste/aluno/{id}/detalhes - Detalhes do aluno com relacionamentos
 */
@RestController
@RequestMapping("/api/teste")
public class TesteController {

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    /**
     * Verifica o status do banco de dados
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> verificarStatus() {
        Map<String, Object> status = new HashMap<>();

        status.put("instrutores", instrutorRepository.count());
        status.put("cursos", cursoRepository.count());
        status.put("alunos", alunoRepository.count());
        status.put("inscricoes", inscricaoRepository.count());
        status.put("pagamentos", pagamentoRepository.count());
        status.put("status", "✅ Banco de dados funcionando");
        status.put("h2Console", "http://localhost:8080/h2-console");
        status.put("jdbcUrl", "jdbc:h2:file:./data/plataformacursos");

        return ResponseEntity.ok(status);
    }

    /**
     * Lista todos os dados resumidos
     */
    @GetMapping("/dados")
    public ResponseEntity<Map<String, Object>> listarTodosDados() {
        Map<String, Object> dados = new HashMap<>();

        dados.put("instrutores", instrutorRepository.findAll());
        dados.put("cursos", cursoRepository.findAll());
        dados.put("alunos", alunoRepository.findAll());
        dados.put("inscricoes", inscricaoRepository.findAll());
        dados.put("pagamentos", pagamentoRepository.findAll());

        return ResponseEntity.ok(dados);
    }

    /**
     * Lista todos os instrutores
     */
    @GetMapping("/instrutores")
    public ResponseEntity<List<Instrutor>> listarInstrutores() {
        return ResponseEntity.ok(instrutorRepository.findAll());
    }

    /**
     * Lista todos os cursos
     */
    @GetMapping("/cursos")
    public ResponseEntity<List<Curso>> listarCursos() {
        return ResponseEntity.ok(cursoRepository.findAll());
    }

    /**
     * Lista todos os alunos
     */
    @GetMapping("/alunos")
    public ResponseEntity<List<Aluno>> listarAlunos() {
        return ResponseEntity.ok(alunoRepository.findAll());
    }

    /**
     * Lista todas as inscrições
     */
    @GetMapping("/inscricoes")
    public ResponseEntity<List<Inscricao>> listarInscricoes() {
        return ResponseEntity.ok(inscricaoRepository.findAll());
    }

    /**
     * Lista todos os pagamentos
     */
    @GetMapping("/pagamentos")
    public ResponseEntity<List<Pagamento>> listarPagamentos() {
        return ResponseEntity.ok(pagamentoRepository.findAll());
    }

    /**
     * Detalhes de um curso específico com relacionamentos
     */
    @GetMapping("/curso/{id}/detalhes")
    public ResponseEntity<Map<String, Object>> detalhesCurso(@PathVariable Long id) {
        return cursoRepository.findById(id)
                .map(curso -> {
                    Map<String, Object> detalhes = new HashMap<>();
                    detalhes.put("curso", curso);
                    detalhes.put("instrutor", curso.getInstrutor());
                    detalhes.put("numeroInscritos", curso.getNumeroInscritos());
                    detalhes.put("vagasDisponiveis", curso.temVagasDisponiveis());
                    detalhes.put("inscricoes", curso.getInscricoes());

                    return ResponseEntity.ok(detalhes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Detalhes de um aluno específico com relacionamentos
     */
    @GetMapping("/aluno/{id}/detalhes")
    public ResponseEntity<Map<String, Object>> detalhesAluno(@PathVariable Long id) {
        return alunoRepository.findById(id)
                .map(aluno -> {
                    Map<String, Object> detalhes = new HashMap<>();
                    detalhes.put("aluno", aluno);
                    detalhes.put("numeroCursosAtivos", aluno.getNumeroCursosAtivos());
                    detalhes.put("temInscricoesPendentes", aluno.temInscricoesPendentes());
                    detalhes.put("inscricoes", aluno.getInscricoes());

                    return ResponseEntity.ok(detalhes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Detalhes de uma inscrição específica com relacionamentos
     */
    @GetMapping("/inscricao/{id}/detalhes")
    public ResponseEntity<Map<String, Object>> detalhesInscricao(@PathVariable Long id) {
        return inscricaoRepository.findById(id)
                .map(inscricao -> {
                    Map<String, Object> detalhes = new HashMap<>();
                    detalhes.put("inscricao", inscricao);
                    detalhes.put("aluno", inscricao.getAluno());
                    detalhes.put("curso", inscricao.getCurso());
                    detalhes.put("pagamento", inscricao.getPagamento());
                    detalhes.put("status", inscricao.getStatus());
                    detalhes.put("isAtiva", inscricao.isAtiva());
                    detalhes.put("podeCancelar", inscricao.podeCancelar());
                    detalhes.put("temDireitoReembolso", inscricao.temDireitoReembolso());

                    return ResponseEntity.ok(detalhes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Testa a criação de novos registros
     */
    @PostMapping("/criar-aluno-teste")
    public ResponseEntity<Map<String, Object>> criarAlunoTeste() {
        Aluno aluno = new Aluno(
                "Teste Automatizado",
                "teste@veridia.com",
                "11122233344"
        );

        Aluno salvo = alunoRepository.save(aluno);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("mensagem", "✅ Aluno criado com sucesso!");
        resultado.put("aluno", salvo);
        resultado.put("id", salvo.getId());

        return ResponseEntity.ok(resultado);
    }
}