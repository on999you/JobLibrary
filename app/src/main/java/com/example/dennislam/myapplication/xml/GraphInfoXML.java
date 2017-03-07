package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 24/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class GraphInfoXML extends ItemsInfoBaseXML {

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
    private GraphInfoItems items;

    public GraphInfoItems getItems() {
        return items;
    }

    public void setItems(GraphInfoItems items) {
        this.items = items;
    }





    @XStreamImplicit
    private List<GraphInfoItem> item;






    @XStreamAlias("items")
    public class GraphInfoItems {

        @XStreamImplicit
        private List<GraphInfoItem> item;

        public List<GraphInfoItem> getItem() {
            return item;
        }

        public void setItem(List<GraphInfoItem> item) {
            this.item = item;
        }
    }





    @XStreamAlias("item")
    public class GraphInfoItem {

        @XStreamAlias("LABEL")
        private String label;

        @XStreamAlias("COUNT")
        private String count;

        @XStreamAlias("SOURCETYPE")
        private String sourceType;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }
    }

}
