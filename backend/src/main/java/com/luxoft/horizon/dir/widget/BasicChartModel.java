package com.luxoft.horizon.dir.widget;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Map;

/**
 * @author rlapin
 */
public class BasicChartModel {
    @Expose
    private List<Double> data;
    @Expose
    private List<String> seriesNames;

    @Expose
    private Map<String,Object> metaData;


    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }

    public List<String> getSeriesNames() {
        return seriesNames;
    }

    public void setSeriesNames(List<String> seriesNames) {
        this.seriesNames = seriesNames;
    }


    public Map<String, Object> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, Object> metaData) {
        this.metaData = metaData;
    }
}
