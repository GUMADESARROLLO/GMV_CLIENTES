package com.tiendaumk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Articulos_facturado {

    @SerializedName("Articulos")
    private String mArticulos;


    @SerializedName("Descripcion")
    private String mDescripcion;

    @SerializedName("Cantidad")
    private String mCantidad;

    @SerializedName("Venta")
    private String mVenta;

    @SerializedName("product_image")
    private String mImagen;

    public String getmImagen() {
        return mImagen;
    }

    public String getmArticulos() {
        return mArticulos;
    }

    public String getmDescripcion() {
        return mDescripcion;
    }

    public String getmCantidad() {
        return mCantidad;
    }

    public String getmVenta() {
        return mVenta;
    }
}
