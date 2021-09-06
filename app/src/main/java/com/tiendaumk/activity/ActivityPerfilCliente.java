package com.tiendaumk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tiendaumk.adepter.AdapterPerfilLotes;
import com.tiendaumk.model.Facturas_mora;

import org.json.JSONArray;
import com.tiendaumk.R;
import com.tiendaumk.model.Moras;
import com.tiendaumk.model.User;
import com.tiendaumk.retrofit.Constant;
import com.tiendaumk.utils.MyDividerItemDecoration;
import com.tiendaumk.utils.SessionManager;
import com.tiendaumk.utils.Utiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ActivityPerfilCliente extends AppCompatActivity {

    TextView txt_perfil_name_cliente,txt_perfil_disponible,txt_perfil_saldo,txt_perfil_limite;
    TextView txt_perfil_noVencido,txt_perfil_d30,txt_perfil_d60,txt_perfil_d90,txt_perfil_d120,txt_perfil_m120;
    TextView txt_tele,txt_condicion_pago;
    String code_cliente;

    public static ArrayList<String> factura_id = new ArrayList<String>();
    public static ArrayList<String> factura_date = new ArrayList<String>();
    public static ArrayList<String> factura_cant = new ArrayList<String>();
    public static ArrayList<String> factura_monto = new ArrayList<String>();

    View lyt_empty_history;
    RecyclerView recyclerView;
    AdapterPerfilLotes recyclerAdaptePerfilFactura;
    List<Facturas_mora> arrayItemLotes;
    User user;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        sessionManager = new SessionManager(ActivityPerfilCliente.this);
        user = sessionManager.getUserDetails("");
        initToolbar();
    }

    private void initToolbar() {
        lyt_empty_history = findViewById(R.id.lyt_empty_result);

        txt_perfil_name_cliente = findViewById(R.id.id_perfil_name_cliente);
        txt_perfil_disponible   = findViewById(R.id.id_perfil_disponible);
        txt_perfil_saldo        = findViewById(R.id.id_perfil_saldo);
        txt_perfil_limite       = findViewById(R.id.id_perfil_limite);

        txt_tele                = findViewById(R.id.id_tele);
        txt_condicion_pago      = findViewById(R.id.id_condicion_pago);

        txt_perfil_noVencido    = findViewById(R.id.id_perfil_noVencido);
        txt_perfil_d30          = findViewById(R.id.id_perfil_d30);
        txt_perfil_d60          = findViewById(R.id.id_perfil_d60);
        txt_perfil_d90          = findViewById(R.id.id_perfil_d90);
        txt_perfil_d120         = findViewById(R.id.id_perfil_d120);
        txt_perfil_m120         = findViewById(R.id.id_perfil_m120);


        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL, 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerAdaptePerfilFactura = new AdapterPerfilLotes(this, arrayItemLotes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.grey_5));
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),  new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ActivityPerfilCliente.this, ActivityViewFactura.class);
                        intent.putExtra("factura_id",factura_id.get(position));
                        intent.putExtra("factura_date",factura_date.get(position));
                        startActivity(intent);
                    }
                }, 400);
            }
        }));


        Utiles.setSystemBarLight(this);




        Intent intent = getIntent();



        code_cliente = intent.getStringExtra("Client_Code");



        findViewById(R.id.id_history_last_3m).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPerfilCliente.this, ActivityUltimos3m.class);

                intent.putExtra("factura_id",code_cliente);
                intent.putExtra("Name_cliente",txt_perfil_name_cliente.getText());

                startActivity(intent);
            }
        });

        fetchData();

    }
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final ClickListener clickListener) {

            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        public void onClick(View view, int position);
    }
    public void clearData() {
        factura_id.clear();
        factura_date.clear();
        factura_cant.clear();
        factura_monto.clear();
    }

    private void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(ActivityPerfilCliente.this);
        JsonArrayRequest request = new JsonArrayRequest(Constant.GET_PROFIL_USER + code_cliente, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), R.string.failed_fetch_data, Toast.LENGTH_LONG).show();
                    return;
                }



                List<Moras> items = new Gson().fromJson(response.toString(), new TypeToken<List<Moras>>() {
                }.getType());



                if (items.size() > 0) {
                    lyt_empty_history.setVisibility(View.GONE);
                    txt_perfil_noVencido.setText(("C$ ").concat(items.get(0).getNoVencidos()));
                    txt_perfil_d30.setText(("C$ ").concat(items.get(0).getDias30()));
                    txt_perfil_d60.setText(("C$ ").concat(items.get(0).getDias60()));
                    txt_perfil_d90.setText(("C$ ").concat(items.get(0).getDias90()));
                    txt_perfil_d120.setText(("C$ ").concat(items.get(0).getDias120()));
                    txt_perfil_m120.setText(("C$ ").concat(items.get(0).getMas120()));


                    txt_perfil_name_cliente.setText(items.get(0).getNombreCliente());

                    if (items.get(0).getMoroso().equals("S")){
                        txt_tele.setText("Se encuentra en un estado de Morosidad.");
                    }

                    txt_condicion_pago.setText("");
                    txt_perfil_disponible.setText(("C$ ").concat(items.get(0).getDisponible()));
                    txt_perfil_saldo.setText(("C$ ").concat(items.get(0).getSaldo()));
                    txt_perfil_limite.setText(("C$ ").concat(items.get(0).getLimite()));


                    clearData();



                    if (items.get(0).getFACT_PEND().equals("")){
                        Log.e("TAG_Erro", "onResponse: SinFacturas" );
                    }else{
                        List<String> data = Arrays.asList(items.get(0).getFACT_PEND().split(","));

                        for (int i = 0; i < data.size(); i++) {

                            List<String> row = Arrays.asList(data.get(i).split(":"));
                            factura_id.add(row.get(0));
                            factura_cant.add(row.get(1));
                            factura_date.add(row.get(2));
                            factura_monto.add(row.get(3));

                        }
                    }



                    recyclerView.setAdapter(recyclerAdaptePerfilFactura);
                } else {
                    lyt_empty_history.setVisibility(View.VISIBLE);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityPerfilCliente.this, "onErrorResponse", Toast.LENGTH_SHORT).show();
                Log.e("INFO", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil_cliente, menu);
        return true;
    }
}

