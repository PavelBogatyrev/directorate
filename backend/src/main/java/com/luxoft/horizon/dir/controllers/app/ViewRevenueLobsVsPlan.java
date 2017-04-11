package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.domain.IViewRevenueLobsVsPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rlapin
 */
@RequestMapping("/api")
@RestController
public class ViewRevenueLobsVsPlan extends BaseController{
    @Autowired
    IViewRevenueLobsVsPlanService service;

    /**
     *
     * @return Revenue and cm per lobs(FY)
     */
    @RequestMapping("/lobsRevenueLeft")
    public String lobsRevenueLeft() {
        return gson.toJson(service.getLobsRevenueLeft());
    }
    @RequestMapping("/lobsRevenueRight")
    public String lobsRevenueRight() {
        return gson.toJson(service.getLobsRevenueRight());
    }
}
