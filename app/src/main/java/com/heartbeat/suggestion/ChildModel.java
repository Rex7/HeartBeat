package com.heartbeat.suggestion;

public class ChildModel {
    private String name;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    private int icon;
    public ChildModel(String name,String appSize,String populatrity,int icon){
        this.name=name;

        this.icon=icon;
    }
    public ChildModel(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
