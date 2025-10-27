package com.veridia.gestao.plataformacursos.model;

import com.veridia.gestao.plataformacursos.exception.ValidacaoException;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Table(name = "alunos")
public class Aluno {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern CPF_PATTERN = 
        Pattern.compile("^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$");
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String cpf;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Inscricao> inscricoes = new ArrayList<>();
    
    // Construtores
    public Aluno() {
    }
    
    public Aluno(String nome, String email, String cpf) {
        validarNome(nome);
        validarEmail(email);
        validarCpf(cpf);
        
        this.nome = nome;
        this.email = email;
        this.cpf = limparCpf(cpf);
    }
    
    // Getters e Setters
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
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        validarCpf(cpf);
        this.cpf = limparCpf(cpf);
    }
    
    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }
    
    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }
    
    // Métodos de validação
    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome não pode ser vazio");
        }
        if (nome.trim().length() < 3) {
            throw new ValidacaoException("Nome deve ter no mínimo 3 caracteres");
        }
    }
    
    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidacaoException("Email não pode ser vazio");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidacaoException("Email inválido: " + email);
        }
    }
    
    private void validarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new ValidacaoException("CPF não pode ser vazio");
        }
        String cpfLimpo = limparCpf(cpf);
        if (!CPF_PATTERN.matcher(cpf).matches()) {
            throw new ValidacaoException("CPF inválido: " + cpf);
        }
        if (cpfLimpo.length() != 11) {
            throw new ValidacaoException("CPF deve ter 11 dígitos");
        }
    }
    
    private String limparCpf(String cpf) {
        return cpf.replaceAll("[^0-9]", "");
    }
    
    // Métodos de negócio
    /**
     * Verifica se o aluno pode se inscrever em um curso
     */
    public boolean podeSeInscrever(Curso curso) {
        if (curso == null) {
            return false;
        }
        
        // Verifica se já está inscrito no curso
        boolean jaInscrito = inscricoes.stream()
            .anyMatch(i -> i.getCurso().equals(curso) && 
                          i.getStatus() != Inscricao.StatusInscricao.CANCELADA);
        
        return !jaInscrito && curso.temVagasDisponiveis();
    }
    
    /**
     * Retorna o número de cursos ativos do aluno
     */
    public long getNumeroCursosAtivos() {
        return inscricoes.stream()
            .filter(i -> i.getStatus() == Inscricao.StatusInscricao.PAGO ||
                        i.getStatus() == Inscricao.StatusInscricao.CONFIRMADA)
            .count();
    }
    
    /**
     * Verifica se o aluno tem inscrições pendentes de pagamento
     */
    public boolean temInscricoesPendentes() {
        return inscricoes.stream()
            .anyMatch(i -> i.getStatus() == Inscricao.StatusInscricao.PENDENTE);
    }
}
