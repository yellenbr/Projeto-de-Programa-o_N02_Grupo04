package com.veridia.gestao.plataformacursos.model;

import com.veridia.gestao.plataformacursos.exception.NegocioException;
import com.veridia.gestao.plataformacursos.exception.ValidacaoException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Curso {
    
    private static final int VAGAS_MINIMAS = 1;
    private static final int VAGAS_MAXIMAS = 60;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(length = 1000)
    private String descricao;
    
    @Column(nullable = false)
    private BigDecimal preco;
    
    private Integer cargaHoraria;
    
    @Column(nullable = false)
    private Integer vagas = VAGAS_MAXIMAS; // Limite máximo de vagas
    
    @Column(nullable = false)
    private Boolean ativo = true; // Curso ativo ou cancelado
    
    @ManyToOne
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;
    
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<Inscricao> inscricoes = new ArrayList<>();
    
    // Construtores
    public Curso() {
    }
    
    public Curso(String nome, String descricao, BigDecimal preco, Integer cargaHoraria) {
        validarNome(nome);
        validarPreco(preco);
        validarCargaHoraria(cargaHoraria);
        
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.cargaHoraria = cargaHoraria;
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
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getPreco() {
        return preco;
    }
    
    public void setPreco(BigDecimal preco) {
        validarPreco(preco);
        this.preco = preco;
    }
    
    public Integer getCargaHoraria() {
        return cargaHoraria;
    }
    
    public void setCargaHoraria(Integer cargaHoraria) {
        validarCargaHoraria(cargaHoraria);
        this.cargaHoraria = cargaHoraria;
    }
    
    public Instrutor getInstrutor() {
        return instrutor;
    }
    
    public void setInstrutor(Instrutor instrutor) {
        this.instrutor = instrutor;
    }
    
    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }
    
    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }
    
    public Integer getVagas() {
        return vagas;
    }
    
    public void setVagas(Integer vagas) {
        if (vagas < VAGAS_MINIMAS || vagas > VAGAS_MAXIMAS) {
            throw new ValidacaoException(
                "Número de vagas deve estar entre " + VAGAS_MINIMAS + " e " + VAGAS_MAXIMAS);
        }
        this.vagas = vagas;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    // Métodos de validação
    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome do curso não pode ser vazio");
        }
        if (nome.trim().length() < 3) {
            throw new ValidacaoException("Nome do curso deve ter no mínimo 3 caracteres");
        }
    }
    
    private void validarPreco(BigDecimal preco) {
        if (preco == null) {
            throw new ValidacaoException("Preço não pode ser nulo");
        }
        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("Preço deve ser maior que zero");
        }
    }
    
    private void validarCargaHoraria(Integer cargaHoraria) {
        if (cargaHoraria != null && cargaHoraria <= 0) {
            throw new ValidacaoException("Carga horária deve ser maior que zero");
        }
    }
    
    // Métodos de negócio
    public long getNumeroInscritos() {
        if (inscricoes == null) return 0;
        return inscricoes.stream()
            .filter(i -> i.getStatus() != Inscricao.StatusInscricao.CANCELADA &&
                        i.getStatus() != Inscricao.StatusInscricao.REEMBOLSADA)
            .count();
    }
    
    public boolean temVagasDisponiveis() {
        return getNumeroInscritos() < vagas && ativo;
    }
    
    /**
     * Reserva uma vaga para o curso
     * @throws NegocioException se não houver vagas disponíveis
     */
    public void reservarVaga() {
        if (!temVagasDisponiveis()) {
            throw new NegocioException(
                "Curso sem vagas disponíveis (limite: " + vagas + " alunos)");
        }
        if (!ativo) {
            throw new NegocioException("Curso está inativo ou cancelado");
        }
    }
    
    /**
     * Libera uma vaga do curso (quando aluno cancela)
     */
    public void liberarVaga() {
        // Lógica de liberação é automática pela contagem de inscrições ativas
    }
    
    /**
     * Cancela o curso (por instrutor ou administrador)
     * @throws NegocioException se já estiver cancelado
     */
    public void cancelar() {
        if (!ativo) {
            throw new NegocioException("Curso já está cancelado");
        }
        this.ativo = false;
    }
    
    /**
     * Reativa o curso
     */
    public void reativar() {
        if (ativo) {
            throw new NegocioException("Curso já está ativo");
        }
        this.ativo = true;
    }
    
    /**
     * Verifica se o curso já começou (simulação - baseado em data futura)
     */
    public boolean isCursoComecou() {
        // Implementação simplificada - sempre retorna false
        // Em produção, comparar com data de início do curso
        return false;
    }
    
    /**
     * Verifica se o curso está lotado
     */
    public boolean isLotado() {
        return getNumeroInscritos() >= vagas;
    }
}
