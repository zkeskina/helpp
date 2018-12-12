package com.helpp.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.helpp.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.edit_emailpw)
    EditText editEmailPasswd;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        if (firebaseAuth.getCurrentUser() != null){

            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            finish();

        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        String email = editEmail.getText().toString().trim();
        String emailPasswd = editEmailPasswd.getText().toString().trim();

        if (TextUtils.isEmpty(email)){

            Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(emailPasswd)){

            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (emailPasswd.length()<6) {
            Toast.makeText(getApplicationContext(), "Parola en az 6 haneli olmalıdır", Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,emailPasswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()){

                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                    finish();

                }else {

                    Toast.makeText(RegisterActivity.this, "Could not register, Try again.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
