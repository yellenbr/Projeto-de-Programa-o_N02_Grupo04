package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.model.Curso;
import com.veridia.gestao.plataformacursos.model.Instrutor;
import com.veridia.gestao.plataformacursos.service.InstrutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/instrutor")
public class InstrutorController {

    @Autowired
    private InstrutorService instrutorService;

    @GetMapping
    public List<Instrutor> getAll() {
        return instrutorService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Instrutor> getById(@PathVariable Long id) {
        return instrutorService.getById(id);
    }

    @PostMapping
    public Instrutor create(@RequestBody Instrutor instrutor) {
        return instrutorService.save(instrutor);
    }

    @PutMapping("/{id}")
    public Instrutor update(@PathVariable Long id, @RequestBody Instrutor instrutor) {
        instrutor.setId(id);
        return instrutorService.save(instrutor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        instrutorService.delete(id);
    }

    @GetMapping("/{id}/cursos")
    public List<Curso> exibirCursosMinistrados(@PathVariable Long id) {
        return instrutorService.exibirCursosMinistrados(id);
    }

    @PostMapping("/{instrutorId}/curso/{cursoId}")
    public Curso escolherCurso(@PathVariable Long instrutorId, @PathVariable Long cursoId) {
        return instrutorService.escolherCurso(instrutorId, cursoId);
    }

    @DeleteMapping("/{instrutorId}/curso/{cursoId}")
    public void cancelarCurso(@PathVariable Long instrutorId, @PathVariable Long cursoId) {
        instrutorService.cancelarCurso(instrutorId, cursoId);
    }

    @PostMapping("/{instrutorId}/curso/{cursoId}/nota")
    public Curso cadastrarNota(@PathVariable Long instrutorId,
                               @PathVariable Long cursoId,
                               @RequestBody Double nota) {
        return instrutorService.cadastrarNotas(instrutorId, cursoId, nota);
    }

}
