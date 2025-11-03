package com.veridia.gestao.plataformacursos.repository;

import com.veridia.gestao.plataformacursos.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    List<Inscricao> findByAluno_Id(Long alunoId);

    List<Inscricao> findByCurso_Id(Long cursoId);

    List<Inscricao> findByStatus(Inscricao.StatusInscricao status);

    Optional<Inscricao> findByAluno_IdAndCurso_Id(Long alunoId, Long cursoId);

    boolean existsByAluno_IdAndCurso_IdAndStatusNot(Long alunoId, Long cursoId, Inscricao.StatusInscricao status);
}