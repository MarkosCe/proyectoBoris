package com.proyect.proyectoboris;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public Task<Void> update(User user){
        Map<String, Object> map = new HashMap<>();
        map.put("name", user.getName());
        map.put("email",user.getEmail());
        map.put("image", user.getImage());
        return mDataBase.child(user.getId()).updateChildren(map);
    }
}
