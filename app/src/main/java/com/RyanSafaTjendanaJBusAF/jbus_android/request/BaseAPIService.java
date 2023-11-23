package com.ryansafatjendanajbusaf.jbus_android.request;

import com.ryansafatjendanajbusaf.jbus_android.model.Account;
import com.ryansafatjendanajbusaf.jbus_android.model.BaseResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseAPIService {
    @GET("account/{id}")
    Call<Account> getAccountbyId (@Path("id") int id);
}
