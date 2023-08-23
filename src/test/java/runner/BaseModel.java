package runner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseModel {

    private final WebDriver driver;
    private final Map<Integer, WebDriverWait> waitMap = new HashMap<>();

    public BaseModel(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(getDriver(), this);
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait getWait(int seconds) {
        return waitMap.computeIfAbsent(seconds, duration -> new WebDriverWait(driver, Duration.ofSeconds(duration)));
    }
}
