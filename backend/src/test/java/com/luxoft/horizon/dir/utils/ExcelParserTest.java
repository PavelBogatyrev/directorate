package com.luxoft.horizon.dir.utils;

import com.luxoft.horizon.dir.Application;
import com.luxoft.horizon.dir.service.app.impl.ExcelParserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;

/**
 * Created by bogatp on 02.04.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ExcelParserTest {

    @Autowired
    ExcelParserService service;

    @Test
    public void test() throws Exception {
        InputStream is = ExcelParserService.class.getResourceAsStream("/dir_data_test.xlsx");
        service.loadFile(is);
        service.importData();
    }

}
