package com.proyect.proyectoboris;

import java.util.ArrayList;

public class Group {

    String id;
    String name;
    String image;
    String code;
    ArrayList<String> codes;

    public Group(){
        this.codes = new ArrayList<>();
    }

    public Group(String id, String name, String image, String code) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.code = code;
        this.codes = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList getCodeUser() {
        return codes;
    }

    public void setCodeUser(String codeUser) {
        this.codes.add(codeUser);
    }
}
