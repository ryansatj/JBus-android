package com.ryansafatjendanajbusaf.jbus_android;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ryansafatjendanajbusaf.jbus_android.model.BaseResponse;
import com.ryansafatjendanajbusaf.jbus_android.model.Bus;
import com.ryansafatjendanajbusaf.jbus_android.model.Invoice;
import com.ryansafatjendanajbusaf.jbus_android.model.Payment;
import com.ryansafatjendanajbusaf.jbus_android.request.BaseApiService;
import com.ryansafatjendanajbusaf.jbus_android.request.UtilsApi;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentArrayAdapter extends ArrayAdapter<Payment> {
    public static Payment mainSelectedBus;
    private BusPriceCallback callback;
    private Context mContext;
    private BaseApiService mApiService;
    public static Bus selectedBusPayment;
    public static Payment selectedPayment;
    public PaymentArrayAdapter(@NonNull Context context, List<Payment> list, BusPriceCallback callback) {
        super(context, 0, list);
        mContext = context;
        mApiService = UtilsApi.getApiService();
        this.callback = callback;
    }
    @NonNull

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.payment_view, parent, false);
        }
        Payment payment = getItem(position);

        if (payment != null) {
            PaymentActivity.allBusPrice.clear();
            mApiService.getBusPrice(payment.getBusId()).enqueue(new Callback<BaseResponse<Bus>>(){
                @Override
                public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    BaseResponse<Bus>res = response.body();
                    if (res.success) {
                        if(payment.status == Invoice.PaymentStatus.WAITING) {
                            PaymentActivity.allBusPrice.add(res.payload);
                            if (callback != null) {
                                callback.onBusPriceReceived(PaymentActivity.allBusPrice);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                    Toast.makeText(mContext, "Problem with the server",
                            Toast.LENGTH_SHORT).show();
                }
            });

            View view = currentItemView.findViewById(R.id.footer);
            view.setOnClickListener(v -> {
                handleGetBusPrice(position);
            });
            SimpleDateFormat SDF = new SimpleDateFormat("MMMM dd, yyyy hh:mm");
            TextView textView1 = currentItemView.findViewById(R.id.text_view);
            String tanggal = SDF.format(payment.departureDate);
            textView1.setText(tanggal);
            textView1.setOnClickListener(v -> {
                handleGetBusPrice(position);
            });
            TextView textView2 = currentItemView.findViewById(R.id.text_view2);
            textView2.setText(payment.busSeat.get(0).toString());
            textView2.setOnClickListener(v -> {
                handleGetBusPrice(position);
            });
            TextView textView3 = currentItemView.findViewById(R.id.text_view3);
            textView3.setText(payment.status.toString());
            textView3.setOnClickListener(v -> {
                handleGetBusPrice(position);
            });
            Button buttonDelete = currentItemView.findViewById(R.id.trash);
            buttonDelete.setOnClickListener(v -> {
                selectedPayment = getItem(position);
                if(selectedPayment.status == Invoice.PaymentStatus.WAITING){
                    Toast.makeText(mContext, "Lakukan pembayaran atau cancel untuk menghapus", Toast.LENGTH_SHORT).show();
                }
                else {
                    assert selectedPayment != null;
                    mApiService.deletePayment(selectedPayment.id).enqueue(new Callback<BaseResponse<Payment>>(){
                        @Override
                        public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            BaseResponse<Payment>res = response.body();
                            if (res.success) {
                                Intent intent = new Intent(mContext, MainActivity.class);
                                mContext.startActivity(intent);
                                Intent intent1 = new Intent(mContext, PaymentActivity.class);
                                mContext.startActivity(intent1);
                            }
                        }
                        @Override
                        public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                            Toast.makeText(mContext, "Problem with the server",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });

        }
        return currentItemView;
    }

    public void handleGetBusPrice(int position){
        selectedPayment = getItem(position);

        mApiService.getBusPrice(selectedPayment.getBusId()).enqueue(new Callback<BaseResponse<Bus>>(){
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Bus>res = response.body();
                if (res.success) {
                    selectedBusPayment = res.payload;
                    Intent intent = new Intent(mContext, PaymentDetailActivity.class);
                    mContext.startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
