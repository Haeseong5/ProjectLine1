package com.example.haeseong.projectline1.data;

public class Post {
    String title;
    String content;
    String time;
    String name;

    public Post(String title, String content, String time, String name) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
