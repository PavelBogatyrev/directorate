package com.luxoft.horizon.dir.widget;

import com.google.gson.annotations.Expose;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by bogatp on 18.04.16.
 */
public class MapChartModel {

    @Expose
    List<MapData> values;

    public MapChartModel() {
        values = new LinkedList<>();
    }

    public void addData(int id, String location, Double value) {
        values.add(new MapData(id, location, value));
    }


    public List<MapData> getValues() {
        return values;
    }

}
