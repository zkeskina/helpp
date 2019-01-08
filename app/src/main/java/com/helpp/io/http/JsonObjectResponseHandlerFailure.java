package com.helpp.io.http;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public abstract class JsonObjectResponseHandlerFailure extends AsyncHttpResponseHandler {

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {
            String responseString = new String(responseBody, "UTF-8");
            JSONObject jsonObject = new JSONObject(responseString);

            onSuccess(jsonObject);
        } catch (UnsupportedEncodingException e) {
            onError(e);
        } catch (JSONException e) {
            onError(e);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

        try {
            if (responseBody != null) {
                String responseString = new String(responseBody, "UTF-8");
                JSONObject jsonObject = new JSONObject(responseString);
                onFailure(jsonObject);
            } else {
                onError(error);
            }

        } catch (UnsupportedEncodingException e) {
            onError(error);
            e.printStackTrace();
        } catch (JSONException e) {
            onError(error);
            e.printStackTrace();
        }


    }

    public abstract void onSuccess(JSONObject response);

    public abstract void onError(Throwable error);

    public abstract void onFailure(JSONObject response);
}
