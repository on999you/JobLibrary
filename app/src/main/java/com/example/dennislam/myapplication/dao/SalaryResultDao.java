package com.example.dennislam.myapplication.dao;

import android.util.Log;

import com.example.dennislam.myapplication.xml.ItemsInfoBaseXML;
import com.example.dennislam.myapplication.xml.SalaryResultXML;
import com.example.dennislam.myapplication.xml.SendFeedbackXML;
import com.thoughtworks.xstream.XStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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
 * Created by dennislam on 16/1/2017.
 */

public class SalaryResultDao {

    static final String URL = "http://192.168.232.66:8009/API_CT2_SALARY/GET_RESULT_FROM_SALARY_CHECK.aspx";
    private List<SalaryResultXML.SalaryResultItem> salaryResultItemList;

    private List<ItemsInfoBaseXML> getItemsInfo;

    int statusCode;
    public int getStatusCode() {
        return statusCode;
    }

    int itemsTotal;
    public int getItemsTotal() {
        return itemsTotal;
    }


    public List<SalaryResultXML.SalaryResultItem> getSalaryResultItemDao(String jobTitle, Boolean withSimilarWord, String workExpFrom, String workExpTo, String salarySourceValue){

        String xml;

        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 3000);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(URL);

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
            nameValuePair.add(new BasicNameValuePair("jobTitle", jobTitle));
            nameValuePair.add(new BasicNameValuePair("withSimilarWord", withSimilarWord.toString()));
            nameValuePair.add(new BasicNameValuePair("expFrom", workExpFrom));
            nameValuePair.add(new BasicNameValuePair("expTo", workExpTo));
            nameValuePair.add(new BasicNameValuePair("salarySource", salarySourceValue));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity, HTTP.UTF_8);

            String content = xml;
            byte[] bytes = content.getBytes();
            String contentNoBom = new String(bytes, 3, bytes.length - 3);

            XStream xStream = new XStream();
            xStream.processAnnotations(SalaryResultXML.class);

            SalaryResultXML rss = (SalaryResultXML) xStream.fromXML(contentNoBom);

            getItemsInfo = rss.getItemsInfo();
            statusCode = getItemsInfo.get(0).getStatus_code();
            itemsTotal = getItemsInfo.get(0).getItemsTotal();

            if(statusCode == 0) {
                salaryResultItemList = rss.getItems().getItem();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return salaryResultItemList;
    }

}
