package com.veridia.gestao.plataformacursos.Controller;

import com.veridia.gestao.plataformacursos.Service.InstrutorService;
import com.veridia.gestao.plataformacursos.Model.Instrutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/instrutores")
public class InstrutorController {

    @Autowired
    private InstrutorService instrutorService;

    @PostMapping
    public ResponseEntity<Instrutor> criarInstrutor(@RequestBody Instrutor instrutor) {
        Instrutor novoInstrutor = instrutorService.salvar(instrutor);
        return new ResponseEntity<>(novoInstrutor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Instrutor>> listarTodos() {
        List<Instrutor> instrutores = instrutorService.buscarTodos();
        return new ResponseEntity<>(instrutores, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instrutor> buscarPorId(@PathVariable Long id) {
        Optional<Instrutor> instrutor = instrutorService.buscarPorId(id);
        return instrutor.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instrutor> atualizarInstrutor(@PathVariable Long id, @RequestBody Instrutor instrutor) {
        Instrutor instrutorAtualizado = instrutorService.atualizar(id, instrutor);
        return new ResponseEntity<>(instrutorAtualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInstrutor(@PathVariable Long id) {
        instrutorService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}