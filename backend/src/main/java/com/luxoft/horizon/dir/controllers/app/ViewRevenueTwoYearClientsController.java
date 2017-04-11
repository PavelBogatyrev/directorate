package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.domain.IViewRevenueTwoYearClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rlapin
 */
@RestController
@RequestMapping("/api")
public class ViewRevenueTwoYearClientsController extends BaseController {
    @Autowired
    IViewRevenueTwoYearClientsService service;

    /**
     *
     * @return Revenue for clients(Current year)
     */
    @RequestMapping("/clientsRevenue")
    public String clientsRevenueCY() {
        return gson.toJson(service.getClientsRevenue());
    }

}
