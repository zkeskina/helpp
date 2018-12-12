package com.helpp.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PreferencesHelper {

    private static PreferencesHelper instance;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private static final String PROP_ONE_SIGNAL_USER_ID = "onesignal.user.id";
    private static final String PROP_ONE_SIGNAL_TAG = "onesignal.tag";
    private static final String PROP_TOKEN = "token";
    private static final String PROP_ACCESS_TOKEN = "access_token";
    private static final String PROP_LANGUAGE = "language";
    private static final String PROP_COUNTRY = "country";
    private static final String PROP_DEVICE_MODEL = "device_model";
    private static final String PROP_DEVICE_BRAND = "device_brand";
    private static final String PROP_PREMIUM = "is_premium";
    private static final String PROP_STATUS = "status";
    private static final String PROP_USING_LIMIT = "limit";
    private static final String PROP_DISTURBING_NAME = "name.disturb";
    private static final String PROP_OPEN_WELCOME = "welcome";
    private static final String PROP_CHECK_UPLOAD = "check.upload";


    private PreferencesHelper(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPreferences.edit();
    }

    public static PreferencesHelper getInstance(Context context) {
        if (instance == null)
            instance = new PreferencesHelper(context);
        return instance;
    }

    public void setOneSignalUserId(String userId) {
        mEditor.putString(PROP_ONE_SIGNAL_USER_ID, userId);
        mEditor.commit();
    }

    public String getOneSignalUserId() {
        return mPreferences.getString(PROP_ONE_SIGNAL_USER_ID, "");
    }

    public void setToken(String token) {
        mEditor.putString(PROP_TOKEN, token);
        mEditor.commit();
    }

    public String getToken() {
        return mPreferences.getString(PROP_TOKEN, "");
    }

    public void setAccesToken(String token) {
        mEditor.putString(PROP_ACCESS_TOKEN, token);
        mEditor.commit();
    }

    public String getAccessToken() {
        return mPreferences.getString(PROP_ACCESS_TOKEN, "");
    }

    public void setLanguageCode(String language) {
        mEditor.putString(PROP_LANGUAGE, language);
        mEditor.commit();
    }

    public String getLanguageCode() {
        return mPreferences.getString(PROP_LANGUAGE, "tr");
    }

    public void setCountry(String country) {
        mEditor.putString(PROP_COUNTRY, country);
        mEditor.commit();
    }

    public String getCountry() {
        return mPreferences.getString(PROP_COUNTRY, "Turkey");
    }

    public void setDeviceModel(String deviceModel) {
        mEditor.putString(PROP_DEVICE_MODEL, deviceModel);
        mEditor.commit();
    }

    public String getDeviceModel() {
        return mPreferences.getString(PROP_DEVICE_MODEL, "");
    }

    public void setDeviceBrand(String deviceBrand) {
        mEditor.putString(PROP_DEVICE_BRAND, deviceBrand);
        mEditor.commit();
    }

    public String getDeviceBrand() {
        return mPreferences.getString(PROP_DEVICE_BRAND, "");
    }

    public void setPremium(int premium) {
        mEditor.putInt(PROP_PREMIUM, premium);
        mEditor.commit();
    }

    public int getPremium() {
        return mPreferences.getInt(PROP_PREMIUM, 0);
    }

    public void setOneSignalTag(boolean tag) {
        mEditor.putBoolean(PROP_ONE_SIGNAL_TAG, tag);
        mEditor.commit();
    }

    public boolean getOneSignalTag() {
        return mPreferences.getBoolean(PROP_ONE_SIGNAL_TAG, true);
    }


    public void setStatus(int status) {
        mEditor.putInt(PROP_STATUS, status);
        mEditor.commit();
    }

    public int getStatus() {
        return mPreferences.getInt(PROP_STATUS, 0);
    }

    public void setUsingLimit(int usingLimit) {
        mEditor.putInt(PROP_USING_LIMIT, usingLimit);
        mEditor.commit();

    }

    public int getUsingLimit() {
        return mPreferences.getInt(PROP_USING_LIMIT, 1);
    }


    public String getDisturbingId() {
        return mPreferences.getString(PROP_DISTURBING_NAME, "");
    }

    public void setDisturbingId(String name) {
        mEditor.putString(PROP_DISTURBING_NAME, name);
        mEditor.commit();
    }

    public void setOpenWelcome(boolean open) {
        mEditor.putBoolean(PROP_OPEN_WELCOME, open);
        mEditor.commit();
    }

    public boolean getOpenWelcome() {
        return mPreferences.getBoolean(PROP_OPEN_WELCOME, false);
    }

    public void setCheckUpload(String date) {
        mEditor.putString(PROP_CHECK_UPLOAD, date);
        mEditor.commit();
    }

    public String getCheckUpload() {
        return mPreferences.getString(PROP_CHECK_UPLOAD, "");
    }

    public void clear() {

        mEditor.clear().commit();
    }


}
