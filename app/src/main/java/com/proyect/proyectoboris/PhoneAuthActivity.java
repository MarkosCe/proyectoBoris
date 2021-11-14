package com.proyect.proyectoboris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class PhoneAuthActivity extends AppCompatActivity {

    String mExtraPhone;
    String mVerificationId;

    TextView mTextViewVerificar;
    TextView mTextViewMsgCheck;

    Button mButtonCodeVerification;
    EditText mEditTextCodeVerification;

    AuthProvider mAuthProvider;

    UserProvider mUserProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        mTextViewVerificar = findViewById(R.id.textViewCheck);
        mTextViewMsgCheck = findViewById(R.id.textViewMsgCheck);

        mButtonCodeVerification = findViewById(R.id.btnCodeVerification);
        mEditTextCodeVerification = findViewById(R.id.editTextCodeVerification);

        mAuthProvider = new AuthProvider();

        mUserProvider = new UserProvider();

        mExtraPhone = getIntent().getStringExtra("phone");
        mTextViewVerificar.setText("Verificar " + mExtraPhone);
        mTextViewMsgCheck.setText("Esperando para detectar automáticamente el SMS enviado al " + mExtraPhone);
        //Toast.makeText(PhoneAuthActivity.this, "Telefono: " + mExtraPhone, Toast.LENGTH_LONG).show();

        mAuthProvider.sendCodeVerification(mExtraPhone, mcallbacks, this);

        mButtonCodeVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mEditTextCodeVerification.getText().toString();
                if(!code.equals("") && code.length() >= 6){
                    signIn(code);
                }else{
                    Toast.makeText(PhoneAuthActivity.this, "Ingresa el codigo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //SE EJECUTA CUANDO LA AUTENTICACION SE REALIZA EXITOSAMENTE
            //OCURRE DE DOS FORMAS
            //1. CUANDO EL USUARIO HALLA INSERTADO CORRECTAMENTE EL CODIGO DE VERIFICACION
            //2. CUANDO EL DISPOSITIVO MOVIL HALLA DETECTADO AUTOMATICAMENTE EL CODIGO DE VERIFICACION

            String code = phoneAuthCredential.getSmsCode();

            if(code != null){
                mEditTextCodeVerification.setText(code);
                signIn(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            //CUANDO EL ENVIA DEL SMS FALLA
            Toast.makeText(PhoneAuthActivity.this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            //SE EJECUTA CUANDO EL CODIGO DE VERIFICACION SE ENVIA A TRAVES DE MENSAJE DE TEXTO
            super.onCodeSent(verificationId, forceResendingToken);
            Toast.makeText(PhoneAuthActivity.this, "El codigo se envio", Toast.LENGTH_LONG).show();
            mVerificationId = verificationId;
        }
    };

    private void signIn(String code) {
        mAuthProvider.signInPhone(mVerificationId, code).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //EL USUARIO YA INICIO SESION CORRECTAMENTE

                    //Toast.makeText(PhoneAuthActivity.this, "sii entraa", Toast.LENGTH_SHORT).show();

                    mUserProvider.getUser(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(PhoneAuthActivity.this, "x2", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PhoneAuthActivity.this, MapUserActivity.class);
                                startActivity(intent);
                            }else{
                                createInfo();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    Toast.makeText(PhoneAuthActivity.this, "No se pudo iniciar sesion", Toast.LENGTH_SHORT).show();
                }
            }

            private void createInfo() {
                User user = new User();
                user.setId(mAuthProvider.getId());
                user.setPhone(mExtraPhone);
                //user.setCode(generateCode());

                mUserProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> taskCreate) {
                        if(taskCreate.isSuccessful()){
                            Intent intent = new Intent(PhoneAuthActivity.this, RegisterUserActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(PhoneAuthActivity.this, "No se pudo crear", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }

    /*private String generateCode(){
        Random r = new Random();
        int n = 100000 + r.nextInt(900000);
        return String.valueOf(n);
    }*/

}