package com.arabic.schoolg.data;

public class Lecture {
    private int id;
    private String name;
    private String teacher;
    private String time;

    public Lecture(int id, String name, String teacher, String time) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.time = time;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getTime() {
        return time;
    }

}
