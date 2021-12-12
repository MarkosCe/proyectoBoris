package com.proyect.proyectoboris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CodeActivity extends AppCompatActivity {

    TextView mTextViewCode;
    Button mButtonOk;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        mTextViewCode = findViewById(R.id.codeTextView);
        mButtonOk = findViewById(R.id.okButton);

        code = getIntent().getStringExtra("codigo");

        mTextViewCode.setText(code);

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CodeActivity.this, GroupViewActivity.class);
                intent.putExtra("codigo", code);
                startActivity(intent);
            }
        });
    }
}