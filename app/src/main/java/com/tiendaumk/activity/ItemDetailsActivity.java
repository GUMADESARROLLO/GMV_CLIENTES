package com.tiendaumk.activity;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.tiendaumk.R;
import com.tiendaumk.adepter.AdapterBonificado;
import com.tiendaumk.database.DatabaseHelper;
import com.tiendaumk.database.MyCart;
import com.tiendaumk.model.Price;
import com.tiendaumk.model.ProductItem;
import com.tiendaumk.utils.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tiendaumk.fragment.ItemListFragment.itemListFragment;
import static com.tiendaumk.utils.SessionManager.currncy;

public class ItemDetailsActivity extends AppCompatActivity {
    ProductItem productItem;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_cart)
    ImageView imgCart;
    @BindView(R.id.txt_tcount)
    TextView txtTcount;
    @BindView(R.id.lvl_cart)
    RelativeLayout lvlCart;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_desc)
    TextView txtDesc;
    @BindView(R.id.lvl_pricelist)
    LinearLayout lvlPricelist;
    @BindView(R.id.btn_addtocart)
    TextView btnAddtocart;
    ArrayList<Price> priceslist;
    DatabaseHelper databaseHelper;
    SessionManager sessionManager;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.txt_item_offer)
    TextView txtItemOffer;
    @BindView(R.id.txt_seler)
    TextView txtSeler;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabview)
    TabLayout tabview;

    @BindView(R.id.product_description)
    WebView webview_product_description;

    @BindView(R.id.recycler_bonificado)
    RecyclerView rcViewBnfc;


    AdapterBonificado rcBonificado;
    List<String> sList;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        databaseHelper = new DatabaseHelper(ItemDetailsActivity.this);
        productItem = (ProductItem) getIntent().getParcelableExtra("MyClass");
        priceslist = getIntent().getParcelableArrayListExtra("MyList");


        if (productItem != null) {

            double res = (Double.parseDouble(String.valueOf(productItem.getStock())));

            txtTitle.setText("" + productItem.getProductName());
            txtDesc.setText("Stock: " + new DecimalFormat("###,###.##").format(res));
            txtSeler.setText("" + productItem.getSellerName());



            List<String> Arealist = new ArrayList<>();
            for (int i = 0; i < priceslist.size(); i++) {
                Arealist.add(priceslist.get(i).getProductType());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, Arealist);
            spinner.setAdapter(dataAdapter);
            updateItem();
        }


        sList = Arrays.asList(productItem.getmbonificado().split(","));
        rcViewBnfc.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        rcBonificado = new AdapterBonificado(this, sList);
        rcViewBnfc.setAdapter(rcBonificado);






        webview_product_description.setBackgroundColor(Color.parseColor("#ffffff"));
        webview_product_description.setFocusableInTouchMode(false);
        webview_product_description.setFocusable(false);
        webview_product_description.getSettings().setDefaultTextEncodingName("UTF-8");

        WebSettings webSettings = webview_product_description.getSettings();

        webSettings.setDefaultFontSize(16);
        webSettings.setJavaScriptEnabled(true);

        String mimeType = "text/html; charset=UTF-8";
        String encoding = "utf-8";
        String htmlText = ((productItem.getShortDesc().equals("")) ? "Sin Descripcion" : productItem.getShortDesc());

        String text = "<html><head>"
                + "<style type=\"text/css\">body{color: #525252;}"
                + "</style></head>"
                + "<body>"
                + htmlText
                + "</body></html>";

        webview_product_description.loadDataWithBaseURL(null, text, mimeType, encoding, null);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (productItem.getmDiscount() > 0) {
                    double res = (Double.parseDouble(priceslist.get(position).getProductPrice()) / 100.0f) * productItem.getmDiscount();
                    res = Double.parseDouble(priceslist.get(position).getProductPrice()) - res;

                    
                    txtItemOffer.setText(sessionManager.getStringData(currncy) + priceslist.get(position).getProductPrice());
                    txtItemOffer.setPaintFlags(txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    String price = String.format(Locale.ENGLISH, "%1$,.2f", res);
                    txtPrice.setText(sessionManager.getStringData(currncy)  + price );

                    txtItemOffer.setText(sessionManager.getStringData(currncy) + priceslist.get(position).getProductPrice());
                } else {
                    txtItemOffer.setVisibility(View.GONE);
                    String price = String.format(Locale.ENGLISH, "%1$,.2f", Double.parseDouble(priceslist.get(position).getProductPrice()));
                    txtPrice.setText(sessionManager.getStringData(currncy)  + price );
                }
                setJoinPlayrList(lvlPricelist, productItem, priceslist.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> myList = new ArrayList<>();
        myList.add(productItem.getProductImage());
       /* if (productItem.getProductImage() != null && productItem.getProductImage().length() != 0) {
            myList.addAll(Arrays.asList(productItem.getProductImage().split(",")));
            tabview.setupWithViewPager(viewPager, true);
        }*/
        MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(this, myList);
        viewPager.setAdapter(myCustomPagerAdapter);
    }

    public void updateItem() {
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            txtTcount.setText("0");
        } else {
            txtTcount.setText("" + res.getCount());
        }
    }

    @OnClick({R.id.img_back, R.id.lvl_cart, R.id.btn_addtocart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.lvl_cart:
                fragment();
                break;
            case R.id.btn_addtocart:
                finish();
                break;
            default:
                break;
        }
    }

    private void setJoinPlayrList(LinearLayout lnrView, ProductItem datum, Price price) {

        lnrView.removeAllViews();
        final int[] count = {0};
        DatabaseHelper helper = new DatabaseHelper(lnrView.getContext());
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custome_additem, null);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout lvl_addremove = view.findViewById(R.id.lvl_addremove);
        LinearLayout lvl_addcart = view.findViewById(R.id.lvl_addcart);
        LinearLayout img_mins = view.findViewById(R.id.img_mins);
        LinearLayout img_plus = view.findViewById(R.id.img_plus);

        MyCart myCart = new MyCart();
        myCart.setPid(datum.getId());
        myCart.setImage(datum.getProductImage());
        myCart.setTitle(datum.getProductName());
        myCart.setWeight(price.getProductType());
        myCart.setCost(price.getProductPrice());
        myCart.setDiscount(datum.getmDiscount());
        myCart.setIva(datum.getmIva());

        int qrt = helper.getCard(myCart.getPid(), myCart.getCost());
        if (qrt != -1) {
            count[0] = qrt;
            txtcount.setText("" + count[0]);
            lvl_addremove.setVisibility(View.VISIBLE);
            lvl_addcart.setVisibility(View.GONE);
        } else {
            lvl_addremove.setVisibility(View.GONE);
            lvl_addcart.setVisibility(View.VISIBLE);

        }
        img_mins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count[0] = Integer.parseInt(txtcount.getText().toString());

                count[0] = count[0] - 1;
                if (count[0] <= 0) {
                    lvl_addremove.setVisibility(View.GONE);
                    lvl_addcart.setVisibility(View.VISIBLE);
                    txtcount.setText("0");
                    helper.deleteRData(myCart.getPid(), myCart.getCost());
                } else {
                    txtcount.setVisibility(View.VISIBLE);
                    txtcount.setText("" + count[0]);

                    myCart.setQty(String.valueOf(count[0]));
                    myCart.setReglas(datum.getmbonificado());
                    myCart.setBonifi(getBonificado(datum.getmbonificado(),count[0]));
                    myCart.setIva(datum.getmIva());
                    myCart.setCat(datum.getmCategoria());
                    helper.insertData(myCart);
                }
                updateItem();
                if (itemListFragment != null)
                    itemListFragment.updateItem();
            }
        });
        img_plus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {



                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                dialog.setContentView(R.layout.dialog_input_cant);
                dialog.setCancelable(true);

                LinearLayout lyt = dialog.findViewById(R.id.lyt);
                TextView txt_title = dialog.findViewById(R.id.title);
                EditText txt_msg = dialog.findViewById(R.id.ed_titulo);

                txt_title.setText("Cantidad Personalizada");
              //  txt_msg.setText(strMsg);
                lyt.setBackgroundColor(context.getResources().getColor(R.color.light_green_400));;



                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


                (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lvl_addcart.setVisibility(View.GONE);
                        lvl_addremove.setVisibility(View.VISIBLE);
                        count[0] = Integer.parseInt(txt_msg.getText().toString());


                        txtcount.setText("" + count[0]);
                        myCart.setQty(String.valueOf(count[0]));
                        myCart.setReglas(datum.getmbonificado());
                        myCart.setBonifi(getBonificado(datum.getmbonificado(),count[0]));
                        myCart.setIva(datum.getmIva());
                        myCart.setCat(datum.getmCategoria());
                        Log.e("INsert", "--> " + helper.insertData(myCart));
                        updateItem();
                        if (itemListFragment != null)
                            itemListFragment.updateItem();
                        dialog.dismiss();
                    }
                });

                dialog.show();
                dialog.getWindow().setAttributes(lp);


                return false;
            }
        });

        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0] = Integer.parseInt(txtcount.getText().toString());
                count[0] = count[0] + 1;

                if (count[0] > datum.getStock()){
                    Toast.makeText(context, "No hay suficiente Stock", Toast.LENGTH_SHORT).show();
                }else{


                txtcount.setText("" + count[0]);
                myCart.setQty(String.valueOf(count[0]));
                myCart.setReglas(datum.getmbonificado());
                myCart.setBonifi(getBonificado(datum.getmbonificado(),count[0]));
                myCart.setIva(datum.getmIva());
                myCart.setCat(datum.getmCategoria());
                Log.e("INsert", "--> " + helper.insertData(myCart));
                updateItem();
                if (itemListFragment != null)
                    itemListFragment.updateItem();
                }
            }
        });
        lvl_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count[0] > datum.getStock()){
                    Toast.makeText(context, "No hay suficiente Stock", Toast.LENGTH_SHORT).show();
                }else{
                    lvl_addcart.setVisibility(View.GONE);
                    lvl_addremove.setVisibility(View.VISIBLE);
                    count[0] = Integer.parseInt(txtcount.getText().toString());

                    count[0] = count[0] + 1;
                    txtcount.setText("" + count[0]);
                    myCart.setQty(String.valueOf(count[0]));
                    myCart.setReglas(datum.getmbonificado());
                    myCart.setBonifi(getBonificado(datum.getmbonificado(),count[0]));
                    myCart.setIva(datum.getmIva());
                    myCart.setCat(datum.getmCategoria());
                    Log.e("INsert", "--> " + helper.insertData(myCart));
                    updateItem();
                    if (itemListFragment != null)
                        itemListFragment.updateItem();
                    }
                }



        });

        rcViewBnfc.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final String str_bonificado =  sList.get(position);
                final List<String> row_arr = new ArrayList<>();

                row_arr.add(Arrays.asList(str_bonificado.replace("+", ",").split(",")).get(0));


                if (!str_bonificado.equalsIgnoreCase("0")) {

                    double cnt_valor = Double.parseDouble(row_arr.get(0));
                    double cant_stock = (Double.parseDouble(String.valueOf(productItem.getStock())));

                    if (cnt_valor > cant_stock){
                        Toast.makeText(context, "El Stock no es suficiente", Toast.LENGTH_SHORT).show();
                    }else{
                        lvl_addcart.setVisibility(View.GONE);
                        lvl_addremove.setVisibility(View.VISIBLE);
                        count[0] = Integer.parseInt(txtcount.getText().toString());
                        count[0] = count[0] + Integer.parseInt(row_arr.get(0));
                        txtcount.setText("" + count[0]);
                        myCart.setQty(String.valueOf(count[0]));
                        myCart.setReglas(datum.getmbonificado());
                        myCart.setBonifi(getBonificado(datum.getmbonificado(),count[0]));
                        myCart.setIva(datum.getmIva());
                        myCart.setCat(datum.getmCategoria());
                        Log.e("INsert", "--> " + helper.insertData(myCart));
                        updateItem();
                        if (itemListFragment != null)
                            itemListFragment.updateItem();
                    }
                    //String _cnt_valor = String.format(Locale.ENGLISH, "%1$,.0f", cnt_valor);
                }
            }
        }));
        lnrView.addView(view);

    }



    public String getBonificado(String Bonificado,int Cantidad){



        List<String> sList = Arrays.asList(Bonificado.split(","));

        final List<String> row_arr = new ArrayList<>();
        for (int i = 0; i < sList.size(); i++) row_arr.add(Arrays.asList(sList.get(i).replace("+", ",").split(",")).get(0));
        int position = row_arr.indexOf(String.valueOf(Cantidad));

        if (position == -1) {
            return "0";
        }else{
            return sList.get(position);
        }


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
            View child = rcViewBnfc.findChildViewUnder(e.getX(), e.getY());
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

    public void fragment() {
        SessionManager.iscart = true;
        finish();

    }

    public class MyCustomPagerAdapter extends PagerAdapter {
        Context context;
        List<String> imageList;
        LayoutInflater layoutInflater;

        public MyCustomPagerAdapter(Context context, List<String> bannerDatumList) {
            this.context = context;
            this.imageList = bannerDatumList;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.item_image, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            Glide.with(ItemDetailsActivity.this).load(imageList.get(position)).placeholder(R.drawable.empty).into(imageView);
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
    public interface ClickListener {
        public void onClick(View view, int position);
    }
}
