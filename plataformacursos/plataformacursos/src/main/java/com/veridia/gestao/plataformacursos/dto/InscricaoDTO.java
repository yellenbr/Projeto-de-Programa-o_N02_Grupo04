package com.veridia.gestao.plataformacursos.dto;

import com.veridia.gestao.plataformacursos.model.MetodoPagamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InscricaoDTO {
    private Long idAluno;
    private Long idCurso;
    private String tipoInscricao;
    private Double valor;
    private MetodoPagamento metodoPagamento;
}
