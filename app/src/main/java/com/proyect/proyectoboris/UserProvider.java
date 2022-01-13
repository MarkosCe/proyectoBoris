package com.proyect.proyectoboris;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class UserProvider {
    DatabaseReference mDataBase;

    public UserProvider(){
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public Task<Void> create(User user){
        return mDataBase.child(user.getId()).setValue(user);
    }

    public DatabaseReference getUser(String idUser){
        return mDataBase.child(idUser);
    }

    public void createToken(String idUser){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()){
                    return;
                }
                String token = task.getResult();
                Map<String, Object> map = new HashMap<>();
                map.put("token", token);
                mDataBase.child(idUser).updateChildren(map);
            }
        });
    }

    public Task<Void> update(User user){
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getName());
        map.put("email",user.getEmail());
        map.put("image", user.getImage());
        map.put("code", user.getCode());
        return mDataBase.child(user.getId()).updateChildren(map);
    }
}
