package tests;

import model.page.CheckersGamePage;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectDataUtils;

import java.util.HashMap;
import java.util.Map;

public class CheckersGameTest extends BaseTest {

    private static final String[][] INITIAL_BOARD_LAYOUT = {
            {"W", "E", "W", "E", "W", "E", "W", "E"},
            {"E", "W", "E", "W", "E", "W", "E", "W"},
            {"W", "E", "W", "E", "W", "E", "W", "E"},
            {"E", "E", "E", "E", "E", "E", "E", "E"},
            {"E", "E", "E", "E", "E", "E", "E", "E"},
            {"E", "B", "E", "B", "E", "B", "E", "B"},
            {"B", "E", "B", "E", "B", "E", "B", "E"},
            {"E", "B", "E", "B", "E", "B", "E", "B"}
    };

    private static final Map<String, String> INITIAL_BOARD_MAP = new HashMap<>();

    static {
        for (int i = 0; i < INITIAL_BOARD_LAYOUT.length; i++) {
            for (int j = 0; j < INITIAL_BOARD_LAYOUT.length; j++) {
                INITIAL_BOARD_MAP.put(String.format("%d%d", i, j), INITIAL_BOARD_LAYOUT[j][i]);
            }
        }
    }

    @Test
    public void testNavigationToCheckers() {
        final CheckersGamePage checkersGamePage = new CheckersGamePage(getDriver());

        Assert.assertEquals(getDriver().getCurrentUrl(), ProjectDataUtils.CHECKERS_GAME_URL);
        Assert.assertEquals(checkersGamePage.getHeader(), "Checkers");
        Assert.assertEquals(checkersGamePage.getBoard(), INITIAL_BOARD_MAP);
    }

    @Test(dependsOnMethods = "testNavigationToCheckers")
    public void testFiveLegalMovesAsOrange() throws InterruptedException {
        CheckersGamePage checkersGamePage = new CheckersGamePage(getDriver())
                .moveDraught(0, 2, 1, 3)
                .confirmCanTakeNextStep()
                .moveDraught(1, 1, 0, 2)
                .confirmCanTakeNextStep()
                .moveDraught(1, 3, 3, 5)
                .confirmCanTakeNextStep()
                .moveDraught(2, 2, 1, 3)
                .confirmCanTakeNextStep()
                .moveDraught(4, 2, 2, 4)
                .confirmCanTakeNextStep()
                .clickRestart();

        Assert.assertEquals(checkersGamePage.getBoard(), INITIAL_BOARD_MAP);
    }
}
