package com.veridia.gestao.plataformacursos.repository;

import com.veridia.gestao.plataformacursos.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {}
