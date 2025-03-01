package com.arabic.schoolg.data;

public class Category {
    private int id;
    private int image;
    private String name;

    public Category(int id, int image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

}
