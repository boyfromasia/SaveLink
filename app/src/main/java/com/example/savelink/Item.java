package com.example.savelink;

public class Item {
    String name;
    String url;
    String image;
    String folder;
    String count;
    String id;

    public Item(String folder, String name, String url, String count, String id) {
        this.folder = folder;
        this.name = name;
        this.url = url;
        this.count = count;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
