package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.domain.IViewFourPracticesService;
import com.luxoft.horizon.dir.service.domain.KPIRepository;
import com.luxoft.horizon.dir.utils.Budget;
import com.luxoft.horizon.dir.utils.RevenueCM;
import com.luxoft.horizon.dir.utils.Utils;
import com.luxoft.horizon.dir.widget.BarChartDataModel;
import com.luxoft.horizon.dir.widget.LineChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author rlapin
 */
@Service
public class ViewFourPracticesServiceImpl implements IViewFourPracticesService {


    private static final List<String> CHART_ORDER = Collections.unmodifiableList(Arrays.asList(Budget.PFACT, Budget.PLAN, Budget.PF, Budget.FF));
    ;
    @Autowired
    KPIRepository repository;

    @Override
    public Object getDataForFourPractices() {
        Map<String, Object> data = new HashMap<>();
        Object[] fourPracticesData = repository.findFourPracticesData();
        Map<String, Map<Budget, RevenueCM>> values = getKpiValues(fourPracticesData);


        values.entrySet().forEach((entrySet) -> {
            Map<Budget, RevenueCM> value = entrySet.getValue();
            Object[] chartData = getChartData(value);
            data.put(entrySet.getKey(), chartData);
        });

        return data;
    }

    /**
     * Get revenueData, cmData and deviations for practice
     *
     * @param value
     * @return
     */
    private Object[] getChartData(Map<Budget, RevenueCM> value) {
        Object[] chartData = new Object[3];
        BarChartDataModel revenueModel = new BarChartDataModel();
        BarChartDataModel cmModel = new BarChartDataModel();
        List<Map<String,Object>> lines = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        List<Double> cmOfRevenuePercents = new ArrayList<>();
        Object deviations = getDeviationsForPractice(value);
        chartData[0] = revenueModel;
        chartData[1] = cmModel;
        chartData[2] = deviations;
        value.entrySet().forEach((entrySet) -> {
            revenueModel.addData(entrySet.getKey().getCaption(), entrySet.getValue().revenue);
            cmModel.addData(entrySet.getKey().getCaption(),entrySet.getValue().cm);
            cmOfRevenuePercents.add(Utils.getSafePercent(entrySet.getValue().cm,entrySet.getValue().revenue));

        });
        map.put("values",cmOfRevenuePercents);
        map.put("name","CM, %");
        lines.add(map);
        cmModel.addMetaData("lines", lines);
        revenueModel.setLabels(Collections.singletonList("Revenue"));
        cmModel.setLabels(Collections.singletonList("CM"));
        revenueModel.convert();
        cmModel.convert();
        return chartData;
    }

    private Object getDeviationsForPractice(Map<Budget, RevenueCM> value) {
        List<List<Double>> result = new ArrayList<>();
        RevenueCM ffValue = value.get(new Budget(Budget.FF));
        RevenueCM planValue = value.get(new Budget(Budget.PLAN));
        RevenueCM pfValue = value.get(new Budget(Budget.PF));

        List<Double> resultLane = new ArrayList<>();
        resultLane.add(ffValue.revenue - planValue.revenue);
        resultLane.add(ffValue.revenue - pfValue.revenue);
        result.add(resultLane);

        resultLane = new ArrayList<>();
        resultLane.add(ffValue.cm - planValue.cm);
        resultLane.add(ffValue.cm - pfValue.cm);
        result.add(resultLane);

        resultLane = new ArrayList<>();
        resultLane.add(Utils.getSafePercent(ffValue.cm,ffValue.revenue) - Utils.getSafePercent(planValue.cm,planValue.revenue));
        resultLane.add(Utils.getSafePercent(ffValue.cm, ffValue.revenue) - Utils.getSafePercent(pfValue.cm, pfValue.revenue));

        result.add(resultLane);
        return result;
    }

    private Map<String, Map<Budget, RevenueCM>> getKpiValues(Object[] fourPracticesData) {
        Map<String, Map<Budget, RevenueCM>> values = new LinkedHashMap<>();
        for (Object obj : fourPracticesData) {
            if (obj instanceof Object[]) {
                Object[] objValues = (Object[]) obj;
                if (objValues.length > 5) {
                    String kpiType = (String) objValues[0];
                    Map<Budget, RevenueCM> kpiValues = values.getOrDefault(kpiType, new TreeMap<>(this::compare));
                    Budget budget = new Budget((String) objValues[3]);
                    budget.setYear((Integer) objValues[4]);
                    kpiValues.put(budget, new RevenueCM(((BigDecimal) objValues[2]).doubleValue(), ((BigDecimal) objValues[1]).doubleValue()));
                    values.putIfAbsent(kpiType, kpiValues);
                }
            }
        }
        return values;
    }

    private int compare(Budget a, Budget b) {
        return CHART_ORDER.indexOf(a.getBudget()) - CHART_ORDER.indexOf(b.getBudget());
    }

}
