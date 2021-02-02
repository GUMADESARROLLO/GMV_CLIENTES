
package com.tiendaumk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Address implements Serializable {



    @SerializedName("id")
    private String id;

    @SerializedName("uid")
    private String uid;

    @SerializedName("area")
    private String area;

    @SerializedName("Titulo")
    private String Titulo;

    @SerializedName("Direec")
    private String Direec;

    @SerializedName("Referecia")
    private String Referecia;

    @SerializedName("status")
    private String status;


    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDireec() {
        return Direec;
    }

    public void setDireec(String direec) {
        Direec = direec;
    }

    public String getReferecia() {
        return Referecia;
    }

    public void setReferecia(String referecia) {
        Referecia = referecia;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
