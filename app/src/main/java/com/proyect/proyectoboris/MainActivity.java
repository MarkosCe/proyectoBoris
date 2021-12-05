package com.proyect.proyectoboris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mButtonIniciar;
    AuthProvider mAuthProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthProvider = new AuthProvider();

        mButtonIniciar = findViewById(R.id.buttonIniciar);

        mButtonIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
    }

    /*protected void onStart() {
        super.onStart();

        if(mAuthProvider.existSession()){
            Intent intent = new Intent(MainActivity.this, MapUserActivity.class);
            startActivity(intent);
        }
    }*/

    private void goToRegister() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}