package com.proyect.proyectoboris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class CreateGroup extends AppCompatActivity {

    private Button btn_crearGrupo;

    TextInputEditText mTextInputName;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        //tomamos una referencia del botón
        btn_crearGrupo = findViewById(R.id.btnCreateGroup);
        mTextInputName = findViewById(R.id.textInputName);

        btn_crearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegister();
            }
        });
    }

    void clickRegister(){
        final String name = mTextInputName.getText().toString();
        code = generateCode();

        if(!name.isEmpty()){
            User user = new User();
           // user.setId(mAuthProvider.getId());
            user.setName(name);
            //user.setEmail(email);
            user.setCode(code);

            //update(user);

            Group group = new Group();
            //group.setId();
        }else{
            Toast.makeText(this, "Ingrese un nobre para el grupo", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateCode(){
        Random r = new Random();
        int n = 100000 + r.nextInt(900000);
        return String.valueOf(n);
    }

}