package com.luxoft.horizon.dir.utils;

import com.luxoft.horizon.dir.entities.app.Screenshot;
import com.luxoft.horizon.dir.entities.app.ScreenshotStatus;
import com.luxoft.horizon.dir.service.app.ScreenshotRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by bogatp on 06.04.16.
 */
@Service
public class ScreenshotService implements Runnable {

    //TODO: Move to service user
    public static final String SUPERUSER = "screenshot_user";
    public static final int WEBDRIVER_WAIT_TIMEOUT = 10;
    private static Logger log = Logger.getLogger(ScreenshotService.class.getName());

    @Value("${selenium.max.threads}")
    private int maxThreads = 5;

    @Value("${selenium.hub}")
    private String seleniumHub;

    @Value("${selenium.render.url}")
    private String seleniumRenderURL;

    @Autowired
    private ScreenshotRepository screenshotRepository;

    @Value("${horizon.service.dir.active}")
    private boolean active;

    private ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

    @PostConstruct
    private void init() {
        new Thread(this).start();
    }

    @Override
    public void run() {

        while (active) {
            try {
                List<Screenshot> list = screenshotRepository.findByStatus(ScreenshotStatus.NONE);
                // Pickup as max as maxThreads tasks to execute
                List<Future> futures = new LinkedList<>();
//            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor();

                for (int i = 0; i < maxThreads && i < list.size(); i++) {
                    Screenshot screenshot = list.get(i);
                    String urlPattern = "{0}/#/{1}?mode=print&activePeriod={2}";
                    String url = MessageFormat.format(urlPattern, seleniumRenderURL, screenshot.getView(), screenshot.getPeriod());
                    Runnable worker = new WorkerThread(seleniumHub, url, seleniumRenderURL, screenshot, screenshotRepository);
                    futures.add(executor.submit(worker));
                }

                boolean allDone = false;
                while (!allDone) {
                    allDone = true;
                    for (Future future : futures) {
                        allDone = future.isDone();
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                log.warning("Thread interrupted...");
            }
        }

    }

}


class WorkerThread extends Thread {

    public static final int DATA_TIMEOUT = 15;

    private static Logger log = Logger.getLogger(ScreenshotService.class.getName());

    private String seleniumHub;
    private String url;
    private String seleniumRenderURL;
    private Screenshot screenshot;
    private ScreenshotRepository screenshotRepository;

    public WorkerThread(String seleniumHub, String url, String seleniumRenderURL, Screenshot page, ScreenshotRepository screenshotRepository) {
        this.seleniumHub = seleniumHub;
        this.url = url;
        this.seleniumRenderURL = seleniumRenderURL;
        this.screenshot = page;
        this.screenshotRepository = screenshotRepository;
    }

    public void run() {
        log.info(MessageFormat.format("Start processing screenshot, Period {0}, screenshot: {1} ", screenshot.getPeriod(), screenshot.getView()));

        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(seleniumHub), DesiredCapabilities.chrome());

            driver.get("about:blank");
            driver.manage().window().maximize();

            // Login
            driver.get(seleniumRenderURL + "/#/login");
            WebDriverWait wait = new WebDriverWait(driver, ScreenshotService.WEBDRIVER_WAIT_TIMEOUT);
            // Wait login field to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//input[@id=''])[2]")));

            // Eneter login and password
            driver.findElement(By.xpath("(//input[@id=''])[2]")).clear();
            driver.findElement(By.xpath("(//input[@id=''])[2]")).sendKeys(ScreenshotService.SUPERUSER);
            driver.findElement(By.cssSelector("input[type='button']")).click();

            // Wait for 1 view to appear
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("label_id_2")));

            // Open requested URL
            log.info(MessageFormat.format("Open URL {0} ", url));
            driver.get(url);

            TimeUnit.SECONDS.sleep(DATA_TIMEOUT);

            log.info(MessageFormat.format("Taking screenshot, Period {0}, screenshot: {1} ", screenshot.getPeriod(), screenshot.getView()));

            // Cast to augmeneted driver to take a screenshot
            WebDriver augmentedDriver = new Augmenter().augment(driver);

            // Load screenshot
            byte[] data = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);

            screenshot.setData(data);
            screenshot.setStatus(ScreenshotStatus.DONE);
            screenshotRepository.save(screenshot);

        } catch (Exception e) {
            log.warning(MessageFormat.format("Error processing screenshot: {0}, caused by {1} ", screenshot.getView(), e.getMessage()));
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }

    }

}