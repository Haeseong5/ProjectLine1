package com.example.haeseong.projectline1.data;

import java.util.ArrayList;

public class UserData {
    ArrayList<String> photos = new ArrayList<>();
    String name;
    String age;
    String area;
    String height;
    String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public UserData(String photo) {
        this.photo = photo;
    }

    public  UserData(){

    }
    public UserData(ArrayList<String> photos, String name, String age, String area, String height) {
        this.photos = photos;
        this.name = name;
        this.age = age;
        this.area = area;
        this.height = height;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
