package com.tiendaumk.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tiendaumk.R;
import com.tiendaumk.adepter.ArticulosFacturadoAdp;
import com.tiendaumk.adepter.CrecimientoFacturadoMensualAdp;
import com.tiendaumk.fragment.CrecArticuloFacturado;
import com.tiendaumk.fragment.CrecArticuloNoFacturado;
import com.tiendaumk.model.Articulos_facturado;
import com.tiendaumk.model.Articulos_venta_anual;
import com.tiendaumk.model.Home_crecimiento;
import com.tiendaumk.retrofit.APIClient;
import com.tiendaumk.retrofit.GetResult;
import com.tiendaumk.utils.Utiles;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
public class ActivityPerfilCrecimiento extends AppCompatActivity implements GetResult.MyListener{

    BarChart chart;

    RecyclerView recyclerView;


    List<Articulos_venta_anual> VentaAnualList;


    CrecimientoFacturadoMensualAdp adap;


    CrecArticuloFacturado crecArticuloFacturado;
    CrecArticuloNoFacturado crecArticuloNoFacturado ;

    ViewPager viewPager;
    TabLayout tabLayout;

    List<Articulos_facturado> crecimeinto_articulos_facturados = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_crecimiento);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);




        recyclerView = findViewById(R.id.id_rc_meses);
        chart = findViewById(R.id.chart1);
        VentaAnualList = new ArrayList<>();
        adap = new CrecimientoFacturadoMensualAdp(VentaAnualList);
        recyclerView.setAdapter(adap);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);

        initToolbar();
        getHome();
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
    private void getHome() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", "118");
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
                crecimeinto_articulos_facturados = new ArrayList<>();

                Gson gson = new Gson();
                Home_crecimiento perfil_crecimiento = gson.fromJson(result.toString(), Home_crecimiento.class);

                VentaAnualList.addAll(perfil_crecimiento.getResultHome().getFacturaAnualItems());
                adap = new CrecimientoFacturadoMensualAdp(VentaAnualList);
                recyclerView.setAdapter(adap);

                crecimeinto_articulos_facturados.addAll(perfil_crecimiento.getResultHome().getArticulosFacturadotItems());


                setGrafica(VentaAnualList);
            }

        } catch (Exception e) {
            e.toString();
        }
    }

    private void setGrafica( List<Articulos_venta_anual> dataList) {




        List<Float> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {

            entries.add(Float.valueOf(dataList.get(i).getmCantidad()));

            labels.add( dataList.get(i).getmMes());

        }
        create_graph(labels,entries);


        crecArticuloFacturado = new CrecArticuloFacturado(crecimeinto_articulos_facturados);
        crecArticuloNoFacturado = new CrecArticuloNoFacturado();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(crecArticuloFacturado, " Facturados");
        viewPagerAdapter.addFragment(crecArticuloNoFacturado, "Nuevos Articulos");
        viewPager.setAdapter(viewPagerAdapter);
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
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_notes);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_80), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Utiles.setSystemBarColor(this, android.R.color.white);
        Utiles.setSystemBarLight(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting_outline, menu);
        Utiles.changeMenuIconColor(menu, getResources().getColor(R.color.grey_80));
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

}