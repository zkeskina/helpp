package com.helpp.io.http;

import com.helpp.io.util.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.entity.StringEntity;


public class HttpClient {

    private static final int DEFAULT_TIMEOUT = 120 * 1000;
    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(DEFAULT_TIMEOUT);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
       // MyLog.log("Request Url -> " + getAbsoluteUrl(url));
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void directGet(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        //MyLog.log("Request Url -> " + getAbsoluteUrl(url));
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
       // MyLog.log("Request Url -> " + getAbsoluteUrl(url));
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }




    public static void patch(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        // MyLog.log("Request Url -> " + getAbsoluteUrl(url));
        client.patch(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void directPost(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        //MyLog.log("Request Url -> " + getAbsoluteUrl(url));
        client.post(url, params, responseHandler);
    }

    public static void directPostX(String url, StringEntity params, AsyncHttpResponseHandler responseHandler) {
        //MyLog.log("Request Url -> " + getAbsoluteUrl(url));
        //client.post(url, params, responseHandler);

    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        //MyLog.log("Request Url -> " + getAbsoluteUrl(url));
        client.put(getAbsoluteUrl(url), params, responseHandler);

    }

    public static void addHeader(String key, String value) {
        client.addHeader(key, value);
    }

    public static void removeAllHeaders() {
        client.removeAllHeaders();
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return(Constants.BASE_URL) + relativeUrl;

    }
}
