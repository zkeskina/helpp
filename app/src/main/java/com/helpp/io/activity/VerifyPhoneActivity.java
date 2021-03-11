package com.helpp.io.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.helpp.io.R;

import butterknife.ButterKnife;

public class VerifyPhoneActivity extends AppCompatActivity {


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    private String verificatiobId;
    private FirebaseAuth mAuth;


    private static final String TAG = "PhoneLogin";

    CountryCodePicker codePicker;
    TextView t1, t2, countrycode;
    EditText e1, e2;
    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        ButterKnife.bind(this);


        codePicker = findViewById(R.id.codePicker);
        countrycode = findViewById(R.id.countrycode);
        e1 = (EditText) findViewById(R.id.Phonenoedittext);
        b1 = (Button) findViewById(R.id.PhoneVerify);
        e2 = (EditText) findViewById(R.id.OTPeditText);
        b2 = (Button) findViewById(R.id.OTPVERIFY);
        mAuth = FirebaseAuth.getInstance();


        countrycode.setText("+" + codePicker.getSelectedCountryCode());
        codePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode.setText("+" + codePicker.getSelectedCountryCode());
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                Toast.makeText(VerifyPhoneActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.i(TAG, "onVerificationFailed: " + e);

                FirebaseException xxx=e;
                // Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(VerifyPhoneActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(VerifyPhoneActivity.this, "InValid Phone Number", Toast.LENGTH_SHORT).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(VerifyPhoneActivity.this, "Too many request.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(VerifyPhoneActivity.this, "Verification code has been send on your number", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                e1.setVisibility(View.GONE);
                b1.setVisibility(View.GONE);
                countrycode.setVisibility(View.GONE);
                codePicker.setVisibility(View.GONE);
                e2.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);

            }
        };

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        countrycode.getText().toString() + e1.getText().toString(),
                        60,
                        java.util.concurrent.TimeUnit.SECONDS,
                        VerifyPhoneActivity.this,
                        mCallbacks);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, e2.getText().toString());
                // [END verify_with_code]
                signInWithPhoneAuthCredential(credential);
            }
        });


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(VerifyPhoneActivity.this, ProfileActivity.class));
                            finish();
                            Toast.makeText(VerifyPhoneActivity.this, "Verification Done", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyPhoneActivity.this, "Invalid Verification", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


}
