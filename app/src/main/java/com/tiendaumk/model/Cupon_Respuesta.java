package com.tiendaumk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cupon_Respuesta {
    @SerializedName("ResponseCode")
    private String mResponseCode;

    @SerializedName("Result")
    private String mResult;

    @SerializedName("ResponseMsg")
    private String mResponseMsg;

    @SerializedName("ResultData")
    @Expose
    private List<Articulos_cupon> ArticulosCupon;

    public List<Articulos_cupon> getArticulosCupon() {
        return ArticulosCupon;
    }

    public void setArticulosCupon(List<Articulos_cupon> articulosCupon) {
        ArticulosCupon = articulosCupon;
    }

    public String getmResponseCode() {
        return mResponseCode;
    }

    public void setmResponseCode(String mResponseCode) {
        this.mResponseCode = mResponseCode;
    }

    public String getmResponseMsg() {
        return mResponseMsg;
    }

    public void setmResponseMsg(String mResponseMsg) {
        this.mResponseMsg = mResponseMsg;
    }

    public String getmResult() {
        return mResult;
    }

    public void setmResult(String mResult) {
        this.mResult = mResult;
    }
}
