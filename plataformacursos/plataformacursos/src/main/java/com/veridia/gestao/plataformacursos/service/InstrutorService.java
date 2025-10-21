package com.veridia.gestao.plataformacursos.service;

import com.veridia.gestao.plataformacursos.model.Curso;
import com.veridia.gestao.plataformacursos.model.Instrutor;
import com.veridia.gestao.plataformacursos.repository.CursoRepository;
import com.veridia.gestao.plataformacursos.repository.InstrutorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstrutorService {

    private InstrutorRepository instrutorRepository;
    private CursoRepository cursoRepository;

    public InstrutorService(InstrutorRepository instrutorRepository, CursoRepository cursoRepository) {
        this.instrutorRepository = instrutorRepository;
        this.cursoRepository = cursoRepository;
    }

    public List<Instrutor> getAll() {
        return instrutorRepository.findAll();
    }

    public Optional<Instrutor> getById(Long id) {
        if (!instrutorRepository.existsById(id)) {
            throw new RuntimeException("Instrutor não encontrado.");
        }
        return instrutorRepository.findById(id);
    }

    public Instrutor save(Instrutor instrutor) {
        return instrutorRepository.save(instrutor);
    }

    public Instrutor update(Long id, Instrutor instrutor) {
        if (!instrutorRepository.existsById(id)) {
            throw new RuntimeException("Instrutor não encontrado.");
        }
        instrutor.setId(id);
        return instrutorRepository.save(instrutor);
    }

    public void delete(Long id) {
        if (instrutorRepository.existsById(id)) {
            instrutorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Instrutor não encontrado.");
        }
    }

    public List<Curso> exibirCursosMinistrados(Long instrutorId) {
        Instrutor instrutor = instrutorRepository.findById(instrutorId)
                .orElseThrow(() -> new RuntimeException("Instrutor não encontrado."));

        if (instrutor.getCursosMinistrados() == null) {
            instrutor.setCursosMinistrados(new ArrayList<>());
        }

        return instrutor.getCursosMinistrados();
    }

    public Curso escolherCurso(Long instrutorId, Long cursoId) {
        Instrutor instrutor = instrutorRepository.findById(instrutorId)
                .orElseThrow(() -> new RuntimeException("Instrutor não encontrado."));
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado."));

        curso.setProfessorAlocado(instrutor);

        if (instrutor.getCursosMinistrados() == null) {
            instrutor.setCursosMinistrados(new ArrayList<>());
        }

        if (!instrutor.getCursosMinistrados().contains(curso)) {
            instrutor.getCursosMinistrados().add(curso);
        }

        instrutorRepository.save(instrutor);
        return cursoRepository.save(curso);
    }

    public void cancelarCurso(Long instrutorId, Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado."));

        if (curso.getProfessorAlocado() == null || !curso.getProfessorAlocado().getId().equals(instrutorId)) {
            throw new RuntimeException("Instrutor não ministra esse curso.");
        }

        curso.setProfessorAlocado(null);
        cursoRepository.save(curso);
    }

    public Curso cadastrarNotas(Long instrutorId, Long cursoId, Double nota) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado."));

        if (curso.getProfessorAlocado() == null || !curso.getProfessorAlocado().getId().equals(instrutorId)) {
            throw new RuntimeException("Instrutor não autorizado a lançar nota nesse curso.");
        }

        curso.setAvaliacao(nota);
        return cursoRepository.save(curso);
    }


}
