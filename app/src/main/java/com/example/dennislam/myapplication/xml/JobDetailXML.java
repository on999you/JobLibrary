package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 24/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class JobDetailXML extends ItemsInfoBaseXML {

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
    private JobDetailItems items;

    public JobDetailItems getItems() {
        return items;
    }

    public void setItems(JobDetailItems items) {
        this.items = items;
    }





    @XStreamImplicit
    private List<JobDetailItem> item;






    @XStreamAlias("items")
    public class JobDetailItems {

        @XStreamImplicit
        private List<JobDetailItem> item;

        public List<JobDetailItem> getItem() {
            return item;
        }

        public void setItem(List<JobDetailItem> item) {
            this.item = item;
        }
    }





    @XStreamAlias("item")
    public class JobDetailItem {

        @XStreamAlias("JOBTITLE")
        private String jobTitle;

        @XStreamAlias("INDUSTRY_NAME")
        private String industry;

        @XStreamAlias("JOBAREA_NAME")
        private String jobArea;

        @XStreamAlias("COMPANY_NAME")
        private String company;

        @XStreamAlias("EMAIL")
        private String email;

        @XStreamAlias("SIMPLE_BODY")
        private String simpleBody;

        @XStreamAlias("JOBDESC")
        private String jobDesc;

        @XStreamAlias("CONTACT")
        private String contact;

        @XStreamAlias("COMPANY_DESC")
        private String companyDesc;

        @XStreamAlias("CREATE_DATE")
        private String createDate;


        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getJobArea() {
            return jobArea;
        }

        public void setJobArea(String jobArea) {
            this.jobArea = jobArea;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSimpleBody() {
            return simpleBody;
        }

        public void setSimpleBody(String simpleBody) {
            this.simpleBody = simpleBody;
        }

        public String getJobDesc() {
            return jobDesc;
        }

        public void setJobDesc(String jobDesc) {
            this.jobDesc = jobDesc;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getCompanyDesc() {
            return companyDesc;
        }

        public void setCompanyDesc(String companyDesc) {
            this.companyDesc = companyDesc;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }





    }
}
