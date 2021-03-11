package com.helpp.io.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.helpp.io.R;

//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;

public class ForgetPasswdActivity extends AppCompatActivity {

    private EditText email;
    private Button yeniParolaGonder;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_passwd);

        email = (EditText) findViewById(R.id.uyeEmail);
        yeniParolaGonder = (Button) findViewById(R.id.yeniParolaGonder);


        //FirebaseAuth sınıfının referans olduğu nesneleri kullanabilmek için getInstance methodunu kullanıyoruz.
        auth = FirebaseAuth.getInstance();


        yeniParolaGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = email.getText().toString().trim();

                if (TextUtils.isEmpty(mail)) {
                    Toast.makeText(getApplication(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }


                //FirebaseAuth sınıfı üzerinden sendPasswordResetEmail nesnesi ile girilen maile parola sıfırlama bağlantısı gönderebiliyoruz.
                auth.sendPasswordResetEmail(mail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetPasswdActivity.this, "The link required for the new password has been sent to your address!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgetPasswdActivity.this, "Sending email error!", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                startActivity(new Intent(ForgetPasswdActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

}