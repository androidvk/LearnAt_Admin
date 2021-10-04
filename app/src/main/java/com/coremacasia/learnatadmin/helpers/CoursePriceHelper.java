package com.coremacasia.learnatadmin.helpers;

public class CoursePriceHelper {
    public int getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public String getPrice_unit() {
        return price_unit;
    }

    public String getDuration_unit() {
        return duration_unit;
    }

    private int price,duration;
    private String price_unit,duration_unit;
}
