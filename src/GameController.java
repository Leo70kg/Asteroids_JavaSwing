import screens.ApplicationWindow;
import screens.GameScreen;
import shapes.*;
import ucd.comp2011j.engine.Game;


public class GameController implements Game {

    private boolean isGameOver;
    private GameScreen gameScreen;
    private int TICKS_PER_SECOND;

    public GameController(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        isGameOver = false;
        TICKS_PER_SECOND = 10;
    }

    @Override
    public int getPlayerScore() {
        return gameScreen.getScores();
    }

    @Override
    public int getLives() {
        return gameScreen.getLives();
    }

    @Override
    public void updateGame() {
        final int MAX_LOOPS = getTargetFPS() / 5;
        int loops = 0;
        while (loops < MAX_LOOPS) {
            gameScreen.move();
            if (gameScreen.isLevelUp()) break;
            loops++;
        }

    }

    @Override
    public boolean isPaused() {
        return gameScreen.isPause();
    }

    @Override
    public void checkForPause() {

    }

    @Override
    public void startNewGame() {
        isGameOver = false;
    }

    @Override
    public boolean isLevelFinished() {

        return gameScreen.isLevelUp();
    }

    @Override
    public int getTargetFPS() {
        return TICKS_PER_SECOND;
    }

    @Override
    public boolean isPlayerAlive() {
        return gameScreen.isAlive();
    }

    @Override
    public void resetDestroyedPlayer() {
        gameScreen.setShip(gameScreen.createShip());
        gameScreen.addKeyListener(gameScreen.getShip());
        gameScreen.setAlive(true);
    }

    @Override
    public void moveToNextLevel() {
        gameScreen.setLevelUp(false);
        gameScreen.setInitialNumOfAsteroids(gameScreen.getInitialNumOfAsteroids()+1);
        gameScreen.setRandomAsteroids(gameScreen.createRandomAsteroids(gameScreen.getInitialNumOfAsteroids(),
                AsteroidSize.LARGE));
    }

    @Override
    public boolean isGameOver() {
        if (gameScreen.getLives() == 0) {
            isGameOver = true;
        }
        return isGameOver;
    }

    @Override
    public int getScreenWidth() {
        return ApplicationWindow.SCREEN_WIDTH;
    }

    @Override
    public int getScreenHeight() {
        return ApplicationWindow.SCREEN_HEIGHT;
    }

}
