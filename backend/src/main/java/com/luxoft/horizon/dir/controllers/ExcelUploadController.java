package com.luxoft.horizon.dir.controllers;

import com.luxoft.horizon.dir.entities.app.Screenshot;
import com.luxoft.horizon.dir.service.app.ScreenshotRepository;
import com.luxoft.horizon.dir.service.app.impl.ExcelParserService;
import com.luxoft.horizon.dir.utils.ScreenshotService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author rlapin
 */

@Controller
public class ExcelUploadController {
    private static final Logger LOG = LoggerFactory.getLogger(ExcelUploadController.class);
    private static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final int MAX_FILE_SIZE = -1;
    private static final int MAX_MEM_SIZE = 4 * 1024;

    @Autowired
    ExcelParserService excelParserService;

    @Autowired
    ScreenshotService screenshotService;

    @Autowired
    ScreenshotRepository screenshotRepository;

    public static String getExtension(String name) {
        String extension = null;
        final int dotIndex = name.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = name.substring(dotIndex);
        }
        return extension;
    }

    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public void fileUpload_async(@RequestParam("file") MultipartFile file,
                                 HttpServletResponse response) throws IOException {
        String name = UUID.randomUUID().toString() + ".xlsx";

        if (!file.isEmpty()) {
            if (!".xlsx".equals(getExtension(file.getOriginalFilename()))) {
                response.getWriter().write("File format is not .xlsx");
                LOG.info("incorrect format");
            } else {
                try {
                    File outputFile = new File(FileUtils.getTempDirectory() + "/" + name);
                    BufferedOutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(outputFile));
                    FileCopyUtils.copy(file.getInputStream(), stream);
                    stream.close();

                    excelParserService.loadFile(outputFile.getAbsolutePath());
                    excelParserService.importData();

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("File upload successfully");
                    LOG.info("File upload");
                } catch (Exception e) {
                    response.getWriter().write("File upload with errors" + e.getMessage());
                    LOG.info("File upload with errors" + e.getMessage());
                }
            }
        } else {
            response.getWriter().write("No files were uploaded");
            LOG.info("No files were uploaded");
        }
    }


}
