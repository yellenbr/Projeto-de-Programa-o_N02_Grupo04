package com.veridia.gestao.plataformacursos.model;

import com.veridia.gestao.plataformacursos.exception.ValidacaoException;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Table(name = "instrutores")
public class Instrutor {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String especialidade;
    
    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL)
    private List<Curso> cursos = new ArrayList<>();
    
    public Instrutor() {
    }
    
    public Instrutor(String nome, String email, String especialidade) {
        validarNome(nome);
        validarEmail(email);
        
        this.nome = nome;
        this.email = email;
        this.especialidade = especialidade;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        validarEmail(email);
        this.email = email;
    }
    
    public String getEspecialidade() {
        return especialidade;
    }
    
    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
    
    public List<Curso> getCursos() {
        return cursos;
    }
    
    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }
    
    // Métodos de validação
    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do instrutor não pode ser vazio");
        }
        if (nome.trim().length() < 3) {
            throw new ValidacaoException("Nome do instrutor deve ter no mínimo 3 caracteres");
        }
    }
    
    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidacaoException("Email do instrutor não pode ser vazio");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidacaoException("Email inválido: " + email);
        }
    }
    
    // Métodos de negócio
    /**
     * Adiciona um curso ao instrutor
     */
    public void adicionarCurso(Curso curso) {
        if (curso == null) {
            throw new ValidacaoException("Curso não pode ser nulo");
        }
        if (!cursos.contains(curso)) {
            cursos.add(curso);
            curso.setInstrutor(this);
        }
    }
    
    /**
     * Remove um curso do instrutor
     */
    public void removerCurso(Curso curso) {
        if (curso != null && cursos.contains(curso)) {
            cursos.remove(curso);
            curso.setInstrutor(null);
        }
    }
    
    /**
     * Retorna o número de cursos ativos do instrutor
     */
    public long getNumeroCursosAtivos() {
        return cursos.stream()
            .filter(Curso::getAtivo)
            .count();
    }
    
    /**
     * Retorna o total de alunos em todos os cursos do instrutor
     */
    public long getTotalAlunos() {
        return cursos.stream()
            .mapToLong(Curso::getNumeroInscritos)
            .sum();
    }
}
