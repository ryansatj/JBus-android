package com.ryansafatjendanajbusaf.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AboutMeCompany extends AppCompatActivity {
    private TextView name, address, phone, profileInitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me_company);
        name = findViewById(R.id.nameCompany);
        address = findViewById(R.id.addressCompany);
        phone = findViewById(R.id.phoneCompany);
        profileInitial = findViewById(R.id.initialCompany);
        Button back = findViewById(R.id.back_button);

        String initialS = LoginActivity.loggedAccount.company.companyName.charAt(0) + "";
        String nameS = LoginActivity.loggedAccount.company.companyName;
        String addressS = LoginActivity.loggedAccount.company.address;
        String phoneS = LoginActivity.loggedAccount.company.phoneNumber;

        name.setText(nameS);
        profileInitial.setText(initialS);
        address.setText(addressS);
        phone.setText(phoneS);

        back.setOnClickListener(v->{
            finish();
        });

    }
}