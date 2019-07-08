package com.example.haeseong.projectline1.helper;

import com.example.haeseong.projectline1.data.Comment;
import com.example.haeseong.projectline1.data.Post;

import java.util.ArrayList;

public class GlobalUser {
    private static GlobalUser instance = null;
    private String name;
    private String nickName;
    private String email;
    private String grade;
    private String school;
    private String photo;
    private String sex;
    private ArrayList<Post> posts;
    private ArrayList<Comment> comments;
    /**
     * 싱글턴, 하나의 객체만을 사용함.
     * @return
     */
    public static GlobalUser getInstance(){
        if(instance == null){
            synchronized (GlobalUser.class){
                if(instance == null){
                    instance = new GlobalUser();
                }
            }
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
