package com.tiendaumk.model;

import com.google.gson.annotations.SerializedName;

public class Factura_lineas {
    @SerializedName("ARTICULO")
    String ARTICULO;

    @SerializedName("DESCRIPCION")
    String DESCRIPCION;

    @SerializedName("CANTIDAD")
    String CANTIDAD;

    @SerializedName("VENTA")
    String VENTA;

    @SerializedName("FECHA")
    String FECHA;

    @SerializedName("IMAGEN")
    String IMAGEN;

    @SerializedName("OBSERVACIONES")
    String OBSERVACIONES;

    public String getOBSERVACIONES() {
        return OBSERVACIONES;
    }

    public String getIMAGEN() {
        return IMAGEN;
    }

    public String getARTICULO() {
        return ARTICULO;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public String getCANTIDAD() {
        return CANTIDAD;
    }

    public String getVENTA() {
        return VENTA;
    }

    public String getFECHA() {
        return FECHA;
    }
}