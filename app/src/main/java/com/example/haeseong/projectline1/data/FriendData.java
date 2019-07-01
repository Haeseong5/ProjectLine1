package com.example.haeseong.projectline1.data;

public class FriendData {
    String name;
    String photo;
    public FriendData(){

    }
    public FriendData(String name, String photo) {
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
