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

    public String getThumbnail() {
        return thumbnail;
    }

    public CategoryHelper() {
    }

    private String description;

}
