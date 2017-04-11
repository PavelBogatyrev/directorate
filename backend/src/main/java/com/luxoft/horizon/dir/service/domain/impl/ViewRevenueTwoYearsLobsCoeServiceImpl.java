package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.domain.IViewRevenueTwoYearsLobsCoeService;
import com.luxoft.horizon.dir.service.domain.KPIRepository;
import com.luxoft.horizon.dir.utils.Utils;
import com.luxoft.horizon.dir.widget.BarChartDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author rlapin
 */
@Service
public class ViewRevenueTwoYearsLobsCoeServiceImpl implements IViewRevenueTwoYearsLobsCoeService {
    public static final BigDecimal DEFAULT_VALUE = BigDecimal.valueOf(0);
    public static final String COE = "CoE";

    @Autowired
    KPIRepository repository;
    public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#.#");
    private class KPIYear {
        private final String kpiType;
        private double revenue;
        private double cm;
        private final int year;

        public KPIYear(String kpiType, double revenue, double cm, int year) {
            this.kpiType = kpiType;
            this.revenue = revenue;
            this.cm = cm;
            this.year = year;
        }

    }

    public List<BarChartDataModel> getRightLobsRevenue(Object[] currentData, Object[] previousData) {
        List<BarChartDataModel> models = new ArrayList<>();
        BarChartDataModel cmModel = new BarChartDataModel();
        BarChartDataModel revenueModel = new BarChartDataModel();

        List<Map<String, Object>> lines = new ArrayList<>();
        List<Double> prevRevenuePercents = new ArrayList<>();
        List<Double> curRevenuePercents = new ArrayList<>();
        if (currentData.length == previousData.length) {
            for (int i = 0; i < currentData.length; i++) {
                Object current = currentData[i];
                Object previous = previousData[i];
                if (current instanceof Object[] && previous instanceof Object[]) {
                    Object[] currentArr = (Object[]) current;
                    Object[] previousArr = (Object[]) previous;
                    if (currentArr.length == 6 && previousArr.length == 6) {
                        double cmValueCur = Utils.defaultIfNull((BigDecimal) currentArr[3], DEFAULT_VALUE).doubleValue();
                        double cmValuePrev = Utils.defaultIfNull((BigDecimal) previousArr[3], DEFAULT_VALUE).doubleValue();
                        double revenueValueCur = Utils.defaultIfNull((BigDecimal) currentArr[2], DEFAULT_VALUE).doubleValue();
                        double revenueValuePrev = Utils.defaultIfNull((BigDecimal) previousArr[2], DEFAULT_VALUE).doubleValue();
                        double percent1 = Utils.getSafePercent(cmValueCur - cmValuePrev, cmValuePrev);
                        double percent2 = Utils.getSafePercent(revenueValueCur - revenueValuePrev, revenueValuePrev);
                        double prevPercent = Utils.getSafePercent(cmValuePrev, revenueValuePrev);
                        double curPercent = Utils.getSafePercent(cmValueCur, revenueValueCur);
                        String group1;
                        String group2;
                        if (percent1 > 0) {
                            group1 = currentArr[1] + "<br>(+" + PERCENT_FORMAT.format(curPercent - prevPercent) + "%){{up}}";
                        } else {
                            group1 = currentArr[1] + "<br>(" + PERCENT_FORMAT.format(curPercent - prevPercent) + "%){{down}}";
                        }
                        if (percent2 > 0) {
                            group2 = currentArr[1] + "<br>(+" + PERCENT_FORMAT.format(percent2) + "%){{up}}";
                        } else {
                            group2 = currentArr[1] + "<br>(" + PERCENT_FORMAT.format(percent2) + "%){{down}}";
                        }

                        cmModel.addData(group1, cmValuePrev);
                        cmModel.addData(group1, cmValueCur);
                        revenueModel.addData(group2, revenueValuePrev);
                        revenueModel.addData(group2, revenueValueCur);
                        prevRevenuePercents.add(prevPercent);

                        curRevenuePercents.add(curPercent);
                    }

                }
            }
            if (currentData.length != 0) {
                int year = (int) ((Object[]) currentData[0])[5];
                Map<String, Object> map = new HashMap<>();
                map.put("values", prevRevenuePercents);
                int prevYear = year - 1;
                map.put("name", "CM% FY" + prevYear);
                lines.add(map);
                map = new HashMap<>();
                map.put("values", curRevenuePercents);
                map.put("name", "CM% FY" + year);
                lines.add(map);
                cmModel.setSeriesNames(Arrays.asList("CM FY" + prevYear, "CM FY" + year));
                cmModel.setSeriesNames(Arrays.asList("CM% FY" + prevYear, "CM% FY" + year));

                revenueModel.setSeriesNames(Arrays.asList("Revenue FY" + prevYear, "Revenue FY" + year));
            }
        }

        cmModel.addMetaData("lines", lines);
        revenueModel.convert();
        cmModel.convert();
        models.add(revenueModel);
        models.add(cmModel);

        return models;
    }

    private List<BarChartDataModel> getLeftLobsRevenue(Object[] currentData, Object[] previousData) {
        List<BarChartDataModel> models = new ArrayList<>();
        BarChartDataModel cmModel = new BarChartDataModel();
        BarChartDataModel revenueModel = new BarChartDataModel();

        List<Map<String, Object>> lines = new ArrayList<>();
        List<Double> prevRevenuePercents = new ArrayList<>();
        List<Double> curRevenuePercents = new ArrayList<>();

        Map<String, List<KPIYear>> currentValues = getValuesGroupedByLob(currentData);
        Map<String, List<KPIYear>> prevValues = getValuesGroupedByLob(previousData);
        int year = 0;
        int prevYear = 0;
        for (Map.Entry<String, List<KPIYear>> current : currentValues.entrySet()) {
            List<KPIYear> previous = prevValues.get(current.getKey());
            String groupName = current.getKey();
            if(year==0) {
                year = getYear(current.getValue());
            }
            if(prevYear == 0){
                prevYear = getYear(previous);
            }
            KPIYear curSumKPI = new KPIYear("", 0,0,0);
            KPIYear prevSumKPI = new KPIYear("",0,0,0);

            current.getValue().forEach((val)->{
                        curSumKPI.revenue+=val.revenue;
                        curSumKPI.cm+=val.cm;
                    }
            );
            previous.forEach((val) -> {
                        prevSumKPI.revenue += val.revenue;
                        prevSumKPI.cm += val.cm;
                    }
            );

            double percent1 = Utils.getSafePercent(curSumKPI.cm - prevSumKPI.cm, prevSumKPI.cm);
            double percent2 = Utils.getSafePercent(curSumKPI.revenue - prevSumKPI.revenue, prevSumKPI.revenue);
            double prevPercent = Utils.getSafePercent(prevSumKPI.cm, prevSumKPI.revenue);
            double curPercent = Utils.getSafePercent(curSumKPI.cm , curSumKPI.revenue);
            String group1;
            String group2;
            if (percent1 > 0) {
                group1 = groupName + "<br>(+" + PERCENT_FORMAT.format(percent1) + "%){{up}}";
            } else {
                group1 = groupName + "<br>(" + PERCENT_FORMAT.format(percent1) + "%){{down}}";
            }
            if (percent2 > 0) {
                group2 = groupName + "<br>(+" + PERCENT_FORMAT.format(percent2) + "%){{up}}";
            } else {
                group2 = groupName + "<br>(" + PERCENT_FORMAT.format(percent2) + "%){{down}}";
            }

            current.getValue().stream().sorted((a, b) -> COE.equals(a.kpiType) ? 1 : -1).forEach((val) -> {
                        revenueModel.addData(group1, val.revenue);
                        cmModel.addData(group2, val.cm);

                    }
            );
            previous.stream().sorted((a, b) -> COE.equals(a.kpiType) ? 1 : -1).forEach((val) -> {
                        revenueModel.addData(group1 + " ", val.revenue);
                        cmModel.addData(group2 + " ", val.cm);
                        prevSumKPI.revenue += val.revenue;
                        prevSumKPI.cm+=val.cm;
                    }
            );
            prevRevenuePercents.add(prevPercent);
            curRevenuePercents.add(curPercent);

        }
        if (currentData.length != 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("values", prevRevenuePercents);
            map.put("name", "CM% FY" + prevYear);
            lines.add(map);
            map = new HashMap<>();
            map.put("values", curRevenuePercents);
            map.put("name", "CM% FY" + year);
            lines.add(map);
        }
        cmModel.setSeriesNames(Arrays.asList("CM FY" + prevYear, "CM FY" + year));
        revenueModel.setSeriesNames(Arrays.asList("Revenue FY" + prevYear, "Revenue FY" + year));
        cmModel.addMetaData("lines", lines);
        revenueModel.convert();
        cmModel.convert();
        models.add(revenueModel);
        models.add(cmModel);
        return models;
    }

    private int getYear(List<KPIYear> current) {
        return current.stream().findAny().orElseGet(() -> new KPIYear("", 0, 0, 0)).year;
    }

    private Map<String, List<KPIYear>> getValuesGroupedByLob(Object[] data) {
        Map<String, List<KPIYear>> result = new LinkedHashMap<>();
        for (Object aCurrentData : data) {
            Object[] currentValues = (Object[]) aCurrentData;
            String key = (String) currentValues[1];
            List<KPIYear> kpis;
            if ((kpis = result.get(key)) == null) {
                kpis = new ArrayList<>();
                result.put(key, kpis);
            }
            kpis.add(new KPIYear((String) currentValues[2], Utils.defaultIfNull((BigDecimal) currentValues[3], DEFAULT_VALUE).doubleValue(), Utils.defaultIfNull((BigDecimal) currentValues[4], DEFAULT_VALUE).doubleValue(), (Integer) currentValues[6]));
        }
        return result;
    }

    @Override
    public List<BarChartDataModel> getRevenueCoeByLobsTwoYearsLeft() {
        Object[] previousData = repository.findLobsRevenueCMCoePreviousYearLeft();
        Object[] currentData = repository.findLobsRevenueCMCoeCurrentYearLeft();
        return getLeftLobsRevenue(currentData, previousData);
    }


    @Override
    public List<BarChartDataModel> getRevenueCoeByLobsTwoYearsRight() {
        Object[]  previousData = repository.findLobsRevenueCMCoePreviousYearRight();
        Object[] currentData = repository.findLobsRevenueCMCoeCurrentYearRight();
        return getRightLobsRevenue(currentData, previousData);
    }


}
