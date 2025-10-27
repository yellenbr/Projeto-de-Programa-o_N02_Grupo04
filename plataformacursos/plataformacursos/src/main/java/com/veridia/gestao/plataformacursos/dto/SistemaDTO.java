package com.veridia.gestao.plataformacursos.dto;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class SistemaDTO {
    private Long id;
    private String nome;
    private String descricao;
    private boolean ativo;
    private LocalDateTime dataCriacao;


}

