package com.helpp.app.http;


import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;


public abstract class JsonArrayResponseHandler extends AsyncHttpResponseHandler {

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            String responseString = new String(responseBody, "UTF-8");
            JSONArray jsonArray = new JSONArray(responseString);

            onSuccess(jsonArray);
        } catch (UnsupportedEncodingException e) {
            onError(e);
        } catch (JSONException e) {
            onError(e);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        onError(error);
    }

    public abstract void onSuccess(JSONArray response);

    public abstract void onError(Throwable error);
}
