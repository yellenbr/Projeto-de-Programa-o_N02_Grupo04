package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.model.Instrutor;
import com.veridia.gestao.plataformacursos.repository.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instrutores")
@CrossOrigin(origins = "*")
public class InstrutorController {

    @Autowired
    private InstrutorRepository instrutorRepository;

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
}
