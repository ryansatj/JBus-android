package com.ryansafatjendanajbusaf.jbus_android.model;

import java.sql.Timestamp;
import java.util.List;

public class Payment extends Invoice {
    private int busId;
    public Timestamp departureDate;
    public List<String> busSeat;

    public int getBusId(){
        return this.busId;
    }

}
