package com.luxoft.horizon.dir.service.domain;

import com.luxoft.horizon.dir.widget.BarChartDataModel;

/**
 * Created by ygorenburgov on 09.06.16.
 */
public interface IViewRevenue2YearsLobsService {

    BarChartDataModel getRevenueByLobs2YearsSmall();
    BarChartDataModel getRevenueByLobs2YearsBig();
    BarChartDataModel getCMByLobs2YearsSmall();
    BarChartDataModel getCMByLobs2YearsBig();

}
