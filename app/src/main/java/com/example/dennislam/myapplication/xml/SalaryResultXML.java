package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 16/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class SalaryResultXML extends ItemsInfoBaseXML {

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
    private SalaryResultItems items;

    public SalaryResultItems getItems() {
        return items;
    }

    public void setItems(SalaryResultItems items) {
        this.items = items;
    }





    @XStreamImplicit
    private List<SalaryResultItem> item;





    @XStreamAlias("items")
    public class SalaryResultItems  {

        @XStreamImplicit
        private List<SalaryResultItem> item;

        public List<SalaryResultItem> getItem() {
            return item;
        }

        public void setItem(List<SalaryResultItem> item) {
            this.item = item;
        }
    }






    @XStreamAlias("item")
    public class SalaryResultItem {

        @XStreamAlias("JOBTITLE")
        private String jobTitle;

        @XStreamAlias("MED_SALARY")
        private String medSalary;

        @XStreamAlias("MAX_SALARY")
        private String maxSalary;

        @XStreamAlias("MIN_SALARY")
        private String minSalary;

        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }

        public String getMedSalary() {
            return medSalary;
        }

        public void setMedSalary(String medSalary) {
            this.medSalary = medSalary;
        }

        public String getMaxSalary() {
            return maxSalary;
        }

        public void setMaxSalary(String maxSalary) {
            this.maxSalary = maxSalary;
        }

        public String getMinSalary() {
            return minSalary;
        }

        public void setMinSalary(String minSalary) {
            this.minSalary = minSalary;
        }

    }

}
