package com.luxoft.horizon.dir.widget;

import com.google.gson.annotations.Expose;

/**
 * @author rlapin
 */
public class TableModel {

    @Expose
    private String [][]data;

    public void setData(String[][] data) {
        this.data = data;
    }
}
