package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 9/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class GetWorkExpXML extends ItemsInfoBaseXML{

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
    private WorkExpItems items;

    public WorkExpItems getItems() {
        return items;
    }

    public void setItems(WorkExpItems items) {
        this.items = items;
    }

    @XStreamImplicit
    private List<WorkExpItem> item;

    @XStreamAlias("items")
    public class WorkExpItems {

        @XStreamImplicit
        private List<WorkExpItem> item;

        public List<WorkExpItem> getItem() {
            return item;
        }

        public void setItem(List<WorkExpItem> item) {
            this.item = item;
        }
    }

    @XStreamAlias("item")
    public class WorkExpItem {

        @XStreamAlias("EXPERIENCE_ID")
        private String workExpid;

        @XStreamAlias("EXPERIENCE_NAME")
        private String workExpname;

        @XStreamAlias("EXPERIENCE_NAME_CHI")
        private String workExpnameChi;

        public String getWorkExpid() {
            return workExpid;
        }

        public void setWorkExpid(String workExpid) {
            this.workExpid = workExpid;
        }

        public String getWorkExpname() {
            return workExpname;
        }

        public void setWorkExpname(String workExpname) {
            this.workExpname = workExpname;
        }

        public String getWorkExpnameChi() {
            return workExpnameChi;
        }

        public void setWorkExpnameChi(String workExpnameChi) {
            this.workExpnameChi = workExpnameChi;
        }

    }
}
