package com.veridia.gestao.plataformacursos.repository;

import com.veridia.gestao.plataformacursos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    
    Optional<Pagamento> findByInscricao_Id(Long inscricaoId);
    
    List<Pagamento> findByStatus(Pagamento.StatusPagamento status);
}
