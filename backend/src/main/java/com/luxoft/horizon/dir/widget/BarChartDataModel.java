package com.luxoft.horizon.dir.widget;

import com.google.gson.annotations.Expose;

import java.util.*;

/**
 * Created by bogatp on 23.03.16.
 */
public class BarChartDataModel extends AbstractChartModel implements IChartDataModel {


    @Expose
    protected Map<String, Object> metaData = new LinkedHashMap<>();

    public void addMetaData(String name, Object value) {
        metaData.put(name, value);
    }


    public Map<String, Object> getMetaData() {
        return metaData;
    }
}
