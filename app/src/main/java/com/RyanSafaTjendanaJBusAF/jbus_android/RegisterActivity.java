package com.RyanSafaTjendanaJBusAF.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

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

        userEditText = findViewById(R.id.username_reg);
        emailEditText = findViewById(R.id.email_reg);

        passwordEditText = findViewById(R.id.pass_reg);

        Button in_button = findViewById(R.id.sign_button);

        in_button.setOnClickListener(v ->{
            moveActivity(this, LoginActivity.class);
        });
    }

    public void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
}