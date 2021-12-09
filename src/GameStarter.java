import utilities.PersistentScoreKeeper;
import screens.*;
import ucd.comp2011j.engine.GameManager;

public class GameStarter {
    public static void main(String[] args) {
        ApplicationWindow applicationWindow = new ApplicationWindow("Asteroids");
        MenuListener menuListener = MenuListener.getInstance();
        MenuScreen menuScr = new MenuScreen();
        AboutScreen aboutScr = new AboutScreen();
        ScoreScreen scoreScr = new ScoreScreen();
        GameScreen gameScr = new GameScreen();
        PersistentScoreKeeper scoreKeeper = PersistentScoreKeeper.getInstance();
        GameController game = new GameController(gameScr);

        GameManager gameManager = new GameManager(game, applicationWindow, menuListener, menuScr,
                aboutScr, scoreScr, gameScr, scoreKeeper);

        gameManager.run();
    }
}
