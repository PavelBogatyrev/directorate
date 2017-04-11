package com.luxoft.horizon.dir.controllers.app;

import com.luxoft.horizon.dir.controllers.BaseController;
import com.luxoft.horizon.dir.service.domain.IViewRevenue2YearsLobsService;
import com.luxoft.horizon.dir.service.domain.IViewRevenueTwoYearsLobsCoeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rlapin
 */

@RestController
@RequestMapping("/api")
public class ViewRevenueTwoYearsLobsCoeController extends BaseController {

    @Autowired
    IViewRevenueTwoYearsLobsCoeService service;

    @RequestMapping("/revenueCoeByLobsTwoYearsLeft")
    public String revenueCoeByLobsTwoYearsLeft() {
        return gson.toJson(service.getRevenueCoeByLobsTwoYearsLeft());
    }

    @RequestMapping("/revenueCoeByLobsTwoYearsRight")
    public String revenueCoeByLobsTwoYearsRight() {
        return gson.toJson(service.getRevenueCoeByLobsTwoYearsRight());
    }
}

