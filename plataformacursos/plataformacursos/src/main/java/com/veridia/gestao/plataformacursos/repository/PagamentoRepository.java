package com.veridia.gestao.plataformacursos.repository;

import com.veridia.gestao.plataformacursos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {}


