package com.veridia.gestao.plataformacursos.repository;

import com.veridia.gestao.plataformacursos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}
