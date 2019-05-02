package com.example.haeseong.projectline1.data;

public class ChatData {
    String email;
    String text;

    public ChatData(){

    }
    public ChatData(String email, String text) {
        this.email = email;
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
