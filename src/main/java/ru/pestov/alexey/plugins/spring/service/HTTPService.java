package ru.pestov.alexey.plugins.spring.service;


import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

@Named
public class HTTPService {

    private static JSONService jsonService;

    @Inject
    public HTTPService(JSONService jsonService) {
        HTTPService.jsonService = jsonService;
    }

    public static void doPost() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        JSONObject jsonObject = jsonService.getJsonObject();
        HttpPost post = new HttpPost("http://localhost:2990/jira/rest/cab/1.0/systems/uploaddata");
        try {
            StringEntity params = new StringEntity(jsonObject.toString());
            post.addHeader("content-type", "application/x-www-form-urlencoded");
            post.setEntity(params);
            httpClient.execute((HttpUriRequest) httpClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPost() {


        //Creating a HttpClient object
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        //Creating a HttpGet object
        HttpGet httpget = new HttpGet("http://localhost:2990/jira/rest/cab/1.0/systems/uploaddata ");

        //Printing the method used
        System.out.println("Request Type: " + httpget.getMethod());

        try {
            //Executing the Get request
            HttpResponse httpresponse = httpclient.execute(httpget);
            return httpresponse.getEntity().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "wrong";
        }
    }
}
