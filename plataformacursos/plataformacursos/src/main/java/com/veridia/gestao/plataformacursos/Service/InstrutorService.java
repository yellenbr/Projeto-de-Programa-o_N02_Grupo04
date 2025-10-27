package com.veridia.gestao.plataformacursos.Service;

import com.veridia.gestao.plataformacursos.Instrutor;
import com.veridia.gestao.plataformacursos.Repository.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InstrutorService {

    @Autowired
    private InstrutorRepository instrutorRepository;

    public List<Instrutor> buscarTodos() {
        return instrutorRepository.findAll();
    }

    public Optional<Instrutor> buscarPorId(Long id) {
        return instrutorRepository.findById(id);
    }

    public Instrutor salvar(Instrutor instrutor) {
        return instrutorRepository.save(instrutor);
    }

    public Instrutor atualizar(Long id, Instrutor instrutorAtualizado) {
        Optional<Instrutor> instrutorExistente = instrutorRepository.findById(id);

        if (instrutorExistente.isPresent()) {
            Instrutor instrutor = instrutorExistente.get();
            instrutor.setNome(instrutorAtualizado.getNome());
            instrutor.setEmail(instrutorAtualizado.getEmail());
            instrutor.setEspecialidade(instrutorAtualizado.getEspecialidade());
            return instrutorRepository.save(instrutor);
        } else {
            throw new RuntimeException("Instrutor não encontrado com ID: " + id);
        }
    }

    public void deletar(Long id) {
        if (instrutorRepository.existsById(id)) {
            instrutorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Instrutor não encontrado com ID: " + id);
        }
    }
}
