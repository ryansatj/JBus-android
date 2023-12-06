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
                        PaymentActivity.allBusPrice.add(res.payload);
                        if (callback != null) {
                            callback.onBusPriceReceived(PaymentActivity.allBusPrice);
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
                Intent intent = new Intent(mContext, PaymentDetailActivity.class);
                mContext.startActivity(intent);
            });
            SimpleDateFormat SDF = new SimpleDateFormat("MMMM dd, yyyy hh:mm");
            TextView textView1 = currentItemView.findViewById(R.id.text_view);
            String tanggal = SDF.format(payment.departureDate);
            textView1.setText(tanggal);
            TextView textView2 = currentItemView.findViewById(R.id.text_view2);
            textView2.setText(payment.busSeat.get(0).toString());
            TextView textView3 = currentItemView.findViewById(R.id.text_view3);
            textView3.setText(payment.status.toString());
        }
        return currentItemView;
    }

}
