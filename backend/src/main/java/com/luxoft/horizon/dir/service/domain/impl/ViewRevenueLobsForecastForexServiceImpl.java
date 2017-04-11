package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.domain.IViewRevenueLobsForecastForexService;
import com.luxoft.horizon.dir.service.domain.KPIRepository;
import com.luxoft.horizon.dir.utils.Utils;
import com.luxoft.horizon.dir.widget.BasicChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by pavelbogatyrev on 29/05/16.
 */
@Service
public class ViewRevenueLobsForecastForexServiceImpl implements IViewRevenueLobsForecastForexService {

    public static final String BUDGET_PLAN = "plan";
    public static final String BUDGET_FACT = "fact";
    public static final String BUDGET_FACTFORECAST = "factforecast";
    public static final int CURRENT_FY = Utils.currentFY();
    public static final String PLAN_REVENUE_PERCENT = "PLAN_REVENUE_PERCENT";
    public static final String FACT_REVENUE_PERCENT = "FACT_REVENUE_PERCENT";
    public static final String FORECAST_REVENUE_PERCENT = "FORECAST_REVENUE_PERCENT";
    private static final String REVENUE_PERCENT = "REVENUE_PERCENT";
    public static final BigDecimal DEFAULT_VALUE = BigDecimal.valueOf(0);
    public static final String FX_INFL = "FX infl.";
    public static final String FACT_REVENUE_PERCENT2 = "FACT_REVENUE_PERCENT2";

    @Autowired
    KPIRepository repository;



    @Override
    public BasicChartModel getCMByLobs() {
        BasicChartModel model = new BasicChartModel();
        List<Double> data = new ArrayList<>();
        List<String> seriesNames = new ArrayList<>();
        Object[] firstValueData = (Object[]) repository.findKpiByLobCMFirstValue();
        double curValue = 0;
        if (firstValueData != null && firstValueData.length > 2) {
            Object obj = firstValueData[0];
            if (obj != null && obj instanceof BigDecimal) {
                curValue = ((BigDecimal) obj).doubleValue();
                data.add(curValue);
                seriesNames.add("FY" + ((int) firstValueData[1]) % 100 + "<br> " + firstValueData[2]);
            } else {
                seriesNames.add("");
                data.add(0.0d);
            }

        } else {
            seriesNames.add("");
            data.add(0.0d);
        }

        Object secondValueData = repository.findKpiByLobCMSecondvalue();
        if (secondValueData != null && secondValueData instanceof BigDecimal) {
            curValue += ((BigDecimal) secondValueData).doubleValue();

        }
        seriesNames.add(FX_INFL);
        data.add(curValue);
        Object[] deltasData = repository.findKpiByLobCMDeltas();
        if (deltasData != null && deltasData.length > 0) {
            for (Object aDeltasData : deltasData) {
                if (aDeltasData instanceof Object[]) {
                    Object[] objs = (Object[]) aDeltasData;
                    if (objs.length > 2) {
                        Object obj = objs[2];
                        if (obj != null && obj instanceof BigDecimal) {
                            double delta = ((BigDecimal) obj).doubleValue();
                            curValue += delta;
                        }
                        seriesNames.add((String) objs[1]);
                    } else {
                        seriesNames.add("");
                    }
                }
                data.add(curValue);

            }
        }
        Object[] lastValueData = (Object[]) repository.findKpiByLobCMLastValue();
        if (lastValueData != null && lastValueData.length > 2) {
            Object obj = lastValueData[0];
            if (obj != null && obj instanceof BigDecimal) {
                data.add(((BigDecimal) obj).doubleValue());
                seriesNames.add("FY" + ((int) lastValueData[1]) % 100 + "<br> " + lastValueData[2]);
            } else {
                data.add(0.0d);
            }
        }

        model.setSeriesNames(seriesNames);
        model.setData(data);
        return model;
    }

    @Override
    public BasicChartModel getRevenueByLobs() {
        BasicChartModel model = new BasicChartModel();
        List<Double> data = new ArrayList<>();
        List<String> seriesNames = new ArrayList<>();
        Object[] firstValueData = (Object[]) repository.findKpiByLobRevenueFirstValue();
        double curValue = 0;
        if (firstValueData != null && firstValueData.length > 2) {
            Object obj = firstValueData[0];
            if (obj != null && obj instanceof BigDecimal) {
                curValue = ((BigDecimal) obj).doubleValue();
                data.add(curValue);
                seriesNames.add("FY" + ((int) firstValueData[1]) % 100 + " <br>" + firstValueData[2]);
            } else {
                seriesNames.add("");
                data.add(0.0d);
            }

        } else {
            seriesNames.add("");
            data.add(0.0d);
        }

        Object secondValueData = repository.findKpiByLobRevenueSecondvalue();
        if (secondValueData != null && secondValueData instanceof BigDecimal) {
            curValue += ((BigDecimal) secondValueData).doubleValue();

        }
        seriesNames.add(FX_INFL);
        data.add(curValue);
        Object[] deltasData = repository.findKpiByLobRevenueDeltas();
        if (deltasData != null && deltasData.length > 0) {
            for (Object aDeltasData : deltasData) {
                if (aDeltasData instanceof Object[]) {
                    Object[] objs = (Object[]) aDeltasData;
                    if (objs.length > 2) {
                        Object obj = objs[2];
                        if (obj != null && obj instanceof BigDecimal) {
                            double delta = ((BigDecimal) obj).doubleValue();
                            curValue += delta;
                        }
                        seriesNames.add((String) objs[1]);
                    } else {
                        seriesNames.add("");
                    }
                }
                data.add(curValue);

            }
        }
        Object[] lastValueData = (Object[]) repository.findKpiByLobRevenueLastValue();
        if (lastValueData != null && lastValueData.length > 2) {
            Object obj = lastValueData[0];
            if (obj != null && obj instanceof BigDecimal) {
                data.add(((BigDecimal) obj).doubleValue());
                seriesNames.add("FY" + ((int) lastValueData[1]) % 100 + " <br>" + lastValueData[2]);
            } else {
                data.add(0.0d);
            }
        }

        model.setSeriesNames(seriesNames);
        model.setData(data);
        return model;
    }

    @Override
    public BasicChartModel getRevenueGCoEByLobs() {
        BasicChartModel model = new BasicChartModel();
        List<Double> data = new ArrayList<>();
        List<String> seriesNames = new ArrayList<>();
        Object[] firstValueData = (Object[]) repository.findKpiByLobRevenueGCoEFirstValue();
        double curValue = 0;
        if (firstValueData != null && firstValueData.length > 2) {
            Object obj = firstValueData[0];
            if (obj != null && obj instanceof BigDecimal) {
                curValue = ((BigDecimal) obj).doubleValue();
                data.add(curValue);
                seriesNames.add("FY" + ((int) firstValueData[1]) % 100 + "<br> " + firstValueData[2]);
            } else {
                seriesNames.add("");
                data.add(0.0d);
            }

        } else {
            seriesNames.add("");
            data.add(0.0d);
        }

        Object secondValueData = repository.findKpiByLobRevenueGCoESecondvalue();
        if (secondValueData != null && secondValueData instanceof BigDecimal) {
            curValue += ((BigDecimal) secondValueData).doubleValue();

        }
        seriesNames.add(FX_INFL);
        data.add(curValue);
        Object[] deltasData = repository.findKpiByLobRevenueGCoEDeltas();
        if (deltasData != null && deltasData.length > 0) {
            for (Object aDeltasData : deltasData) {
                if (aDeltasData instanceof Object[]) {
                    Object[] objs = (Object[]) aDeltasData;
                    if (objs.length > 2) {
                        Object obj = objs[2];
                        if (obj != null && obj instanceof BigDecimal) {
                            double delta = ((BigDecimal) obj).doubleValue();
                            curValue += delta;
                        }
                        seriesNames.add((String) objs[1]);
                    } else {
                        seriesNames.add("");
                    }
                }
                data.add(curValue);

            }
        }
        Object[] lastValueData = (Object[]) repository.findKpiByLobRevenueGCoELastValue();
        if (lastValueData != null && lastValueData.length > 2) {
            Object obj = lastValueData[0];
            if (obj != null && obj instanceof BigDecimal) {
                data.add(((BigDecimal) obj).doubleValue());
                seriesNames.add("FY" + ((int) lastValueData[1]) % 100 + "<br> " + lastValueData[2]);
            } else {
                data.add(0.0d);
            }
        }

        model.setSeriesNames(seriesNames);
        model.setData(data);
        return model;
    }

    @Override
    public BasicChartModel getCMGCoEByLobs() {
        BasicChartModel model = new BasicChartModel();
        List<Double> data = new ArrayList<>();
        List<String> seriesNames = new ArrayList<>();
        Object[] firstValueData = (Object[]) repository.findKpiByLobCMGCoEFirstValue();
        double curValue = 0;
        if (firstValueData != null && firstValueData.length > 2) {
            Object obj = firstValueData[0];
            if (obj != null && obj instanceof BigDecimal) {
                curValue = ((BigDecimal) obj).doubleValue();
                data.add(curValue);
                seriesNames.add("FY" + ((int) firstValueData[1]) % 100 + "<br>" + firstValueData[2]);
            } else {
                seriesNames.add("");
                data.add(0.0d);
            }

        } else {
            seriesNames.add("");
            data.add(0.0d);
        }

        Object secondValueData = repository.findKpiByLobCMGCoESecondvalue();
        if (secondValueData != null && secondValueData instanceof BigDecimal) {
            curValue += ((BigDecimal) secondValueData).doubleValue();

        }
        seriesNames.add(FX_INFL);
        data.add(curValue);
        Object[] deltasData = repository.findKpiByLobCMGCoEDeltas();
        if (deltasData != null && deltasData.length > 0) {
            for (Object aDeltasData : deltasData) {
                if (aDeltasData instanceof Object[]) {
                    Object[] objs = (Object[]) aDeltasData;
                    if (objs.length > 2) {
                        Object obj = objs[2];
                        if (obj != null && obj instanceof BigDecimal) {
                            double delta = ((BigDecimal) obj).doubleValue();
                            curValue += delta;
                        }
                        seriesNames.add((String) objs[1]);
                    } else {
                        seriesNames.add("");
                    }
                }
                data.add(curValue);

            }
        }
        Object[] lastValueData = (Object[]) repository.findKpiByLobCMGCoELastValue();
        if (lastValueData != null && lastValueData.length > 2) {
            Object obj = lastValueData[0];
            if (obj != null && obj instanceof BigDecimal) {
                data.add(((BigDecimal) obj).doubleValue());
                seriesNames.add("FY" + ((int) lastValueData[1]) % 100 + "<br>" + lastValueData[2]);
            } else {
                data.add(0.0d);
            }
        }

        model.setSeriesNames(seriesNames);
        model.setData(data);
        return model;
    }





}
