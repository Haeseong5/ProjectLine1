package com.example.haeseong.projectline1.item;

public class ProfileItem {
    String text;
    int icon;
    String text2;

    public ProfileItem(String text, int icon, String text2) {
        this.text = text;
        this.icon = icon;
        this.text2 = text2;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }
}
