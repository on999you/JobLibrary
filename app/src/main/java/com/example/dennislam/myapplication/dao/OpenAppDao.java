package com.example.dennislam.myapplication.dao;

import com.example.dennislam.myapplication.xml.ItemsInfoBaseXML;
import com.example.dennislam.myapplication.xml.OpenAppXML;
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

public class OpenAppDao {

    static final String URL = "http://192.168.232.66:8009/API/OPEN_APP_CHECK_VERISON.aspx";
    private List<OpenAppXML.OpenAppItem> openAppItemList;

    private List<ItemsInfoBaseXML> getItemsInfo;
    int statusCode;
    public int getStatusCode() {
        return statusCode;
    }

    public List<OpenAppXML.OpenAppItem> getOpenAppItemDao(String udid,String appVersion,String mobAppId,String osType){

        String xml;

        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 20000);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(URL);

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
            nameValuePair.add(new BasicNameValuePair("udid", udid));
            nameValuePair.add(new BasicNameValuePair("appVersion", appVersion));
            nameValuePair.add(new BasicNameValuePair("mobAppId", mobAppId));
            nameValuePair.add(new BasicNameValuePair("osType", osType));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity, HTTP.UTF_8);

            String content = xml;
            byte[] bytes = content.getBytes();
            String contentNoBom = new String(bytes, 3, bytes.length - 3);

            XStream xStream = new XStream();
            xStream.processAnnotations(OpenAppXML.class);

            OpenAppXML xmlFile = (OpenAppXML) xStream.fromXML(contentNoBom);

            getItemsInfo = xmlFile.getItemsInfo();
            statusCode = getItemsInfo.get(0).getStatus_code();

            System.out.println(statusCode);
            System.out.println(getItemsInfo.get(0).getMsg());

            if(statusCode == 0) {
                openAppItemList = xmlFile.getItems().getItem();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return openAppItemList;
    }

}
