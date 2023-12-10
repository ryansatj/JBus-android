package com.ryansafatjendanajbusaf.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ryansafatjendanajbusaf.jbus_android.model.Account;
import com.ryansafatjendanajbusaf.jbus_android.model.BaseResponse;
import com.ryansafatjendanajbusaf.jbus_android.model.Invoice;
import com.ryansafatjendanajbusaf.jbus_android.model.Payment;
import com.ryansafatjendanajbusaf.jbus_android.request.BaseApiService;
import com.ryansafatjendanajbusaf.jbus_android.request.UtilsApi;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentDetailActivity extends AppCompatActivity {
    private TextView busName, capacity, price, departure, arrival, facilities, busType, schedule, seat, status;
    private String busNameS, capacityS, priceS, departureS, arrivalS, facilitiesS, busTypeS, scheduleS, seatS;
    private Context mContext;
    private BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            getSupportActionBar().hide();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        if(PaymentArrayAdapter.selectedPayment.status == Invoice.PaymentStatus.WAITING){
            setContentView(R.layout.activity_payment_detail);
            Button payButton = findViewById(R.id.pay_button);
            payButton.setOnClickListener(v -> {
                handlePay();
            });
            Button cancelButton = findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(v -> {
                handleCancel();
            });
        } else if (PaymentArrayAdapter.selectedPayment.status == Invoice.PaymentStatus.SUCCESS) {
            setContentView(R.layout.activity_payment_detail_success);
            Button backButton = findViewById(R.id.back_button);
            status = findViewById(R.id.status);
            status.setText(PaymentArrayAdapter.selectedPayment.status.toString());
            backButton.setOnClickListener(v -> {
                finish();
            });
        } else if (PaymentArrayAdapter.selectedPayment.status == Invoice.PaymentStatus.FAILED) {
            setContentView(R.layout.activity_payment_detail_failed);
            Button repayButton = findViewById(R.id.repay_button);
            repayButton.setOnClickListener(v -> {
                handlePay();
            });
        }
        busName = findViewById(R.id.name_bus);
        capacity = findViewById(R.id.capacity_bus);
        price = findViewById(R.id.price_bus);
        departure = findViewById(R.id.departure_dropdown);
        arrival = findViewById(R.id.arrival_dropdown);
        facilities = findViewById(R.id.facilities_bus);
        busType = findViewById(R.id.bus_type_dropdown);
        schedule = findViewById(R.id.schedule_detail);
        seat = findViewById(R.id.seat_detail);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        busNameS = PaymentArrayAdapter.selectedBusPayment.name;
        Integer cap = Integer.valueOf(PaymentArrayAdapter.selectedBusPayment.capacity);
        capacityS = cap.toString();
        Double pri = Double.valueOf(PaymentArrayAdapter.selectedBusPayment.price.price);
        priceS = pri.toString();
        departureS = PaymentArrayAdapter.selectedBusPayment.departure.city.toString();
        arrivalS = PaymentArrayAdapter.selectedBusPayment.arrival.city.toString();
        facilitiesS = PaymentArrayAdapter.selectedBusPayment.facilities.toString();
        busTypeS = PaymentArrayAdapter.selectedBusPayment.busType.toString();
        SimpleDateFormat SDF = new SimpleDateFormat("MMMM dd, yyyy hh:mm");
        scheduleS = SDF.format(PaymentArrayAdapter.selectedPayment.departureDate);
        seatS = PaymentArrayAdapter.selectedPayment.busSeat.get(0);

        busName.setText(busNameS);
        capacity.setText(capacityS);
        price.setText(priceS);
        departure.setText(departureS);
        arrival.setText(arrivalS);
        facilities.setText(facilitiesS);
        busType.setText(busTypeS);
        schedule.setText(scheduleS);
        seat.setText(seatS);
    }

    protected void handlePay() {
        mApiService.accept(PaymentArrayAdapter.selectedPayment.id).enqueue(new Callback<BaseResponse<Payment>>(){
            @Override
            public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Payment> res = response.body();
                if (res.success) {
                    LoginActivity.loggedAccount.balance = LoginActivity.loggedAccount.balance - PaymentArrayAdapter.selectedBusPayment.price.price;
                    PaymentArrayAdapter.selectedPayment.status = Invoice.PaymentStatus.SUCCESS;
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, PaymentActivity.class);
                    mContext.startActivity(intent);
                }
                else{
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void handleCancel() {
        mApiService.cancel(PaymentArrayAdapter.selectedPayment.id).enqueue(new Callback<BaseResponse<Payment>>(){
            @Override
            public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Payment> res = response.body();
                if (res.success) {
                    PaymentArrayAdapter.selectedPayment.status = Invoice.PaymentStatus.FAILED;
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, PaymentActivity.class);
                    mContext.startActivity(intent);
                }
                else{
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}