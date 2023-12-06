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
import android.widget.Toast;

import com.ryansafatjendanajbusaf.jbus_android.model.Account;
import com.ryansafatjendanajbusaf.jbus_android.model.BaseResponse;
import com.ryansafatjendanajbusaf.jbus_android.model.Bus;
import com.ryansafatjendanajbusaf.jbus_android.request.BaseApiService;
import com.ryansafatjendanajbusaf.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBusActivity extends AppCompatActivity {
    private Button[] btns;
    private int currentPage = 0;
    private int pageSize = 10;
    private int listSize;
    private int noOfPages;
    private List<Bus> listBus = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView busListView = null;
    private HorizontalScrollView pageScroll = null;
    private BaseApiService mApiService;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            getSupportActionBar().hide();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);

        mContext = this;
        mApiService = UtilsApi.getApiService();
        handleBus();
        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
        busListView = findViewById(R.id.list_personal_bus);
        Button addbutton = findViewById(R.id.add_bus);
        Button aboutcomp = findViewById(R.id.about_company);
        Button back = findViewById(R.id.back_button);

        addbutton.setOnClickListener(v -> {
            moveActivity(this, AddBusActivity.class);
        });
        aboutcomp.setOnClickListener((v->{
            moveActivity(this, AboutMeCompany.class);
        }));

        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0? currentPage-1 : 0;
            goToPage(currentPage);
        });

        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages -1? currentPage+1 : currentPage;
            goToPage(currentPage);
        });

        back.setOnClickListener(v->{
            moveActivity(this, AboutMeActivity.class);
        });

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
                viewPaginatedList(listBus, currentPage);

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
    private void viewPaginatedList(List<Bus> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Bus> paginatedList = listBus.subList(startIndex, endIndex);
        PersonalBusArrayAdapter paginatedAdapter = new PersonalBusArrayAdapter(this, paginatedList);
        busListView = findViewById(R.id.list_personal_bus);
        busListView.setAdapter(paginatedAdapter);
    }

    protected void handleBus() {

        if(LoginActivity.loggedAccount != null)
        {
            mApiService.getMyBus(LoginActivity.loggedAccount.id).enqueue(new Callback<BaseResponse<List<Bus>>>(){
                @Override
                public void onResponse(Call<BaseResponse<List<Bus>>> call, Response<BaseResponse<List<Bus>>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(mContext, "Application error " +
                                response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    BaseResponse<List<Bus>>res = response.body();
                    if (res.success) {
                        Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                        listBus = res.payload;
                        listSize = listBus.size();
                        paginationFooter();
                        goToPage(currentPage);
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<List<Bus>>> call, Throwable t) {
                    Toast.makeText(mContext, "Problem with the server",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}