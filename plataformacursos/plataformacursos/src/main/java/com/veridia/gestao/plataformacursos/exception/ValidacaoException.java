package com.veridia.gestao.plataformacursos.exception;

/**
 * Exceção lançada quando dados inválidos são fornecidos
 */
public class ValidacaoException extends RuntimeException {
    
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
    
    public ValidacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
