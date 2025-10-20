package com.veridia.gestao.plataformacursos.Repository;

import com.veridia.gestao.plataformacursos.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {

}
