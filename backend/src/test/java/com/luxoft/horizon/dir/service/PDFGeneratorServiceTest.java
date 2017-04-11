package com.luxoft.horizon.dir.service;

import com.luxoft.horizon.dir.Application;
import com.luxoft.horizon.dir.entities.app.Screenshot;
import com.luxoft.horizon.dir.utils.PDFGeneratorService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bogatp on 16.04.16.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class PDFGeneratorServiceTest {

    @Autowired
    PDFGeneratorService service;

    @Test
    public void test() throws Exception {
        List<String> images = Arrays.asList(new String[]{
                "/tmp/1.png",
                "/tmp/3.png",
                "/tmp/4.png",
                "/tmp/5.png"
        });

        List<Screenshot> screenshots = new LinkedList<>();

        for(String imagePath: images){
            Path path = Paths.get(imagePath);
            Screenshot s = new Screenshot();
            s.setData(Files.readAllBytes(path));
            screenshots.add(s);
        }


        service.createPDFFromImage(screenshots, "/tmp/out.pdf");
    }

}
