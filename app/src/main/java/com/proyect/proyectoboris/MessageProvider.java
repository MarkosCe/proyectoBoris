package com.proyect.proyectoboris;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class MessageProvider {

    private DatabaseReference mDataBase;

    public MessageProvider(){
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Messages");
    }

    /*public String getIdGroup(){
        return mDataBase.push().getKey();
    }*/

    /*public Task<Void> create(String iduser, Group group){
        return mDataBase.child(iduser).child(code).setValue(group);
    }*/

    public Task<Void> create(Message message){
        return mDataBase.child(message.getId()).setValue(message);
    }

    public String getIdGroup(){
        return mDataBase.push().getKey();
    }

    public Query getMessagesByChat(String idChat){
        Query query = mDataBase
                .orderByChild("idChat")
                .equalTo(idChat);
        return query;
    }

    /*public Query getLastMessagesByChatAndSender(String idChat, String idSender) {
        ArrayList<String> status = new ArrayList<>();
        status.add("ENVIADO");
        status.add("RECIBIDO");

        return mCollection
                .whereEqualTo("idChat", idChat)
                .whereEqualTo("idSender", idSender)
                .whereIn("status", status)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5);
    }*/

    public DatabaseReference getGroup(){
        return mDataBase;
    }

    public DatabaseReference getGroupId(String id){ return mDataBase.child(id);}

    public Task<Void> updateStatus(String idMessage, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        return mDataBase.child(idMessage).updateChildren(map);
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
        //map.put("members/"+uid+"/", true);

        return mDataBase.child(group.getId()).updateChildren(map);
    }

}
