package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.domain.IViewRevenue2YearsLobsService;
import com.luxoft.horizon.dir.service.domain.IViewRevenueClientsPlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ygorenburgov on 09.06.16.
 */

@RestController
@RequestMapping("/api")
public class ViewRevenueClientsPlController extends BaseController {

    @Autowired
    IViewRevenueClientsPlService service;

    @RequestMapping("/revenueByClientsPl")
    public String revenueByClientsPl() {
        return gson.toJson(service.getRevenueByClientsPl());
    }

    @RequestMapping("/cmByClientsPl")
    public String cmByClientsPl() {
        return gson.toJson(service.getCMByClientsPl());
    }

}

