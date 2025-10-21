package com.veridia.gestao.plataformacursos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "administracao_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Administracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private Double salario;
    private String cargo;

}
