package com.luxoft.horizon.dir;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogatp on 28.04.16.
 */
@Component
@ConfigurationProperties(prefix="horizon")
public class Config {

    private List<String> adminAccounts = new ArrayList<String>();
    private List<String> userAccounts = new ArrayList<String>();

    public List<String> getAdminAccounts() {
        return this.adminAccounts;
    }

    public List<String> getUserAccounts() {
        return userAccounts;
    }

}
