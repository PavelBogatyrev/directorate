package com.luxoft.horizon.dir;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pavelbogatyrev on 14/04/16.
 */
public class PrintTask {

    private String activePeriod;

    private List<String> pages = new LinkedList<>();

    public PrintTask() {
    }

    public List<String> getPages() {
        return pages;
    }

    public void setPages(List<String> pages) {
        this.pages = pages;
    }

    public String getActivePeriod() {
        return activePeriod;
    }

    public void setActivePeriod(String activePeriod) {
        this.activePeriod = activePeriod;
    }
}
