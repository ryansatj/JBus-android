package com.ryansafatjendanajbusaf.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ryansafatjendanajbusaf.jbus_android.model.Account;
import com.ryansafatjendanajbusaf.jbus_android.model.BaseResponse;
import com.ryansafatjendanajbusaf.jbus_android.model.Bus;
import com.ryansafatjendanajbusaf.jbus_android.model.BusType;
import com.ryansafatjendanajbusaf.jbus_android.model.City;
import com.ryansafatjendanajbusaf.jbus_android.model.Facility;
import com.ryansafatjendanajbusaf.jbus_android.model.Renter;
import com.ryansafatjendanajbusaf.jbus_android.model.Station;
import com.ryansafatjendanajbusaf.jbus_android.request.BaseApiService;
import com.ryansafatjendanajbusaf.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBusActivity extends AppCompatActivity {
    private BusType[] busType = BusType.values();
    private List<Station> stationList = new ArrayList<>();
    private City[] cityType = City.values();
    private int selectedDeptStationID;
    private int selectedArrStationID;

    private BusType selectedBusType;
    private Spinner busTypeSpinner;
    private BaseApiService mApiService;
    private Context mContext;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private List<Facility> selectedFacilities = new ArrayList<>();
    private EditText busName, busCapacity, busPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        busTypeSpinner = this.findViewById(R.id.bus_type_dropdown);
        ArrayAdapter adBus = new ArrayAdapter(this, android.R.layout.simple_list_item_1, busType);
        adBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        busTypeSpinner.setAdapter(adBus);
        busTypeSpinner.setOnItemSelectedListener(busTypeOISL);

        busName = findViewById(R.id.name_bus);
        busCapacity = findViewById(R.id.capacity_bus);
        busPrice = findViewById(R.id.price_bus);
        acCheckBox = findViewById(R.id.ac_box);
        wifiCheckBox = findViewById(R.id.wifi_box);
        toiletCheckBox = findViewById(R.id.toilet_box);
        lcdCheckBox = findViewById(R.id.tv_box);
        coolboxCheckBox = findViewById(R.id.coolbox_box);
        lunchCheckBox = findViewById(R.id.lunch_box);
        baggageCheckBox = findViewById(R.id.large_box);
        electricCheckBox = findViewById(R.id.electric_box);

        handleStationDept();
        handleStationArr();

        Button createButton = findViewById(R.id.bussAdd_button);
        createButton.setOnClickListener(v->{
            handleAddBus();
        });


    }
    AdapterView.OnItemSelectedListener busTypeOISL = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    selectedBusType = busType[position];
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
    };

    protected void handleStationDept() {
        mApiService.getAllStation().enqueue(new Callback<List<Station>>(){
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                stationList = response.body();
                List <String> namelist = new ArrayList<>();
                for(int i = 0; i < stationList.size();i++)
                {
                    namelist.add(stationList.get(i).stationName);
                }
                Spinner departureSpinner = findViewById(R.id.departure_dropdown);
                ArrayAdapter deptBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, namelist);
                deptBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                departureSpinner.setAdapter(deptBus);


                AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                        selectedDeptStationID = stationList.get(position).id;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                departureSpinner.setOnItemSelectedListener(deptOISL);

            }
            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void handleStationArr() {
        mApiService.getAllStation().enqueue(new Callback<List<Station>>(){
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                stationList = response.body();
                List <String> namelist = new ArrayList<>();
                for(int i = 0; i < stationList.size();i++)
                {
                    namelist.add(stationList.get(i).stationName);
                }
                Spinner departureSpinner = findViewById(R.id.arrival_dropdown);
                ArrayAdapter deptBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, namelist);
                deptBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                departureSpinner.setAdapter(deptBus);


                AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                        selectedDeptStationID = stationList.get(position).id;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                departureSpinner.setOnItemSelectedListener(deptOISL);

            }
            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void handleAddBus() {
        String nameS = busName.getText().toString();
        String capacityS = busCapacity.getText().toString();
        String priceS = busPrice.getText().toString();
        int capac = Integer.valueOf(capacityS);
        int pric = Integer.valueOf(priceS);
        selectedFacilities.clear();
        if (acCheckBox.isChecked()) { selectedFacilities.add(Facility.AC);}
        if (wifiCheckBox.isChecked()) { selectedFacilities.add(Facility.WIFI);}
        if (toiletCheckBox.isChecked()){ selectedFacilities.add(Facility.TOILET);}
        if (lcdCheckBox.isChecked()){ selectedFacilities.add(Facility.LCD_TV);}
        if (coolboxCheckBox.isChecked()){ selectedFacilities.add(Facility.COOL_BOX);}
        if (lunchCheckBox.isChecked()){ selectedFacilities.add(Facility.LUNCH);}
        if (baggageCheckBox.isChecked()){ selectedFacilities.add(Facility.LARGE_BAGGAGE);}
        if (electricCheckBox.isChecked()){ selectedFacilities.add(Facility.ELECTRIC_SOCKET);}


        if (nameS.isEmpty() || capacityS.isEmpty() || priceS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.create(LoginActivity.loggedAccount.id, nameS, capac, selectedFacilities, selectedBusType, pric, selectedDeptStationID, selectedArrStationID).enqueue(new Callback<BaseResponse<Bus>>(){
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<Bus> res = response.body();
                if (res.success) {
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    moveActivity(mContext, ManageBusActivity.class);
                }
                else{
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
}