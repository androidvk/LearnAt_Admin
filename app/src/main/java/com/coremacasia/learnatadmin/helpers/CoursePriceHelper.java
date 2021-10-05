package com.coremacasia.learnatadmin.helpers;

public class CoursePriceHelper {
    private String duration_unit, price, duration;

    public String getDuration_unit() {
        return duration_unit;
    }

    public String getPrice() {
        return price;
    }

    public String getDuration() {
        return duration;
    }

    public CoursePriceHelper(String duration_unit, String price, String duration) {
        this.duration_unit = duration_unit;
        this.price = price;
        this.duration = duration;
    }
}
