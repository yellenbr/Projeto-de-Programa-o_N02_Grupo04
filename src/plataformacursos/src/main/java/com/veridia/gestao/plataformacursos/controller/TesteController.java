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

    @GetMapping("/dados")
    public ResponseEntity<Map<String, Object>> getTodosOsDados() {
        try {
            List<Aluno> alunos = alunoRepository.findAll();
            List<Curso> cursos = cursoRepository.findAll();
            List<Instrutor> instrutores = instrutorRepository.findAll();
            List<Inscricao> inscricoes = inscricaoRepository.findAll();
            List<Pagamento> pagamentos = pagamentoRepository.findAll();

            // Criar listas simplificadas para evitar referências cíclicas
            List<Map<String, Object>> alunosSimples = alunos.stream()
                    .map(a -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", a.getId());
                        map.put("nome", a.getNome());
                        map.put("email", a.getEmail());
                        map.put("cpf", a.getCpf());
                        return map;
                    })
                    .collect(Collectors.toList());

            List<Map<String, Object>> cursosSimples = cursos.stream()
                    .map(c -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", c.getId());
                        map.put("nome", c.getNome());
                        map.put("descricao", c.getDescricao());
                        map.put("preco", c.getPreco());
                        map.put("cargaHoraria", c.getCargaHoraria());
                        map.put("limiteVagas", c.getLimiteVagas());
                        map.put("ativo", c.getAtivo());
                        if (c.getInstrutor() != null) {
                            map.put("instrutorId", c.getInstrutor().getId());
                            map.put("instrutorNome", c.getInstrutor().getNome());
                        }
                        return map;
                    })
                    .collect(Collectors.toList());

            List<Map<String, Object>> instrutoresSimples = instrutores.stream()
                    .map(i -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", i.getId());
                        map.put("nome", i.getNome());
                        map.put("email", i.getEmail());
                        map.put("especialidade", i.getEspecialidade());
                        return map;
                    })
                    .collect(Collectors.toList());

            List<Map<String, Object>> inscricoesSimples = inscricoes.stream()
                    .map(insc -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", insc.getId());
                        map.put("alunoId", insc.getAluno().getId());
                        map.put("alunoNome", insc.getAluno().getNome());
                        map.put("cursoId", insc.getCurso().getId());
                        map.put("cursoNome", insc.getCurso().getNome());
                        map.put("status", insc.getStatus().toString());
                        map.put("dataInscricao", insc.getDataInscricao());
                        return map;
                    })
                    .collect(Collectors.toList());

            List<Map<String, Object>> pagamentosSimples = pagamentos.stream()
                    .map(p -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", p.getId());
                        map.put("inscricaoId", p.getInscricao().getId());
                        map.put("valor", p.getValor());
                        map.put("metodoPagamento", p.getMetodoPagamento());
                        map.put("status", p.getStatus().toString());
                        map.put("dataPagamento", p.getDataPagamento());
                        return map;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("alunos", alunosSimples);
            response.put("cursos", cursosSimples);
            response.put("instrutores", instrutoresSimples);
            response.put("inscricoes", inscricoesSimples);
            response.put("pagamentos", pagamentosSimples);
            response.put("totais", Map.of(
                    "alunos", alunos.size(),
                    "cursos", cursos.size(),
                    "instrutores", instrutores.size(),
                    "inscricoes", inscricoes.size(),
                    "pagamentos", pagamentos.size()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("erro", "Erro ao carregar dados");
            error.put("mensagem", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
