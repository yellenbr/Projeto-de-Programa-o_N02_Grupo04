package com.veridia.gestao.plataformacursos.exception;

/**
 * Exceção lançada quando uma regra de negócio é violada
 */
public class NegocioException extends RuntimeException {
    
    public NegocioException(String mensagem) {
        super(mensagem);
    }
    
    public NegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
