package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.model.Curso;
import com.veridia.gestao.plataformacursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listarTodos() {
        return ResponseEntity.ok(cursoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        return cursoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Curso>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(cursoService.buscarPorNome(nome));
    }

    @GetMapping("/instrutor/{instrutorId}")
    public ResponseEntity<List<Curso>> buscarPorInstrutor(@PathVariable Long instrutorId) {
        return ResponseEntity.ok(cursoService.buscarPorInstrutor(instrutorId));
    }

    @PostMapping
    public ResponseEntity<Curso> criar(@RequestBody Curso curso) {
        Curso novoCurso = cursoService.salvar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCurso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> atualizar(@PathVariable Long id, @RequestBody Curso curso) {
        try {
            Curso cursoAtualizado = cursoService.atualizar(id, curso);
            return ResponseEntity.ok(cursoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}