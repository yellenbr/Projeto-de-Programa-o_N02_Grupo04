package com.veridia.gestao.plataformacursos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "curso_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCurso;
    private Integer quantidadeAlunos;
    private String descricaoCurso;
    private Double valorCurso;
    private Double quantidadeHora;
    private Double avaliacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id")
    @JsonBackReference
    private Instrutor professorAlocado;

    @ManyToMany(mappedBy = "cursosInscritos", fetch = FetchType.LAZY)
    private List<Aluno> alunosInscritos = new ArrayList<>();
}
