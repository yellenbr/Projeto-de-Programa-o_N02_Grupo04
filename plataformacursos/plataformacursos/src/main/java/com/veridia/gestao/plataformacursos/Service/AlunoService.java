package com.veridia.gestao.plataformacursos.service;

import com.veridia.gestao.plataformacursos.exception.NegocioException;
import com.veridia.gestao.plataformacursos.exception.RecursoNaoEncontradoException;
import com.veridia.gestao.plataformacursos.model.Aluno;
import com.veridia.gestao.plataformacursos.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return alunoRepository.findById(id);
    }

    public Optional<Aluno> buscarPorEmail(String email) {
        return alunoRepository.findByEmail(email);
    }

    public Aluno salvar(Aluno aluno) {
        if (alunoRepository.existsByEmail(aluno.getEmail())) {
            throw new NegocioException("Email já cadastrado: " + aluno.getEmail());
        }
        if (alunoRepository.existsByCpf(aluno.getCpf())) {
            throw new NegocioException("CPF já cadastrado: " + aluno.getCpf());
        }
        return alunoRepository.save(aluno);
    }

    public Aluno atualizar(Long id, Aluno alunoAtualizado) {
        return alunoRepository.findById(id)
                .map(aluno -> {
                    aluno.setNome(alunoAtualizado.getNome());
                    aluno.setEmail(alunoAtualizado.getEmail());
                    aluno.setCpf(alunoAtualizado.getCpf());
                    return alunoRepository.save(aluno);
                })
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + id));
    }

    public void deletar(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }
}