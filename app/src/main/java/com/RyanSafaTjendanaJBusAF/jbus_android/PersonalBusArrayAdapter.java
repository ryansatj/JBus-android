package com.ryansafatjendanajbusaf.jbus_android;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ryansafatjendanajbusaf.jbus_android.model.Bus;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ryansafatjendanajbusaf.jbus_android.model.Bus;
import java.util.ArrayList;
import java.util.List;

public class PersonalBusArrayAdapter extends ArrayAdapter<Bus> {
    Context mContext;
    public static Bus selectedBus;

    public PersonalBusArrayAdapter(@NonNull Context context, List<Bus> list) {
        super(context, 0, list);
        mContext = context;
    }   @NonNull

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.personal_bus_view, parent, false);
        }

        Bus bus = getItem(position);
        if (bus != null) {
            TextView textView1 = currentItemView.findViewById(R.id.viewbus);
            textView1.setText(bus.name);

            Button scheduleButton = currentItemView.findViewById(R.id.manage_bus);
            scheduleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    selectedBus = getItem(position);
                    Intent intent = new Intent(mContext, AddScheduleActivity.class);
                    mContext.startActivity(intent);
                }
            });

            Button busDetail = currentItemView.findViewById(R.id.bus_detail);
            busDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    selectedBus = getItem(position);
                    Intent intent = new Intent(mContext, BusDetailActivity.class);
                    mContext.startActivity(intent);
                }
            });

            Button busSchedules = currentItemView.findViewById(R.id.bus_schedules);
            busSchedules.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedBus = getItem(position);
                    Intent intent = new Intent(mContext, ScheduleDetailActivity.class);
                    mContext.startActivity(intent);
                }
            }));
        }
        return currentItemView;
    }

}
