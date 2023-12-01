package com.ryansafatjendanajbusaf.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ryansafatjendanajbusaf.jbus_android.R;
import com.ryansafatjendanajbusaf.jbus_android.model.Account;
import com.ryansafatjendanajbusaf.jbus_android.model.BaseResponse;
import com.ryansafatjendanajbusaf.jbus_android.model.Bus;
import com.ryansafatjendanajbusaf.jbus_android.model.Schedule;
import com.ryansafatjendanajbusaf.jbus_android.request.BaseApiService;
import com.ryansafatjendanajbusaf.jbus_android.request.UtilsApi;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddScheduleActivity extends AppCompatActivity {
    private TextView busName, capacity, price, departure, arrival, facilities, busType;
    private EditText schedAdd;
    private String busNameS, capacityS, priceS, departureS, arrivalS, facilitiesS, busTypeS;
    private BaseApiService mApiService;
    private Context mContext;
    private static final String REGEX_DATE = "^[2000-2023]+[-]+(?:[0]\\d|1[0-2])+[-]+(?:[0]\\d|3[0-1])+[ ]+(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d+[.]+(?:[01]\\d|[2-9][0-9])$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_schedule);
        mContext = this;
        mApiService = UtilsApi.getApiService();
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

        schedAdd = findViewById(R.id.schedule);


        Button addSched = findViewById(R.id.add_schedule_button);
        addSched.setOnClickListener(v->{
            handleRegister();
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v->{
            finish();
        });
    }
    protected void handleRegister() {
        String schedS = schedAdd.getText().toString();
        if(schedS.isEmpty()) {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
        }

        Pattern patternSched = Pattern.compile(REGEX_DATE);
        Matcher matcherSched = patternSched.matcher(schedS);
        boolean matchDateFound = matcherSched.find();

        if(!matchDateFound) {
            Toast.makeText(this, "format tanggal salah", Toast.LENGTH_SHORT).show();
        }
        

        mApiService.addSchedule(PersonalBusArrayAdapter.selectedBus.id, schedS).enqueue(new Callback<BaseResponse<Bus>>(){
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Bus> res = response.body();
                if (res.success) {
                    PersonalBusArrayAdapter.selectedBus.schedules = res.payload.schedules;
                    moveActivity(mContext, ManageBusActivity.class);
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
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