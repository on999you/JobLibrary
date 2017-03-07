package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 10/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class JobCatXML extends ItemsInfoBaseXML {

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
    private JobCatItems items;

    public JobCatItems getItems() {
        return items;
    }

    public void setItems(JobCatItems items) {
        this.items = items;
    }





    @XStreamImplicit
    private List<JobCatItem> item;






    @XStreamAlias("items")
    public class JobCatItems {

        @XStreamImplicit
        private List<JobCatItem> item;

        public List<JobCatItem> getItem() {
            return item;
        }

        public void setItem(List<JobCatItem> item) {
            this.item = item;
        }
    }





    @XStreamAlias("item")
    public class JobCatItem {

        @XStreamAlias("JOBCAT_ID")
        private String jobCatID;

        @XStreamAlias("JOBCAT_NAME")
        private String jobCatName;

        @XStreamAlias("JOBCAT_NAME_CHI")
        private String jobCatNameChi;

        public String getJobCatID() {
            return jobCatID;
        }

        public void setJobCatID(String jobCatID) {
            this.jobCatID = jobCatID;
        }

        public String getJobCatName() {
            return jobCatName;
        }

        public void setJobCatName(String jobCatName) {
            this.jobCatName = jobCatName;
        }

        public String getJobCatNameChi() {
            return jobCatNameChi;
        }

        public void setJobCatNameChi(String jobCatNameChi) {
            this.jobCatNameChi = jobCatNameChi;
        }

    }
}
