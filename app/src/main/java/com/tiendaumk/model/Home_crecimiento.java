package com.tiendaumk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Home_crecimiento {
    @SerializedName("ResponseCode")
    private String mResponseCode;

    @SerializedName("Result")
    private String mResult;

    @SerializedName("ResponseMsg")
    private String mResponseMsg;

    @SerializedName("ResultData")
    @Expose
    private ResultHome_crecimiento resultData;

    @SerializedName("Cliente_Meta")
    private String mCliente_Meta;

    @SerializedName("Cliente_Venta")
    private String mCliente_Venta;

    @SerializedName("Porce_cump")
    private String mPorce_cump;

    public String getmPorce_cump() {
        return mPorce_cump;
    }

    public void setmPorce_cump(String mPorce_cump) {
        this.mPorce_cump = mPorce_cump;
    }

    public String getmCliente_Meta() {
        return mCliente_Meta;
    }

    public void setmCliente_Meta(String mCliente_Meta) {
        this.mCliente_Meta = mCliente_Meta;
    }

    public String getmCliente_Venta() {
        return mCliente_Venta;
    }

    public void setmCliente_Venta(String mCliente_Venta) {
        this.mCliente_Venta = mCliente_Venta;
    }

    public ResultHome_crecimiento getResultHome() {
        return resultData;
    }

    public void setResultHome(ResultHome_crecimiento resultData) {
        this.resultData = resultData;
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
