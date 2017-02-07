package com.bonrita.recyclerviewdemo;


public class ListItem {

    private String heading;
    private String description;
    private String imgaeUrl;

    public ListItem(String heading, String description, String imageUrl) {
        this.heading = heading;
        this.description = description;
        this.imgaeUrl = imageUrl;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public String getImgaeUrl() {
        return imgaeUrl;
    }
}
