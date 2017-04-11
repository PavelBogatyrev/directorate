package com.luxoft.horizon.dir.service;

import com.luxoft.horizon.dir.Application;

import com.luxoft.horizon.dir.entities.app.Screenshot;
import com.luxoft.horizon.dir.entities.app.ScreenshotStatus;
import com.luxoft.horizon.dir.service.app.ScreenshotRepository;
import com.luxoft.horizon.dir.utils.IConstants;
import com.luxoft.horizon.dir.utils.PDFGeneratorService;
import com.luxoft.horizon.dir.utils.ScreenshotService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by bogatp on 13.04.16.
 */
@Ignore
@SpringApplicationConfiguration(Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenshotServiceTest {

    public static final String QUARTER = "2016Q3";
    @Autowired
    ScreenshotRepository screenshotRepository;

    @Autowired
    PDFGeneratorService pdfGeneratorService;

    @Autowired
    ScreenshotService screenshotService;

    @Test
    public void test() throws Exception {
        int[] screens = new int[]{10,20,30,40,50,60,70,80,90,91,100,120,130,140,150,160};
        List<Screenshot> list = new LinkedList<>();
        for (int i : screens) {
//            list.add(new Screenshot(QUARTER, i, ScreenshotStatus.NONE));
        }
        screenshotRepository.save(list);

      /*  while (true) {
            int done;
            do {
                done = screenshotRepository.findNumByQuarterAndStatus(QUARTER, ScreenshotStatus.DONE);
                TimeUnit.SECONDS.sleep(1);
            } while (done != list.size());

            System.out.println("All is done, creating PDF...");
            pdfGeneratorService.createPDFFromImage((List) screenshotRepository.findAll(), IConstants.TMP_DIR + "/out.pdf");

            System.out.println("Reset status...");
            screenshotRepository.updateStatus(ScreenshotStatus.NONE, QUARTER);
        }
*/
//        Files.write(lastSaved.getData(), new File("/tmp/out.png"));

    }
}
