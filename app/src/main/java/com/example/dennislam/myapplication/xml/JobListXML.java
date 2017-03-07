package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 24/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class JobListXML extends ItemsInfoBaseXML {

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
    private JobListItems items;

    public JobListItems getItems() {
        return items;
    }

    public void setItems(JobListItems items) {
        this.items = items;
    }





    @XStreamImplicit
    private List<JobListItem> item;






    @XStreamAlias("items")
    public class JobListItems {

        @XStreamImplicit
        private List<JobListItem> item;

        public List<JobListItem> getItem() {
            return item;
        }

        public void setItem(List<JobListItem> item) {
            this.item = item;
        }
    }





    @XStreamAlias("item")
    public class JobListItem {

        @XStreamAlias("JOB_ID")
        private String jobID;

        @XStreamAlias("JOBTITLE")
        private String jobTitle;

        @XStreamAlias("COMPANY_NAME")
        private String company;

        @XStreamAlias("CREATE_DATE")
        private String createDate;

        @XStreamAlias("SALARY")
        private String salary;


        public String getJobID() {
            return jobID;
        }

        public void setJobID(String jobID) {
            this.jobID = jobID;
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

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

    }

}
