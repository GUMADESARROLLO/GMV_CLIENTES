package com.tiendaumk.model;

import com.google.gson.annotations.SerializedName;

public class Articulos_cupon {
    @SerializedName("Articulos")
    private String mArticulos;


    @SerializedName("Porcent_Cupon")
    private String mPorcent_Cupon;

    @SerializedName("Cantidad_Cupon")
    private String mCantidad_Cupon;

    @SerializedName("Cupon_cat")
    private String mCupon_cat;

    public String getmCupon_cat() {
        return mCupon_cat;
    }

    public void setmCupon_cat(String mCupon_cat) {
        this.mCupon_cat = mCupon_cat;
    }

    public String getmCantidad_Cupon() {
        return mCantidad_Cupon;
    }

    public void setmCantidad_Cupon(String mCantidad_Cupon) {
        this.mCantidad_Cupon = mCantidad_Cupon;
    }

    public String getmArticulos() {
        return mArticulos;
    }

    public void setmArticulos(String mArticulos) {
        this.mArticulos = mArticulos;
    }

    public String getmPorcent_Cupon() {
        return mPorcent_Cupon;
    }

    public void setmPorcent_Cupon(String mPorcent_Cupon) {
        this.mPorcent_Cupon = mPorcent_Cupon;
    }
}
