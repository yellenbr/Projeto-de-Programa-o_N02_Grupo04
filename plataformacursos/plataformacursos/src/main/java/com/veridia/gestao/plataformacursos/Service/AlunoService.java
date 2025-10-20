package com.veridia.gestao.plataformacursos.Service;

import com.veridia.gestao.plataformacursos.Aluno;
import com.veridia.gestao.plataformacursos.Repository.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return alunoRepository.findById(id);
    }

    @Transactional
    public Aluno salvar(Aluno aluno) {
        return criar(aluno);
    }

    @Transactional
    public Aluno criar(Aluno aluno) {
        if (aluno.getNome() == null || aluno.getNome().isBlank())
            throw new IllegalArgumentException("Nome obrigatório");

        if (alunoRepository.existsByCpf(aluno.getCpf()))
            throw new IllegalArgumentException("CPF já cadastrado");

        if (alunoRepository.existsByEmail(aluno.getEmail()))
            throw new IllegalArgumentException("E-mail já cadastrado");

        return alunoRepository.save(aluno);
    }

    @Transactional
    public Aluno atualizar(Long id, Aluno novoAluno) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        if (!aluno.getCpf().equals(novoAluno.getCpf()) &&
                alunoRepository.existsByCpf(novoAluno.getCpf()))
            throw new IllegalArgumentException("CPF já cadastrado");

        if (!aluno.getEmail().equals(novoAluno.getEmail()) &&
                alunoRepository.existsByEmail(novoAluno.getEmail()))
            throw new IllegalArgumentException("E-mail já cadastrado");

        aluno.setNome(novoAluno.getNome());
        aluno.setEmail(novoAluno.getEmail());
        aluno.setCpf(novoAluno.getCpf());
        return alunoRepository.save(aluno);
    }

    @Transactional
    public void excluir(Long id) {
        if (!alunoRepository.existsById(id))
            throw new IllegalArgumentException("Aluno não encontrado");
        alunoRepository.deleteById(id);
    }
}