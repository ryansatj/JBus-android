package com.ryansafatjendanajbusaf.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class BusDetailActivity extends AppCompatActivity {
    private TextView busName, capacity, price, departure, arrival, facilities, busType;
    private String busNameS, capacityS, priceS, departureS, arrivalS, facilitiesS, busTypeS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);
        busName = findViewById(R.id.name_bus);
        capacity = findViewById(R.id.capacity_bus);
        price = findViewById(R.id.price_bus);
        departure = findViewById(R.id.departure_dropdown);
        arrival = findViewById(R.id.arrival_dropdown);
        facilities = findViewById(R.id.facilities_bus);
        busType = findViewById(R.id.bus_type_dropdown);

        busNameS = PersonalBusArrayAdapter.selectedBus.name;
        Integer cap = Integer.valueOf(PersonalBusArrayAdapter.selectedBus.capacity);
        capacityS = cap.toString();
        Double pri = Double.valueOf(PersonalBusArrayAdapter.selectedBus.price.price);
        priceS = pri.toString();
        departureS = PersonalBusArrayAdapter.selectedBus.departure.city.toString();
        arrivalS = PersonalBusArrayAdapter.selectedBus.arrival.city.toString();
        facilitiesS = PersonalBusArrayAdapter.selectedBus.facilities.toString();
        busTypeS = PersonalBusArrayAdapter.selectedBus.busType.toString();

        busName.setText(busNameS);
        capacity.setText(capacityS);
        price.setText(priceS);
        departure.setText(departureS);
        arrival.setText(arrivalS);
        facilities.setText(facilitiesS);
        busType.setText(busTypeS);

        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(v->{
            finish();
        });
    }
}