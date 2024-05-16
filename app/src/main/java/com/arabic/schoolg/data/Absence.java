package com.arabic.schoolg.data;

public class Absence {
    private String id;
    private String title;
    private String time;
    private Boolean hasReason;
    private String history;
    private String email;
    private String linkReason;

    public Absence(String id, String title, String time, Boolean hasReason,String history,String email,String linkReason) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.hasReason = hasReason;
        this.history = history;
        this.email = email;
        this.linkReason = linkReason;


    }

    public String getId() {
        return id;
    }


    public String getTopic() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public Boolean getHasReason() {
        return hasReason;
    }

    public String getEmail() {
        return email;
    }

    public String getHistory() {
        return history;
    }

    public String getLinkReason() {
        return linkReason;
    }
}
