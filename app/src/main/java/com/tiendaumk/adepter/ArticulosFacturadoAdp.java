package com.tiendaumk.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.tiendaumk.R;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import com.tiendaumk.model.Articulos_facturado;
import com.tiendaumk.model.News;
import com.tiendaumk.utils.Utiles;

import java.util.ArrayList;
import java.util.List;

public class ArticulosFacturadoAdp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Articulos_facturado> items = new ArrayList<>();

    private Context ctx;

    @LayoutRes
    private int layout_id;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, News obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public ArticulosFacturadoAdp(Context context, List<Articulos_facturado> items, @LayoutRes int layout_id) {
        this.items = items;
        ctx = context;
        this.layout_id = layout_id;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView subtitle;
        public TextView date;
        public View lyt_parent;
        private Context context;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            title = v.findViewById(R.id.title);
            subtitle = v.findViewById(R.id.subtitle);
            date = v.findViewById(R.id.date);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout_id, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Articulos_facturado n = items.get(position);

            view.title.setText(n.getmDescripcion());
            view.subtitle.setText(n.getmArticulos());
            view.date.setText(n.getmVenta());

            Transformation transformation = new RoundedTransformationBuilder()
                    .cornerRadiusDp(6)
                    .oval(false)
                    .build();

            Picasso.with(ctx)
                    .load(n.getmImagen())
                    .placeholder(R.drawable.ic_loading)
                    .resize(250, 250)
                    .centerCrop()
                    .transform(transformation)
                    .into(view.image);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}