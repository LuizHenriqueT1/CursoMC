package com.rick.cursomc.enums;

public enum TipoCliente {
    PESSOAFISICA(0, "PESSOA_FISICA"),
    PESSOAJURIDICA(1, "PESSOA_JURIDICA");

    private Integer cod;
    private String descricao;

    private TipoCliente(Integer cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public Integer getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoCliente toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (TipoCliente x : TipoCliente.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Tipo de Cliente Inválido");
    }
}
