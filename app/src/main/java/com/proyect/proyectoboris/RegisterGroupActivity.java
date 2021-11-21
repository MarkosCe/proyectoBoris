package com.proyect.proyectoboris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
        mTextInputName = findViewById(R.id.textInputName);
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
        final String name = mTextInputName.getText().toString();
        code = generateCode();

        if(!name.isEmpty()){
            Group grupo = new Group();
            grupo.setCode(code);
            grupo.setName(name);

            String idU = mAuthProvider.getId();
            mGroupProvider.create(idU, grupo);

            //update(user);

            //Group group = new Group();
            //group.setId();
        }else{
            Toast.makeText(this, "Ingrese un nombre para el grupo", Toast.LENGTH_SHORT).show();
        }


        /*final String name = mTextInputName.getText().toString();
        final String email = mTextInputEmail.getText().toString();
        code = generateCode();

        if(!name.isEmpty() && !email.isEmpty()){
            User user = new User();
            user.setId(mAuthProvider.getId());
            user.setName(name);
            user.setEmail(email);
            user.setCode(code);

            update(user);
        }else{
            Toast.makeText(this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }*/
    }

    private String generateCode(){
        Random r = new Random();
        int n = 100000 + r.nextInt(900000);
        return String.valueOf(n);
    }

}