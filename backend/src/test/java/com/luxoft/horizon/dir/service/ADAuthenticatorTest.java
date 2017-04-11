package com.luxoft.horizon.dir.service;

import com.luxoft.horizon.dir.Application;
import com.luxoft.horizon.dir.security.ADAuthenticationService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by bogatp on 16.04.16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ADAuthenticatorTest {

    @Autowired
    ADAuthenticationService service;

    @Test
    public void test() throws Exception {
        String user = "user";
        String domain = "entities";
        String password = "password";
        Assert.assertTrue(service.authenticate(user, domain, password));
        Assert.assertTrue(service.isUser(user));
        Assert.assertTrue(service.isAdmin(user));
    }

}
