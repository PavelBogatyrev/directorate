package com.luxoft.horizon.dir.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by bogatp on 26.03.16.
 */
public class Utils {
    private static final String[] MONTH_NAMES = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static final String FINANCIAL_YEAR = "FY";
    private static final DecimalFormat DEC_FORMAT = new DecimalFormat("#,##0.0");


    public static String getQuarter(String quarter) {
        return quarter.substring(4, quarter.length());
    }

    public static String getQuarterInt(String quarter) {
        return quarter.substring(5, quarter.length());
    }

    public static String getYear(String quarter) {
        return quarter.substring(0, 4);
    }


    //TODO: Generalize
    //TODO: Fix negative period
    public static List<String> getPastQuarters(String currentQuarter, int lastYears) {
        List<String> list = new LinkedList<>();
        int currentYear = Integer.valueOf(getYear(currentQuarter));
        String quarter = getQuarter(currentQuarter);

        if (lastYears == 0) {
            if (quarter.equalsIgnoreCase("Q1")) {
                list.add(currentYear + "Q1");
            } else if (quarter.equalsIgnoreCase("Q2")) {
                list.add(currentYear + "Q1");
                list.add(currentYear + "Q2");
            } else if (quarter.equalsIgnoreCase("Q3")) {
                list.add(currentYear + "Q1");
                list.add(currentYear + "Q2");
                list.add(currentYear + "Q3");
            } else if (quarter.equalsIgnoreCase("Q4")) {
                list.add(currentYear + "Q1");
                list.add(currentYear + "Q2");
                list.add(currentYear + "Q3");
                list.add(currentYear + "Q4");
            }

            return list;
        } else {
            List<String> lastYearQuarters = getQuartersOfYear(currentYear + lastYears);
            list.addAll(lastYearQuarters);
            return list;
        }
    }

    private static List<String> getQuartersOfYear(int year) {
        List<String> list = new LinkedList<>();
        list.add(year + "Q1");
        list.add(year + "Q2");
        list.add(year + "Q3");
        list.add(year + "Q4");
        return list;
    }




    /**
     * get Labels for years starting with year-yearCount and end with year
     * @param year
     * @param yearCount
     */
    public static List<String> getYearLabels(int year, int yearCount) {
        return IntStream.rangeClosed(0, yearCount).mapToObj((yearOffset) -> "FY" + String.valueOf(year - yearCount + yearOffset).substring(2, 4)).collect(Collectors.toList());
    }


    public static <T>T defaultIfNull(List<T[]> values,int firstIndex,int secondIndex, T defaultValue) {
        if(values!=null && values.size()>firstIndex){
            T arr[]= values.get(firstIndex);
            if(arr!=null && arr.length>secondIndex && arr[secondIndex]!=null){
                return arr[secondIndex];
            }else{
                return defaultValue;
            }
        }else{
            return defaultValue;
        }
    }
    public static int currentFY(){
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        if(month>=4){
            year++;
        }
        return year;
    }
    /**
     * Format FYNN from NN
     * @param year
     * @return
     */
    public static String formatYear(int year) {
        return "FY"+String.valueOf(year).substring(2);
    }

    public static String formatMonth(int month) {
        if(month>=1 && month<=12){
            return LocalDate.of(1,month,1).getMonth().toString();
        }
        return IConstants.N_A;
    }

    public static double getSafePercent(double value, double total) {
        if (total == 0) {
            if (value == 0) {
                return 0D;
            } else {
                return 100D;
            }
        } else {
            return value * 100D / total;
        }
    }

    public static <T>T defaultIfNull(T value, T defaultValue) {
        if(value != null){
            return value;
        }else{
            return defaultValue;
        }
    }

    public static String getOneDecPercentString(double v) {
        
        return DEC_FORMAT.format(v) + "%";
    }
    public static String getMonthName(int month) {
        return MONTH_NAMES[month];
    }

    public static Integer[] getMonthAndYearFromString(String activePeriod) {
        return new Integer[]{Integer.valueOf(activePeriod.substring(4)),Integer.valueOf(activePeriod.substring(0, 4))};
    }
}


