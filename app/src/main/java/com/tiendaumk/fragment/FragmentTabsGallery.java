package com.tiendaumk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import com.tiendaumk.R;
import com.tiendaumk.adepter.ArticulosFacturadoAdp;
import com.tiendaumk.model.Articulos_facturado;

public class FragmentTabsGallery extends Fragment {

    List<Articulos_facturado>  Arrray_articulos;
    ArticulosFacturadoAdp mAdapter;


    public void setTxt_(List<Articulos_facturado>  Arg) {
        this.Arrray_articulos = Arg;
    }

    public FragmentTabsGallery() {
    }

    public static FragmentTabsGallery newInstance() {
        FragmentTabsGallery fragment = new FragmentTabsGallery();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tabs_gallery, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);

        mAdapter = new ArticulosFacturadoAdp(root.getContext(), Arrray_articulos, R.layout.item_news_horizontal_simple);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);




        return root;
    }
}