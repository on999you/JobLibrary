package com.example.dennislam.myapplication;

import android.app.Application;

/**
 * Created by dennislam on 23/1/2017.
 */

public class GlobalClass extends Application {

    private String udid;

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    private Boolean network;

    public Boolean getNetwork() {
        return network;
    }

    public void setNetwork(Boolean network) {
        this.network = network;
    }

}
