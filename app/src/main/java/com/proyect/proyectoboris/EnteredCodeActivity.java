package com.proyect.proyectoboris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chaos.view.PinView;

public class EnteredCodeActivity extends AppCompatActivity {

    Button mUnirmeButton;
    private PinView mPinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entered_code);

        MyToolbar.show(this, "Unirse", true);

        mUnirmeButton = findViewById(R.id.unirmeButton);

        mPinView = findViewById(R.id.pinView);

        mUnirmeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unirme();
            }
        });

    }

    public void unirme() {
        String codigo = mPinView.getText().toString();
        if(!codigo.isEmpty()) {
            //nos unimos al grupo

        }
    }
}