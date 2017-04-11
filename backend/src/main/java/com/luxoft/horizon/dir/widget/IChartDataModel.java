package com.luxoft.horizon.dir.widget;

import java.util.List;

/**
 * Created by bogatp on 06.04.16.
 */
public interface IChartDataModel {

    void addData(String groupName, Double value);

    void setLabels(List<String> labels);

}
