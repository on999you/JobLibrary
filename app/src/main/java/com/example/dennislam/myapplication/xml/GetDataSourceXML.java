package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 3/4/2017.
 */

@XStreamAlias("CTItemListRSS")
public class GetDataSourceXML extends ItemsInfoBaseXML{

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
    private DataSourceItems items;

    public DataSourceItems getItems() {
        return items;
    }

    public void setItems(DataSourceItems items) {
        this.items = items;
    }

    @XStreamImplicit
    private List<DataSourceItem> item;


    @XStreamAlias("items")
    public class DataSourceItems {

        @XStreamImplicit
        private List<DataSourceItem> item;

        public List<DataSourceItem> getItem() {
            return item;
        }

        public void setItem(List<DataSourceItem> item) {
            this.item = item;
        }
    }

    @XStreamAlias("item")
    public class DataSourceItem {

        @XStreamAlias("DATA_SOURCE")
        private String dataSource;

        @XStreamAlias("DATA_SOURCE_CHI")
        private String dataSourceChi;

        public String getDataSourceChi() {
            return dataSourceChi;
        }

        public void setDataSourceChi(String dataSourceChi) {
            this.dataSourceChi = dataSourceChi;
        }

        public String getDataSource() {
            return dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }

    }

}
