package com.tiendaumk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tiendaumk.R;

public class CrecArticuloNoFacturado extends Fragment {

    public CrecArticuloNoFacturado() {
    }
    public static CrecArticuloNoFacturado newInstance() {
        CrecArticuloNoFacturado fragment = new CrecArticuloNoFacturado();
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
        return inflater.inflate(R.layout.fragment_crecimiento_nofacturado, container, false);
    }
}
