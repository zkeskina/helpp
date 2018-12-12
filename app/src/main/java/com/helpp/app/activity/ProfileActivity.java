package com.helpp.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.helpp.app.R;
import com.helpp.app.util.PreferencesHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.btn_exit)
    TextView btnExit;
    @BindView(R.id.kullaniciSil)
    TextView deleteUser;
    @BindView(R.id.webview)
    WebView webView;

    private boolean comeFace;
    private boolean activityControl;
    private String pageUrl;
    String facebookName;
    String facebookLastName;
    String currentUser;
    String platformType;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static AsyncHttpClient client = new AsyncHttpClient();

    final static String url = "https://login.helpp.io:3200/loginuid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        String userUid = firebaseAuth.getUid();
        String deviceId = FirebaseInstanceId.getInstance().getToken();

        Log.i("ProfileActivity", "onCreate: UUID " + userUid + " \n notification token : " + deviceId);

        rawSample();

        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h2>HELPP MOBIL CLIENT</h2>\n" +
                "\n" +
                "<form action=\"https://login.helpp.io:3200/uASzpN9dGgcKGMdzEq795niLJFE3/mobile_login\" target=\"_blank\" method=\"GET\">\n" +
                "  Isminiz:<br>\n" +
                "  <input type=\"text\" name=\"Name\" value=\"None\">\n" +
                "  <br>\n" +
                "  Soyadiniz:<br>\n" +
                "  <input type=\"text\" name=\"Last_Name\" value=\"None\">\n" +
                "  <br>\n" +
                "  <p>Firmaniz tarafindan verilen sube kodunu asagiya giriniz.</p>\n" +
                "  Sube ID:<br>\n" +
                "  <input type=\"text\" name=\"Sube\" value=\"\">\n" +
                "  <br><br>\n" +
                "  <input type=\"submit\" value=\"Kaydet\">\n" +
                "</form>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        String mime = "text/html";
        String encoding = "utf-8";
        String pages = "https://login.helpp.io:3200/uASzpN9dGgcKGMdzEq795niLJFE3/mobile_login";
        String page2 = "https://login.helpp.io:3200/uASzpN9dGgcKGMdzEq795niLJFE3/mobile_login?Name=None&Last_Name=None&Sube=jhvj";
        webView.setWebViewClient(new WebViewClient());
        //  webView.loadUrl(pages);
        webView.loadUrl(page2);
        //webView.loadDataWithBaseURL(null, html, mime, encoding, null);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webSettings.setSafeBrowsingEnabled(false);
        }
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);

        platformType = PreferencesHelper.getInstance(ProfileActivity.this).getAccessToken();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            activityControl = bundle.getBoolean("phoneAct");
            comeFace = bundle.getBoolean("comeFace");
            currentUser = bundle.getString("currentUser");
            //facebookName = bundle.getString("name");
            //facebookLastName = bundle.getString("lastname");


        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {

                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    finish();


                }
            }
        };


       /*
        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
            finish();
        }
        */

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (activityControl != true) {
            //if (comeFace !=true){
            userName.setText("Hoşgeldiniz " + user.getEmail());
            //}else {
            //  userName.setText("Hoşgeldin "+ currentUser);
            //}
        } else {
            userName.setText("Hoşgeldiniz " + user.getPhoneNumber());
        }


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ProfileActivity.this);
                } else {
                    builder = new AlertDialog.Builder(ProfileActivity.this);
                }
                builder.setTitle("Çıkış")
                        .setMessage("Çıkış yapmak istediğinize emin misiniz?")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                //if (platformType == "google") {
                                AuthUI.getInstance().signOut(ProfileActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                        finish();

                                    }
                                });
                                //}else if (platformType == "facebook"){
                                //LoginManager.getInstance().logOut();
                                //startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                //finish();
                                //}else {
                                //startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                //finish();
                                //}
                            }
                        })
                        .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ProfileActivity.this);
                } else {
                    builder = new AlertDialog.Builder(ProfileActivity.this);
                }
                builder.setTitle("Kullanıcıyı Sil")
                        .setMessage("Kullanıcıyı silmek istediğinize emin misiniz?")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (user != null) {
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    //Silme işlemi başarılı oldugunda kullanıcıya bir mesaj gösterilip UyeOlActivity e geçiliyor.
                                                    if (task.isSuccessful()) {
                                                        firebaseAuth.signOut();

                                                        Toast.makeText(ProfileActivity.this, "Hesabın silindi.Yeni bir hesap oluştur!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                                        finish();
                                                    } else {
                                                        //İşlem başarısız olursa kullanıcı bilgilendiriliyor.
                                                        Toast.makeText(ProfileActivity.this, "Hesap silinemedi!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        })
                        .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void rawSample() {
        String userUid = firebaseAuth.getUid();
        String deviceId = FirebaseInstanceId.getInstance().getToken();

        Log.i("ProfileActivity", "onCreate: UUID " + userUid + " \n notification token : " + deviceId);

        JSONObject object = new JSONObject();

        try {
            object.put("User", userUid);
            object.put("deviceid", deviceId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String yourData = object.toString();
        StringEntity entity = null;
        try {
            entity = new StringEntity(yourData);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (Exception e) {
        }

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        client.post(null, url, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    String object = new String(responseBody);
                    JSONObject jsonObject = new JSONObject(object);
                    pageUrl = jsonObject.getString("page");
                    String user = jsonObject.getString("user");
                    String message = jsonObject.getString("message");
                    Log.d("tag", jsonObject.toString());
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
