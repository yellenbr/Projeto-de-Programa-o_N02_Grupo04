package com.veridia.gestao.plataformacursos.Repository;

import com.veridia.gestao.plataformacursos.Model.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {

}
