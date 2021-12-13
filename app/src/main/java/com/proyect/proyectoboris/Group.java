package com.proyect.proyectoboris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group {

    String id;
    String idUser;
    String name;
    String image;
    String code;
    Map<String, Boolean> members;

    public Group(){ }

    public Group(String id, String idUser, String name, String image, String code) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.image = image;
        this.code = code;
        members = new HashMap<>();
        members.put(idUser, true);
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
}
