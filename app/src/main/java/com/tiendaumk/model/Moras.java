package com.tiendaumk.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Moras {

    @SerializedName("NoVencidos")
    String NoVencidos;

    @SerializedName("Vencidos")
    String Vencidos;

    @SerializedName("Dias30")
    String Dias30;

    @SerializedName("Dias60")
    String Dias60;

    @SerializedName("Dias90")
    String Dias90;

    @SerializedName("Dias120")
    String Dias120;

    @SerializedName("Mas120")
    String Mas120;

    @SerializedName("FACT_PEND")
    String FACT_PEND;

    @SerializedName("NombreCliente")
    String NombreCliente;

    @SerializedName("Limite")
    String Limite;

    @SerializedName("Saldo")
    String Saldo;

    @SerializedName("Disponible")
    String Disponible;

    @SerializedName("Moroso")
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
