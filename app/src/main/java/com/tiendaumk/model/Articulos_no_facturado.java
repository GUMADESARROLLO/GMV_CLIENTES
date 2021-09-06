package com.tiendaumk.model;

import com.google.gson.annotations.SerializedName;

public class Articulos_no_facturado {

    @SerializedName("Articulos")
    private String mArticulos;


    @SerializedName("Descripcion")
    private String mDescripcion;

    @SerializedName("Cantidad")
    private String mCantidad;

    public String getmArticulos() {
        return mArticulos;
    }

    public String getmDescripcion() {
        return mDescripcion;
    }

    public String getmCantidad() {
        return mCantidad;
    }
}
