package com.ryansafatjendanajbusaf.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class
RegisterActivity extends AppCompatActivity {

    private EditText userEditText;
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
        setContentView(R.layout.activity_register);

        userEditText = findViewById(R.id.email_log);
        emailEditText = findViewById(R.id.email_reg);
        passwordEditText = findViewById(R.id.pass_reg);

        Button in_button = findViewById(R.id.sign_button);

        in_button.setOnClickListener(v ->{
            moveActivity(this, LoginActivity.class);
        });

        Button create_button = findViewById((R.id.reg_button));
        create_button.setOnClickListener(v->{

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String username = userEditText.getText().toString();

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)){
                viewToast(this, "Masukkan username, email, dan pass");
            } else {
                viewToast(this, "Berhasil Register!");
                moveActivity(this, LoginActivity.class);
            }
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