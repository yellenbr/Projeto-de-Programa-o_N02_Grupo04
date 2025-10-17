package com.veridia.plataforma.api.controller;

import com.veridia.plataforma.domain.model.Curso;
import com.veridia.plataforma.service.CursoService;
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
    public List<Curso> listar() {
        return cursoService.buscarTodos();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        return cursoService.buscarPorId(id)
                .map(curso -> ResponseEntity.ok(curso)) // Retorna 200 OK se encontrar
                .orElse(ResponseEntity.notFound().build()); // Retorna 404 Not Found se não encontrar
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Retorna 201 Created
    public Curso criar(@RequestBody Curso curso) {
        return cursoService.salvar(curso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!cursoService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build(); // Retorna 404 se não existir
        }
        cursoService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}