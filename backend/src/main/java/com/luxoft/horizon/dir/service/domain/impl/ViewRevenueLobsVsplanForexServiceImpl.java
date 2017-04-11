package com.luxoft.horizon.dir.service.domain.impl;

import com.luxoft.horizon.dir.service.domain.IViewRevenueLobsForecastForexService;
import com.luxoft.horizon.dir.service.domain.IViewRevenueLobsVsplanForexService;
import com.luxoft.horizon.dir.service.domain.KPIRepository;
import com.luxoft.horizon.dir.utils.Utils;
import com.luxoft.horizon.dir.widget.BasicChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavelbogatyrev on 29/05/16.
 */
@Service
public class ViewRevenueLobsVsplanForexServiceImpl implements IViewRevenueLobsVsplanForexService {

    @Autowired
    KPIRepository repository;

    @Override
    public BasicChartModel getCMByLobsVsplan() {
        BasicChartModel model = new BasicChartModel();
        List<Double> data = new ArrayList<>();
        List<String> seriesNames = new ArrayList<>();
        Object[] firstValueData = (Object[]) repository.findKpiByLobCMFirstValueVsplan();
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


        //data.add(curValue);
        Object[] deltasData = repository.findKpiByLobCMDeltasVsplan();
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
        Object[] lastValueData = (Object[]) repository.findKpiByLobCMLastValueVsplan();
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
    public BasicChartModel getRevenueByLobsVsplan() {
        BasicChartModel model = new BasicChartModel();
        List<Double> data = new ArrayList<>();
        List<String> seriesNames = new ArrayList<>();
        Object[] firstValueData = (Object[]) repository.findKpiByLobRevenueFirstValueVsplan();
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


        Object[] deltasData = repository.findKpiByLobRevenueDeltasVsplan();
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
        Object[] lastValueData = (Object[]) repository.findKpiByLobRevenueLastValueVsplan();
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
    public BasicChartModel getRevenueGCoEByLobsVsplan() {
        BasicChartModel model = new BasicChartModel();
        List<Double> data = new ArrayList<>();
        List<String> seriesNames = new ArrayList<>();
        Object[] firstValueData = (Object[]) repository.findKpiByLobRevenueGCoEFirstValueVsplan();
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

        Object[] deltasData = repository.findKpiByLobRevenueGCoEDeltasVsplan();
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
        Object[] lastValueData = (Object[]) repository.findKpiByLobRevenueGCoELastValueVsplan();
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
    public BasicChartModel getCMGCoEByLobsVsplan() {
        BasicChartModel model = new BasicChartModel();
        List<Double> data = new ArrayList<>();
        List<String> seriesNames = new ArrayList<>();
        Object[] firstValueData = (Object[]) repository.findKpiByLobCMGCoEFirstValueVsplan();
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

        Object[] deltasData = repository.findKpiByLobCMGCoEDeltasVsplan();
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
        Object[] lastValueData = (Object[]) repository.findKpiByLobCMGCoELastValueVsplan();
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
