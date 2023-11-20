package com.ryansafatjendanajbusaf.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            getSupportActionBar().hide();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_log);
        passwordEditText = findViewById(R.id.pass_reg);

        Button loginButton = findViewById(R.id.log_button);
        Button registerButton = findViewById(R.id.create_button);

        loginButton.setOnClickListener(v->{

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                viewToast(this, "Masukkan email dan pass");
            } else {
                Intent intent = new Intent(this, AboutMeActivity.class);
                intent.putExtra("email", email);
                viewToast(this, "Login");
                moveActivity(this, MainActivity.class);
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
}