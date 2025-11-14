package com.veridia.gestao.plataformacursos.dto;

public class TransferenciaDTO {
    
    private Long inscricaoAtualId;
    private Long cursoNovoId;
    private String nomeAluno;
    private String cursoAntigo;
    private String cursoNovo;
    private String protocolo;
    
    public TransferenciaDTO() {
    }
    
    public TransferenciaDTO(Long inscricaoAtualId, Long cursoNovoId, String nomeAluno, 
                           String cursoAntigo, String cursoNovo) {
        this.inscricaoAtualId = inscricaoAtualId;
        this.cursoNovoId = cursoNovoId;
        this.nomeAluno = nomeAluno;
        this.cursoAntigo = cursoAntigo;
        this.cursoNovo = cursoNovo;
        this.protocolo = "TRANSF-" + System.currentTimeMillis();
    }
    
    
    public Long getInscricaoAtualId() {
        return inscricaoAtualId;
    }
    
    public void setInscricaoAtualId(Long inscricaoAtualId) {
        this.inscricaoAtualId = inscricaoAtualId;
    }
    
    public Long getCursoNovoId() {
        return cursoNovoId;
    }
    
    public void setCursoNovoId(Long cursoNovoId) {
        this.cursoNovoId = cursoNovoId;
    }
    
    public String getNomeAluno() {
        return nomeAluno;
    }
    
    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }
    
    public String getCursoAntigo() {
        return cursoAntigo;
    }
    
    public void setCursoAntigo(String cursoAntigo) {
        this.cursoAntigo = cursoAntigo;
    }
    
    public String getCursoNovo() {
        return cursoNovo;
    }
    
    public void setCursoNovo(String cursoNovo) {
        this.cursoNovo = cursoNovo;
    }
    
    public String getProtocolo() {
        return protocolo;
    }
    
    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }
}
