package com.luxoft.horizon.dir.widget;

import com.google.gson.annotations.Expose;

import java.util.*;

/**
 * Created by bogatp on 09.04.16.
 */
public abstract class AbstractChartModel implements IChartDataModel {

    @Expose
    protected List<BarGroupModel> data = new ArrayList<>();

    protected Map<String, List<Double>> dataMap = new LinkedHashMap<>();


    @Expose
    protected List<String> seriesNames;

    @Expose
    protected List<String> colors;

    @Expose
    protected String title;


    public void setLabels(String[] labels) {
        this.seriesNames = new ArrayList<>(Arrays.asList(labels));
    }

    public void setLabels(List<String> labels) {
        seriesNames = new LinkedList<>();
        seriesNames.addAll(labels);
    }

    public List<BarGroupModel> getData() {
        return data;
    }

    public void setData(List<BarGroupModel> data) {
        this.data = data;
    }


    public Map<String, List<Double>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, List<Double>> dataMap) {
        this.dataMap = dataMap;
    }


    public List<String> getSeriesNames() {
        return seriesNames;
    }

    public void setSeriesNames(List<String> seriesNames) {
        this.seriesNames = seriesNames;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public void setColors(String[] colors) {
        this.colors = new ArrayList<>(Arrays.asList(colors));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void addData(String groupName, List<Double> values) {
        data.add(new BarGroupModel(groupName, values));
    }

    public void convert() {
        data = new ArrayList<>();
        for (String group : dataMap.keySet()) {
            addData(group, dataMap.get(group));
        }
//        setColors(ColorUtils.getDifferentColors(20));
    }

    public void addData(String groupName, Double value) {
        List<Double> list = null;
        if (dataMap.containsKey(groupName)) {
            list = dataMap.get(groupName);
        } else {
            list = new LinkedList<Double>();
            dataMap.put(groupName, list);
        }
        list.add(value);
    }


    class BarGroupModel {
        @Expose
        private String groupName;
        @Expose
        private List<Double> values;

        public BarGroupModel(String groupName, List<Double> values) {
            this.groupName = groupName;
            this.values = values;
        }

        public String getGroupName() {
            return groupName;
        }

        public List<Double> getValues() {
            return values;
        }
    }
}
