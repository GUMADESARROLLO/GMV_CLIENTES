package com.tiendaumk.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tiendaumk.R;
import com.tiendaumk.adepter.CrecimientoFacturadoMensualAdp;
import com.tiendaumk.fragment.CrecArticuloFacturado;
import com.tiendaumk.fragment.CrecArticuloNoFacturado;
import com.tiendaumk.fragment.FragmentTabsGallery;
import com.tiendaumk.model.Articulos_facturado;
import com.tiendaumk.model.Articulos_venta_anual;
import com.tiendaumk.model.Home_crecimiento;
import com.tiendaumk.model.User;
import com.tiendaumk.retrofit.APIClient;
import com.tiendaumk.retrofit.GetResult;
import com.tiendaumk.utils.SessionManager;
import com.tiendaumk.utils.Utiles;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
public class TabsLight extends AppCompatActivity implements GetResult.MyListener{

    BarChart chart;

    private ViewPager view_pager;
    private TabLayout tab_layout;

    List<Articulos_venta_anual> VentaAnualList;
    RecyclerView recyclerView;
    CrecimientoFacturadoMensualAdp adap;

    List<Articulos_facturado> Articulos_Facturados = new ArrayList<>();
    List<Articulos_facturado> Articulos_No_Facturados = new ArrayList<>();

    Intent intent;
    String code_cliente;

    TextView txt_Meta,txt_compra_anual,txt_poce_cumpli;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_light);
        getHome();
        initToolbar();
        initComponent();


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Utiles.setSystemBarColor(this, R.color.grey_5);
        Utiles.setSystemBarLight(this);
    }

    private void initComponent() {

        recyclerView = findViewById(R.id.id_rc_meses);
        chart = findViewById(R.id.chart1);

        txt_Meta  = findViewById(R.id.id_txt_Meta);
        txt_compra_anual = findViewById(R.id.id_txt_compra_anual);



        VentaAnualList = new ArrayList<>();
        adap = new CrecimientoFacturadoMensualAdp(VentaAnualList);
        recyclerView.setAdapter(adap);
        recyclerView.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);



    }
    private void getHome() {
        intent = getIntent();
        code_cliente = intent.getStringExtra("Client_Code");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", code_cliente);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getHome_crecimiento((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "getResultado_Crecimiento");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void  callback(JsonObject result, String callNo) {
        try {
            if(callNo.equalsIgnoreCase("getResultado_Crecimiento")){

                VentaAnualList=new ArrayList<>();
                Articulos_Facturados = new ArrayList<>();
                Articulos_No_Facturados = new ArrayList<>();

                Gson gson = new Gson();
                Home_crecimiento perfil_crecimiento = gson.fromJson(result.toString(), Home_crecimiento.class);

                VentaAnualList.addAll(perfil_crecimiento.getResultHome().getFacturaAnualItems());
                adap = new CrecimientoFacturadoMensualAdp(VentaAnualList);
                recyclerView.setAdapter(adap);

                Articulos_Facturados = perfil_crecimiento.getResultHome().getArticulosFacturadotItems();


                Articulos_No_Facturados = perfil_crecimiento.getResultHome().getArticuloNoFactuadoItems();

                String Meta_ = String.format(Locale.ENGLISH, "%1$,.2f", Double.parseDouble(perfil_crecimiento.getmCliente_Meta()));
                String Vent_ = String.format(Locale.ENGLISH, "%1$,.2f", Double.parseDouble(perfil_crecimiento.getmCliente_Venta()));


                txt_Meta.setText(("C$ ").concat(Meta_));
                txt_compra_anual.setText(("% ").concat(perfil_crecimiento.getmPorce_cump()));
                //txt_poce_cumpli.setText(("% ").concat(perfil_crecimiento.getmPorce_cump()));
                txt_compra_anual.setTextColor(this.getResources().getColor(((Double.parseDouble(perfil_crecimiento.getmPorce_cump()) < 0) ? R.color.colorRad : R.color.txt_price_color)));

                getSupportActionBar().setTitle("Perfil");


                setGrafica(VentaAnualList);
            }

        } catch (Exception e) {
            e.toString();
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());


        FragmentTabsGallery frg01 = FragmentTabsGallery.newInstance();
        FragmentTabsGallery frg02 = FragmentTabsGallery.newInstance();

        frg01.setTxt_(Articulos_Facturados);
        frg02.setTxt_(Articulos_No_Facturados);

        adapter.addFragment(frg01, "Facturados");
        adapter.addFragment(frg02, "Nuevos");
        viewPager.setAdapter(adapter);
    }
    private void setGrafica( List<Articulos_venta_anual> dataList) {
        List<Float> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {

            entries.add(Float.valueOf(dataList.get(i).getmCantidad()));

            labels.add( dataList.get(i).getmMes());

        }
        create_graph(labels,entries);


        setupViewPager(view_pager);
    }
    public void create_graph(List<String> graph_label, List<Float> userScore) {

        try {
            chart.setDrawBarShadow(false);
            chart.setDrawValueAboveBar(false);
            chart.getDescription().setEnabled(false);
            chart.setPinchZoom(false);

            chart.setDrawGridBackground(false);


            YAxis yAxis = chart.getAxisLeft();
            yAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    //return String.valueOf((int) value);
                    return "";
                }
            });

            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);


            yAxis.setGranularity(1f);
            yAxis.setGranularityEnabled(false);

            chart.getAxisRight().setEnabled(false);


            XAxis xAxis = chart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(false);
            xAxis.setCenterAxisLabels(false);
            xAxis.setDrawGridLines(false);

            //xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //xAxis.setValueFormatter(new IndexAxisValueFormatter(graph_label));

            List<BarEntry> yVals1 = new ArrayList<BarEntry>();

            for (int i = 0; i < userScore.size(); i++) {
                yVals1.add(new BarEntry(i, userScore.get(i)));
            }


            BarDataSet set1;

            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                set1.setValues(yVals1);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                // create 2 datasets with different types
                set1 = new BarDataSet(yVals1, "");
                set1.setColor(Color.rgb(255, 204, 0));


                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                chart.setData(data);


            }

            chart.setFitBars(true);

            Legend l = chart.getLegend();
            l.setFormSize(12f); // set the size of the legend forms/shapes
            l.setForm(Legend.LegendForm.SQUARE); // set what type of form/shape should be used

            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
            l.setTextSize(10f);
            l.setTextColor(Color.BLACK);
            l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
            l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis

            chart.invalidate();

            chart.animateY(2000);

        } catch (Exception ignored) {
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        Utiles.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}