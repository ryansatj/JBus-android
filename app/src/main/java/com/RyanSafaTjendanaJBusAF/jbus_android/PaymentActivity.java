package com.ryansafatjendanajbusaf.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ryansafatjendanajbusaf.jbus_android.model.BaseResponse;
import com.ryansafatjendanajbusaf.jbus_android.model.Bus;
import com.ryansafatjendanajbusaf.jbus_android.model.Payment;
import com.ryansafatjendanajbusaf.jbus_android.request.BaseApiService;
import com.ryansafatjendanajbusaf.jbus_android.request.UtilsApi;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements BusPriceCallback   {
    private Button[] btns;
    private int currentPage = 0;
    private final int pageSize = 10;
    private int listSize;
    private int noOfPages;
    private List<Payment> listPayment = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView paymentListView = null;
    private HorizontalScrollView pageScroll = null;
    private BaseApiService mApiService;
    private Context mContext;
    private static Double busPriceTotal;
    public static Double exactBusPriceTotal;
    public static List<Bus> allBusPrice = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
        paymentListView = findViewById(R.id.payment_list);

        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0? currentPage-1 : 0;
            goToPage(currentPage);
        });

        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages -1? currentPage+1 : currentPage;
            goToPage(currentPage);
        });

        handlePayment();
    }
    public void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    private void paginationFooter() {
        int val = listSize % pageSize;
        val = val == 0 ? 0:1;
        noOfPages = listSize / pageSize + val;
        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];
        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity =
                    Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i]=new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText(""+(i+1));
            btns[i].setTextColor(getResources().getColor(R.color.white));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100,
                    100);
            ll.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }
    }

    private void goToPage(int index) {

        for (int i = 0; i< noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.baseline_circle_24));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                scrollToItem(btns[index]);
                viewPaginatedList(listPayment, currentPage);

            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
            }
        }
    }

    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) / 2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }
    private void viewPaginatedList(List<Payment> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Payment> paginatedList = listBus.subList(startIndex, endIndex);

        PaymentArrayAdapter paginatedAdapter = new PaymentArrayAdapter(this, paginatedList, this);
        paymentListView = findViewById(R.id.payment_list);
        paymentListView.setAdapter(paginatedAdapter);
    }
    protected void handlePayment() {

        mApiService.getMySeat(LoginActivity.loggedAccount.id).enqueue(new Callback<BaseResponse<List<Payment>>>(){
            @Override
            public void onResponse(Call<BaseResponse<List<Payment>>> call, Response<BaseResponse<List<Payment>>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<List<Payment>>res = response.body();
                if (res.success) {
                    listPayment = res.payload;
                    listSize = listPayment.size();
                    paginationFooter();
                    goToPage(currentPage);
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<List<Payment>>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBusPriceReceived(List<Bus> busPrices) {
        busPriceTotal = 0.0;
        System.out.println(busPrices.toString());
        System.out.println(allBusPrice.toString());
        if (busPrices != null) {
            for (int i = 0; i < busPrices.size(); i++) {
                if (busPrices.get(i).price != null) {
                    busPriceTotal += busPrices.get(i).price.price;
                }
            }
        }
        exactBusPriceTotal = busPriceTotal/2;
            TextView textTotal = findViewById(R.id.total);
            textTotal.setText(exactBusPriceTotal.toString());
    }
}