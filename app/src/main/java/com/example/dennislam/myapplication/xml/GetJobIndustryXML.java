package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 10/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class GetJobIndustryXML extends ItemsInfoBaseXML{

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
    private IndustryItems items;

    public IndustryItems getItems() {
        return items;
    }

    public void setItems(IndustryItems items) {
        this.items = items;
    }






    @XStreamImplicit
    private List<IndustryItem> item;





    @XStreamAlias("items")
    public class IndustryItems {

        @XStreamImplicit
        private List<IndustryItem> item;

        public List<IndustryItem> getItem() {
            return item;
        }

        public void setItem(List<IndustryItem> item) {
            this.item = item;
        }
    }






    @XStreamAlias("item")
    public class IndustryItem {

        @XStreamAlias("INDUSTRY_ID")
        private String industryID;

        @XStreamAlias("INDUSTRY_NAME")
        private String industryName;

        @XStreamAlias("INDUSTRY_NAME_CHI")
        private String industryNameChi;

        public String getIndustryID() {
            return industryID;
        }

        public void setIndustryID(String industryID) {
            this.industryID = industryID;
        }

        public String getIndustryName() {
            return industryName;
        }

        public void setIndustryName(String industryName) {
            this.industryName = industryName;
        }

        public String getIndustryNameChi() {
            return industryNameChi;
        }

        public void setIndustryNameChi(String industryNameChi) {
            this.industryNameChi = industryNameChi;
        }
    }

}
