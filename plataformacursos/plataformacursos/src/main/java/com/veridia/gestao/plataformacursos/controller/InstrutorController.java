package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.dto.ReembolsoDTO;
import com.veridia.gestao.plataformacursos.model.Curso;
import com.veridia.gestao.plataformacursos.model.Instrutor;
import com.veridia.gestao.plataformacursos.service.InstrutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instrutores")
public class InstrutorController {
    
    @Autowired
    private InstrutorService instrutorService;
    
    
    @GetMapping
    public ResponseEntity<List<Instrutor>> listarTodos() {
        return ResponseEntity.ok(instrutorService.listarTodos());
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<Instrutor> buscarPorId(@PathVariable Long id) {
        return instrutorService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
   
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Instrutor instrutor) {
        try {
            Instrutor novoInstrutor = instrutorService.salvar(instrutor);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoInstrutor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Instrutor instrutor) {
        try {
            Instrutor instrutorAtualizado = instrutorService.atualizar(id, instrutor);
            return ResponseEntity.ok(instrutorAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/{id}/cursos/{cursoId}")
    public ResponseEntity<?> vincularCurso(@PathVariable Long id, @PathVariable Long cursoId) {
        try {
            Curso curso = instrutorService.vincularCurso(id, cursoId);
            return ResponseEntity.ok(curso);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}/cursos/{cursoId}")
    public ResponseEntity<?> cancelarCurso(@PathVariable Long id, @PathVariable Long cursoId) {
        try {
            List<ReembolsoDTO> reembolsos = instrutorService.cancelarCurso(id, cursoId);
            return ResponseEntity.ok(reembolsos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}/cursos")
    public ResponseEntity<List<Curso>> listarCursos(@PathVariable Long id) {
        try {
            List<Curso> cursos = instrutorService.listarCursosDoInstrutor(id);
            return ResponseEntity.ok(cursos);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        instrutorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
