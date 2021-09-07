package com.tiendaumk.retrofit;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    static Retrofit retrofit = null;
    //public static String baseUrl = "http://186.1.15.167:8448/tnd/";
    //public static String baseUrl = "http://192.168.1.139:8448/tnd/";
    public static String baseUrl = "http://186.1.15.167:8448/umk/";
    //public static String baseUrl = "http://hungrygrocerydelivery.cscodetech.com/";
    public static final String APPEND_URL = "api/";


    public static final String ADMIN_PANEL_URL = "http://186.1.15.166:8448/gmv3";

    public static UserService getInterface() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(UserService.class);
    }

}
