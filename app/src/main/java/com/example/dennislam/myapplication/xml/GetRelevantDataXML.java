package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 23/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class GetRelevantDataXML extends ItemsInfoBaseXML {

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
    private RelevantDataItems items;

    public RelevantDataItems getItems() {
        return items;
    }

    public void setItems(RelevantDataItems items) {
        this.items = items;
    }





    @XStreamImplicit
    private List<RelevantDataItem> item;






    @XStreamAlias("items")
    public class RelevantDataItems {

        @XStreamImplicit
        private List<RelevantDataItem> item;

        public List<RelevantDataItem> getItem() {
            return item;
        }

        public void setItem(List<RelevantDataItem> item) {
            this.item = item;
        }
    }





    @XStreamAlias("item")
    public class RelevantDataItem {

        @XStreamAlias("JOBTITLE")
        private String jobTitle;

        @XStreamAlias("JOBCAT_NAME")
        private String jobCat;

        @XStreamAlias("EXPERIENCE_NAME")
        private String exp;

        @XStreamAlias("SALARY")
        private String salary;


        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }

        public String getJobCat() {
            return jobCat;
        }

        public void setJobCat(String jobCat) {
            this.jobCat = jobCat;
        }

        public String getExp() {
            return exp;
        }

        public void setExp(String exp) {
            this.exp = exp;
        }

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

    }

}
