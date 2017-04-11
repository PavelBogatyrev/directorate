package com.luxoft.horizon.dir.service.domain;

import com.luxoft.horizon.dir.widget.BarChartDataModel;

/**
 * Created by ygorenburgov on 09.06.16.
 */
public interface IViewRevenueClientsPlService {

    BarChartDataModel getRevenueByClientsPl();
    BarChartDataModel getCMByClientsPl();

}
