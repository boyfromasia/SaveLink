package com.example.savelink;

public class Folder {
    String name;
    String password;
    String image;
    String id;
    String count;

    public Folder(String id, String name, String password, String count) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
