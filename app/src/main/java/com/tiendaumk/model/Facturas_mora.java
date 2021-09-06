package com.tiendaumk.model;

import com.google.gson.annotations.SerializedName;

public class Facturas_mora {
    @SerializedName("Codigo")
    String Codigo;
    @SerializedName("Saldo")
    String Saldo;
    @SerializedName("Fecha")
    String Fecha;

    public Facturas_mora(){

    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public void setSaldo(String saldo) {
        Saldo = saldo;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getCodigo() {
        return Codigo;
    }

    public String getSaldo() {
        return Saldo;
    }

    public String getFecha() {
        return Fecha;
    }
}
