package model.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import runner.BaseModel;
import runner.ProjectDataUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckersGamePage extends BaseModel {

    @Getter
    @AllArgsConstructor
    private static class BoardElement {

        private String positionXY;
        private String element;
    }


    @FindBy(tagName = "h1")
    private WebElement header;

    @FindBy(css = ".line > img")
    private List<WebElement> board;

    @FindBy(id = "message")
    private WebElement hint;

    @FindBy(css = "a[href='./']")
    private WebElement restart;


    public CheckersGamePage(WebDriver driver) {
        super(driver);
    }

    public String getHeader() {
        return header.getText();
    }

    public Map<String, String> getBoard() {
        return board.stream()
                .map(this::createBoardElement)
                .collect(Collectors.toMap(BoardElement::getPositionXY, BoardElement::getElement));
    }

    public CheckersGamePage moveDraught(int fromX, int fromY, int toX, int toY) {
        getDriver().findElement(By.cssSelector(String.format("img[name='space%d%d']", fromX, fromY))).click();
        getDriver().findElement(By.cssSelector(String.format("img[name='space%d%d']", toX, toY))).click();

        return this;
    }

    public CheckersGamePage confirmCanTakeNextStep() {
        getWait(5).pollingEvery(Duration.ofSeconds(2)).until(ExpectedConditions.textToBe(By.id("message"), "Make a move."));

        return this;
    }

    public CheckersGamePage clickRestart() {
        restart.click();

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
