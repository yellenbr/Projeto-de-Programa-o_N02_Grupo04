package com.veridia.plataforma.service;

import com.veridia.plataforma.domain.model.Curso;
import com.veridia.plataforma.domain.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired // Injeta o Repositório para acessar o banco de dados
    private CursoRepository cursoRepository;

    public List<Curso> buscarTodos() {
        return cursoRepository.findAll();
    }

    public Optional<Curso> buscarPorId(Long id) {
        return cursoRepository.findById(id);
    }

    public Curso salvar(Curso curso) {
        // **AQUI ENTRA A LÓGICA DE NEGÓCIO, EXEMPLO:**
        // 1. Validar se o preço é positivo.
        // 2. Garantir que o Instrutor existe, etc.

        return cursoRepository.save(curso);
    }

    public void deletar(Long id) {
        // **AQUI ENTRA A LÓGICA DE NEGÓCIO, EXEMPLO:**
        // 1. Verificar se há alunos inscritos antes de deletar.
        cursoRepository.deleteById(id);
    }
}