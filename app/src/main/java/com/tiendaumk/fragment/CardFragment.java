package com.tiendaumk.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tiendaumk.R;
import com.tiendaumk.activity.HomeActivity;
import com.tiendaumk.activity.LoginActivity;
import com.tiendaumk.database.DatabaseHelper;
import com.tiendaumk.database.MyCart;
import com.tiendaumk.model.Moras;
import com.tiendaumk.model.User;
import com.tiendaumk.retrofit.Constant;
import com.tiendaumk.utils.SessionManager;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.tiendaumk.utils.SessionManager.currncy;
import static com.tiendaumk.utils.SessionManager.login;
import static com.tiendaumk.utils.SessionManager.oMin;


public class CardFragment extends Fragment {
    @BindView(R.id.txt_notfound)
    TextView txtNotfound;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    @BindView(R.id.my_recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    DatabaseHelper databaseHelper;
    List<MyCart> myCarts;

    @BindView(R.id.txt_item)
    TextView txtItem;


    @BindView(R.id.txt_valor_iva)
    TextView txtvalor_iva;

    @BindView(R.id.txt_monto_final)
    TextView monto_final;



    @BindView(R.id.totleAmount)
    TextView totleAmount;

    @BindView(R.id.lvlbacket)
    LinearLayout lvlBacket;

    StaggeredGridLayoutManager gridLayoutManager;

    @BindView(R.id.rlvSubTotal)
    RelativeLayout rlv_SubTotal;

    @BindView(R.id.rlvIva)
    RelativeLayout rlv_Iva;


    SessionManager sessionManager;
    User user;

    String Moroso = "N/D";
    String Disponible = "0.00";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        unbinder = ButterKnife.bind(this, view);
        databaseHelper = new DatabaseHelper(getActivity());
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        HomeActivity.getInstance().serchviewShow();
        myCarts = new ArrayList<>();

        gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            lvlNotfound.setVisibility(View.VISIBLE);
            txtNotfound.setText("Carrito vacío");
            lvlBacket.setVisibility(View.GONE);
            rlv_SubTotal.setVisibility(View.GONE);
            rlv_Iva.setVisibility(View.GONE);


        }
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setId(res.getString(0));
            rModel.setPid(res.getString(1));
            rModel.setImage(res.getString(2));
            rModel.setTitle(res.getString(3));
            rModel.setWeight(res.getString(4));
            rModel.setCost(res.getString(5));
            rModel.setQty(res.getString(6));
            rModel.setDiscount(res.getInt(7));
            rModel.setReglas(res.getString(8));
            rModel.setBonifi(res.getString(9));
            myCarts.add(rModel);
        }

        ItemAdp itemAdp = new ItemAdp(getActivity(), myCarts);
        recyclerView.setAdapter(itemAdp);
        updateItem();
        fetchData();
        return view;
    }

    private void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest request = new JsonArrayRequest(Constant.GET_PROFIL_USER + user.getCodclient(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    Toast.makeText(getContext(), R.string.failed_fetch_data, Toast.LENGTH_LONG).show();
                    return;
                }

                List<Moras> items = new Gson().fromJson(response.toString(), new TypeToken<List<Moras>>() {
                }.getType());

                if (items.size() > 0) {
                    Moroso =  items.get(0).getMoroso();
                    Disponible = items.get(0).getDisponible();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e("INFO", "Error: " + error.getMessage());
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    double total = 0;
    double monto_total_final = 0;


    public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {
        final int[] count = {0};
        double[] totalAmount = {0};
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        private List<MyCart> mData;
        private LayoutInflater mInflater;
        Context mContext;
        SessionManager sessionManager;

        public ItemAdp(Context context, List<MyCart> data) {
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
            View view = mInflater.inflate(R.layout.custome_mycard, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            MyCart cart = mData.get(i);
            Glide.with(getActivity()).load(cart.getImage()).thumbnail(Glide.with(getActivity()).load(R.drawable.lodingimage)).into(holder.imgIcon);
            double res = (Double.parseDouble(cart.getCost()) * myCarts.get(i).getDiscount()) / 100;
            res = Double.parseDouble(cart.getCost()) - res;

            holder.txtGram.setText("  " + cart.getWeight() + "  ");
            holder.txtPrice.setText(sessionManager.getStringData(currncy) + new DecimalFormat(" ##.##").format(res * Double.parseDouble(cart.getQty())));
            holder.txtTitle.setText("" + cart.getTitle());
            holder.txtBonificado.setText(cart.bonifi);
            holder.txtcantidad.setText((sessionManager.getStringData(currncy)).concat(new DecimalFormat(" ##.##").format(res)));

            MyCart myCart = new MyCart();
            myCart.setPid(cart.getPid());
            myCart.setImage(cart.getImage());
            myCart.setTitle(cart.getTitle());
            myCart.setWeight(cart.getWeight());
            myCart.setCost(cart.getCost());
            myCart.setDiscount(cart.getDiscount());
            myCart.setReglas(cart.getReglas());
            myCart.setBonifi(cart.getBonifi());
            int qrt = helper.getCard(myCart.getPid(), myCart.getCost());
            if (qrt != -1) {
                count[0] = qrt;
                holder.txtcount.setText("" + count[0]);
                holder.txtcount.setVisibility(View.VISIBLE);
                holder.imgMins.setVisibility(View.VISIBLE);

            } else {
                holder.txtcount.setVisibility(View.INVISIBLE);
                holder.imgMins.setVisibility(View.INVISIBLE);
            }
            double ress = (Double.parseDouble(myCart.getCost()) / 100.0f) * myCart.getDiscount();
            ress = Double.parseDouble(myCart.getCost()) - ress;
            double temp = ress * qrt;
            totalAmount[0] = totalAmount[0] + temp;
            holder.imgMins.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count[0] = Integer.parseInt(holder.txtcount.getText().toString());
                    count[0] = count[0] - 1;

                    if (count[0] <= 0) {
                        holder.txtcount.setVisibility(View.INVISIBLE);
                        holder.imgMins.setVisibility(View.INVISIBLE);
                        holder.txtcount.setText("" + count[0]);
                        helper.deleteRData(myCart.getPid(), myCart.getCost());
                        myCarts.remove(cart);

                        totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getCost());
                        if (totalAmount[0] == 0) {
                            lvlBacket.setVisibility(View.GONE);
                            rlv_SubTotal.setVisibility(View.GONE);
                            rlv_Iva.setVisibility(View.GONE);
                        }
                        notifyDataSetChanged();
                        updateItem();
                    } else {
                        holder.txtcount.setVisibility(View.VISIBLE);
                        holder.txtcount.setText("" + count[0]);
                        myCart.setQty(String.valueOf(count[0]));
                        totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getCost());
                        myCart.setBonifi(getBonificado(myCart.getReglas(),count[0]));

                        helper.insertData(myCart);
                        notifyDataSetChanged();
                        updateItem();

                    }
                }
            });
            holder.imgPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.txtcount.setVisibility(View.VISIBLE);
                    holder.imgMins.setVisibility(View.VISIBLE);
                    count[0] = Integer.parseInt(holder.txtcount.getText().toString());
                    totalAmount[0] = totalAmount[0] + Double.parseDouble(myCart.getCost());
                    count[0] = count[0] + 1;
                    holder.txtcount.setText("" + count[0]);
                    myCart.setQty(String.valueOf(count[0]));
                    myCart.setBonifi(getBonificado(myCart.getReglas(),count[0]));
                    helper.insertData(myCart);
                    updateItem();
                }
            });
            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog myDelete = new AlertDialog.Builder(getActivity())
                            .setTitle("Borrar")
                            .setMessage("Quieres borrar")
                            .setIcon(R.drawable.ic_delete)
                            .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Log.d("sdj", "" + whichButton);
                                    dialog.dismiss();
                                    totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getCost());
                                    helper.deleteRData(myCart.getPid(), myCart.getCost());
                                    myCarts.remove(cart);
                                    updateItem();
                                    notifyDataSetChanged();
                                }

                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("sdj", "" + which);
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    myDelete.show();
                }
            });

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

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img_icon)
            ImageView imgIcon;
            @BindView(R.id.txt_title)
            TextView txtTitle;
            @BindView(R.id.txt_price)
            TextView txtPrice;
            @BindView(R.id.txt_bonificado)
            TextView txtBonificado;
            @BindView(R.id.txt_gram)
            TextView txtGram;
            @BindView(R.id.img_delete)
            ImageView imgDelete;
            @BindView(R.id.img_mins)
            ImageButton imgMins;
            @BindView(R.id.txtcount)
            TextView txtcount;
            @BindView(R.id.img_plus)
            ImageButton imgPlus;


            @BindView(R.id.txt_cantidad)
            TextView txtcantidad;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }


    }

    public void updateItem() {
        Cursor res = databaseHelper.getAllData();
        double totalRs = 0;
        double totalRsIva = 0;
        double ress = 0;
        double valor_iva = 0;
        double var_monto_final = 0;


        int totalItem = 0;

        if (res.getCount() == 0) {
            lvlNotfound.setVisibility(View.VISIBLE);
            txtNotfound.setText("Carrito vacío");
            lvlBacket.setVisibility(View.GONE);
            rlv_SubTotal.setVisibility(View.GONE);
            rlv_Iva.setVisibility(View.GONE);

        }
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setCost(res.getString(5));
            rModel.setQty(res.getString(6));
            rModel.setDiscount(res.getInt(7));
            rModel.setIva(res.getInt(10));

            ress = (Double.parseDouble(res.getString(5)) * rModel.getDiscount()) / 100;
            ress = Double.parseDouble(res.getString(5)) - ress;


            //ress = ress + valor_iva;

            double temp = Integer.parseInt(res.getString(6)) * ress;
            valor_iva = (temp * rModel.getIva()) / 100;

            totalRs = totalRs + temp;
            totalRsIva = totalRsIva + valor_iva;

            totalItem = totalItem + Integer.parseInt(res.getString(6));

            var_monto_final = totalRs + totalRsIva;


        }
        total = Double.parseDouble(String.valueOf(totalRs));
        monto_total_final = Double.parseDouble(String.valueOf(var_monto_final));

        //txtItem.setText(totalItem + " Items");
        txtvalor_iva.setText(sessionManager.getStringData(currncy) + new DecimalFormat(" ##.##").format(totalRsIva) );
        totleAmount.setText(sessionManager.getStringData(currncy) + new DecimalFormat(" ##.##").format(totalRs));
        monto_final.setText(sessionManager.getStringData(currncy) + new DecimalFormat(" ##.##").format(var_monto_final));
        HomeActivity.getInstance().setFrameMargin(60);





    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.txt_countinue)
    public void onViewClicked() {

        if (sessionManager.getBooleanData(login)) {

            if(Moroso.equals("N/D")){
                Toast.makeText(getActivity(), "Consultando saldo...", Toast.LENGTH_LONG).show();
            }else{
                if (Moroso.equals("N")){
                    if (monto_total_final <=  Double.parseDouble(String.valueOf(Disponible.replaceAll(",","")))){
                        if (sessionManager.getIntData(oMin) <= total) {
                            HomeActivity.getInstance().serchviewHide();
                            HomeActivity.getInstance().titleChange("Pedido realizado ahora");
                            // PlaceOrderFragment fragment = new PlaceOrderFragment();
                            OrderSumrryFragment fragment = new OrderSumrryFragment();
                            HomeActivity.getInstance().callFragment(fragment);
                        } else {
                            Toast.makeText(getActivity(), "Valor mínimo de pedido de " + sessionManager.getStringData(currncy) + " " + sessionManager.getIntData(oMin), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), "Valor del Pedido excede el Disponible " + sessionManager.getStringData(currncy) + " " +Disponible, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), " Se encuenta en un estado de Morosidad, Contactar a su Ejecutivo de Venta para mayor información" , Toast.LENGTH_LONG).show();
                }
            }




        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.getInstance().serchviewShow();
        HomeActivity.getInstance().setFrameMargin(60);
        HomeActivity.getInstance().titleChange("Mi Carrito");

    }
}
