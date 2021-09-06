package com.tiendaumk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultHome_crecimiento {

    @SerializedName("Facturado_anual")
    private List<Articulos_venta_anual> FacturaAnualItems;

    @SerializedName("Articulos_facturado")
    private List<Articulos_facturado> ArticulosFacturadotItems;

    @SerializedName("Articulos_no_facturado")
    private List<Articulos_facturado> ArticuloNoFactuadoItems;


    public List<Articulos_venta_anual> getFacturaAnualItems() {
        return FacturaAnualItems;
    }

    public List<Articulos_facturado> getArticulosFacturadotItems() {
        return ArticulosFacturadotItems;
    }

    public List<Articulos_facturado> getArticuloNoFactuadoItems() {
        return ArticuloNoFactuadoItems;
    }
}
