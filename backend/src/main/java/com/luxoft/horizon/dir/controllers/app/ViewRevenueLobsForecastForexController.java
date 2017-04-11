package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.domain.IViewRevenueLobsForecastForexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rlapin
 */
@RestController
@RequestMapping("/api")
public class ViewRevenueLobsForecastForexController extends BaseController {
    @Autowired
    IViewRevenueLobsForecastForexService service;
    @RequestMapping("/cmByLobs")
    public String cmByLobs() {
        return gson.toJson(service.getCMByLobs());
    }
    @RequestMapping("/revenueByLobs")
    public String revenueByLobs() {
        return gson.toJson(service.getRevenueByLobs());
    }
    @RequestMapping("/revenueGCoEByLobs")
    public String revenueGCoEByLobs() {
        return gson.toJson(service.getRevenueGCoEByLobs());
    }
    @RequestMapping("/cmGCoEByLobs")
    public String cmGCoEByLobs() {
        return gson.toJson(service.getCMGCoEByLobs());
    }
}
