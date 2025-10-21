package com.veridia.gestao.plataformacursos.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instrutor_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Instrutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    private String email;
    private Double salario;

    @OneToMany(mappedBy = "professorAlocado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Curso> cursosMinistrados = new ArrayList<>();
}
