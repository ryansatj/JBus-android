package com.ryansafatjendanajbusaf.jbus_android.request;

public class UtilsApi {
    public static final String BASE_URL_API = "http://10.0.2.2:5000/";

    public static BaseAPIService getApiService() {
        return RetrofitClient.getClient(BASE_URL_API).create(BaseAPIService.class);
    }
}
