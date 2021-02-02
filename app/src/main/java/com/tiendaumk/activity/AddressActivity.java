package com.tiendaumk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.tiendaumk.R;
import com.tiendaumk.model.Address;
import com.tiendaumk.model.Area;
import com.tiendaumk.model.AreaD;
import com.tiendaumk.model.UpdateAddress;
import com.tiendaumk.model.User;
import com.tiendaumk.retrofit.APIClient;
import com.tiendaumk.retrofit.GetResult;
import com.tiendaumk.utils.SessionManager;
import com.tiendaumk.utils.Utiles;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static com.tiendaumk.utils.Utiles.isRef;

public class AddressActivity extends BaseActivity implements GetResult.MyListener {

    @BindView(R.id.ed_titulo)
    EditText txt_ed_titulo;

    @BindView(R.id.ed_direcc)
    EditText txt_ed_direcc;

    @BindView(R.id.spinner_lugar)
    Spinner select_spinner_lugar;

    @BindView(R.id.ed_referencia)
    EditText txt_ed_referencia;

    String areaSelect;
    List<AreaD> areaDS = new ArrayList<>();
    SessionManager sessionManager;
    User user;
    Address address;

    @BindView(R.id.id_lly_btn_save)
    LinearLayout llt_btn_Send;

    @BindView(R.id.ly_id_Area)
    LinearLayout llt_area;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        setTitle("Agregar Dirección");
        ButterKnife.bind(this);
        sessionManager = new SessionManager(AddressActivity.this);
        user = sessionManager.getUserDetails("");
        address = (Address) getIntent().getSerializableExtra("MyClass");
        select_spinner_lugar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaSelect = areaDS.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (address != null) {
            if (address.getId().equals("00")){
                llt_btn_Send.setVisibility(View.GONE);
                llt_area.setVisibility(View.GONE);
            }
        }


        getArea();
        if (address != null)
            setcountaint(address);
    }

    private void setcountaint(Address address) {
        txt_ed_titulo.setText("" + address.getTitulo());
        txt_ed_direcc.setText("" + address.getDireec());
        txt_ed_referencia.setText("" + address.getReferecia());
    }

    private void getArea() {
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        Call<JsonObject> call = APIClient.getInterface().getArea((JsonObject) jsonParser.parse(jsonObject.toString()));
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");
    }

    @OnClick(R.id.txt_save)
    public void onViewClicked() {
        if (validation()) {
            if (address != null) {
                updateUser(address.getId());
            } else {
                updateUser("0");
            }
        }
    }

    private void updateUser(String aid) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid", user.getId());
            jsonObject.put("aid", aid);
            jsonObject.put("Titulo", txt_ed_titulo.getText().toString());
            jsonObject.put("Direc", txt_ed_direcc.getText().toString());
            jsonObject.put("area", areaSelect);
            jsonObject.put("Refe", txt_ed_referencia.getText().toString());

            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().updateAddress((JsonObject) jsonParser.parse(jsonObject.toString()));
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
                Gson gson = new Gson();
                UpdateAddress response = gson.fromJson(result.toString(), UpdateAddress.class);


                if (response.getResult().equals("true")) {
                    sessionManager.setAddress("", response.getAddress());
                    isRef = true;
                    finish();
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Area area = gson.fromJson(result.toString(), Area.class);
                areaDS = area.getData();
                List<String> arrayList = new ArrayList<>();
                for (int i = 0; i < areaDS.size(); i++) {
                    if (areaDS.get(i).getStatus().equalsIgnoreCase("1")) {
                        arrayList.add(areaDS.get(i).getName());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_spinner_lugar.setAdapter(dataAdapter);
                int spinnerPosition = dataAdapter.getPosition(address.getArea());
                select_spinner_lugar.setSelection(spinnerPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validation() {
        if (txt_ed_titulo.getText().toString().isEmpty()) {
            txt_ed_titulo.setError("Campo Requerido");
            return false;
        }

        if (txt_ed_direcc.getText().toString().isEmpty()) {
            txt_ed_referencia.setError("Campo Requerido");
            return false;
        }

        if (txt_ed_referencia.getText().toString().isEmpty()) {
            txt_ed_referencia.setError("Campo Requerido");
            return false;
        }

        return true;
    }


}
