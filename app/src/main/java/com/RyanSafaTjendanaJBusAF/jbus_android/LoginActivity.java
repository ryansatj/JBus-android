package com.ryansafatjendanajbusaf.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ryansafatjendanajbusaf.jbus_android.model.Account;
import com.ryansafatjendanajbusaf.jbus_android.model.BaseResponse;
import com.ryansafatjendanajbusaf.jbus_android.request.BaseApiService;
import com.ryansafatjendanajbusaf.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText email, password;
    public static Account loggedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            getSupportActionBar().hide();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email_log);
        password = findViewById(R.id.pass_reg);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        Button loginButton = findViewById(R.id.log_button);
        Button registerButton = findViewById(R.id.create_button);

        loginButton.setOnClickListener(v->{
            String getEmail = email.getText().toString();
            String getPassword = password.getText().toString();

            if(TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword)){
                viewToast(this, "Masukkan email dan pass");
            } else {
                handleLogin();
            }
        });

        registerButton.setOnClickListener(v ->{
            viewToast(this, "Menuju register");
            moveActivity(this, RegisterActivity.class);
        });

    }

    public void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    private void viewToast(Context ctx, String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void handleLogin() {
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();
        if (emailS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.login(emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>(){
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Account> res = response.body();
                if (res.success) {
                    finish();
                    loggedAccount = res.payload;
                    viewToast(mContext, "selamat datang di JBUS " + loggedAccount.name );
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    moveActivity(mContext, MainActivity.class);
                }
                else{
                    viewToast(mContext, "Email atau Password salah");
                }

            }
            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}