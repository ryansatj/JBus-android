package com.ryansafatjendanajbusaf.jbus_android;

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
import com.ryansafatjendanajbusaf.jbus_android.model.Schedule;

import java.util.List;

public class BusSchedulesListAdapter extends ArrayAdapter<Schedule> {
    Context mContext;
    public static Bus selectedBus;

    public BusSchedulesListAdapter(@NonNull Context context, List<Schedule> list) {
        super(context, 0, list);
        mContext = context;
    }   @NonNull

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_detail_view, parent, false);
        }

        Schedule schedule = getItem(position);
        if (schedule != null) {
            TextView textView1 = currentItemView.findViewById(R.id.viewSchedule);
            textView1.setText(schedule.departureSchedule.toString());
        }
        return currentItemView;
    }


}
