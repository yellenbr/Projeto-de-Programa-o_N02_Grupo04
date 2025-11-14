package com.veridia.gestao.plataformacursos.controller;

import com.veridia.gestao.plataformacursos.model.Inscricao;
import com.veridia.gestao.plataformacursos.repository.InscricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscricoes")
@CrossOrigin(origins = "*")
public class InscricaoController {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @GetMapping
    public List<Inscricao> listarTodas() {
        return inscricaoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscricao> buscarPorId(@PathVariable Long id) {
        return inscricaoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Inscricao criar(@RequestBody Inscricao inscricao) {
        return inscricaoRepository.save(inscricao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (inscricaoRepository.existsById(id)) {
            inscricaoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
