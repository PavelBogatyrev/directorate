package com.luxoft.horizon.dir.widget;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rlapin
 */
public class LineChartModel implements IChartDataModel{
    @Expose
    private List<DataSet> data = new ArrayList<>();

    @Expose
    private List<String> seriesNames = new ArrayList<>();


    @Override
    public void addData(String groupName, Double value) {
        DataSet dataSet = data.stream().filter((v)->v.groupName.equals(groupName)).findFirst().orElse(null);
        if(dataSet==null) {
            dataSet = new DataSet();
            dataSet.groupName = groupName;
            data.add(dataSet);
        }
        dataSet.values.add(value);
    }

    @Override
    public void setLabels(List<String> labels) {
        seriesNames.clear();
        seriesNames.addAll(labels);
    }

    private class DataSet {
        @Expose
        List<Double> values = new ArrayList<>();
        @Expose
        String groupName;


    }
}
