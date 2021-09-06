package com.tiendaumk.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tiendaumk.R;
import com.tiendaumk.activity.HomeActivity;
import com.tiendaumk.adepter.ArticulosFacturadoAdp;
import com.tiendaumk.model.Articulos_facturado;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.tiendaumk.utils.SessionManager.iscart;

public class CrecArticuloFacturado  extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView rcArticulosVendidos;

    ArticulosFacturadoAdp mAdapter;

    List<Articulos_facturado> Arrray_articulos = new ArrayList<>();

    public CrecArticuloFacturado(List<Articulos_facturado> crecimeinto_articulos_facturados) {
        this.Arrray_articulos = crecimeinto_articulos_facturados;
    }
    public CrecArticuloFacturado(){}
    public static CrecArticuloFacturado newInstance() {
        CrecArticuloFacturado fragment = new CrecArticuloFacturado();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crecimiento_facturado, container, false);


        mAdapter = new ArticulosFacturadoAdp(view.getContext(), Arrray_articulos, R.layout.item_news_horizontal_simple);

        Log.e("TAG_error", "onCreateView: " + Arrray_articulos.size());

        rcArticulosVendidos.setLayoutManager(new LinearLayoutManager(getContext()));
        rcArticulosVendidos.setHasFixedSize(true);
        rcArticulosVendidos.setAdapter(mAdapter);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

    }
}
