package com.proyect.proyectoboris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnteredCodeActivity extends AppCompatActivity {

    Button mUnirmeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entered_code);

        MyToolbar.show(this, "Unirse", true);

        mUnirmeButton = findViewById(R.id.unirmeButton);

        mUnirmeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}