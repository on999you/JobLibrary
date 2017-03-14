package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 14/3/2017.
 */

@XStreamAlias("CTItemListRSS")
public class GetCvXML extends ItemsInfoBaseXML{

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
    private GetCvItems items;

    public GetCvItems getItems() {
        return items;
    }

    public void setItems(GetCvItems items) {
        this.items = items;
    }





    @XStreamImplicit
    private List<GetCvItem> item;






    @XStreamAlias("items")
    public class GetCvItems {

        @XStreamImplicit
        private List<GetCvItem> item;

        public List<GetCvItem> getItem() {
            return item;
        }

        public void setItem(List<GetCvItem> item) {
            this.item = item;
        }
    }





    @XStreamAlias("item")
    public class GetCvItem {

        @XStreamAlias("NAME")
        private String name;

        @XStreamAlias("EMAIL_ADDRESS")
        private String emailAddress;

        @XStreamAlias("MOBILE_NO")
        private String mobileNo;

        @XStreamAlias("EXPECTED_SALARY")
        private String expectedSalary;

        @XStreamAlias("EDUCATION_LEVEL")
        private String educationLevel;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getExpectedSalary() {
            return expectedSalary;
        }

        public void setExpectedSalary(String expectedSalary) {
            this.expectedSalary = expectedSalary;
        }

        public String getEducationLevel() {
            return educationLevel;
        }

        public void setEducationLevel(String educationLevel) {
            this.educationLevel = educationLevel;
        }


    }

}