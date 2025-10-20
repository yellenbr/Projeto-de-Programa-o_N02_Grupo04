package com.veridia.gestao.plataformacursos.Repository;

import com.veridia.plataforma.domain.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

}