package com.tiendaumk.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.tiendaumk.R;
import com.tiendaumk.adepter.AdapterFacturasLineas;
import com.tiendaumk.model.Factura_lineas;
import com.tiendaumk.utils.Utiles;

import static com.tiendaumk.retrofit.Constant.GET_DETALLE_FACTURA;

public class ActivityViewFactura extends AppCompatActivity {
    String cod_factura,date_factura;

    private AdapterFacturasLineas mAdapter;
    private RecyclerView recyclerView;
    private List<Factura_lineas> productList;
    TextView txt_factura_total,txt_observacion_factura;
    final Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_simple);
        initToolbar();
    }

    private void initToolbar() {
        Intent intent = getIntent();
        cod_factura = intent.getStringExtra("factura_id");
        date_factura = intent.getStringExtra("factura_date");

        getSupportActionBar().setTitle(("Nº ").concat(cod_factura));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.grey_5));
        }
        Utiles.setSystemBarLight(this);

        txt_factura_total = findViewById(R.id.id_factura_total);
        txt_observacion_factura = findViewById(R.id.id_observacion);

        recyclerView = findViewById(R.id.recycler_view);
        productList = new ArrayList<>();
        mAdapter = new AdapterFacturasLineas(this, productList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        fetchData();
    }


    private void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(GET_DETALLE_FACTURA + cod_factura, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), R.string.failed_fetch_data, Toast.LENGTH_LONG).show();
                    return;
                }
                List<Factura_lineas> items = new Gson().fromJson(response.toString(), new TypeToken<List<Factura_lineas>>() {
                }.getType());

                double Order_price = 0;

                for (int i = 0; i < items.size(); i++) {

                    double Sub_total_price = Double.parseDouble(items.get(i).getVENTA());
                    Order_price += Sub_total_price;



                }

                String _Order_price = String.format(Locale.ENGLISH, "%1$,.2f", Order_price);
                txt_observacion_factura.setText(items.get(0).getOBSERVACIONES());
                txt_factura_total.setText(("C$ ").concat(_Order_price));


                productList.clear();
                productList.addAll(items);

                // refreshing recycler view
                mAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e("INFO", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        MenuInflater inflater = getMenuInflater();
         /*inflater.inflate(R.menu.menu_factura, menu);
        Utils.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));

       MenuItem Item_observacion = menu.findItem(R.id.item_commit_factura);

        if (txt_observacion_factura.getText()=="") {
            Item_observacion.setVisible(false);
        }else{
            Item_observacion.setVisible(true);
        }*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;


            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    public void ShowDialog(String strTitle, String strMsg, int color) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);

        LinearLayout lyt = dialog.findViewById(R.id.lyt);
        TextView txt_title = dialog.findViewById(R.id.title);
        TextView txt_msg = dialog.findViewById(R.id.content);

        txt_title.setText(strTitle);
        txt_msg.setText(strMsg);
        lyt.setBackgroundColor(context.getResources().getColor(color));;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
