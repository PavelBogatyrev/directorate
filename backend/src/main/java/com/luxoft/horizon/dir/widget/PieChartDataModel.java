package com.luxoft.horizon.dir.widget;

import com.google.gson.annotations.Expose;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by bogatp on 23.03.16.
 */
public class PieChartDataModel extends AbstractChartModel implements IChartDataModel{

    @Expose
    protected Map<String, Object> metaData = new LinkedHashMap<>();

    public void addMetadata(String name, Object value) {
        metaData.put(name, value);
    }

    public Map<String, Object> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, Object> metaData) {
        this.metaData = metaData;
    }
}
