package com.luxoft.horizon.dir.service.domain.impl;

import com.google.common.collect.Lists;
import com.luxoft.horizon.dir.service.app.IConfigurationService;
import com.luxoft.horizon.dir.service.domain.IViewPracticesService;
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
public class ViewPracticeServiceImpl implements IViewPracticesService {
    private static final List<String> LINE_CHART_ORDER = Collections.unmodifiableList(Arrays.asList(Budget.PFACT, Budget.PLAN, Budget.PF, Budget.FACT, Budget.FF));
    private static final String CURRENCY_ACTUAL = "actual";
    private static final String CURRENCY_PLAN = "plan";
    private static final String LOB = "LOB";
    private static final String COE = "CoE";
    @Autowired
    IConfigurationService config;
    @Autowired
    KPIRepository repository;


    public void addCMDataForBudget(BarChartDataModel model, Budget budget, Map<String, Map<Budget, RevenueCM>> values, List<Double> lineValues) {

        Map<Budget, RevenueCM> map = values.values().stream().findFirst().orElseGet(HashMap::new);
        map.keySet().stream().filter(b -> b.equals(budget)).forEach(b -> {
            model.addData(b.getCaption(), map.get(b).cm);
            lineValues.add(Utils.getSafePercent(map.get(b).cm, map.get(b).revenue));
        });


    }

    /**
     * Calculate deviations using plan and actual values
     *
     * @param values
     * @param planValues
     * @return
     */
    public Object calculateDeviations(Map<String, Map<Budget, RevenueCM>> values, Map<String, Map<Budget, RevenueCM>> planValues) {
        Map<Budget, RevenueCM> budgetRevenueCMMap = values.values().stream().findFirst().orElseGet(HashMap::new);
        Map<Budget, RevenueCM> planBudgetRevenueCMMap = planValues.values().stream().findFirst().orElseGet(HashMap::new);
        RevenueCM revenueCMFF = budgetRevenueCMMap.get(new Budget(Budget.FF));
        RevenueCM revenueCMPL = budgetRevenueCMMap.get(new Budget(Budget.PLAN));
        RevenueCM revenueCMPF = budgetRevenueCMMap.get(new Budget(Budget.PF));
        RevenueCM planRevenueCMFF = planBudgetRevenueCMMap.get(new Budget(Budget.FF));
        RevenueCM planRevenueCMPL = planBudgetRevenueCMMap.get(new Budget(Budget.PLAN));
        RevenueCM planRevenueCMPF = planBudgetRevenueCMMap.get(new Budget(Budget.PF));
        double value;
        List<List<Double>> result = new ArrayList<>();
        List<Double> resultElements = new ArrayList<>();
        //First row
        if(revenueCMPL!=null) {
            value = (revenueCMFF.revenue) - (revenueCMPL.revenue);
            resultElements.add(value);
        }else{
            resultElements.add(0.0);
        }
        value = (revenueCMFF.revenue) - (revenueCMPF.revenue);
        resultElements.add(value);

        if(planRevenueCMPL!=null) {
            value = (planRevenueCMFF.revenue) - (planRevenueCMPL.revenue);
            resultElements.add(value);
        }else{
            resultElements.add(0.0);
        }
        value = (planRevenueCMFF.revenue) - (planRevenueCMPF.revenue);
        resultElements.add(value);

        result.add(resultElements);

        //Second row
        resultElements = new ArrayList<>();
        if(revenueCMPL!=null) {
            value = (revenueCMFF.cm) - (revenueCMPL.cm);
            resultElements.add(value);
        }else{
            resultElements.add(0.0);
        }
        value = (revenueCMFF.cm) - (revenueCMPF.cm);
        resultElements.add(value);
        if(planRevenueCMPL!=null) {
            value = (planRevenueCMFF.cm) - (planRevenueCMPL.cm);
            resultElements.add(value);
        }else{
            resultElements.add(0.0);
        }
        value = (planRevenueCMFF.cm) - (planRevenueCMPF.cm);
        resultElements.add(value);


        result.add(resultElements);
        //Third row
        resultElements = new ArrayList<>();
        if(revenueCMPL!=null) {
            value = Utils.getSafePercent(revenueCMFF.cm, revenueCMFF.revenue) - Utils.getSafePercent(revenueCMPL.cm, revenueCMPL.revenue);
            resultElements.add(value);
        }else{
            resultElements.add(0.0);
        }
        value = Utils.getSafePercent(revenueCMFF.cm, revenueCMFF.revenue) - Utils.getSafePercent(revenueCMPF.cm, revenueCMPF.revenue);
        resultElements.add(value);
        if(planRevenueCMPL!=null) {
            value = Utils.getSafePercent(planRevenueCMFF.cm, planRevenueCMFF.revenue) - Utils.getSafePercent(planRevenueCMPL.cm, planRevenueCMPL.revenue);
            resultElements.add(value);
        }else{
            resultElements.add(0.0);
        }
        value = Utils.getSafePercent(planRevenueCMFF.cm, planRevenueCMFF.revenue) - Utils.getSafePercent(planRevenueCMPF.cm, planRevenueCMPF.revenue);
        resultElements.add(value);
        result.add(resultElements);
        return result;
    }


    /**
     * Get data from values for given budget and add to chartmodel
     *
     * @param model
     * @param budget
     * @param values
     */
    public void addDataForBudget(BarChartDataModel model, Budget budget, Map<String, Map<Budget, RevenueCM>> values) {
        Map<Budget, RevenueCM> map = values.values().stream().findFirst().orElseGet(HashMap::new);
        map.keySet().stream().filter(b -> b.equals(budget)).forEach(b -> {
            model.addData(b.getCaption(), map.get(b).revenue);
        });
    }


    @Override
    public Object practiceData(String practiceName) {
        Object[] actualValues = repository.findPracticesData(practiceName, CURRENCY_ACTUAL);
        Object[] planValues = repository.findPracticesData(practiceName, CURRENCY_PLAN);
        return calculateValuesForPractices(actualValues, planValues);
    }

    private Object calculateValuesForPractices(Object[] actualValues, Object[] planValues) {
        BarChartDataModel revenueChartModel = new BarChartDataModel();
        BarChartDataModel cmChartModel = new BarChartDataModel();
        Object deviationsModel;
        Map<String, Map<Budget, RevenueCM>> values = getKPIValues(actualValues);
        Map<String, Map<Budget, RevenueCM>> plvalues = getKPIValues(planValues);
        addDataForBudget(revenueChartModel, new Budget(Budget.PFACT), values);
        addDataForBudget(revenueChartModel, new Budget(Budget.PLAN), values);
        addDataForBudget(revenueChartModel, new Budget(Budget.PF), values);
        addDataForBudget(revenueChartModel, new Budget(Budget.FACT), values);
        addDataForBudget(revenueChartModel, new Budget(Budget.FF), values);
        revenueChartModel.setSeriesNames(Lists.newArrayList("Revenue"));

        List<Double> lineValues = new ArrayList<>();
        addCMDataForBudget(cmChartModel, new Budget(Budget.PFACT), values, lineValues);
        addCMDataForBudget(cmChartModel, new Budget(Budget.FACT), values, lineValues);
        addCMDataForBudget(cmChartModel, new Budget(Budget.PLAN), values, lineValues);
        addCMDataForBudget(cmChartModel, new Budget(Budget.PF), values, lineValues);
        addCMDataForBudget(cmChartModel, new Budget(Budget.FF), values, lineValues);
        List<Map<String, Object>> lines = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("values", lineValues);
        map.put("name", "CM, %");
        lines.add(map);
        cmChartModel.setSeriesNames(Lists.newArrayList("CM"));


        cmChartModel.addMetaData("lines", lines);
        revenueChartModel.convert();
        cmChartModel.convert();
        Map<String, Object> resultValues = new HashMap<>();
        resultValues.put("revenue", revenueChartModel);
        resultValues.put("cm", cmChartModel);

        deviationsModel = calculateDeviations(values, plvalues);
        resultValues.put("deviations", deviationsModel);
        return resultValues;
    }

    /**
     * Get values grouped by kpi
     *
     * @param inputValues [KPITYPE,REVENUE,CM,BUDGET,YEAR,CURRENCY]
     * @return ordered values grouped by kpi
     */
    public Map<String, Map<Budget, RevenueCM>> getKPIValues(Object[] inputValues) {
        Map<String, Map<Budget, RevenueCM>> values = new LinkedHashMap<>();
        for (Object obj : inputValues) {
            if (obj instanceof Object[]) {
                Object[] objValues = (Object[]) obj;
                if (objValues.length > 5) {
                    String kpiType = (String) objValues[0];
                    Map<Budget, RevenueCM> kpiValues = values.getOrDefault(kpiType, new TreeMap<>(this::compare));
                    Budget budget = new Budget((String) objValues[3]);
                    if (objValues.length == 6) {
                        budget.setYear((Integer) objValues[4]);
                    } else {
                        budget.setYear((Integer) objValues[5]);
                        budget.setMonth((Integer) objValues[4]);
                    }
                    kpiValues.put(budget, new RevenueCM(((BigDecimal) objValues[2]).doubleValue(), ((BigDecimal) objValues[1]).doubleValue()));
                    values.putIfAbsent(kpiType, kpiValues);
                }
            }
        }
        return values;
    }

    private int compare(Budget a, Budget b) {
        return LINE_CHART_ORDER.indexOf(a.getBudget()) - LINE_CHART_ORDER.indexOf(b.getBudget());
    }

    @Override
    public Object practiceHC(String practiceName) {
        LineChartModel chartModel = new LineChartModel();
        Object[] objects = repository.findHeadCountByPractice(practiceName);
        List<String> series = new ArrayList<>();
        for (int i = objects.length - 1; i >= 0; i--) {
            Object obj = objects[i];
            if (obj instanceof Object[]) {
                Object[] objValues = (Object[]) obj;
                chartModel.addData("data", ((BigDecimal) objValues[2]).doubleValue());
                series.add(((String) objValues[1]).replace(" ", "<br>"));
            }
        }

        chartModel.setLabels(series);
        return chartModel;
    }
}
