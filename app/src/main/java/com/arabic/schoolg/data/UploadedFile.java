package com.arabic.schoolg.data;

public class UploadedFile {
    private final String id;
    private final String image;
    private final String desc;

    public UploadedFile(String id, String image, String desc) {
        this.id = id;
        this.image = image;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }


    public String getDesc() {
        return desc;
    }


}
