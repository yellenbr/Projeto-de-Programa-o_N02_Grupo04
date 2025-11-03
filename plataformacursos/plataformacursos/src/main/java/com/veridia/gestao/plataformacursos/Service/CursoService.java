package com.veridia.gestao.plataformacursos.Service;

import com.veridia.gestao.plataformacursos.model.Curso;
import com.veridia.gestao.plataformacursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public Optional<Curso> buscarPorId(Long id) {
        return cursoRepository.findById(id);
    }

    public List<Curso> buscarPorNome(String nome) {
        return cursoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Curso> buscarPorInstrutor(Long instrutorId) {
        return cursoRepository.findByInstrutor_Id(instrutorId);
    }

    public Curso salvar(Curso curso) {
        return cursoRepository.save(curso);
    }

    public Curso atualizar(Long id, Curso cursoAtualizado) {
        return cursoRepository.findById(id)
                .map(curso -> {
                    curso.setNome(cursoAtualizado.getNome());
                    curso.setDescricao(cursoAtualizado.getDescricao());
                    curso.setPreco(cursoAtualizado.getPreco());
                    curso.setCargaHoraria(cursoAtualizado.getCargaHoraria());
                    curso.setInstrutor(cursoAtualizado.getInstrutor());
                    return cursoRepository.save(curso);
                })
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
    }

    public void deletar(Long id) {
        cursoRepository.deleteById(id);
    }
}