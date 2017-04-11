package com.luxoft.horizon.dir;

/**
 * Created by pavelbogatyrev on 14/04/16.
 */
public class PrintPage {

    private String period;

    private String view;

    public PrintPage() {
    }


    public PrintPage(String period, String view) {
        this.period = period;
        this.view = view;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrintPage printPage = (PrintPage) o;

        if (period != null ? !period.equals(printPage.period) : printPage.period != null) return false;
        return !(view != null ? !view.equals(printPage.view) : printPage.view != null);

    }

    @Override
    public int hashCode() {
        int result = period != null ? period.hashCode() : 0;
        result = 31 * result + (view != null ? view.hashCode() : 0);
        return result;
    }
}

