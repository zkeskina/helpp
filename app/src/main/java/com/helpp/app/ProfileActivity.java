package com.helpp.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.btn_exit)
    Button btnExit;
    @BindView(R.id.kullaniciSil)
    Button deleteUser;

    private boolean activityControl;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            activityControl = bundle.getBoolean("phoneAct");

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

        if (activityControl != true){
            userName.setText("Hoşgeldin "+user.getEmail());
        }else {
            userName.setText("Hoşgeldin " + user.getPhoneNumber());
        }


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == btnExit){
                    firebaseAuth.signOut();

                    finish();
                    startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                }
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
