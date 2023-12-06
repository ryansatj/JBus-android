package com.ryansafatjendanajbusaf.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ryansafatjendanajbusaf.jbus_android.model.BaseResponse;
import com.ryansafatjendanajbusaf.jbus_android.model.Bus;
import com.ryansafatjendanajbusaf.jbus_android.model.Facility;
import com.ryansafatjendanajbusaf.jbus_android.model.Payment;
import com.ryansafatjendanajbusaf.jbus_android.model.Schedule;
import com.ryansafatjendanajbusaf.jbus_android.request.BaseApiService;
import com.ryansafatjendanajbusaf.jbus_android.request.UtilsApi;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainBusDetailActivity extends AppCompatActivity {
    private TextView busName, capacity, price, departure, arrival, facilities, busType;
    private String busNameS, capacityS, priceS, departureS, arrivalS, facilitiesS, busTypeS;
    public String selectedScheduleString;
    private static Schedule selectedSchedule;

    private BaseApiService mApiService;
    private Context mContext;
    private static String selectedSeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bus_detail);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        busName = findViewById(R.id.name_bus);
        capacity = findViewById(R.id.capacity_bus);
        price = findViewById(R.id.price_bus);
        departure = findViewById(R.id.departure_dropdown);
        arrival = findViewById(R.id.arrival_dropdown);
        facilities = findViewById(R.id.facilities_bus);
        busType = findViewById(R.id.bus_type_dropdown);

        busNameS = BusArrayAdapter.mainSelectedBus.name;
        Integer cap = Integer.valueOf(BusArrayAdapter.mainSelectedBus.capacity);
        capacityS = cap.toString();
        Double pri = Double.valueOf(BusArrayAdapter.mainSelectedBus.price.price);
        priceS = pri.toString();
        departureS = BusArrayAdapter.mainSelectedBus.departure.city.toString();
        arrivalS = BusArrayAdapter.mainSelectedBus.arrival.city.toString();
        facilitiesS = BusArrayAdapter.mainSelectedBus.facilities.toString();
        busTypeS = BusArrayAdapter.mainSelectedBus.busType.toString();

        busName.setText(busNameS);
        capacity.setText(capacityS);
        price.setText(priceS);
        departure.setText(departureS);
        arrival.setText(arrivalS);
        facilities.setText(facilitiesS);
        busType.setText(busTypeS);

        Context mContext = this;
        List<Schedule> scheduleList = new ArrayList<>();
        List<String> scheduleListString = new ArrayList<>();
        List<String> seatList = new ArrayList<>();
        if (BusArrayAdapter.mainSelectedBus.schedules != null) {
            for(int i = 0 ; i < BusArrayAdapter.mainSelectedBus.schedules.size(); i++){

                scheduleListString.add(BusArrayAdapter.mainSelectedBus.schedules.get(i).departureSchedule.toString());
                scheduleList.add(BusArrayAdapter.mainSelectedBus.schedules.get(i));
            }

            Spinner schedulesSpinner = findViewById(R.id.schedules);

            ArrayAdapter schedulesSpinString = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, scheduleListString);
            schedulesSpinString.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            schedulesSpinner.setAdapter(schedulesSpinString);

            AdapterView.OnItemSelectedListener schedOISL = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    selectedScheduleString = scheduleListString.get(position);
                    selectedSchedule = scheduleList.get(position);

                    Set <String> seats = selectedSchedule.seatAvailability.keySet();
                    seatList.clear();
                    for (String seat : seats) {
                        if (selectedSchedule.seatAvailability.get(seat)) {
                            seatList.add(seat);
                        }
                    }

                    Spinner seatSpinner = findViewById(R.id.seats);
                    ArrayAdapter seatSpinString = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, seatList);
                    seatSpinString.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                    seatSpinner.setAdapter(seatSpinString);

                    AdapterView.OnItemSelectedListener seatOISL = new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                            selectedSeat = seatList.get(position);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    };
                    seatSpinner.setOnItemSelectedListener(seatOISL);

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };
            schedulesSpinner.setOnItemSelectedListener(schedOISL);
        }

        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(v->{
            handleMakeBooking();
        });
    }
    public void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    protected void handleMakeBooking() {
        List<String> selectedSeatList = new ArrayList<>();
        selectedSeatList.add(selectedSeat);
        mApiService.makeBooking(LoginActivity.loggedAccount.id, BusArrayAdapter.mainSelectedBus.accountId, BusArrayAdapter.mainSelectedBus.id, selectedSeatList, selectedScheduleString).enqueue(new Callback<BaseResponse<Payment>>(){
            @Override
            public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Payment> res = response.body();
                if (res.success) {
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    moveActivity(mContext, MainActivity.class);
                }
                else{
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Log.e("API_CALL", "Failed to communicate with the server", t);
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}