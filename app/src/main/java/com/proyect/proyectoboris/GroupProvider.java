package com.proyect.proyectoboris;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class GroupProvider {

    DatabaseReference mDataBase;

    public GroupProvider(){
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Group");
    }

    public String getIdGroup(){
        return mDataBase.push().getKey();
    }

    public Task<Void> create(String id, Group group){
        return mDataBase.child(id).child(getIdGroup()).setValue(group);
    }

    public DatabaseReference getGroup(String idGroup){
        return mDataBase;
    }

    public Task<Void> update(Group group){
        Map<String, Object> map = new HashMap<>();
        map.put("name", group.getName());
        map.put("image", group.getImage());
        map.put("code", group.getCode());
        return mDataBase.child(group.getId()).updateChildren(map);
    }
}
