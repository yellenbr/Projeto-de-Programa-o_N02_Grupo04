package com.veridia.gestao.plataformacursos.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.veridia.gestao.plataformacursos.exception.NegocioException;
import com.veridia.gestao.plataformacursos.exception.ValidacaoException;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscricoes")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
        validarAluno(aluno);
        validarCurso(curso);

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
        validarAluno(aluno);
        this.aluno = aluno;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        validarCurso(curso);
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

    // Métodos de validação
    private void validarAluno(Aluno aluno) {
        if (aluno == null) {
            throw new ValidacaoException("Aluno não pode ser nulo");
        }
    }

    private void validarCurso(Curso curso) {
        if (curso == null) {
            throw new ValidacaoException("Curso não pode ser nulo");
        }
    }

    // Métodos de negócio
    /**
     * Confirma o pagamento da inscrição
     * @param pagamento Objeto de pagamento aprovado
     * @throws NegocioException se o status não permitir pagamento
     */
    public void confirmarPagamento(Pagamento pagamento) {
        if (this.status != StatusInscricao.PENDENTE) {
            throw new NegocioException(
                    "Inscrição não está pendente de pagamento. Status atual: " + this.status);
        }

        if (pagamento == null) {
            throw new ValidacaoException("Pagamento não pode ser nulo");
        }

        this.pagamento = pagamento;
        this.status = StatusInscricao.PAGO;
    }

    /**
     * Cancela a inscrição
     * @return true se houve reembolso, false caso contrário
     * @throws NegocioException se já estiver cancelada
     */
    public boolean cancelar() {
        if (this.status == StatusInscricao.CANCELADA ||
                this.status == StatusInscricao.REEMBOLSADA) {
            throw new NegocioException("Inscrição já está cancelada");
        }

        if (this.status == StatusInscricao.CONCLUIDA) {
            throw new NegocioException("Não é possível cancelar inscrição de curso concluído");
        }

        // Se o curso não começou e está pago, gera reembolso
        boolean temReembolso = !curso.isCursoComecou() &&
                this.pagamento != null &&
                this.pagamento.getStatus() == Pagamento.StatusPagamento.APROVADO;

        if (temReembolso) {
            this.status = StatusInscricao.REEMBOLSADA;
            this.pagamento.cancelar();
        } else {
            this.status = StatusInscricao.CANCELADA;
        }

        return temReembolso;
    }

    /**
     * Confirma a inscrição após pagamento
     */
    public void confirmar() {
        if (this.status != StatusInscricao.PAGO) {
            throw new NegocioException(
                    "Apenas inscrições pagas podem ser confirmadas. Status atual: " + this.status);
        }
        this.status = StatusInscricao.CONFIRMADA;
    }

    /**
     * Marca a inscrição como concluída
     */
    public void concluir() {
        if (this.status != StatusInscricao.CONFIRMADA &&
                this.status != StatusInscricao.PAGO) {
            throw new NegocioException(
                    "Apenas inscrições confirmadas ou pagas podem ser concluídas");
        }
        this.status = StatusInscricao.CONCLUIDA;
    }

    /**
     * Verifica se a inscrição está ativa
     */
    public boolean isAtiva() {
        return this.status == StatusInscricao.PAGO ||
                this.status == StatusInscricao.CONFIRMADA;
    }

    /**
     * Verifica se pode ser cancelada
     */
    public boolean podeCancelar() {
        return this.status != StatusInscricao.CANCELADA &&
                this.status != StatusInscricao.REEMBOLSADA &&
                this.status != StatusInscricao.CONCLUIDA;
    }

    /**
     * Verifica se tem direito a reembolso
     */
    public boolean temDireitoReembolso() {
        return !curso.isCursoComecou() &&
                this.pagamento != null &&
                this.pagamento.getStatus() == Pagamento.StatusPagamento.APROVADO;
    }
}