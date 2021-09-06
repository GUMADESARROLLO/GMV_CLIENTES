package com.tiendaumk.adepter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tiendaumk.R;
import com.tiendaumk.model.SubcatItem;
import com.tiendaumk.retrofit.APIClient;

import java.util.List;
import java.util.Random;

public class SubCategoryAdp extends RecyclerView.Adapter<SubCategoryAdp.MyViewHolder> {

    private Context mContext;
    private List<SubcatItem> categoryList;
    private RecyclerTouchListener listener;
    private Random r;

    public String mColors[] ={
            "#8fb545",
            "#056099",
            "#0691e6"
    };

    public interface RecyclerTouchListener {
        public void onClickItem(View v, String cid, String scid);
        public void onLongClickItem(View v, int position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public ImageView  overflow;
        public CardView cardView;
        public MyViewHolder(View view) {
            super(view);
            title       = view.findViewById(R.id.txt_title);
            thumbnail   = view.findViewById(R.id.imageView);
            cardView    = view.findViewById(R.id.id_element_cardview);
            r           = new Random();

        }
    }
    public SubCategoryAdp(Context mContext, List<SubcatItem> categoryList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.listener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        SubcatItem category = categoryList.get(position);
        holder.title.setText(category.getName());

        holder.title.setTextColor(Color.parseColor("#E6E6E6"));
        holder.cardView.setCardBackgroundColor(Color.parseColor(mColors[r.nextInt(mColors.length)]));

        Glide.with(mContext).load(APIClient.baseUrl + "/" + category.getImg()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    listener.onClickItem(v, category.getCatId(), category.getId());

            }
        });
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}