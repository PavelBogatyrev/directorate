package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.domain.IViewRevenue2YearsLobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ygorenburgov on 09.06.16.
 */

@RestController
@RequestMapping("/api")
public class ViewRevenue2YearsLobsController extends BaseController {

    @Autowired
    IViewRevenue2YearsLobsService service;

    @RequestMapping("/revenueByLobs2YearsSmall")
    public String revenueByLobs2YearsSmall() {
        return gson.toJson(service.getRevenueByLobs2YearsSmall());
    }

    @RequestMapping("/revenueByLobs2YearsBig")
    public String revenueByLobs2YearsBig() {
        return gson.toJson(service.getRevenueByLobs2YearsBig());
    }

    @RequestMapping("/cmByLobs2YearsSmall")
    public String cmByLobs2YearsSmall() {
        return gson.toJson(service.getCMByLobs2YearsSmall());
    }

    @RequestMapping("/cmByLobs2YearsBig")
    public String cmByLobs2YearsBig() {
        return gson.toJson(service.getCMByLobs2YearsBig());
    }
}

