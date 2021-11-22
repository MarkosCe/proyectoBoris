package com.proyect.proyectoboris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class RegisterGroupActivity extends AppCompatActivity {

    private Button btn_crearGrupo;

    private GroupProvider mGroupProvider;
    private AuthProvider mAuthProvider;
    private UserProvider mUserProvider;

    private TextInputEditText mTextInputName;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        //tomamos una referencia del botón
        btn_crearGrupo = findViewById(R.id.btnCreateGroup);
        mTextInputName = findViewById(R.id.textInputNameGroup);
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();

        mGroupProvider = new GroupProvider();

        btn_crearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegister();
            }
        });
    }

    void clickRegister(){
        String name = mTextInputName.getText().toString();
        code = generateCode();

        if(!name.isEmpty()){
            String idU = mAuthProvider.getId();


            Group grupo = new Group();
            grupo.setCode(code);
            grupo.setName(name);

            mGroupProvider.create(idU, grupo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Intent intent = new Intent(RegisterGroupActivity.this, CodeActivity.class);
                        intent.putExtra("codigo", code);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterGroupActivity.this, "Ingrese un nombre para el grupo", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(RegisterGroupActivity.this, "Ingrese un nombre para el grupo", Toast.LENGTH_SHORT).show();
        }

    }

    private String generateCode(){
        Random r = new Random();
        int n = 100000 + r.nextInt(900000);
        return String.valueOf(n);
    }

}