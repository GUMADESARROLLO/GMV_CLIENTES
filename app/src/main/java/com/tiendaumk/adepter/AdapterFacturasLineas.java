package com.tiendaumk.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.tiendaumk.model.Factura_lineas;

import java.util.List;
import java.util.Locale;
import com.tiendaumk.R;
import com.tiendaumk.retrofit.APIClient;

public class AdapterFacturasLineas extends RecyclerView.Adapter<AdapterFacturasLineas.MyViewHolder>  {

    private Context context;
    private List<Factura_lineas> productList;
    private List<Factura_lineas> productListFiltered;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name, product_cant,product_venta,txt_sku;
        public ImageView product_image;
        public MyViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.product_name);
            txt_sku = view.findViewById(R.id.id_sku);
            product_cant = view.findViewById(R.id.product_cant);
            product_venta = view.findViewById(R.id.product_venta);
            product_image = view.findViewById(R.id.category_image);
        }
    }

    public AdapterFacturasLineas(Context context, List<Factura_lineas> productList) {
        this.context = context;
        this.productList = productList;
        this.productListFiltered = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_factura_lineas, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Factura_lineas product = productListFiltered.get(position);




        String price = String.format(Locale.ENGLISH, "%1$,.2f", Double.parseDouble(product.getVENTA()));
        holder.txt_sku.setText(product.getARTICULO());
        holder.product_name.setText(product.getDESCRIPCION());
        holder.product_venta.setText(("C$ ").concat(price));
        holder.product_cant.setText(("Cant. ").concat(product.getCANTIDAD()));

        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(6)
                .oval(false)
                .build();

        Picasso.with(context)
                .load(APIClient.ADMIN_PANEL_URL + "/upload/product/" + product.getIMAGEN())
                .placeholder(R.drawable.ic_loading)
                .resize(250, 250)
                .centerCrop()
                .transform(transformation)
                .into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }




}
