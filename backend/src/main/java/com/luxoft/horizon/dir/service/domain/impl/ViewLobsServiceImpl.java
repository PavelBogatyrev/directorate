package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.app.IConfigurationService;
import com.luxoft.horizon.dir.service.domain.IViewLobsService;
import com.luxoft.horizon.dir.utils.Budget;
import com.luxoft.horizon.dir.utils.RevenueCM;
import com.luxoft.horizon.dir.utils.Utils;
import com.luxoft.horizon.dir.widget.BarChartDataModel;
import com.luxoft.horizon.dir.widget.LineChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author rlapin
 */
@Service
public class ViewLobsServiceImpl extends ViewLobsBaseServiceImpl implements IViewLobsService {
    private static final String CURRENCY_ACTUAL = "actual";
    private static final String CURRENCY_PLAN = "plan";
    private static final String LOB = "LOB";
    private static final String COE = "CoE";
    public static final String LOB_WO_COE = "LOB without CoE";

    @Autowired
    IConfigurationService config;

    @Override
    public Object lobsYear(String lobName) {
        Object[] actualValues = repository.findLobsYear(lobName, CURRENCY_ACTUAL);
        Object[] planValues = repository.findLobsYear(lobName, CURRENCY_PLAN);
        return calculateValuesForLobs(actualValues, planValues, false);
    }

    @Override
    public Object lobsMonth(String lobName, int month, int year) {
        Object[] actualValues = repository.findLobsMonth(month,year, lobName, CURRENCY_ACTUAL);
        Object[] planValues = repository.findLobsMonth(month,year, lobName, CURRENCY_PLAN);
        return calculateValuesForLobs(actualValues, planValues, true);
    }


    public void addPercentDataForBudget(LineChartModel model, Budget budget, Map<String, Map<Budget, RevenueCM>> values) {

        Map<Budget, RevenueCM> revenueByLobs = values.get(LOB);
        Map<Budget, RevenueCM> revenueByCoe = values.get(COE);
        if (revenueByLobs == null || revenueByLobs.get(budget) == null) {
            return;
        }
        RevenueCM revenueCMLob = revenueByLobs.get(budget);

        if (revenueCMLob != null) {
            if (revenueByCoe != null && revenueByCoe.get(budget) != null) {
                RevenueCM revenueCMCoe = revenueByCoe.get(budget);
                if (revenueCMCoe == null) {
                    revenueCMCoe = new RevenueCM(0, 0);
                }
                model.addData("LOB", Utils.getSafePercent(revenueCMLob.cm + revenueCMCoe.cm, revenueCMLob.revenue + revenueCMCoe.revenue));
                model.addData("LOB driven by GCOEs", Utils.getSafePercent(revenueCMCoe.cm, revenueCMCoe.revenue));
            } else {
                model.addData("LOB", Utils.getSafePercent(revenueCMLob.cm, revenueCMLob.revenue));
            }
        }
    }

    /**
     * Calculate deviations using plan and actual values
     *
     * @param values
     * @param planValues
     * @return
     */
    public Object calculateDeviations(Map<String, Map<Budget, RevenueCM>> values, Map<String, Map<Budget, RevenueCM>> planValues) {
        RevenueCM lobFF, pllobFF;
        RevenueCM coeFF, plcoeFF;
        RevenueCM lobPl, pllobPl;
        RevenueCM coePl, plcoePl;
        RevenueCM lobPF, pllobPF;
        RevenueCM coePF, plcoePF;
        List<List<Double>> result = new ArrayList<>();
        //Total lob to plan
        //As is
        String firstBudget = values.get(LOB).get(new Budget(Budget.FF)) == null ? Budget.FACT : Budget.FF;
        lobFF = values.get(LOB).get(new Budget(firstBudget));
        lobPF = values.get(LOB).get(new Budget(Budget.PF));
        lobPl = values.get(LOB).get(new Budget(Budget.PLAN));
        pllobFF = planValues.get(LOB).get(new Budget(firstBudget));
        pllobPF = planValues.get(LOB).get(new Budget(Budget.PF));
        pllobPl = planValues.get(LOB).get(new Budget(Budget.PLAN));


        double value;

        List<Double> resultElements = new ArrayList<>();
        if (values.get(COE) != null) {
            coeFF = values.get(COE).get(new Budget(firstBudget));
            coePF = values.get(COE).get(new Budget(Budget.PF));
            coePl = values.get(COE).get(new Budget(Budget.PLAN));
            plcoeFF = planValues.get(COE).get(new Budget(firstBudget));
            plcoePF = planValues.get(COE).get(new Budget(Budget.PF));
            plcoePl = planValues.get(COE).get(new Budget(Budget.PLAN));
            //First row
            value = (lobFF.revenue + coeFF.revenue) - (lobPl.revenue + coePl.revenue);
            resultElements.add(value);
            value = (lobFF.revenue + coeFF.revenue) - (lobPF.revenue + coePF.revenue);
            resultElements.add(value);

            value = (pllobFF.revenue + plcoeFF.revenue) - (pllobPl.revenue + plcoePl.revenue);
            resultElements.add(value);
            value = (pllobFF.revenue + plcoeFF.revenue) - (pllobPF.revenue + plcoePF.revenue);
            resultElements.add(value);

            result.add(resultElements);

            //Second row
            resultElements = new ArrayList<>();
            value = (lobFF.cm + coeFF.cm) - (lobPl.cm + coePl.cm);
            resultElements.add(value);
            value = (lobFF.cm + coeFF.cm) - (lobPF.cm + coePF.cm);
            resultElements.add(value);

            value = (pllobFF.cm + plcoeFF.cm) - (pllobPl.cm + plcoePl.cm);
            resultElements.add(value);
            value = (pllobFF.cm + plcoeFF.cm) - (pllobPF.cm + plcoePF.cm);
            resultElements.add(value);

            result.add(resultElements);

            //Third row
            resultElements = new ArrayList<>();
            value = Utils.getSafePercent((lobFF.cm + coeFF.cm), (lobFF.revenue + coeFF.revenue)) - Utils.getSafePercent((lobPl.cm + coePl.cm), (lobPl.revenue + coePl.revenue));
            resultElements.add(value);
            value = Utils.getSafePercent((lobFF.cm + coeFF.cm), (lobFF.revenue + coeFF.revenue)) - Utils.getSafePercent((lobPF.cm + coePF.cm), (lobPF.revenue + coePF.revenue));
            resultElements.add(value);

            value = Utils.getSafePercent((pllobFF.cm + plcoeFF.cm), (pllobFF.revenue + plcoeFF.revenue)) - Utils.getSafePercent((pllobPl.cm + plcoePl.cm), (pllobPl.revenue + plcoePl.revenue));
            resultElements.add(value);
            value = Utils.getSafePercent((pllobFF.cm + plcoeFF.cm), (pllobFF.revenue + plcoeFF.revenue)) - Utils.getSafePercent((pllobPF.cm + plcoePF.cm), (pllobPF.revenue + plcoePF.revenue));
            resultElements.add(value);

            result.add(resultElements);
        }
        //Forth row
        resultElements = new ArrayList<>();
        value = lobFF.revenue - lobPl.revenue;
        resultElements.add(value);
        value = lobFF.revenue - lobPF.revenue;
        resultElements.add(value);

        value = pllobFF.revenue - pllobPl.revenue;
        resultElements.add(value);
        value = pllobFF.revenue - pllobPF.revenue;
        resultElements.add(value);

        result.add(resultElements);

        //Fifth row
        resultElements = new ArrayList<>();
        value = lobFF.cm - lobPl.cm;
        resultElements.add(value);
        value = lobFF.cm - lobPF.cm;
        resultElements.add(value);

        value = pllobFF.cm - pllobPl.cm;
        resultElements.add(value);
        value = pllobFF.cm - pllobPF.cm;
        resultElements.add(value);

        result.add(resultElements);

        //Sixth row
        resultElements = new ArrayList<>();
        value = Utils.getSafePercent(lobFF.cm, lobFF.revenue) - Utils.getSafePercent(lobPl.cm, lobPl.revenue);
        resultElements.add(value);
        value = Utils.getSafePercent(lobFF.cm, lobFF.revenue) - Utils.getSafePercent(lobPF.cm, lobPF.revenue);
        resultElements.add(value);

        value = Utils.getSafePercent(pllobFF.cm, pllobFF.revenue) - Utils.getSafePercent(pllobPl.cm, pllobPl.revenue);
        resultElements.add(value);
        value = Utils.getSafePercent(pllobFF.cm, pllobFF.revenue) - Utils.getSafePercent(pllobPF.cm, pllobPF.revenue);
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
        Set<String> keys = values.keySet();
        for (String key : keys) {
            Map<Budget, RevenueCM> map = values.get(key);
            map.keySet().stream().filter(b -> b.equals(budget)).forEach(b -> {
                model.addData(b.getCaption(), map.get(b).revenue);
            });

        }
    }


}
