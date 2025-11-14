package com.veridia.gestao.plataformacursos.repository;

import com.veridia.gestao.plataformacursos.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
	boolean existsByCpf(String cpf);
	boolean existsByEmailIgnoreCase(String email);
}
