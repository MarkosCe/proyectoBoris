package com.proyect.proyectoboris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity {

    Button mButtonEnviar;
    CountryCodePicker mCountryCode;
    EditText mEditTextPhone;

    AuthProvider mAuthProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuthProvider = new AuthProvider();

        mButtonEnviar = findViewById(R.id.buttonEnviarCodigo);
        mCountryCode = findViewById(R.id.ccp);
        mEditTextPhone = findViewById(R.id.editTextPhone);

        mButtonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    public void goToLogin() {
        String code = mCountryCode.getSelectedCountryCodeWithPlus();
        String phone = mEditTextPhone.getText().toString();
        if (!phone.equals("")){
            Intent intent = new Intent(RegisterActivity.this, PhoneAuthActivity.class);
            intent.putExtra("phone", code + phone);
            startActivity(intent);
        }else {
            Toast.makeText(RegisterActivity.this, "Debes ingresar el telefono", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuthProvider.existSession()){
            Intent intent = new Intent(RegisterActivity.this, MapUserActivity.class);
            startActivity(intent);
        }
    }
}