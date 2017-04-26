package com.example.dennislam.myapplication.dao;

import com.example.dennislam.myapplication.activity.BaseActivity;
import com.example.dennislam.myapplication.xml.ItemsInfoBaseXML;
import com.example.dennislam.myapplication.xml.GetRelevantDataXML;
import com.thoughtworks.xstream.XStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennislam on 23/1/2017.
 */

public class GetRelevantDataDao {

    static final String URL = BaseActivity.dynamic_url+"/API/GET_RELEVANT_SALARY_DATA.aspx";
    private List<GetRelevantDataXML.RelevantDataItem> relevantDataItemList;

    private List<ItemsInfoBaseXML> getItemsInfo;
    int statusCode;
    int itemsTotal;

    public int getItemsTotal() {
        return itemsTotal;
    }

    public int getStatusCode() {
        return statusCode;
    }


    public List<GetRelevantDataXML.RelevantDataItem> getRelevantDataItemDao(int rownumStart, int rownumEnd, String jobTitle, Boolean withSimilarWord, ArrayList<String> finalSelectedJobCatArray, ArrayList<String> finalSelectedJobIndustryArray, String workExpFrom, String workExpTo, String salarySourceValue, String dataSourceValue){

        String xml;

        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 20000);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(URL);

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(10);
            nameValuePair.add(new BasicNameValuePair("rownumStart", String.valueOf(rownumStart)));
            nameValuePair.add(new BasicNameValuePair("rownumEnd", String.valueOf(rownumEnd)));
            nameValuePair.add(new BasicNameValuePair("jobTitle", jobTitle));
            nameValuePair.add(new BasicNameValuePair("withSimilarWord", withSimilarWord.toString()));
            nameValuePair.add(new BasicNameValuePair("jobCat", finalSelectedJobCatArray.toString()));
            nameValuePair.add(new BasicNameValuePair("jobIndustry", finalSelectedJobIndustryArray.toString()));
            nameValuePair.add(new BasicNameValuePair("expFrom", workExpFrom));
            nameValuePair.add(new BasicNameValuePair("expTo", workExpTo));
            nameValuePair.add(new BasicNameValuePair("salarySource", salarySourceValue));
            nameValuePair.add(new BasicNameValuePair("dataSource", dataSourceValue));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity, HTTP.UTF_8);

            String content = xml;
            byte[] bytes = content.getBytes();
            String contentNoBom = new String(bytes, 3, bytes.length - 3);

            XStream xStream = new XStream();
            xStream.processAnnotations(GetRelevantDataXML.class);

            GetRelevantDataXML xmlFile = (GetRelevantDataXML) xStream.fromXML(contentNoBom);

            getItemsInfo = xmlFile.getItemsInfo();
            statusCode = getItemsInfo.get(0).getStatus_code();
            itemsTotal = getItemsInfo.get(0).getItemsTotal();

            if(statusCode == 0) {
                relevantDataItemList = xmlFile.getItems().getItem();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relevantDataItemList;
    }

}
