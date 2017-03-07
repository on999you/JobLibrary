package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by dennislam on 9/1/2017.
 */

@XStreamAlias("itemsInfo")
public class ItemsInfoBaseXML {

    @XStreamAlias("status_code")
    private int status_code;

    @XStreamAlias("itemsTotal")
    private int itemsTotal;

    @XStreamAlias("msg")
    private String msg;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public int getItemsTotal() {
        return itemsTotal;
    }

    public void setItemsTotal(int itemsTotal) {
        this.itemsTotal= itemsTotal;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg= msg;
    }

}
