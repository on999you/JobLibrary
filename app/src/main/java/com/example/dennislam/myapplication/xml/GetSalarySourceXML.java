package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 9/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class GetSalarySourceXML extends ItemsInfoBaseXML{

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
    private SalarySourceItems items;

    public SalarySourceItems getItems() {
        return items;
    }

    public void setItems(SalarySourceItems items) {
        this.items = items;
    }

    @XStreamImplicit
    private List<SalarySourceItem> item;


    @XStreamAlias("items")
    public class SalarySourceItems {

        @XStreamImplicit
        private List<SalarySourceItem> item;

        public List<SalarySourceItem> getItem() {
            return item;
        }

        public void setItem(List<SalarySourceItem> item) {
            this.item = item;
        }
    }

    @XStreamAlias("item")
    public class SalarySourceItem {

        @XStreamAlias("SOURCE_ID")
        private String soruce_id;

        @XStreamAlias("MONTH")
        private String month;

        @XStreamAlias("DESCRIPTION")
        private String description;

        @XStreamAlias("DESCRIPTION_CHI")
        private String descriptionChi;

        public String getSoruce_id() {
            return soruce_id;
        }

        public void setSoruce_id(String soruce_id) {
            this.soruce_id = soruce_id;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescriptionChi() {
            return descriptionChi;
        }

        public void setDescriptionChi(String descriptionChi) {
            this.descriptionChi = descriptionChi;
        }

    }

}
