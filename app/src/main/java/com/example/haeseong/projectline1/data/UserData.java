package com.example.haeseong.projectline1.data;

public class UserData {
    String name;
    String email;
    String grade;
    String school;
    String photoUri;

    public UserData(String name, String email, String grade, String school, String photoUri) {
        this.name = name;
        this.email = email;
        this.grade = grade;
        this.school = school;
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGrade() {
        return grade;
    }

    public String getSchool() {
        return school;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}
