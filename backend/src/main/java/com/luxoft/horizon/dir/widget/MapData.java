package com.luxoft.horizon.dir.widget;

import com.google.gson.annotations.Expose;

/**
 * Created by bogatp on 18.04.16.
 */
public class MapData {
    @Expose
    int id;
    @Expose
    String country;

    @Expose
    Double value;

    public MapData(int id,String country, Double value) {
        this.id = id;
        this.country = country;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
