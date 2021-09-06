package com.tiendaumk.model;

import com.google.gson.annotations.SerializedName;

public class Articulos_venta_anual {

    @SerializedName("Mes")
    private String mMes;


    @SerializedName("Cantidad")
    private String mCantidad;

    public String getmMes() {
        return mMes;
    }

    public String getmCantidad() {
        return mCantidad;
    }
}
