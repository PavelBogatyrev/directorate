package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.domain.IViewRevenueLobsVsPlanService;
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
public class ViewRevenueLobsVsPlanServiceImpl implements IViewRevenueLobsVsPlanService{
    public static final String PLAN_REVENUE_PERCENT = "PLAN_REVENUE_PERCENT";
    public static final String FACT_REVENUE_PERCENT = "FACT_REVENUE_PERCENT";
    public static final String FORECAST_REVENUE_PERCENT = "FORECAST_REVENUE_PERCENT";
    public static final BigDecimal DEFAULT_VALUE = BigDecimal.valueOf(0);

    @Autowired
    KPIRepository repository;
    public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#.#");


    public List<BarChartDataModel> getLobsRevenue(Object[] currentData, Object[] previousData){
        List<BarChartDataModel> models = new ArrayList<>();
        BarChartDataModel cmModel = new BarChartDataModel();
        BarChartDataModel revenueModel = new BarChartDataModel();

        List<Map<String,Object>> lines = new ArrayList<>();
        List<Double> prevRevenuePercents = new ArrayList<>();
        List<Double> curRevenuePercents = new ArrayList<>();
        if(currentData.length == previousData.length){
            for(int i=0;i<currentData.length;i++){
                Object current = currentData[i];
                Object previous = previousData[i];
                if(current instanceof Object[] && previous instanceof Object[]){
                    Object[] currentArr = (Object[]) current;
                    Object[] previousArr = (Object[]) previous;
                    if(currentArr.length == 5 && previousArr.length == 5){
                        double cmValueCur = Utils.defaultIfNull((BigDecimal) currentArr[3], DEFAULT_VALUE).doubleValue();
                        double cmValuePrev = Utils.defaultIfNull((BigDecimal)previousArr[3],DEFAULT_VALUE).doubleValue();
                        double revenueValueCur = Utils.defaultIfNull((BigDecimal)currentArr[2],DEFAULT_VALUE).doubleValue();
                        double revenueValuePrev = Utils.defaultIfNull((BigDecimal)previousArr[2],DEFAULT_VALUE).doubleValue();
                        double percent1 = Utils.getSafePercent(cmValueCur - cmValuePrev, cmValuePrev);
                        double percent2 = Utils.getSafePercent(revenueValueCur - revenueValuePrev, revenueValuePrev);
                        double prevPercent = Utils.getSafePercent(cmValuePrev, revenueValuePrev);
                        double curPercent = Utils.getSafePercent(cmValueCur, revenueValueCur);
                        String group1;
                        String group2;
                        if(percent1 > 0){
                            group1 = currentArr[1] + "<br>(+"+PERCENT_FORMAT.format(curPercent-prevPercent)+"%){{up}}";
                        }else{
                            group1 = currentArr[1] + "<br>(" + PERCENT_FORMAT.format(curPercent-prevPercent)+"%){{down}}";
                        }
                        if(percent2 > 0){
                            group2 = currentArr[1] + "<br>(+"+PERCENT_FORMAT.format(percent2)+"%){{up}}";
                        }else{
                            group2 = currentArr[1] + "<br>(" + PERCENT_FORMAT.format(percent2)+"%){{down}}";
                        }

                        cmModel.addData(group1,cmValuePrev);
                        cmModel.addData(group1,cmValueCur);
                        revenueModel.addData(group2,revenueValuePrev);
                        revenueModel.addData(group2,revenueValueCur);
                        prevRevenuePercents.add(prevPercent);

                        curRevenuePercents.add(curPercent);
                    }

                }
            }
            if(currentData.length !=0){
                int year = (int) ((Object[])currentData[0])[4];
                Map<String,Object> map = new HashMap<>();
                map.put("values",prevRevenuePercents);
                map.put("name", "CM% FY" + year + " Pl");
                lines.add(map);
                map = new HashMap<>();
                map.put("values",curRevenuePercents);
                map.put("name","CM% FY" + year + " F");
                lines.add(map);
                cmModel.setSeriesNames(Arrays.asList("CM FY" + year + "Pl", "CM FY" + year + " F"));
                cmModel.setSeriesNames(Arrays.asList("CM% FY" + year + "Pl","CM% FY" + year + " F"));

                revenueModel.setSeriesNames(Arrays.asList("Revenue FY" + year + " Pl","Revenue FY" + year + " F"));
            }
        }

        cmModel.addMetaData("lines",lines);
        revenueModel.convert();
        cmModel.convert();
        models.add(revenueModel);
        models.add(cmModel);

        return models;
    }

    @Override
    public List<BarChartDataModel> getLobsRevenueRight() {
        Object[] currentData = repository.findLobsRevenueCMFactRight();
        Object[] previousData = repository.findLobsRevenueCMPlanRight();
        return getLobsRevenue(currentData,previousData);
    }
    @Override
    public List<BarChartDataModel> getLobsRevenueLeft() {
        Object[] currentData = repository.findLobsRevenueCMFactLeft();
        Object[] previousData = repository.findLobsRevenueCMPlanLeft();
        return getLobsRevenue(currentData,previousData);
    }
}
