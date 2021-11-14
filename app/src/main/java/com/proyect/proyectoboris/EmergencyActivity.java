package com.proyect.proyectoboris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EmergencyActivity extends AppCompatActivity {

    Button mButtonEmergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        MyToolbar.show(this, "", true);

        mButtonEmergency = findViewById(R.id.emergency_button);

        mButtonEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EmergencyActivity.this, "LISTO!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}