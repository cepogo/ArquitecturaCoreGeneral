package com.banquito.core.general.excepcion;

public class EntidadNoEncontradaException extends RuntimeException{
    private Integer errorCode;
    private String entityName;

    public EntidadNoEncontradaException(String message, Integer errorCode, String entityName) {
        super(message);
        this.errorCode = 2;
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return "errorCode=" + errorCode + ", entityName=" + entityName + ", message=" + super.getMessage();
    }
}
