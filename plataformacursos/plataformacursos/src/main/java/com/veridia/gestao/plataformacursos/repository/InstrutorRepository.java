package com.veridia.gestao.plataformacursos.repository;

import com.veridia.gestao.plataformacursos.model.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {}
