package com.helpp.app.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asksira.webviewsuite.WebViewSuite;
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

    @BindView(R.id.refresh)
    ImageView refresh;
    //@BindView(R.id.webViewSuite)
    //WebViewSuite webViewSuite;

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

    private String page = "https://login.helpp.io:3200/k2MgdWZ6BbROQEoNDje1zs6W2Nz2/mobile_login";

    private String pages = "https://login.helpp.io:3200/ZcF9N5mDt9cdHzzEG1yA8VQbArJ3/mobile_login?branch=564532";

    private static AsyncHttpClient client = new AsyncHttpClient();

    final static String url = "https://login.helpp.io:3200/loginuid";

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.O)
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

        webView.setWebViewClient(new AppWebViewClients());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);



        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload();
                Toast.makeText(ProfileActivity.this, "Sayfa Yenilendi.", Toast.LENGTH_SHORT).show();
            }
        });

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

                    //startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    //finish();


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
                        .setIcon(android.R.drawable.ic_dialog_info)
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
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });

    }

    public class AppWebViewClients extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
           handler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
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
                    //userName.setText(pageUrl);
                    String userid = jsonObject.getString("user");
                    String message = jsonObject.getString("message");

                    webView.loadUrl(pageUrl);

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


  /*

    @Override
    public void onBackPressed() {
        if (!webViewSuite.goBackIfPossible()) super.onBackPressed();
    }
*/
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
