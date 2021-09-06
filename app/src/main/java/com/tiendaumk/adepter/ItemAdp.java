package com.tiendaumk.adepter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tiendaumk.R;
import com.tiendaumk.activity.ItemDetailsActivity;
import com.tiendaumk.database.DatabaseHelper;
import com.tiendaumk.database.MyCart;
import com.tiendaumk.model.Price;
import com.tiendaumk.model.ProductItem;
import com.tiendaumk.utils.SessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tiendaumk.fragment.ItemListFragment.itemListFragment;
import static com.tiendaumk.utils.SessionManager.currncy;

public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {
    private List<ProductItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context mContext;
    SessionManager sessionManager;



    public ItemAdp(Context context, List<ProductItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        sessionManager = new SessionManager(mContext);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_custome, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductItem datum = mData.get(position);


        if (datum.getStock() == 0) {
            holder.lvlOutofstock.setVisibility(View.VISIBLE);
        } else {
            holder.lvlOutofstock.setVisibility(View.GONE);

        }




        String _stock = String.format(Locale.ENGLISH, "%1$,.0f", Double.parseDouble(String.valueOf(datum.getStock())));
        holder.txtStock.setText("Stock: " + _stock);
        Glide.with(mContext).load(datum.getProductImage()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.imgIcon);
        holder.txtTitle.setText("" + datum.getProductName());
        holder.imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ItemDetailsActivity.class).putExtra("MyClass", datum).putParcelableArrayListExtra("MyList", datum.getPrice()));
            }
        });
        if (datum.getmDiscount() > 0) {
            holder.lvlOffer.setVisibility(View.VISIBLE);
            holder.txtOffer.setText(datum.getmDiscount() + "% Desc");
        } else {
            holder.lvlOffer.setVisibility(View.GONE);
        }
        List<String> Arealist = new ArrayList<>();
        for (int i = 0; i < datum.getPrice().size(); i++) {

            Arealist.add(datum.getPrice().get(i).getProductType());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_layout, Arealist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        holder.spinner.setAdapter(dataAdapter);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (datum.getmDiscount() > 0) {
                    double res = (Double.parseDouble(datum.getPrice().get(position).getProductPrice()) / 100.0f) * datum.getmDiscount();
                    res = Double.parseDouble(datum.getPrice().get(position).getProductPrice()) - res;

                    String _price = String.format(Locale.ENGLISH, "%1$,.2f", Double.parseDouble(datum.getPrice().get(0).getProductPrice()));

                    holder.txtItemOffer.setText(sessionManager.getStringData(currncy) + " " + _price);
                    holder.txtItemOffer.setPaintFlags(holder.txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    holder.txtPrice.setText(sessionManager.getStringData(currncy) + new DecimalFormat("###,###.##").format(res));

                    holder.txtItemOffer.setText(sessionManager.getStringData(currncy) + datum.getPrice().get(position).getProductPrice());
                } else {
                    holder.txtItemOffer.setVisibility(View.GONE);

                    double res = (Double.parseDouble(datum.getPrice().get(position).getProductPrice()) );

                    holder.txtPrice.setText(sessionManager.getStringData(currncy) + " " +  new DecimalFormat("###,###.##").format(res));
                }
                setJoinPlayrList(holder.lvlSubitem, datum, datum.getPrice().get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txt_offer)
        TextView txtOffer;
        @BindView(R.id.txt_item_offer)
        TextView txtItemOffer;
        @BindView(R.id.txt_price)
        TextView txtPrice;
        @BindView(R.id.txt_stock)
        TextView txtStock;
        @BindView(R.id.lvl_subitem)
        LinearLayout lvlSubitem;
        @BindView(R.id.lvl_offer)
        LinearLayout lvlOffer;
        @BindView(R.id.lvl_outofstock)
        LinearLayout lvlOutofstock;
        @BindView(R.id.spinner)
        Spinner spinner;
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    private void setJoinPlayrList(LinearLayout lnrView, ProductItem datum, Price price) {
        lnrView.removeAllViews();
        final int[] count = {0};
        DatabaseHelper helper = new DatabaseHelper(lnrView.getContext());
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.custome_prize, null);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout lvl_addremove = view.findViewById(R.id.lvl_addremove);
        LinearLayout lvl_addcart = view.findViewById(R.id.lvl_addcart);
        LinearLayout img_mins = view.findViewById(R.id.img_mins);
        LinearLayout img_plus = view.findViewById(R.id.img_plus);

        MyCart myCart = new MyCart();
        myCart.setPid(datum.getId());
        myCart.setImage(datum.getProductImage());
        myCart.setTitle(datum.getProductName());
        myCart.setWeight(price.getProductType());
        myCart.setCost(price.getProductPrice());
        myCart.setDiscount(datum.getmDiscount());
        myCart.setCat(datum.getmCategoria());

        int qrt = helper.getCard(myCart.getPid(), myCart.getCost());
        if (qrt != -1) {
            count[0] = qrt;
            txtcount.setText("" + count[0]);
            lvl_addremove.setVisibility(View.VISIBLE);
            lvl_addcart.setVisibility(View.GONE);
        } else {
            lvl_addremove.setVisibility(View.GONE);
            lvl_addcart.setVisibility(View.VISIBLE);

        }
        img_mins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                count[0] = Integer.parseInt(txtcount.getText().toString());

                count[0] = count[0] - 1;
                if (count[0] <= 0) {
                    txtcount.setText("" + count[0]);
                    lvl_addremove.setVisibility(View.GONE);
                    lvl_addcart.setVisibility(View.VISIBLE);
                    helper.deleteRData(myCart.getPid(), myCart.getCost());
                } else {

                    txtcount.setVisibility(View.VISIBLE);
                    txtcount.setText("" + count[0]);
                    myCart.setQty(String.valueOf(count[0]));
                    myCart.setReglas(datum.getmbonificado());
                    myCart.setBonifi(getBonificado(datum.getmbonificado(),count[0]));
                    myCart.setCat(datum.getmCategoria());
                    helper.insertData(myCart);
                }
                itemListFragment.updateItem();
            }
        });

        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0] = Integer.parseInt(txtcount.getText().toString());
                count[0] = count[0] + 1;


                if (count[0] > datum.getStock()){
                    Toast.makeText(mContext, "No hay suficiente Stock", Toast.LENGTH_SHORT).show();
                }else{
                    txtcount.setText("" + count[0]);
                    myCart.setQty(String.valueOf(count[0]));
                    myCart.setReglas(datum.getmbonificado());
                    myCart.setReglas(datum.getmbonificado());
                    myCart.setBonifi(getBonificado(datum.getmbonificado(),count[0]));
                    myCart.setIva(datum.getmIva());
                    myCart.setCat(datum.getmCategoria());
                    Log.e("INsert", "--> " + helper.insertData(myCart));
                    itemListFragment.updateItem();
                }
            }
        });

        img_plus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {



                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_input_cant);
                dialog.setCancelable(true);

                LinearLayout lyt = dialog.findViewById(R.id.lyt);
                TextView txt_title = dialog.findViewById(R.id.title);
                EditText txt_msg = dialog.findViewById(R.id.ed_titulo);

                txt_title.setText("Cantidad Personalizada");
                //  txt_msg.setText(strMsg);
                lyt.setBackgroundColor(mContext.getResources().getColor(R.color.light_green_400));;



                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


                (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lvl_addcart.setVisibility(View.GONE);
                        lvl_addremove.setVisibility(View.VISIBLE);
                        count[0] = Integer.parseInt(txt_msg.getText().toString());


                        txtcount.setText("" + count[0]);
                        myCart.setQty(String.valueOf(count[0]));
                        myCart.setReglas(datum.getmbonificado());
                        myCart.setBonifi(getBonificado(datum.getmbonificado(),count[0]));
                        myCart.setIva(datum.getmIva());
                        myCart.setCat(datum.getmCategoria());
                        Log.e("INsert", "--> " + helper.insertData(myCart));
                        itemListFragment.updateItem();
                        if (itemListFragment != null)
                            itemListFragment.updateItem();
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setAttributes(lp);


                return false;
            }
        });
        lvl_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count[0] > datum.getStock()){
                    Toast.makeText(mContext, "No hay suficiente Stock", Toast.LENGTH_SHORT).show();
                }else{
                    lvl_addcart.setVisibility(View.GONE);
                    lvl_addremove.setVisibility(View.VISIBLE);
                    count[0] = Integer.parseInt(txtcount.getText().toString());
                    count[0] = count[0] + 1;
                    txtcount.setText("" + count[0]);
                    myCart.setQty(String.valueOf(count[0]));
                    myCart.setReglas(datum.getmbonificado());
                    myCart.setBonifi(getBonificado(datum.getmbonificado(),count[0]));
                    myCart.setIva(datum.getmIva());
                    myCart.setCat(datum.getmCategoria());
                    Log.e("INsert", "--> " + helper.insertData(myCart));
                    itemListFragment.updateItem();
                }
            }
        });

        lnrView.addView(view);

    }
    public String getBonificado(String Bonificado,int Cantidad){



        List<String> sList = Arrays.asList(Bonificado.split(","));

        final List<String> row_arr = new ArrayList<>();
        for (int i = 0; i < sList.size(); i++) row_arr.add(Arrays.asList(sList.get(i).replace("+", ",").split(",")).get(0));
        int position = row_arr.indexOf(String.valueOf(Cantidad));

        if (position == -1) {
           return "0";
        }else{
            return sList.get(position);
        }


    }

}