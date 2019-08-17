package com.example.haeseong.projectline1.market;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

class MarketData {
    public String writer;
    public String price;
    public Timestamp time;
    public String title;
    public ArrayList<String> photo;
    String uid;
    String content;
    public MarketData(){}

    public MarketData(String writer, String price, Timestamp time, String title, ArrayList<String> photo, String uid, String content) {
        this.writer = writer;
        this.price = price;
        this.time = time;
        this.title = title;
        this.photo = photo;
        this.uid = uid;
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<String> photo) {
        this.photo = photo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}