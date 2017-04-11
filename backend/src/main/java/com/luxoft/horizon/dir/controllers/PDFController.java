package com.luxoft.horizon.dir.controllers;

import com.google.gson.annotations.Expose;
import com.luxoft.horizon.dir.PrintTask;
import com.luxoft.horizon.dir.entities.app.Screenshot;
import com.luxoft.horizon.dir.entities.app.ScreenshotStatus;
import com.luxoft.horizon.dir.service.app.ScreenshotRepository;
import com.luxoft.horizon.dir.service.domain.IReportPeriodService;
import com.luxoft.horizon.dir.utils.IConstants;
import com.luxoft.horizon.dir.utils.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pavelbogatyrev on 17/04/16.
 */
@RestController
@RequestMapping("/pdf")
public class PDFController extends BaseController {

    @Autowired
    private ScreenshotRepository screenshotRepository;

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Autowired
    private IReportPeriodService periodService;

    private SimpleDateFormat sdf = new SimpleDateFormat("YYYYMM");


    @RequestMapping(value = "/print", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> update(@RequestBody PrintTask task) throws Exception {

        List<Screenshot> list = new LinkedList<>();

        for(String page : task.getPages()){
            String period = task.getActivePeriod();
            list.addAll(screenshotRepository.findByPeriodAndViewAndStatus(period, page, ScreenshotStatus.DONE));
        }

        String filepath = IConstants.TMP_DIR + "/dir.pdf";
        File file = new File(filepath);

        pdfGeneratorService.createPDFFromImage(list, filepath);

        InputStream is = new FileInputStream(file);
        InputStreamResource resource = new InputStreamResource(is);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = "dir.pdf";
        headers.setContentDispositionFormData("attachment", filename);

//        headers.set("Content-Disposition", "attachment;filename='" + filename +"'");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

    }

    @RequestMapping("/status/{quarter}")
    public String pdfStatus(@PathVariable String quarter) {
        int total = screenshotRepository.findNumByPeriod(quarter);
        int done = screenshotRepository.findNumByPeriodAndStatus(quarter, ScreenshotStatus.DONE);
        return gson.toJson(new PDFStatus(total, done));
    }

    @RequestMapping("/regenerate/{period}")
    public String regenerate(@PathVariable String period) {
        screenshotRepository.deleteByPeriod(period);
        int counter = 0;
        for(String view: IConstants.pages){
            Screenshot screenshot = new Screenshot(period, view, ScreenshotStatus.NONE);
            screenshotRepository.save(screenshot);
            counter++;
        }
        return String.valueOf(counter);
    }

    @RequestMapping("/regenerateAll")
    public String regenerate() {
        screenshotRepository.deleteAll();
        int counter = 0;
        for(Date periodDate: periodService.getReportPeriods()){
            String period = sdf.format(periodDate);
            for(String view: IConstants.pages){
                Screenshot screenshot = new Screenshot(period, view, ScreenshotStatus.NONE);
                screenshotRepository.save(screenshot);
                counter++;
            }
        }
        return String.valueOf(counter);
    }

}

class PDFStatus {
    @Expose
    private int total = 0;
    @Expose
    private int done = 0;

    public PDFStatus(int total, int done) {
        this.total = total;
        this.done = done;
    }
}
