package com.tiendaumk.adepter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tiendaumk.R;
import com.tiendaumk.model.Articulos_venta_anual;
import com.tiendaumk.model.CatItem;
import com.tiendaumk.retrofit.APIClient;

import java.util.List;
import java.util.Locale;

public class CrecimientoFacturadoMensualAdp extends RecyclerView.Adapter<CrecimientoFacturadoMensualAdp.MyViewHolder> {

    private List<Articulos_venta_anual> MesesFacturados;

    public interface RecyclerTouchListener {
        public void onClickItem(String titel, int position);

        public void onLongClickItem(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView MesFacturado;
        public TextView CantidadFacturado;

        public MyViewHolder(View view) {
            super(view);
            MesFacturado        = (TextView) view.findViewById(R.id.id_mes_facturado);
            CantidadFacturado   = (TextView) view.findViewById(R.id.id_cantidad_facturada);


        }
    }

    public CrecimientoFacturadoMensualAdp(List<Articulos_venta_anual> MesesFacturados) {
        this.MesesFacturados = MesesFacturados;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crecimiento_facturado_anual, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Articulos_venta_anual category = MesesFacturados.get(position);

        String cantidad = String.format(Locale.ENGLISH, "%1$,.2f", Double.parseDouble(category.getmCantidad()));


        holder.MesFacturado.setText(category.getmMes());
        holder.CantidadFacturado.setText(("C$ ").concat(cantidad));


    }

    @Override
    public int getItemCount() {
        return MesesFacturados.size();
    }
}