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
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RegisterGroupActivity extends AppCompatActivity {

    private Button btn_crearGrupo;

    private Group grupo;
    private GroupProvider mGroupProvider;
    private AuthProvider mAuthProvider;
    private UserProvider mUserProvider;
    //private ChatProvider mChatProvider;

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
        //mChatProvider = new ChatProvider();

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
            grupo = new Group();
            grupo.setId(mGroupProvider.getIdGroup());
            grupo.setName(name);
            grupo.setIdUser(idU);
            grupo.setCode(code);
            grupo.setTimestamp(new Date().getTime());
            grupo.setNumberMessages(1);
            grupo.setWriting("");
            Random random = new Random();
            int n = random.nextInt(100000);
            grupo.setIdNotification(n);

            ArrayList<String> ids = new ArrayList<>();
            ids.add(mAuthProvider.getId());

            grupo.setIds(ids);

            //grupo = new Group(mGroupProvider.getIdGroup(), idU, name, "url", code);

            mGroupProvider.create(grupo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Intent intent = new Intent(RegisterGroupActivity.this, CodeActivity.class);
                        intent.putExtra("codigo", code);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterGroupActivity.this, "No es posible crear el grupo", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            String topic = grupo.getId();
            FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //Toast.makeText(EmergencyActivity.this, "suscrito al topico", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(RegisterGroupActivity.this, "Ingrese un nombre para el grupo", Toast.LENGTH_SHORT).show();
        }

    }

    /*private void createChat(String name){
        Random random = new Random();
        int n = random.nextInt(100000);
        Chat chat = new Chat();
        chat.setId(UUID.randomUUID().toString());
        chat.setGroupName(name);
        chat.setTimestamp(new Date().getTime());
        chat.setNumberMessages(1);
        chat.setWriting("");
        chat.setIdNotification(n);
        chat.setMulti(true);

        ArrayList<String> ids = new ArrayList<>();
        ids.add(mAuthProvider.getId());

        chat.setIds(ids);

        mChatProvider.create(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterGroupActivity.this, "Chat creado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterGroupActivity.this, "No se puedo crear el chat", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    private String generateCode(){
        Random r = new Random();
        int n = 100000 + r.nextInt(900000);
        return String.valueOf(n);
    }

}