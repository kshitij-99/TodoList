package com.example.todolist;

public class DataModel {

    private String title;
    private String date;
    private String time;
    private String check;

    public DataModel(String title, String date, String time,String check) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.check=check;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCheck() { return check; }
}
