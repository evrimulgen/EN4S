package com.tobbetu.en4s.backend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class Requests {

    public static final String url = "http://en4s.msimav.net/";
    private static Requests instance = null;
    private HttpClient httpclient;

    private Requests() {
        HttpParams utf8params = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(utf8params, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(utf8params, HTTP.UTF_8);

        httpclient = new DefaultHttpClient(utf8params);
        httpclient.getParams().setParameter("http.protocol.content-charset",
                HTTP.UTF_8);
    }

    public static Requests getInstance() {
        if (instance == null)
            instance = new Requests();
        return instance;
    }

    public HttpClient getHttpClient() {
        return httpclient;
    }

    public static HttpResponse get(String uri) throws IOException {
        return Requests.get(uri, null);
    }

    public static HttpResponse get(String uri, HttpParams params)
            throws IOException {
        HttpClient httpclient = Requests.getInstance().getHttpClient();
        HttpGet getRequest = new HttpGet(uri);
        if (params != null) {
            getRequest.setParams(params);
        }
        HttpResponse response = httpclient.execute(getRequest);

        return response;
    }

    public static HttpResponse post(String uri, String data) throws IOException {
        HttpClient httpclient = Requests.getInstance().getHttpClient();
        HttpResponse response;
        HttpPost postRequest = new HttpPost(uri);
        postRequest.setHeader("Accept", "application/json");
        StringEntity input = null;
        try {
            input = new StringEntity(data, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            Log.e("Requests.post", "Unexpected UnsupportedEncodingException", e);
        }
        input.setContentType("application/json; charset=UTF-8");
        input.setContentEncoding("UTF-8");

        postRequest.setEntity(input);
        response = httpclient.execute(postRequest);

        return response;
    }

    public static HttpResponse put(String uri, String data) throws IOException {
        HttpClient httpclient = Requests.getInstance().getHttpClient();
        HttpResponse response;
        HttpPut putRequest = new HttpPut(uri);
        putRequest.setHeader("Accept", "application/json");

        if (data != null) {
            StringEntity input = null;
            try {
                input = new StringEntity(data, HTTP.UTF_8);
            } catch (UnsupportedEncodingException e) {
                Log.e("Requests.post",
                        "Unexpected UnsupportedEncodingException", e);
            }
            input.setContentType("application/json; charset=UTF-8");
            input.setContentEncoding("UTF-8");
            putRequest.setEntity(input);
        }

        response = httpclient.execute(putRequest);
        return response;
    }

    public static byte[] download(String image) throws IOException {
        HttpResponse get = Requests.get("http://en4s.msimav.net" + image);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        get.getEntity().writeTo(out);
        out.close();

        return out.toByteArray();
    }

    public static String readResponse(HttpResponse response) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        response.getEntity().writeTo(out);
        out.close();
        Log.d("Requests.readResponse",
                "statuscode: " + statusLine.getStatusCode());
        return out.toString();

    }

    public static boolean checkStatusCode(HttpResponse response, int expected) {
        return response.getStatusLine().getStatusCode() == expected;
    }

    public static HttpParams buildParams(Object... args) {
        HttpParams newParams = new BasicHttpParams();
        for (int i = 0; i < args.length; i += 2) {
            newParams.setParameter((String) args[i], args[i + 1]);
        }

        return newParams;
    }
}