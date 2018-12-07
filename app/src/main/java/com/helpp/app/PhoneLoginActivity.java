package com.helpp.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneLoginActivity extends AppCompatActivity {

    @BindView(R.id.countrycode)
    TextView countrycode;
    @BindView(R.id.editTextMobile)
    EditText editTextMobile;
    @BindView(R.id.buttonContinue)
    Button buttonContinue;
    @BindView(R.id.codePicker)
    CountryCodePicker codePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        ButterKnife.bind(this);

        countrycode.setText("+"+codePicker.getSelectedCountryCode());
        codePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode.setText("+" + codePicker.getSelectedCountryCode());
            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codePicker.getSelectedCountryCode();

                String number = editTextMobile.getText().toString();

                if (number.isEmpty()){
                    editTextMobile.setError("Valid number is required");
                    editTextMobile.requestFocus();
                    return;
                }
                String phoneNumber = "+" +code+number;

                Intent intent = new Intent(PhoneLoginActivity.this,VerifyPhoneActivity.class);
                intent.putExtra("phonenumber",phoneNumber);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(PhoneLoginActivity.this,ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}