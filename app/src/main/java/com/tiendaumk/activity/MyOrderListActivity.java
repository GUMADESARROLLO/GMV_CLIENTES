package com.tiendaumk.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.tiendaumk.R;
import com.tiendaumk.model.MyOrder;
import com.tiendaumk.model.Productinfo;
import com.tiendaumk.model.RestResponse;
import com.tiendaumk.model.User;
import com.tiendaumk.retrofit.APIClient;
import com.tiendaumk.retrofit.GetResult;
import com.tiendaumk.utils.CustPrograssbar;
import com.tiendaumk.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

import static com.tiendaumk.utils.SessionManager.currncy;
import static com.tiendaumk.utils.Utiles.isrates;

public class MyOrderListActivity extends BaseActivity implements GetResult.MyListener {

    User user;
    SessionManager sessionManager;
    String oid;
    String id;

    @BindView(R.id.txt_orderid)
    TextView txtOrderid;

    @BindView(R.id.txt_subtotal)
    TextView txtSubtotal;

    @BindView(R.id.txt_date)
    TextView txtDate;

    @BindView(R.id.txt_delivery)
    TextView txtDelivery;

    @BindView(R.id.txt_tax)
    TextView txtTax;

    @BindView(R.id.edt_order_list)
    TextView txt_order_list;



    @BindView(R.id.txt_qty)
    TextView txtQty;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.lvl_items)
    LinearLayout lvlItems;
    @BindView(R.id.lvl_data)
    LinearLayout lvlData;
    @BindView(R.id.txt_stutus)
    TextView txtStutus;
    @BindView(R.id.txt_ptype)
    TextView txtPtype;
    @BindView(R.id.txt_Address)
    TextView txtAddress;
    String phonecall;
  /*  @BindView(R.id.time_view)
    StateProgressBar timeView;*/
    CustPrograssbar custPrograssbar;
    public static MyOrderListActivity orderListActivity = null;

    public static MyOrderListActivity getInstance() {
        return orderListActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Resumen ");
        getSupportActionBar().setElevation(0);
        custPrograssbar=new CustPrograssbar();
        sessionManager = new SessionManager(MyOrderListActivity.this);
        user = sessionManager.getUserDetails("");
        String[] descriptionData = {"Pending", "Ready to Ship", "Delivered"};
        //timeView.setStateDescriptionData(descriptionData);
        //timeView.checkStateCompleted(true);
        Intent intent = getIntent();
        id = intent.getStringExtra("oid");
        getHistry(id);
    }

    private void getHistry(String id) {
        custPrograssbar.prograssCreate(MyOrderListActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("uid", user.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getPlist((JsonObject) jsonParser.parse(jsonObject.toString()));
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
            if (callNo.equalsIgnoreCase("1")) {
                List<Productinfo> list = new ArrayList<>();
                Gson gson = new Gson();
                MyOrder myOrder = gson.fromJson(result.toString(), MyOrder.class);
                if (myOrder.getResult().equals("true")) {
                    list.addAll(myOrder.getProductinfo());
                    if (myOrder.getpMethod().equalsIgnoreCase(getResources().getString(R.string.pic_myslf))) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lvlData.getLayoutParams();
                        params.setMargins(0, 0, 0, 100);
                        lvlData.setLayoutParams(params);

                    } else {

                    }
                    phonecall = myOrder.getRiderMobile();
                    txtOrderid.setText("" + myOrder.getmOrderid());
                    txtStutus.setText( " " + myOrder.getStatus());
                    txtDate.setText("" + myOrder.getOrderDate());
                    txt_order_list.setText("" + myOrder.getmComment());
                    //txtDelivery.setText("" + sessionManager.getStringData(currncy) + myOrder.getdCharge());
                    txtDelivery.setText("DIRECCIÓN DE ENTREGA");
                    //txtPtype.setText(" " + myOrder.getpMethod() + " ");
                    txtQty.setText(list.size()  + " Item(s)");
                    txtAddress.setText("" + myOrder.getAddress());

                    txtTax.setText(sessionManager.getStringData(currncy) + myOrder.getTax());

                    txtSubtotal.setText("" + sessionManager.getStringData(currncy) + new DecimalFormat("##.##").format(myOrder.getSubTotal()));
                    txtTotal.setText("" + sessionManager.getStringData(currncy) + new DecimalFormat("##.##").format(myOrder.getTotalAmt()));
                    oid = myOrder.getmOrderid();

                    Log.e("TAG", "callback: " + myOrder.getStatus() );


                    if (myOrder.getmIsrated().equalsIgnoreCase("No") && myOrder.getStatus().equalsIgnoreCase("Completed")) {
                        //item.setVisible(true);
                        Log.e("TAG", "item: True ");
                    } else {
                        //item.setVisible(true);
                        Log.e("TAG", "item: False ");
                    }
                    if (myOrder.getStatus().equalsIgnoreCase("processing") || myOrder.getStatus().equalsIgnoreCase(getResources().getString(R.string.pic_myslf))) {
                        //itemC.setVisible(true);
                        Log.e("TAG", "itemC: True ");

                    } else {
                        //itemC.setVisible(false);
                        Log.e("TAG", "itemC: False ");
                    }

                    /*if ( myOrder.getpMethod().equalsIgnoreCase(getResources().getString(R.string.pic_myslf))) {
                        timeView.setVisibility(View.GONE);
                    }

                    if (myOrder.getStatus().equalsIgnoreCase("Pending")) {
                        timeView.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                    } else if (myOrder.getStatus().equalsIgnoreCase("processing")) {
                        timeView.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    } else if (myOrder.getStatus().equalsIgnoreCase("Completed")) {
                        timeView.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    } else if (myOrder.getStatus().equalsIgnoreCase("cancelled")) {
                        timeView.setVisibility(View.GONE);
                    }*/
                    setOrderList(lvlItems, list);
                }else {
                    custPrograssbar.closePrograssBar();

                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(MyOrderListActivity.this, response.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.getResult().equalsIgnoreCase("true")) {
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "callback: " + e.getMessage() );
        }
    }

    private void setOrderList(LinearLayout lnrView, List<Productinfo> list) {
        lnrView.removeAllViews();
        int a = 0;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(MyOrderListActivity.this);
                a = a + 1;
                View view = inflater.inflate(R.layout.custome_myoder, null);
                ImageView img_icon = view.findViewById(R.id.img_icon);
                TextView txt_name = view.findViewById(R.id.txt_name);
                TextView txt_boni = view.findViewById(R.id.txt_qty);
                TextView txt_weight = view.findViewById(R.id.txt_weight);
                TextView txt_price = view.findViewById(R.id.txt_price);
                TextView txt_sku = view.findViewById(R.id.id_sku);


                Glide.with(MyOrderListActivity.this).load( list.get(i).getProductImage()).thumbnail(Glide.with(MyOrderListActivity.this).load(R.drawable.lodingimage)).into(img_icon);
                txt_name.setText(list.get(i).getProductName());

                txt_sku.setText(list.get(i).getProductsku());
                txt_boni.setText(list.get(i).getProductBoni());


                double Total_linea = (Double.parseDouble(list.get(i).getProductPrice()) * Double.parseDouble(list.get(i).getProductQty()));
                txt_weight.setText(sessionManager.getStringData(currncy).concat(String.valueOf(Total_linea)));

                double ress = (Double.parseDouble(list.get(i).getProductPrice()) * list.get(i).getDiscount()) / 100;
                ress = Double.parseDouble(list.get(i).getProductPrice()) - ress;
                txt_price.setText(sessionManager.getStringData(currncy) + ress + " X " + list.get(i).getProductQty());
                lnrView.addView(view);
            }
        }
        custPrograssbar.closePrograssBar();

    }

    MenuItem item;
    MenuItem itemC;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rates_menu, menu);
        item = menu.findItem(R.id.item_rates);
        itemC = menu.findItem(R.id.item_cancel);
        item.setVisible(false);
        itemC.setVisible(false);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_rates:
                startActivity(new Intent(MyOrderListActivity.this, RatesActivity.class).putExtra("oid", oid));
                return true;
            case R.id.item_cancel:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phonecall));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);

                    return true;
                }
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (item != null && isrates) {
            isrates = false;
            item.setVisible(false);
        }

    }
}
