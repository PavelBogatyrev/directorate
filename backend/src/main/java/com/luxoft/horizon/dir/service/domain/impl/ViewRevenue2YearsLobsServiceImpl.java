package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.domain.IViewRevenue2YearsLobsService;
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
public class ViewRevenue2YearsLobsServiceImpl implements IViewRevenue2YearsLobsService {

    public static final int CURRENT_FY = Utils.currentFY();
    private static final int FIELD_SORT_ORDER_NUM = 0;
    private static final int FIELD_LOB_NUM = 1;
    private static final int FIELD_REVENUE_NUM = 2;
    private static final int FIELD_CM_NUM = 3;
    private static final int FIELD_BUDGET_NUM = 4;
    private static final int FIELD_YEAR_NUM = 5;

    private static final String HTML_LINE_BREAK = "<br>";
    private static final String REVENUE_SERIE_NAME_PREFIX = "Revenue FY";
    private static final String CM_SERIE_NAME_PREFIX = "CM FY";
    private static final String UP_MARKUP = "{{up}}";
    private static final String DOWN_MARKUP = "{{down}}";



    @Autowired
    KPIRepository repository;


    /**
     * 2-years Revenue comparison model for LOBs with relatively small values of revenue
     * @return model
     */

    @Override
    public BarChartDataModel getRevenueByLobs2YearsSmall() {
        return getLOBRevenue2YearsModel(repository.findKpiByLobsRevenueCMForecastCurrentYearSmall(), repository.findKpiByLobsRevenueCMFactPreviousYearSmall());
    }

    /**
     * 2-years Revenue comparison model for LOBs with relatively large values of revenue
     * @return model
     */

    @Override
    public BarChartDataModel getRevenueByLobs2YearsBig() {
        return getLOBRevenue2YearsModel(repository.findKpiByLobsRevenueCMForecastCurrentYearBig(), repository.findKpiByLobsRevenueCMFactPreviousYearBig());
    }


    /**
     * Create Revenue comparison model by given report data
     * @param currentYearData  -  current year report data
     * @param previousYearData - previous year report data
     * @return
     */

    private BarChartDataModel getLOBRevenue2YearsModel(Object[][] currentYearData, Object[][] previousYearData) {
        BarChartDataModel model = new BarChartDataModel();
        List<String> seriesNames = new ArrayList<>();
        seriesNames.add(REVENUE_SERIE_NAME_PREFIX + (CURRENT_FY - 1));
        seriesNames.add(REVENUE_SERIE_NAME_PREFIX + (CURRENT_FY));
        model.setSeriesNames(seriesNames);
        int groupCount = currentYearData.length;
        for (int i = 0; i < groupCount; i++) {
            Object[] currentYearRecord = currentYearData[i];
            Object[] previousYearRecord = previousYearData[i];
            List<Double> data = new ArrayList<>();
            double currentYearValue = ((BigDecimal) currentYearRecord[FIELD_REVENUE_NUM]).doubleValue();
            double previousYearValue = ((BigDecimal) previousYearRecord[FIELD_REVENUE_NUM]).doubleValue();
            data.add(previousYearValue);
            data.add(currentYearValue);
            String delta = Utils.getOneDecPercentString(Utils.getSafePercent(currentYearValue - previousYearValue, previousYearValue));
            model.addData(currentYearRecord[FIELD_LOB_NUM] + HTML_LINE_BREAK + delta + (previousYearValue > currentYearValue ? DOWN_MARKUP : UP_MARKUP), data);
        }
        return model;
    }


    /**
     * 2-years CM comparison model for LOBs with relatively small values of revenue and CM
     * @return model
     */
    @Override
    public BarChartDataModel getCMByLobs2YearsSmall() {
        return getLobsCM2YearsModel(repository.findKpiByLobsRevenueCMForecastCurrentYearSmall(), repository.findKpiByLobsRevenueCMFactPreviousYearSmall());
    }

    /**
     * 2-years CM comparison model for LOBs with relatively big values of revenue and CM
     * @return model
     */

    @Override
    public BarChartDataModel getCMByLobs2YearsBig() {
        return getLobsCM2YearsModel(repository.findKpiByLobsRevenueCMForecastCurrentYearBig(), repository.findKpiByLobsRevenueCMFactPreviousYearBig());
    }


    /**
     * Create CM comparison model by given report data
     * @param currentYearData  -  current year report data
     * @param previousYearData - previous year report data
     * @return
     */

    private BarChartDataModel getLobsCM2YearsModel(Object[][] currentYearData, Object[][] previousYearData) {
        BarChartDataModel model = new BarChartDataModel();
        List<String> seriesNames = new ArrayList<>();
        seriesNames.add(CM_SERIE_NAME_PREFIX + (CURRENT_FY - 1));
        seriesNames.add(CM_SERIE_NAME_PREFIX + (CURRENT_FY));
        model.setSeriesNames(seriesNames);

        int groupCount = currentYearData.length;
        List<Map<String, Object>> cmPercentLines = new ArrayList<>();

        Map<String, Object> map = new LinkedHashMap<>();
        List<Double> cmPreviousPercentLine = new ArrayList<>();
        map.put("values", cmPreviousPercentLine);
        map.put("name", CM_SERIE_NAME_PREFIX + (CURRENT_FY - 1));
        cmPercentLines.add(map);


        map = new LinkedHashMap<>();
        List<Double> cmCurrentPercentLine = new ArrayList<>();
        map.put("values", cmCurrentPercentLine);
        map.put("name", CM_SERIE_NAME_PREFIX + CURRENT_FY);
        cmPercentLines.add(map);

        for (int i = 0; i < groupCount; i++) {
            Object[] currentYearRecord = currentYearData[i];
            Object[] previousYearRecord = previousYearData[i];
            List<Double> data = new ArrayList<>();
            double currentYearCM = ((BigDecimal) currentYearRecord[FIELD_CM_NUM]).doubleValue();
            double previousYearCM = ((BigDecimal) previousYearRecord[FIELD_CM_NUM]).doubleValue();
            double currentYearRevenue = ((BigDecimal) currentYearRecord[FIELD_REVENUE_NUM]).doubleValue();
            double previousYearRevenue = ((BigDecimal) previousYearRecord[FIELD_REVENUE_NUM]).doubleValue();
            double currentCMPercent = Utils.getSafePercent(currentYearCM, currentYearRevenue);
            double previousCMPercent = Utils.getSafePercent(previousYearCM, previousYearRevenue);
            cmPreviousPercentLine.add(previousCMPercent);
            cmCurrentPercentLine.add(currentCMPercent);
            data.add(previousYearCM);
            data.add(currentYearCM);
            String cmPercentDelta = Utils.getOneDecPercentString(currentCMPercent - previousCMPercent);
            String triangleMurkup = (previousCMPercent > currentCMPercent ? DOWN_MARKUP : UP_MARKUP);
            model.addData(currentYearRecord[FIELD_LOB_NUM] + HTML_LINE_BREAK + cmPercentDelta + triangleMurkup, data);
        }
        model.addMetaData("lines", cmPercentLines);
        return model;
    }


}
