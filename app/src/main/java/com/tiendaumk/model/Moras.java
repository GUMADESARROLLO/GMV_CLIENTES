package com.tiendaumk.model;

public class Moras {
    String NoVencidos;
    String Vencidos;
    String Dias30;
    String Dias60;
    String Dias90;
    String Dias120;
    String Mas120;
    String FACT_PEND;

    String NombreCliente;
    String Limite;
    String Saldo;
    String Disponible;
    String Moroso;

    public String getMoroso() {
        return Moroso;
    }

    public String getNombreCliente() {
        return NombreCliente;
    }

    public String getLimite() {
        return Limite;
    }

    public String getSaldo() {
        return Saldo;
    }

    public String getDisponible() {
        return Disponible;
    }

    public String getVencido() {
        return Vencidos;
    }

    public String getNoVencidos() {
        return NoVencidos;
    }

    public String getDias30() {
        return Dias30;
    }

    public String getDias60() {
        return Dias60;
    }

    public String getDias90() {
        return Dias90;
    }

    public String getDias120() {
        return Dias120;
    }

    public String getMas120() {
        return Mas120;
    }

    public String getFACT_PEND() {
        return FACT_PEND;
    }
}
