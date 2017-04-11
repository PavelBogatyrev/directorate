package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.domain.IViewRevenueLobsForecastForexService;
import com.luxoft.horizon.dir.service.domain.IViewRevenueLobsVsplanForexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rlapin
 */
@RestController
@RequestMapping("/api")
public class ViewRevenueLobsVsplanForexController extends BaseController {
    @Autowired
    IViewRevenueLobsVsplanForexService service;
    @RequestMapping("/cmByLobsVsplan")
    public String cmByLobsVsplan() {
        return gson.toJson(service.getCMByLobsVsplan());
    }
    @RequestMapping("/revenueByLobsVsplan")
    public String revenueByLobsVsplan() {
        return gson.toJson(service.getRevenueByLobsVsplan());
    }
    @RequestMapping("/revenueGCoEByLobsVsplan")
    public String revenueGCoEByLobs() {
        return gson.toJson(service.getRevenueGCoEByLobsVsplan());
    }
    @RequestMapping("/cmGCoEByLobsVsplan")
    public String cmGCoEByLobs() {
        return gson.toJson(service.getCMGCoEByLobsVsplan());
    }
}
