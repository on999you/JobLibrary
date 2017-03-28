package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 24/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class GetAppliedJobXML extends ItemsInfoBaseXML{

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
    private AppliedJobItems items;

    public AppliedJobItems getItems() {
        return items;
    }

    public void setItems(AppliedJobItems items) {
        this.items = items;
    }





    @XStreamImplicit
    private List<AppliedJobItem> item;






    @XStreamAlias("items")
    public class AppliedJobItems {

        @XStreamImplicit
        private List<AppliedJobItem> item;

        public List<AppliedJobItem> getItem() {
            return item;
        }

        public void setItem(List<AppliedJobItem> item) {
            this.item = item;
        }
    }





    @XStreamAlias("item")
    public class AppliedJobItem {

        @XStreamAlias("JOB_ID")
        private String jobId;

        @XStreamAlias("JOBTITLE")
        private String jobTitle;

        @XStreamAlias("COMPANY_NAME")
        private String company;

        @XStreamAlias("APPLY_DATE")
        private String applyDate;

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

    }

}
