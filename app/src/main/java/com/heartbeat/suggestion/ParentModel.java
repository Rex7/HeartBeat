package com.heartbeat.suggestion;

import java.util.ArrayList;

public class ParentModel {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
    private ArrayList<ChildModel> childs;

    public ParentModel(String title,ArrayList<ChildModel> childModels) {
        this.setChilds(childModels);
        this.setTitle(title);
    }


    public ArrayList<ChildModel> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<ChildModel> childs) {
        this.childs = childs;
    }
}
