package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 17/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class OpenAppXML extends ItemsInfoBaseXML {

    @XStreamAlias("itemsInfo")
    @XStreamImplicit
    private List<ItemsInfoBaseXML> itemsInfo;

    public List<ItemsInfoBaseXML> getItemsInfo() {
        return itemsInfo;
    }

    public void setItem(List<ItemsInfoBaseXML> itemsInfo) {
        this.itemsInfo = itemsInfo;
    }




    @XStreamAlias("items")
    private OpenAppItems items;

    public OpenAppItems getItems() {
        return items;
    }

    public void setItems(OpenAppItems items) {
        this.items = items;
    }





    @XStreamImplicit
    private List<OpenAppItem> item;






    @XStreamAlias("items")
    public class OpenAppItems {

        @XStreamImplicit
        private List<OpenAppItem> item;

        public List<OpenAppItem> getItem() {
            return item;
        }

        public void setItem(List<OpenAppItem> item) {
            this.item = item;
        }
    }





    @XStreamAlias("item")
    public class OpenAppItem {

        @XStreamAlias("UDID")
        private String udid;

        public String getUdid() {
            return udid;
        }

        public void setUdid(String udid) {
            this.udid = udid;
        }


    }

}
