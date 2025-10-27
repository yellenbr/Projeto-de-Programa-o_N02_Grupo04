package com.veridia.gestao.plataformacursos.Service;

import com.veridia.gestao.plataformacursos.Curso;
import com.veridia.gestao.plataformacursos.Repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> buscarTodos() {
        return cursoRepository.findAll();
    }

    public Optional<Curso> buscarPorId(Long id) {
        return cursoRepository.findById(id);
    }

    @Transactional
    public Curso salvar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Transactional
    public Curso atualizar(Long id, Curso cursoAtualizado) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + id));

        curso.setNome(cursoAtualizado.getNome());
        curso.setDescricao(cursoAtualizado.getDescricao());
        curso.setPreco(cursoAtualizado.getPreco());
        curso.setLimiteVagas(cursoAtualizado.getLimiteVagas());
        curso.setInstrutor(cursoAtualizado.getInstrutor());
        return cursoRepository.save(curso);
    }

    @Transactional
    public void deletar(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Curso não encontrado com ID: " + id);
        }
        cursoRepository.deleteById(id);
    }
}