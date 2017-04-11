package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.domain.IViewRevenue2YearsLobsService;
import com.luxoft.horizon.dir.service.domain.IViewRevenueClientsPlService;
import com.luxoft.horizon.dir.service.domain.KPIRepository;
import com.luxoft.horizon.dir.utils.Utils;
import com.luxoft.horizon.dir.widget.BarChartDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ygorenburgov on 09.06.16.
 */
@Service
public class ViewRevenueClientsPlServiceImpl  implements IViewRevenueClientsPlService {

    public static final int CURRENT_FY = Utils.currentFY();
    private static final int FIELD_CLIENT_NUM = 0;
    private static final int FIELD_REVENUE_NUM = 1;
    private static final int FIELD_REVENUE_PLAN_NUM = 2;
    private static final int FIELD_CM_NUM = 3;
    private static final int FIELD_CM_PLAN_NUM = 4;


    private static final String HTML_LINE_BREAK = "<br>";
    private static final String REVENUE_SERIE_NAME_PREFIX = "Revenue FY";
    private static final String CM_SERIE_NAME_PREFIX = "CM FY";
    private static final String UP_MARKUP = "{{up}}";
    private static final String DOWN_MARKUP = "{{down}}";



    @Autowired
    KPIRepository repository;


    /**
     * Client revenue comparison model between current year plan and forecast
     * @return model
     */

    @Override
    public BarChartDataModel getRevenueByClientsPl(){

        Object [][] data = repository.findKpiRevenueCMCurrentYear();
        BarChartDataModel model = new BarChartDataModel();
        List<String> seriesNames = new ArrayList<>();
        seriesNames.add(REVENUE_SERIE_NAME_PREFIX + CURRENT_FY + " Pl" );
        seriesNames.add(REVENUE_SERIE_NAME_PREFIX + CURRENT_FY);
        model.setSeriesNames(seriesNames);
        Map<String,List<Double>> dataMap = new LinkedHashMap<>();

        for (int i = 0; i<data.length; i++) {
            List<Double> newGroup = new ArrayList<>();
            Object [] record = data[i];
            String client = (String)record[FIELD_CLIENT_NUM];
            Double planValue = ((BigDecimal)record[FIELD_REVENUE_PLAN_NUM]).doubleValue();
            Double forecastValue = ((BigDecimal)record[FIELD_REVENUE_NUM]).doubleValue();
            String deltaPercentValue = Utils.getOneDecPercentString(Utils.getSafePercent(forecastValue - planValue, planValue));
            String triangleMarkup = (planValue > forecastValue ? DOWN_MARKUP : UP_MARKUP);
            String group = client + HTML_LINE_BREAK+deltaPercentValue+triangleMarkup;
            newGroup.add(planValue);
            newGroup.add(forecastValue);
            dataMap.put(group,newGroup);
        }
        model.setDataMap(dataMap);
        model.convert();
        return model;
    }

    /**
     *  Client CM comparison model between current year plan and forecast
     * @return model
     */

    @Override
    public BarChartDataModel getCMByClientsPl() {
        Object [][] data = repository.findKpiRevenueCMCurrentYear();
        BarChartDataModel model = new BarChartDataModel();
        List<String> seriesNames = new ArrayList<>();
        seriesNames.add(CM_SERIE_NAME_PREFIX + CURRENT_FY + " Pl" );
        seriesNames.add(CM_SERIE_NAME_PREFIX + CURRENT_FY);
        model.setSeriesNames(seriesNames);
        Map<String,List<Double>> dataMap = new LinkedHashMap<>();

        List<Map<String, Object>> cmPercentLines = new ArrayList<>();
        
        Map<String, Object> map = new LinkedHashMap<>();
        List<Double> cmPlanPercentLine = new ArrayList<>();
        map.put("values", cmPlanPercentLine);
        map.put("name", CM_SERIE_NAME_PREFIX + CURRENT_FY +"Pl");
        cmPercentLines.add(map);


        map = new LinkedHashMap<>();
        List<Double> cmForecastPercentLine = new ArrayList<>();
        map.put("values", cmForecastPercentLine);
        map.put("name", CM_SERIE_NAME_PREFIX + CURRENT_FY);
        cmPercentLines.add(map);

        for (int i = 0; i<data.length; i++) {
            List<Double> newGroup = new ArrayList<>();
            Object [] record = data[i];
            String client = (String)record[FIELD_CLIENT_NUM];
            Double planValue = ((BigDecimal)record[FIELD_CM_PLAN_NUM]).doubleValue();
            Double forecastValue = ((BigDecimal)record[FIELD_CM_NUM]).doubleValue();
            String triangleMarkup = (planValue > forecastValue ? DOWN_MARKUP : UP_MARKUP);
            newGroup.add(planValue);
            newGroup.add(forecastValue);
            Double planRevenue = ((BigDecimal)record[FIELD_REVENUE_PLAN_NUM]).doubleValue();
            Double forecastRevenue = ((BigDecimal)record[FIELD_REVENUE_NUM]).doubleValue();
            Double cmPlanPercent = Utils.getSafePercent(planValue, planRevenue);
            cmPlanPercentLine.add(cmPlanPercent);
            Double cmForecastPercent = Utils.getSafePercent(forecastValue, forecastRevenue);
            cmForecastPercentLine.add(cmForecastPercent);
            String deltaPercentValue = Utils.getOneDecPercentString(cmForecastPercent - cmPlanPercent);
            String group = client + HTML_LINE_BREAK+deltaPercentValue+triangleMarkup;
            dataMap.put(group,newGroup);
        }
        model.setDataMap(dataMap);
        model.addMetaData("lines", cmPercentLines);
        model.convert();
        return model;

    }
}

