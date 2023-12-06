package com.ryansafatjendanajbusaf.jbus_android;

import com.ryansafatjendanajbusaf.jbus_android.model.Bus;

import java.util.List;

public interface BusPriceCallback {
    void onBusPriceReceived(List<Bus> busPrices);
}
