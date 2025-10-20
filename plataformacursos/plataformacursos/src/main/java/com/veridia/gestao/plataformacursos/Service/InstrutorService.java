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
        // Implementar Lógica de Negócio do ADM aqui:

        // Se o seu repositório tiver o método findByEmail, você faria uma validação:
        // if (instrutorRepository.findByEmail(instrutor.getEmail()).isPresent()) {
        //     throw new RuntimeException("Já existe um Instrutor cadastrado com este e-mail.");
        // }

        return instrutorRepository.save(instrutor);
    }

    public void deletar(Long id) {
        // Implementar Lógica de Negócio do ADM aqui:

        // Exemplo: O ADM só pode deletar um instrutor se ele não tiver cursos ativos.
        // if (cursoRepository.existsByInstrutorId(id)) {
        //     throw new RuntimeException("Não é possível deletar. O instrutor possui cursos ativos.");
        // }

        instrutorRepository.deleteById(id);
    }
}
