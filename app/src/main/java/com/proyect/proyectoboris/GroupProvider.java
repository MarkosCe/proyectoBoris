package com.proyect.proyectoboris;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class GroupProvider {

    private DatabaseReference mDataBase;

    public GroupProvider(){
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Group");
    }

    /*public String getIdGroup(){
        return mDataBase.push().getKey();
    }*/

    /*public Task<Void> create(String iduser, Group group){
        return mDataBase.child(iduser).child(code).setValue(group);
    }*/

    public Task<Void> create(Group group){
        return mDataBase.child(group.getId()).setValue(group);
    }

    public String getIdGroup(){
        return mDataBase.push().getKey();
    }

    public DatabaseReference getGroup(){
        return mDataBase;
    }

    public Task<Void> update(Group group){
        Map<String, Object> map = new HashMap<>();
        map.put("name", group.getName());
        map.put("image", group.getImage());
        map.put("code", group.getCode());
        return mDataBase.child(group.getId()).updateChildren(map);
    }

    public Task<Void> updateMembers(Group group, String uid){
        Map<String, Object> map = new HashMap<>();
        map.put("members/"+uid, true);

        return mDataBase.child(group.getId()).updateChildren(map);
    }
}
