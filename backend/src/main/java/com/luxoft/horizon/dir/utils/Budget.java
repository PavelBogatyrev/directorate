package com.luxoft.horizon.dir.utils;

import com.luxoft.horizon.dir.service.domain.impl.ViewLobsBaseServiceImpl;

/**
 * @author rlapin
 */
public class Budget {
    public static final String PLAN = "Plan";
    public static final String FF = "FactForecast";
    public static final String PFACT = "Previous FY Fact";
    public static final String PF = "Previous FactForecast";
    public static final String FACT = "Fact";

    private final String budget;
    private int year;
    private int month;

    public Budget(String budget) {
        this.budget = budget;
    }

    public void setYear(int year) {
        this.year = year;
    }


    public String getCaption() {
        String strYear = String.valueOf(year).substring(2);
        String FY = "FY";
        if (month != 0) {
            FY = Utils.getMonthName(month - 1) + "<br> YTD'";
        }
        switch (budget) {
            case FACT:
                return FY + strYear;
            case PF:
                return FY + strYear + " Prev. F";
            case PFACT:
                return FY + strYear;
            case PLAN:
                return FY + strYear + " Pl";
            case FF:
                return FY + strYear + " F";
        }

        return "";
    }

    public String getBudget() {
        return budget;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Budget budget1 = (Budget) o;

        return !(budget != null ? !budget.equals(budget1.budget) : budget1.budget != null);

    }

    @Override
    public int hashCode() {
        return budget != null ? budget.hashCode() : 0;
    }
}
