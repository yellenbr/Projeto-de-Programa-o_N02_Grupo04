package com.veridia.gestao.plataformacursos.Repository;

import com.veridia.gestao.plataformacursos.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
