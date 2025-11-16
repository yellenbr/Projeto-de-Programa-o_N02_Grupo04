package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.dto.CursoComAlunosDTO;
import com.veridia.gestao.plataformacursos.dto.DashboardInstrutorDTO;
import com.veridia.gestao.plataformacursos.model.Aluno;
import com.veridia.gestao.plataformacursos.model.Curso;
import com.veridia.gestao.plataformacursos.model.Inscricao;
import com.veridia.gestao.plataformacursos.model.Instrutor;
import com.veridia.gestao.plataformacursos.model.Pagamento;
import com.veridia.gestao.plataformacursos.repository.CursoRepository;
import com.veridia.gestao.plataformacursos.repository.InscricaoRepository;
import com.veridia.gestao.plataformacursos.repository.InstrutorRepository;
import com.veridia.gestao.plataformacursos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/instrutores")
@CrossOrigin(origins = "*")
public class InstrutorController {

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @GetMapping
    public List<Instrutor> listarTodos() {
        return instrutorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instrutor> buscarPorId(@PathVariable Long id) {
        return instrutorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Instrutor criar(@RequestBody Instrutor instrutor) {
        return instrutorRepository.save(instrutor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instrutor> atualizar(@PathVariable Long id, @RequestBody Instrutor instrutorAtualizado) {
        return instrutorRepository.findById(id)
                .map(instrutor -> {
                    instrutor.setNome(instrutorAtualizado.getNome());
                    instrutor.setEmail(instrutorAtualizado.getEmail());
                    instrutor.setEspecialidade(instrutorAtualizado.getEspecialidade());
                    if (instrutorAtualizado.getSenha() != null) {
                        instrutor.setSenha(instrutorAtualizado.getSenha());
                    }
                    return ResponseEntity.ok(instrutorRepository.save(instrutor));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (instrutorRepository.existsById(id)) {
            instrutorRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<DashboardInstrutorDTO> getDashboard(@PathVariable Long id) {
        Instrutor instrutor = instrutorRepository.findById(id).orElse(null);
        if (instrutor == null) {
            return ResponseEntity.notFound().build();
        }

        List<Curso> cursos = cursoRepository.findAll().stream()
                .filter(c -> c.getInstrutor() != null && c.getInstrutor().getId().equals(id))
                .collect(Collectors.toList());

        Long totalCursos = (long) cursos.size();

        Long totalAlunos = cursos.stream()
                .flatMap(curso -> inscricaoRepository.findByCurso_Id(curso.getId()).stream())
                .filter(insc -> insc.getStatus() == Inscricao.StatusInscricao.ATIVA || 
                               insc.getStatus() == Inscricao.StatusInscricao.CONCLUIDA)
                .map(Inscricao::getAluno)
                .distinct()
                .count();

        BigDecimal receitaTotal = cursos.stream()
                .flatMap(curso -> inscricaoRepository.findByCurso_Id(curso.getId()).stream())
                .filter(insc -> insc.getStatus() == Inscricao.StatusInscricao.ATIVA || 
                               insc.getStatus() == Inscricao.StatusInscricao.CONCLUIDA)
                .flatMap(insc -> {
                    List<Pagamento> pagamentos = pagamentoRepository.findAll().stream()
                            .filter(p -> p.getInscricao().getId().equals(insc.getId()))
                            .filter(p -> p.getStatus() == Pagamento.StatusPagamento.CONFIRMADO)
                            .collect(Collectors.toList());
                    return pagamentos.stream().map(Pagamento::getValor);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DashboardInstrutorDTO dashboard = new DashboardInstrutorDTO(totalCursos, totalAlunos, receitaTotal);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/{id}/cursos")
    public ResponseEntity<List<CursoComAlunosDTO>> getCursosComAlunos(@PathVariable Long id) {
        List<Curso> cursos = cursoRepository.findAll().stream()
                .filter(c -> c.getInstrutor() != null && c.getInstrutor().getId().equals(id))
                .collect(Collectors.toList());

        List<CursoComAlunosDTO> cursosDTO = cursos.stream()
                .map(curso -> {
                    Long totalAlunos = inscricaoRepository.findByCurso_Id(curso.getId()).stream()
                            .filter(insc -> insc.getStatus() == Inscricao.StatusInscricao.ATIVA || 
                                           insc.getStatus() == Inscricao.StatusInscricao.CONCLUIDA)
                            .count();
                    BigDecimal preco = curso.getPreco() != null ? BigDecimal.valueOf(curso.getPreco()) : BigDecimal.ZERO;
                    return new CursoComAlunosDTO(curso.getId(), curso.getNome(), preco, totalAlunos);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(cursosDTO);
    }

    @GetMapping("/{instrutorId}/cursos/{cursoId}/alunos")
    public ResponseEntity<List<Map<String, Object>>> getAlunosDoCurso(
            @PathVariable Long instrutorId, 
            @PathVariable Long cursoId) {
        
        Curso curso = cursoRepository.findById(cursoId).orElse(null);
        if (curso == null || curso.getInstrutor() == null || !curso.getInstrutor().getId().equals(instrutorId)) {
            return ResponseEntity.notFound().build();
        }

        List<Inscricao> inscricoes = inscricaoRepository.findByCurso_Id(cursoId).stream()
                .filter(insc -> insc.getStatus() == Inscricao.StatusInscricao.ATIVA || 
                               insc.getStatus() == Inscricao.StatusInscricao.CONCLUIDA)
                .collect(Collectors.toList());

        List<Map<String, Object>> alunos = inscricoes.stream()
                .map(insc -> {
                    Aluno aluno = insc.getAluno();
                    Map<String, Object> alunoInfo = new HashMap<>();
                    alunoInfo.put("id", aluno.getId());
                    alunoInfo.put("nome", aluno.getNome());
                    alunoInfo.put("email", aluno.getEmail());
                    alunoInfo.put("cpf", aluno.getCpf());
                    alunoInfo.put("status", insc.getStatus().toString());
                    alunoInfo.put("dataInscricao", insc.getDataInscricao());
                    return alunoInfo;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(alunos);
    }
}
