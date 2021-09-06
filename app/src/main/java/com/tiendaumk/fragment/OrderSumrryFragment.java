package com.tiendaumk.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.onesignal.OneSignal;
import com.tiendaumk.R;
import com.tiendaumk.activity.AddressActivity;
import com.tiendaumk.activity.HomeActivity;
import com.tiendaumk.database.DatabaseHelper;
import com.tiendaumk.database.MyCart;
import com.tiendaumk.model.Address;
import com.tiendaumk.model.AddressData;
import com.tiendaumk.model.Cupon_Respuesta;
import com.tiendaumk.model.Home_crecimiento;
import com.tiendaumk.model.LoginUser;
import com.tiendaumk.model.PaymentItem;
import com.tiendaumk.model.RestResponse;
import com.tiendaumk.model.User;
import com.tiendaumk.retrofit.APIClient;
import com.tiendaumk.retrofit.GetResult;
import com.tiendaumk.utils.CustPrograssbar;
import com.tiendaumk.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static com.tiendaumk.fragment.ItemListFragment.itemListFragment;
import static com.tiendaumk.retrofit.Constant.POST_VALIDAR_CUPON;
import static com.tiendaumk.utils.SessionManager.address1;
import static com.tiendaumk.utils.SessionManager.currncy;
import static com.tiendaumk.utils.SessionManager.tax;
import static com.tiendaumk.utils.Utiles.isRef;
import static com.tiendaumk.utils.Utiles.isSelect;
import static com.tiendaumk.utils.Utiles.seletAddress;


public class OrderSumrryFragment extends Fragment implements GetResult.MyListener {

    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.txt_subtotal)
    TextView txtSubtotal;
    /*@BindView(R.id.txt_delivery)
    TextView txtDelivery;*/ 
    //@BindView(R.id.txt_delevritital)
    //TextView txtDelevritital;
   // @BindView(R.id.txt_total)
    //TextView txtTotal;
    @BindView(R.id.btn_cuntinus)
    TextView btnCuntinus;
    @BindView(R.id.lvlone)
    LinearLayout lvlone;
    @BindView(R.id.lvltwo)
    LinearLayout lvltwo;
    @BindView(R.id.txt_changeadress)
    TextView txtChangeadress;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.txt_texo)
    TextView txtTexo;

    @BindView(R.id.txt_id_cupon)
    TextView txtCupon;

    @BindView(R.id.lbl_titulo_codigo)
    TextView txtlblTitulo;



    @BindView(R.id.id_porcent_descuento)
    TextView txt_porcent_descuento;

    @BindView(R.id.txt_descuentos)
    TextView txtdescuentos;
  /*  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String data  = dateFormat.format(Calendar.getInstance().getTime());

   */
    ProgressDialog progressDialog;

    final String data = (String) DateFormat.format("EEE dd MMM yyyy hh:mm aaa'", Calendar.getInstance().getTime());

    @BindView(R.id.id_count_items)
    TextView txt_count_items;
    /*private String time;

    private String payment;*/
    double total;
    double VarIva;
    double VarDesc = 0.00;
    public static int paymentsucsses = 0;
    public static String tragectionID = "0";
    public static boolean isorder = false;
   // PaymentItem paymentItem;
    Address selectaddress;
    String str_comment="";
    String StrMontoFinal = "0";

    @BindView(R.id.edt_order_list)
    TextView txtComentario;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           /* time = getArguments().getString("TIME");
            data = getArguments().getString("DATE");
            payment = getArguments().getString("PAYMENT");
            paymentItem = (PaymentItem) getArguments().getSerializable("PAYMENTDETAILS");*/
        }
        progressDialog = new ProgressDialog(getActivity());
    }

    DatabaseHelper databaseHelper;
    List<MyCart> myCarts;
    SessionManager sessionManager;
    Unbinder unbinder;
    User user;
    CustPrograssbar custPrograssbar;
    StaggeredGridLayoutManager gaggeredGridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_sumrry, container, false);
        unbinder = ButterKnife.bind(this, view);
        custPrograssbar = new CustPrograssbar();
        databaseHelper = new DatabaseHelper(getActivity());
        sessionManager = new SessionManager(getActivity());
        HomeActivity.getInstance().setFrameMargin(0);
        user = sessionManager.getUserDetails("");
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        myRecyclerView.setLayoutManager(gaggeredGridLayoutManager);
        getAddress();
        myCarts = new ArrayList<>();
        Cursor res = databaseHelper.getAllData();

        txt_count_items.setText(res.getCount() + " Items");
        if (res.getCount() == 0) {
            Toast.makeText(getActivity(), "SIN DATOS", Toast.LENGTH_LONG).show();
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
            rModel.setBonifi(res.getString(9));
            rModel.setIva(res.getInt(10));
            myCarts.add(rModel);
        }

        txtlblTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_cupon);
                dialog.setCancelable(true);

                LinearLayout lyt = dialog.findViewById(R.id.lyt);
                TextView txt_title = dialog.findViewById(R.id.title);
                EditText txt_msg = dialog.findViewById(R.id.ed_titulo);
                AppCompatButton appBtn = dialog.findViewById(R.id.bt_close);

                txt_title.setText("¿Cuentas con un código de promoción?");
                txt_msg.setText("");
                lyt.setBackgroundColor(getResources().getColor(R.color.light_green_400));

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                appBtn.setText("Aplicar");

                appBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (txt_msg.getText().toString().equals("")){
                            Toast.makeText(getContext(), "Ingrese el codigo de promoción", Toast.LENGTH_SHORT).show();

                        }else{
                            if (txt_msg.getText().length() <= 3){
                                Toast.makeText(getContext(), "Codigo promocional muy corto", Toast.LENGTH_SHORT).show();
                            }else{
                                ReQuest_Validar_Cupon(txt_msg.getText().toString(),btnCuntinus.getText().toString());
                            }
                        }
                        dialog.dismiss();
                    }
                });

                if (txtCupon.getText().equals(" ")){
                    dialog.show();
                    dialog.getWindow().setAttributes(lp);
                }

            }
        });
        return view;




    }

    private void ReQuest_Validar_Cupon(String Codigo_Cupon, String MontoPedido){

        progressDialog.setTitle("Validando información");
        progressDialog.setMessage("Por favor espere..." );
        progressDialog.show();





        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_VALIDAR_CUPON, new Response.Listener<String>() {
            @Override
            public void onResponse(final String ServerResponse) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Gson gson = new Gson();
                        double[] Cantidad_pedido = {0};
                        Cupon_Respuesta RespuestaCupon = gson.fromJson(ServerResponse, Cupon_Respuesta.class);

                        if (RespuestaCupon.getmResult().equals("true")){

                            Cursor res = databaseHelper.getData_Categorias("");

                            double Cantidad_Minima_cupon = Double.parseDouble(RespuestaCupon.getArticulosCupon().get(0).getmCantidad_Cupon());
                            String Cat_cupon    = RespuestaCupon.getArticulosCupon().get(0).getmCupon_cat();


                            if (RespuestaCupon.getArticulosCupon().get(0).getmArticulos().equals("ALL")){

                                while (res.moveToNext()) {
                                    Cantidad_pedido[0] = Cantidad_pedido[0] + Double.parseDouble(res.getString(1));
                                }

                                if(Cantidad_pedido[0] >= Cantidad_Minima_cupon){
                                    VarDesc = Double.parseDouble(RespuestaCupon.getArticulosCupon().get(0).getmPorcent_Cupon());
                                    txtCupon.setText(Codigo_Cupon);
                                    txtlblTitulo.setText("Código de promoción: ");
                                }else{
                                    Toast.makeText(getContext(), "La Compra Minimo para este aplicar este cupon es : " + Cantidad_Minima_cupon, Toast.LENGTH_LONG).show();
                                }

                            }else {

                                if (RespuestaCupon.getArticulosCupon().get(0).getmArticulos().equals("Custom")){

                                    String articulos_no_facturados = RespuestaCupon.getArticulosCupon().get(0).getmPorcent_Cupon();

                                    Cursor res_02 = databaseHelper.getData_Categorias(articulos_no_facturados);
                                    if(res_02.getCount() == 1 ){
                                        while (res_02.moveToNext()) {
                                            Cantidad_pedido[0] = Double.parseDouble(res_02.getString(1));
                                            if(Cantidad_pedido[0] >= Cantidad_Minima_cupon){
                                                VarDesc = Double.parseDouble(RespuestaCupon.getArticulosCupon().get(0).getmCupon_cat());
                                                txtCupon.setText(Codigo_Cupon);
                                                txtlblTitulo.setText("Código de promoción: ");
                                            }else{
                                                Toast.makeText(getContext(), "La Compra Minimo para este aplicar este cupon es : " + Cantidad_Minima_cupon, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }


                                }else{
                                    if(res.getCount() == 1 ){
                                        while (res.moveToNext()) {
                                            Cantidad_pedido[0] = Double.parseDouble(res.getString(1));
                                            String Cat_pedido   = res.getString(0);
                                            if(Cantidad_pedido[0] >= Cantidad_Minima_cupon){

                                                if (Cat_pedido.equals(Cat_cupon)){
                                                    VarDesc = Double.parseDouble(RespuestaCupon.getArticulosCupon().get(0).getmPorcent_Cupon());
                                                    txtCupon.setText(Codigo_Cupon);
                                                    txtlblTitulo.setText("Código de promoción: ");
                                                }else{
                                                    Toast.makeText(getContext(), "Este Cupon no aplica para estos articulos" , Toast.LENGTH_LONG).show();
                                                }


                                            }else{
                                                Toast.makeText(getContext(), "La Compra Minimo para este aplicar este cupon es : " + Cantidad_Minima_cupon, Toast.LENGTH_LONG).show();
                                            }
                                        }

                                    }else{
                                        Toast.makeText(getContext(), "Pedido con no cumple la condiciones" , Toast.LENGTH_LONG).show();
                                    }
                                }

                            }
                        }else{
                            Toast.makeText(getContext(), RespuestaCupon.getmResponseMsg() , Toast.LENGTH_SHORT).show();
                        }

                        update(myCarts);

                    }
                }, 2000);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cod_cupones", Codigo_Cupon);
                params.put("cod_cliente", user.getCodclient());


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);




    }


    private void update(List<MyCart> mData) {

        double[] totalAmount = {0};

        double totalRsIva = 0;
        double valor_iva = 0;

        double valor_descuento = 0;

        for (int i = 0; i < mData.size(); i++) {
            MyCart cart = mData.get(i);

            double res = (Double.parseDouble(cart.getCost()) / 100.0f) * cart.getDiscount();
            res = Double.parseDouble(cart.getCost()) - res;

            int qrt = databaseHelper.getCard(cart.getPid(), cart.getCost());
            double temp = res * qrt;

            valor_iva = (temp * cart.getIva()) / 100;


            totalAmount[0] = totalAmount[0] + temp;


            totalRsIva = totalRsIva + valor_iva;


        }

        valor_descuento = (totalAmount[0] * (VarDesc / 100));


        txtSubtotal.setText(sessionManager.getStringData(currncy) + new DecimalFormat(" ###,###.##").format(totalAmount[0] ));

        txt_porcent_descuento.setText((" % ").concat(new DecimalFormat(" ###,###.##").format(VarDesc)));
        txtdescuentos.setText(sessionManager.getStringData(currncy) + new DecimalFormat(" ###,###.##").format(valor_descuento));

        StrMontoFinal = new DecimalFormat(" ###,###.##").format(totalRsIva);

        txtTexo.setText(sessionManager.getStringData(currncy) + StrMontoFinal);


       /* if (payment.equalsIgnoreCase(getResources().getString(R.string.pic_myslf))) {
            txtDelivery.setVisibility(View.VISIBLE);
          //  txtDelevritital.setVisibility(View.VISIBLE);
            txtDelivery.setText(sessionManager.getStringData(currncy) + "0");
        } else {
            totalAmount[0] = totalAmount[0] + selectaddress.getDeliveryCharge();
            txtDelivery.setText(sessionManager.getStringData(currncy) + selectaddress.getDeliveryCharge());
        }*/

        totalAmount[0] = totalAmount[0] ;
        //txtDelivery.setText(sessionManager.getStringData(currncy) );

        //txtTotal.setText(sessionManager.getStringData(currncy) + new DecimalFormat("##.##").format(totalAmount[0]));
        btnCuntinus.setText("TOTAL - " + sessionManager.getStringData(currncy)  + " " + new DecimalFormat("###,###.##").format(((totalAmount[0] - valor_descuento) + totalRsIva)) );
        total = totalAmount[0] + totalRsIva;
        VarIva = totalRsIva;


    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_comment);
        dialog.setCancelable(true);
        final TextView et_post =  dialog.findViewById(R.id.et_post);

        et_post.setText("");
        et_post.setText(str_comment);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        final String Fecha = (String) DateFormat.format("EEE dd MMM yyyy hh:mm aaa'", Calendar.getInstance().getTime());

        final AppCompatButton bt_submit = dialog.findViewById(R.id.bt_submit);
        ((TextView) dialog.findViewById(R.id.lbl_date)).setText(Fecha);

        ((EditText) dialog.findViewById(R.id.et_post)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bt_submit.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_comment = et_post.getText().toString();
                txtComentario.setText(str_comment);
                dialog.dismiss();

            }
        });



        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {
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
            View view = mInflater.inflate(R.layout.custome_sumrry, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            MyCart cart = mData.get(i);
            Glide.with(getActivity()).load(cart.getImage()).thumbnail(Glide.with(getActivity()).load(R.drawable.lodingimage)).into(holder.imgIcon);
            double res = (Double.parseDouble(cart.getCost()) / 100.0f) * cart.getDiscount();
            res = Double.parseDouble(cart.getCost()) - res;
            holder.txtTitle.setText("" + cart.getTitle());
            MyCart myCart = new MyCart();
            myCart.setPid(cart.getPid());
            myCart.setImage(cart.getImage());
            myCart.setTitle(cart.getTitle());
            myCart.setWeight(cart.getWeight());
            myCart.setCost(cart.getCost());
            myCart.setBonifi(cart.getBonifi());
            int qrt = helper.getCard(myCart.getPid(), myCart.getCost());
            holder.txtPriceanditem.setText((sessionManager.getStringData(currncy)).concat(new DecimalFormat("###,###.##").format(res)).concat(" x ").concat(String.valueOf(qrt)));
            double temp = res * qrt;
            holder.txtPrice.setText(sessionManager.getStringData(currncy) + new DecimalFormat("###,###.##").format(temp));

            holder.idsku.setText(myCart.getPid());
            holder.idbonificacion.setText(myCart.getBonifi());

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
            @BindView(R.id.txt_priceanditem)
            TextView txtPriceanditem;
            @BindView(R.id.txt_price)
            TextView txtPrice;
            @BindView(R.id.id_sku)
            TextView idsku;
            @BindView(R.id.id_bonificacion)
            TextView idbonificacion;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }


    }


    private void orderPlace(JSONArray jsonArray) {
        if (selectaddress == null) {
            getAddress();
            return;
        }
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {

            String Send_Address = selectaddress.getTitulo().concat(",").concat(selectaddress.getDireec()).concat(",").concat(selectaddress.getReferecia());

            jsonObject.put("uid", user.getId());
            jsonObject.put("timesloat", txtCupon.getText()); // ESTE ES EL EQUIVALENTE AL CODIGO DEL DESCUENTO
            jsonObject.put("porcent_cupon", txt_porcent_descuento.getText()); // ESTE ES EL VALOR PORCENTUAL DEL DESCUENTO
            jsonObject.put("ddate", data);
            jsonObject.put("total", total);
            jsonObject.put("p_method", txtdescuentos.getText()); // ESTE SERA EL VALOR DEL DESCUENTO EN MONEDA
            jsonObject.put("address_id", selectaddress.getId());
            jsonObject.put("address_txt", Send_Address);
            jsonObject.put("tax", VarIva);
            jsonObject.put("tid", tragectionID);
            jsonObject.put("comment", str_comment);
            jsonObject.put("player_id", OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());
            jsonObject.put("pname", jsonArray);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().order((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(getActivity(), "" + response.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.getResult().equals("true")) {
                    lvlone.setVisibility(View.GONE);
                    lvltwo.setVisibility(View.VISIBLE);
                    databaseHelper.deleteCard();
                    isorder = true;

                }
            } else if (callNo.equalsIgnoreCase("2323")) {
                Gson gson = new Gson();
                btnCuntinus.setClickable(true);

                AddressData addressData = gson.fromJson(result.toString(), AddressData.class);
                if (addressData.getResult().equalsIgnoreCase("true")) {
                    if (addressData.getResultData().size() != 0) {
                        selectaddress = addressData.getResultData().get(seletAddress);
                       /* if (selectaddress.isUpdateNeed()) {
                            Toast.makeText(getActivity(), "Please Update Your Area Name.Because It's Not match with Our Delivery Location", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), AddressActivity.class).putExtra("MyClass", selectaddress));
                        } else {
                            //txtAddress.setText(selectaddress.getHno() + "," + selectaddress.getSociety() + "," + selectaddress.getArea() + "," + selectaddress.getLandmark() + "," + selectaddress.getName());

                        }*/
                        txtAddress.setText(selectaddress.getDireec() + "," + selectaddress.getReferecia() );
                        ItemAdp itemAdp = new ItemAdp(getActivity(), myCarts);
                        myRecyclerView.setAdapter(itemAdp);
                        update(myCarts);


                    } else {
                        Toast.makeText(getActivity(), "Agregue su Dirección", Toast.LENGTH_LONG).show();

                        AddressFragment fragment = new AddressFragment();
                        HomeActivity.getInstance().callFragment(fragment);
                    }
                } else {
                    Toast.makeText(getActivity(), "Agregue su Dirección ", Toast.LENGTH_LONG).show();

                    AddressFragment fragment = new AddressFragment();
                    HomeActivity.getInstance().callFragment(fragment);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.txt_changeadress, R.id.btn_cuntinus, R.id.txt_trackorder,R.id.id_img_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.id_img_comment:
                showCustomDialog();
            break;
            case R.id.txt_changeadress:
                isSelect = true;
                AddressFragment fragment = new AddressFragment();
                HomeActivity.getInstance().callFragment(fragment);
                break;
            case R.id.txt_trackorder:
                clearFragment();
                break;
            case R.id.btn_cuntinus:
                btnCuntinus.setClickable(false);
                //Toast.makeText(getContext(), payment, Toast.LENGTH_SHORT).show();

                sendorderServer();

                /*if (payment.equalsIgnoreCase("Razorpay")) {
                    int temtoal = (int) Math.round(total);
                    startActivity(new Intent(getActivity(), RazerpayActivity.class).putExtra("amount", temtoal).putExtra("detail", paymentItem));
                } else if (payment.equalsIgnoreCase("Paypal")) {
                    startActivity(new Intent(getActivity(), PaypalActivity.class).putExtra("amount", total).putExtra("detail", paymentItem));

                } else if (payment.equalsIgnoreCase("Cash On Delivery") || payment.equalsIgnoreCase("Pickup Myself")) {

                }*/

                break;
            default:
                break;
        }
    }

    public void clearFragment() {
        sessionManager = new SessionManager(getActivity());
        User user1 = sessionManager.getUserDetails("");
        HomeActivity.getInstance().titleChange("UNIMARK S,A.");
        MyOrderFragment homeFragment = new MyOrderFragment();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getFragmentManager().beginTransaction().replace(R.id.fragment_frame, homeFragment).addToBackStack(null).commit();
    }

    private void sendorderServer() {
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            return;
        }
        if (user.getArea() != null || user.getSociety() != null || user.getHno() != null || user.getMobile() != null) {
            JSONArray jsonArray = new JSONArray();
            while (res.moveToNext()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", res.getString(0));
                    jsonObject.put("pid", res.getString(1));
                    jsonObject.put("image", res.getString(2));
                    jsonObject.put("title", res.getString(3));
                    jsonObject.put("weight", res.getString(4));
                    jsonObject.put("cost", res.getString(5));
                    jsonObject.put("qty", res.getString(6));
                    jsonObject.put("Boni", res.getString(9));
                    jsonArray.put(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            orderPlace(jsonArray);
        } else {
            startActivity(new Intent(getActivity(), AddressActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.getInstance().serchviewHide();
        HomeActivity.getInstance().setFrameMargin(0);
        try {
            if (btnCuntinus != null) {
                btnCuntinus.setClickable(true);
            }
            if (paymentsucsses == 1) {
                paymentsucsses = 0;
                sendorderServer();
            }
            if (sessionManager != null) {
                selectaddress = sessionManager.getAddress(address1);
                if (selectaddress != null) {
                    //txtAddress.setText(selectaddress.getHno() + "," + selectaddress.getSociety() + "," + selectaddress.getArea() + "," + selectaddress.getLandmark() + "," + selectaddress.getName());
                    txtAddress.setText(selectaddress.getTitulo() );
                    update(myCarts);
                    if(isRef){
                        isRef=false;
                        ItemAdp itemAdp = new ItemAdp(getActivity(), myCarts);
                        myRecyclerView.setAdapter(itemAdp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAddress() {
        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("cod_client", user.getCodclient());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getAddress((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2323");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
