package com.proyect.proyectoboris;

public class User {
    String id;
    String name;
    String email;
    String image;
    String phone;
    String code;

    public User(){

    }

    public User(String id, String name, String email, String image, String phone, String code) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
        this.phone = phone;
        this.code = code;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
