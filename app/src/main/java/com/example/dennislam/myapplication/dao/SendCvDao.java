package com.example.dennislam.myapplication.dao;

import android.util.Log;

import com.android.internal.http.multipart.MultipartEntity;
import com.example.dennislam.myapplication.xml.ItemsInfoBaseXML;
import com.example.dennislam.myapplication.xml.SendCvXML;
import com.example.dennislam.myapplication.xml.SendFeedbackXML;
import com.thoughtworks.xstream.XStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennislam on 16/1/2017.
 */

public class SendCvDao {

    static final String URL = "http://192.168.232.66:8009/API_CT2_MOBILECV/SEND_CV.aspx";
    private List<ItemsInfoBaseXML> cvItemList;

    public List<ItemsInfoBaseXML> sendCvItemDao(File videoFile, String udid, String name, String email, String mobileNo,String expectedSalary,String educationLevelId){

        String xml;

        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 3000);

            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(URL);

            HttpEntity entity = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addTextBody("udid", udid)
                    .addTextBody("name", name)
                    .addTextBody("email", email)
                    .addTextBody("mobileNo", mobileNo)
                    .addTextBody("expectedSalary", expectedSalary)
                    .addTextBody("educationLevel", educationLevelId)
                    .addBinaryBody("video", videoFile)
                    .build();

            httpPost.setEntity(entity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity, HTTP.UTF_8);

            String content = xml;
            byte[] bytes = content.getBytes();
            String contentNoBom = new String(bytes, 3, bytes.length - 3);

            XStream xStream = new XStream();
            xStream.processAnnotations(SendCvXML.class);

            SendCvXML xmlFile = (SendCvXML) xStream.fromXML(contentNoBom);

            cvItemList = xmlFile.getItemsInfo();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cvItemList;
    }

}
