package com.app.ecommerce.utilities;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class HttpClicentService
{

    Context context;

    public HttpClicentService(Context context) {
        this.context = context;
    }


    public String registration(String url, String name,String email,String mobile)
    {
        String responseServer = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("name", name);
            jsonobj.put("email", email);
            jsonobj.put("mobile", mobile);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("req", jsonobj.toString()));
            Log.e("Request>>>>>>>>", "url----> "+url+"\n Request---->" + nameValuePairs.toString());
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            InputStream inputStream = response.getEntity().getContent();
            InputStreamToStringExample str = new InputStreamToStringExample();
            responseServer = InputStreamToStringExample.getStringFromInputStream(inputStream);
            Log.e("response", "registration >>>>>>>>>-----" + responseServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseServer;
    }

    public static class InputStreamToStringExample
    {
        public static void main(String[] args) throws IOException {
            // intilize an InputStream
            InputStream is = new ByteArrayInputStream("file content..".getBytes());
            String result = getStringFromInputStream(is);
            System.out.println(result);
            System.out.println("Done");
        }

        // convert InputStream to String
        public static String getStringFromInputStream(InputStream is) {
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }
    }

}