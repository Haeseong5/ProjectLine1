package com.example.haeseong.projectline1.helper;

public class GlobalUser {
    private static GlobalUser instance = null;
    private String jwt;
    private String myId;
    private boolean isLogin;
    private String pushToken;
    private int heart;
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

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String myId() {
        return myId;
    }

    public void myId(String myId) {
        this.myId = myId;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    @Override
    public String toString() {
        return "GlobalUser{" +
                "jwt='" + jwt + '\'' +
                ", my_id='" + myId + '\'' +
                '}';
    }
}
