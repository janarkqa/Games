package model.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import runner.BaseModel;
import runner.ProjectDataUtils;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckersGamePage extends BaseModel {

    @Getter
    @AllArgsConstructor
    private static class BoardElement {

        private String positionXY;
        private String element;
    }

    private static final By HEADER_LOCATOR = By.tagName("h1");
    private static final By BOARD_LOCATOR = By.cssSelector(".line > img");
    private static final By HINT_LOCATOR = By.id("message");
    private static final By RESTART_LOCATOR = By.cssSelector("a[href='./']");

    public CheckersGamePage(WebDriver driver) {
        super(driver);
    }

    public String getHeader() {
        return getDriver().findElement(HEADER_LOCATOR).getText();
    }

    public Map<String, String> getBoard() {
        return getDriver().findElements(BOARD_LOCATOR).stream()
                .map(this::createBoardElement)
                .collect(Collectors.toMap(BoardElement::getPositionXY, BoardElement::getElement));
    }

    public CheckersGamePage moveDraught(int fromX, int fromY, int toX, int toY) {
        getDriver().findElement(By.cssSelector(String.format("img[name='space%d%d']", fromX, fromY))).click();
        getDriver().findElement(By.cssSelector(String.format("img[name='space%d%d']", toX, toY))).click();

        return this;
    }

    public CheckersGamePage confirmCanTakeNextStep() {
        getWait(5).pollingEvery(Duration.ofSeconds(2)).until(ExpectedConditions.textToBe(HINT_LOCATOR, "Make a move."));

        return this;
    }

    public CheckersGamePage clickRestart() {
        getDriver().findElement(RESTART_LOCATOR).click();

        return this;
    }

    private BoardElement createBoardElement(WebElement webElement) {
        String srcAttribute = webElement.getAttribute("src")
                .replace(ProjectDataUtils.CHECKERS_GAME_URL, "").replace(".gif", "");
        String positionXY = webElement.getAttribute("name").replace("space", "");
        String element = "E";

        if (srcAttribute.equals("me1")) {
            element = "B";
        } else if (srcAttribute.equals("you1") || srcAttribute.equals("you2")) {
            element = "W";
        }

        return new BoardElement(positionXY, element);
    }
}
