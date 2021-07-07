package com.selenium.websockets;

import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.Test;

import java.util.logging.Level;

public class CaptureMessages {

    private static WebDriver driver;
    @Test
    public static void CaptureWebSockets()throws InterruptedException{
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/main/resources/chromedriver.exe");
        LoggingPreferences loggingprefs = new LoggingPreferences();
        loggingprefs.enable(LogType.PERFORMANCE, Level.ALL);

        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
        driver = new ChromeDriver(options);

        driver.navigate().to("https://web-demo.adaptivecluster.com/");

        Thread.sleep(5000);

        LogEntries logEntries = driver.manage().logs().get(LogType.PERFORMANCE);

        driver.close();
        driver.quit();

        logEntries.forEach(logEntry -> {
            JSONObject messageJSON = new JSONObject(logEntry.getMessage());
            String method = messageJSON.getJSONObject("message").getString("method");
            if(method.equalsIgnoreCase("Network.webSocketFrameSent")){
                System.out.println("Message Sent: " + messageJSON.getJSONObject("message").getJSONObject("params").getJSONObject("response").getString("payloadData"));
            }else if(method.equalsIgnoreCase("Network.webSocketFrameReceived")){
                System.out.println("Message Received: " + messageJSON.getJSONObject("message").getJSONObject("params").getJSONObject("response").getString("payloadData"));
            }
        });
    }
}
