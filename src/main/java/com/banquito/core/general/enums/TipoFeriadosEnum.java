package com.banquito.core.general.enums;

public enum TipoFeriadosEnum {

    NACIONAL("NACIONAL"),
    LOCAL("LOCAL");

    private final String valor;

    TipoFeriadosEnum(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
