package com.example.haeseong.projectline1.data;

import java.util.ArrayList;

public class Menu {
    private String date;
    private ArrayList<String> breakfast;
    private ArrayList<String> lunch;
    private ArrayList<String> dinner;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(ArrayList<String> breakfast) {
        this.breakfast = breakfast;
    }

    public ArrayList<String> getLunch() {
        return lunch;
    }

    public void setLunch(ArrayList<String> lunch) {
        this.lunch = lunch;
    }

    public ArrayList<String> getDinner() {
        return dinner;
    }

    public void setDinner(ArrayList<String> dinner) {
        this.dinner = dinner;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "date='" + date + '\'' +
                ", breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                '}';
    }
}
