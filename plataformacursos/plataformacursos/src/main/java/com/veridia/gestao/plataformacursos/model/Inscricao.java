package com.veridia.gestao.plataformacursos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscricoes")
public class Inscricao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;
    
    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
    
    @Column(nullable = false)
    private LocalDateTime dataInscricao;
    
    @Enumerated(EnumType.STRING)
    private StatusInscricao status;
    
    @OneToOne(mappedBy = "inscricao", cascade = CascadeType.ALL)
    private Pagamento pagamento;
    
   
    public enum StatusInscricao {
        PENDENTE,           // Inscrito mas não pagou
        PAGO,              // Pagamento confirmado
        CONFIRMADA,        // Inscriçao confirmada pelo sistema
        CANCELADA,         // Cancelada pelo aluno
        REEMBOLSADA,       // Reembolso processado
        CONCLUIDA          // Curso finalizado
    }
    
    // Construtores
    public Inscricao() {
        this.dataInscricao = LocalDateTime.now();
        this.status = StatusInscricao.PENDENTE;
    }
    
    public Inscricao(Aluno aluno, Curso curso) {
        this.aluno = aluno;
        this.curso = curso;
        this.dataInscricao = LocalDateTime.now();
        this.status = StatusInscricao.PENDENTE;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Aluno getAluno() {
        return aluno;
    }
    
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    
    public Curso getCurso() {
        return curso;
    }
    
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }
    
    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }
    
    public StatusInscricao getStatus() {
        return status;
    }
    
    public void setStatus(StatusInscricao status) {
        this.status = status;
    }
    
    public Pagamento getPagamento() {
        return pagamento;
    }
    
    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
