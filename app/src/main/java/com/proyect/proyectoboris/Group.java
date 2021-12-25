package com.proyect.proyectoboris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group {

    private String id;
    private String idUser;
    private String name;
    private String image;
    private String code;
    private Map<String, Boolean> members;
    private String writing;
    private long timestamp;
    private ArrayList<String> ids;
    private int numberMessages;
    private int idNotification;

    public Group(){ }

    public Group(String id, String idUser, String name, String image, String code, Map<String, Boolean> members, String writing, long timestamp, ArrayList<String> ids, int numberMessages, int idNotification) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.image = image;
        this.code = code;
        this.members = members;
        this.writing = writing;
        this.timestamp = timestamp;
        this.ids = ids;
        this.numberMessages = numberMessages;
        this.idNotification = idNotification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, Boolean> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Boolean> members) {
        this.members = members;
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public int getNumberMessages() {
        return numberMessages;
    }

    public void setNumberMessages(int numberMessages) {
        this.numberMessages = numberMessages;
    }

    public int getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }
}
