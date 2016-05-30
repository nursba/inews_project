package com.example.nursul.myapplication;

/**
 * Created by Nursul on 29.05.2016.
 */
import java.io.Serializable;

public class NewsBean implements Serializable {

    private String name;
    private String desc;
    private String date;
    private String feed;

    public NewsBean() {

    }
    public NewsBean(String name, String desc,String date,String feed) {
        this.name = name;
        this.desc = desc;
        this.date = date;
        this.feed = feed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String name) {
        this.date = date;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }


}