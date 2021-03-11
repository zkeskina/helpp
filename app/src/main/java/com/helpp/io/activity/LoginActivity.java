package com.helpp.io.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.helpp.io.R;

import butterknife.BindView;
import butterknife.ButterKnife;

//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.edit_emailpw)
    EditText editEmailPasswd;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forgetpw)
    TextView tv_forgetpw;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        if (firebaseAuth.getCurrentUser() != null){

            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            finish();

        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        tv_forgetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswdActivity.class));

            }
        });

    }

    private void userLogin() {
        String email = editEmail.getText().toString();
        final String emailPasswd = editEmailPasswd.getText().toString();

        if (TextUtils.isEmpty(email)){

            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();

            return;
        }

        if (TextUtils.isEmpty(emailPasswd)){

            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();

            return;
        }
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,emailPasswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "Your email or password is incorrect.", Toast.LENGTH_SHORT).show();
                    Log.e("Input error",task.getException().getMessage());

                }
            }
        });
    }

}
