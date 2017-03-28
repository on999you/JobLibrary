package com.example.dennislam.myapplication.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by dennislam on 10/1/2017.
 */

@XStreamAlias("CTItemListRSS")
public class GetEducationLevelXML extends ItemsInfoBaseXML {

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
    private EducationLevelItems items;

    public EducationLevelItems getItems() {
        return items;
    }

    public void setItems(EducationLevelItems items) {
        this.items = items;
    }





    @XStreamImplicit
    private List<EducationLevelItem> item;





    @XStreamAlias("items")
    public class EducationLevelItems  {

        @XStreamImplicit
        private List<EducationLevelItem> item;

        public List<EducationLevelItem> getItem() {
            return item;
        }

        public void setItem(List<EducationLevelItem> item) {
            this.item = item;
        }
    }






    @XStreamAlias("item")
    public class EducationLevelItem {

        @XStreamAlias("EDUCATION_ID")
        private String educationID;

        @XStreamAlias("EDUCATION_NAME")
        private String educationName;

        @XStreamAlias("EDUCATION_NAME_CHI")
        private String educationNameChi;

        public String getEducationID() {
            return educationID;
        }

        public void setEducationID(String educationID) {
            this.educationID = educationID;
        }

        public String getEducationName() {
            return educationName;
        }

        public void setEducationName(String educationName) {
            this.educationName = educationName;
        }

        public String getEducationNameChi() {
            return educationNameChi;
        }

        public void setEducationNameChi(String educationNameChi) {
            this.educationNameChi = educationNameChi;
        }
    }

}
