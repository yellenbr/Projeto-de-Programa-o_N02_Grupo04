package com.veridia.gestao.plataformacursos.repository;

import com.veridia.gestao.plataformacursos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    
    List<Curso> findByNomeContainingIgnoreCase(String nome);
    
    List<Curso> findByInstrutor_Id(Long instrutorId);
}
