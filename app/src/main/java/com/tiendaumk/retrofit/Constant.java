package com.tiendaumk.retrofit;

public class Constant {
    //API Transactions
    private static final String BASE_URL = APIClient.ADMIN_PANEL_URL;

    public static final String GET_PROFIL_USER = APIClient.baseUrl + "/api/profile_client.php?get_perfil_user=";
    public static final String GET_LAST_3M = BASE_URL + "/api/api.php?last_3m=";
    public static final String GET_NO_FACTURADO = BASE_URL + "/api/api.php?articulos_sin_facturar=";
    public static final String GET_DETALLE_FACTURA = BASE_URL + "/api/api.php?get_detalle_factura=";
}
