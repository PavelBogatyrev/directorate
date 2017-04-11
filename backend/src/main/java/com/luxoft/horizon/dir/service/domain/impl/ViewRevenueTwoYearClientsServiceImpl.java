package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.domain.IViewRevenueTwoYearClientsService;
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
public class ViewRevenueTwoYearClientsServiceImpl implements IViewRevenueTwoYearClientsService {
    public static final BigDecimal DEFAULT_VALUE = BigDecimal.valueOf(0);

    @Autowired
    KPIRepository repository;
    public static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#.#");

    @Override
    public List<BarChartDataModel> getClientsRevenue() {
        List<BarChartDataModel> models = new ArrayList<>();
        BarChartDataModel cmModel = new BarChartDataModel();
        BarChartDataModel revenueModel = new BarChartDataModel();
        Object[] currentData = repository.findClientsRevenue();

        List<Map<String, Object>> lines = new ArrayList<>();
        List<Double> prevRevenuePercents = new ArrayList<>();
        List<Double> curRevenuePercents = new ArrayList<>();

        for (Object current : currentData) {
            if (current instanceof Object[]) {
                Object[] currentArr = (Object[]) current;
                if (currentArr.length == 7) {
                    double cmValueCur = Utils.defaultIfNull((BigDecimal) currentArr[3], DEFAULT_VALUE).doubleValue();
                    double cmValuePrev = Utils.defaultIfNull((BigDecimal) currentArr[4], DEFAULT_VALUE).doubleValue();
                    double revenueValueCur = Utils.defaultIfNull((BigDecimal) currentArr[1], DEFAULT_VALUE).doubleValue();
                    double revenueValuePrev = Utils.defaultIfNull((BigDecimal) currentArr[2], DEFAULT_VALUE).doubleValue();
                    double percent1 = Utils.getSafePercent(cmValueCur - cmValuePrev, cmValuePrev);
                    double percent2 = Utils.getSafePercent(revenueValueCur - revenueValuePrev, revenueValuePrev);
                    double prevPercent = Utils.getSafePercent(cmValuePrev, revenueValuePrev);
                    double curPercent = Utils.getSafePercent(cmValueCur, revenueValueCur);
                    String group1;
                    String group2;
                    if (percent1 > 0) {
                        group1 = currentArr[0] + "<br>(+" + PERCENT_FORMAT.format(curPercent-prevPercent) + "%){{up}}";
                    } else {
                        group1 = currentArr[0] + "<br>(" + PERCENT_FORMAT.format(curPercent-prevPercent) + "%){{down}}";
                    }
                    if (percent2 > 0) {
                        group2 = currentArr[0] + "<br>(+" + PERCENT_FORMAT.format(percent2) + "%){{up}}";
                    } else {
                        group2 = currentArr[0] + "<br>(" + PERCENT_FORMAT.format(percent2) + "%){{down}}";
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
            int curYear = (int) ((Object[]) currentData[0])[5];
            int prevYear = (int) ((Object[]) currentData[0])[6];
            Map<String, Object> map = new HashMap<>();
            map.put("values", prevRevenuePercents);
            map.put("name", "CM% FY" + prevYear);
            lines.add(map);
            map = new HashMap<>();
            map.put("values", curRevenuePercents);
            map.put("name", "CM% FY" + curYear);
            lines.add(map);
            cmModel.setSeriesNames(Arrays.asList("CM FY" + prevYear, "CM FY" + curYear));

            revenueModel.setSeriesNames(Arrays.asList("Revenue FY" + prevYear, "Revenue FY" + curYear));
        }
        cmModel.addMetaData("lines", lines);
        revenueModel.convert();
        cmModel.convert();
        models.add(revenueModel);
        models.add(cmModel);

        return models;
    }

}
