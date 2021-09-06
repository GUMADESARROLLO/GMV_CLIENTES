package com.tiendaumk.model;

import com.google.gson.annotations.SerializedName;

public class ProductNoFacturado {

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("product_name")
    private String product_name;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("category_name")
    private String category_name;

    @SerializedName("product_price")
    private double product_price;

    @SerializedName("product_status")
    private String product_status;

    @SerializedName("product_image")
    private String product_image;

    @SerializedName("product_description")
    private String product_description;

    @SerializedName("currency_code")
    private String currency_code;

    @SerializedName("product_bonificado")
    private String product_bonificado;

    @SerializedName("product_lotes")
    private String product_lotes;

    @SerializedName("product_und")
    private String product_und;

    @SerializedName("CALIFICATIVO")
    private String CALIFICATIVO;

    @SerializedName("ISPROMO")
    private String ISPROMO;

    @SerializedName("tax")
    private double tax;

    @SerializedName("product_quantity")
    private double product_quantity;

    public String getISPROMO() {
        return ISPROMO;
    }

    public String getCALIFICATIVO() {
        return CALIFICATIVO;
    }

    public String getProduct_und() {
        return product_und;
    }

    public String getProduct_lotes() {
        return product_lotes;
    }

    public String getProduct_bonificado() {
        return product_bonificado;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public double getProduct_price() {
        return product_price;
    }

    public String getProduct_status() {
        return product_status;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getProduct_description() {
        return product_description;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public double getTax() {
        return tax;
    }

    public double getProduct_quantity() {
        return product_quantity;
    }
}
