package com.tiendaumk.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.tiendaumk.model.ProductNoFacturado;

import java.util.ArrayList;
import java.util.List;

import com.tiendaumk.R;
import com.tiendaumk.retrofit.APIClient;

import java.util.Locale;

public class AdapterNoFacturado extends RecyclerView.Adapter<AdapterNoFacturado.MyViewHolder> implements Filterable {

    private List<ProductNoFacturado> productListFiltered;
    private List<ProductNoFacturado> productList;
    private ContactsAdapterListener listener;

    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name, product_cant,product_venta,product_sku;
        public ImageView product_image,product_premiun;
        public MyViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.product_name);
            product_cant = view.findViewById(R.id.product_cant);
            product_venta = view.findViewById(R.id.product_venta);
            product_sku = view.findViewById(R.id.product_sku);
            product_image = view.findViewById(R.id.product_image);
            product_premiun = view.findViewById(R.id.img_premiun);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onContactSelected(productListFiltered.get(getAdapterPosition()));
                }
            });


        }
    }

    public AdapterNoFacturado(Context context, List<ProductNoFacturado> productList, ContactsAdapterListener listener) {
        this.productList = productList;
        this.productListFiltered = productList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_no_facturado, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ProductNoFacturado product = productListFiltered.get(position);

        holder.product_name.setText(product.getProduct_name());

        String price = String.format(Locale.ENGLISH, "%1$,.2f", product.getProduct_price());
        holder.product_venta.setText(("C$ ").concat(price));

        String quantity = String.format(Locale.ENGLISH, "%1$,.2f", product.getProduct_quantity());
        holder.product_cant.setText(quantity.concat(" [" + product.getProduct_und().concat("]")));

        holder.product_sku.setText(product.getProduct_id());




        if (product.getCALIFICATIVO().equals("A")) {
            holder.product_premiun.setVisibility(View.VISIBLE);
        } else {
            holder.product_premiun.setVisibility(View.GONE);
        }


        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(6)
                .oval(false)
                .build();

        Picasso.with(context)
                .load(APIClient.ADMIN_PANEL_URL + "/upload/product/" + product.getProduct_image())
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


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<ProductNoFacturado> filteredList = new ArrayList<>();
                    for (ProductNoFacturado row : productList) {
                        if (row.getProduct_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<ProductNoFacturado>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public interface ContactsAdapterListener {
        void onContactSelected(ProductNoFacturado product);
    }



}
