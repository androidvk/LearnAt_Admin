package com.coremacasia.learnatadmin.menus.category;

public class CategoryHelper {
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    private String id;
    private String name;
    private String thumbnail;

    public CategoryHelper(String id, String name, String thumbnail, String description) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public CategoryHelper() {
    }

    private String description;

    @Override
    public String toString(){
        return name;
    }

}
