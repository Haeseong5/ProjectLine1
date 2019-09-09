package com.example.haeseong.projectline1.data;

import java.util.ArrayList;

public class MenuResult {
    ArrayList<Menu> menu = new ArrayList<>();
    ArrayList<String> server_message;

    public ArrayList<Menu> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }

    public ArrayList<String> getServer_message() {
        return server_message;
    }

    public void setServer_message(ArrayList<String> server_message) {
        this.server_message = server_message;
    }

    @Override
    public String toString() {
        return "MenuResult{" +
                "menu=" + menu +
                ", server_message=" + server_message +
                '}';
    }
}
