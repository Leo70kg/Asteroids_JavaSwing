package screens;

import ucd.comp2011j.engine.MenuCommands;

public class MenuListener implements MenuCommands {

    private MenuListener() {
        hasPressedNewGame = false;
        hasPressedAboutScreen = false;
        hasPressedHighScoreScreen = false;
        hasPressedMenu = false;
        hasPressedExit = false;
    }

    private static MenuListener listener = new MenuListener();

    public static MenuListener getInstance() {
        return listener;
    }

    private boolean hasPressedNewGame;
    private boolean hasPressedAboutScreen;
    private boolean hasPressedHighScoreScreen;
    private boolean hasPressedMenu;
    private boolean hasPressedExit;

    public void setHasPressedNewGame(boolean hasPressedNewGame) {
        this.hasPressedNewGame = hasPressedNewGame;
    }

    public void setHasPressedAboutScreen(boolean hasPressedAboutScreen) {
        this.hasPressedAboutScreen = hasPressedAboutScreen;
    }

    public void setHasPressedHighScoreScreen(boolean hasPressedHighScoreScreen) {
        this.hasPressedHighScoreScreen = hasPressedHighScoreScreen;
    }

    public void setHasPressedMenu(boolean hasPressedMenu) {
        this.hasPressedMenu = hasPressedMenu;
    }

    public void setHasPressedExit(boolean hasPressedExit) {
        this.hasPressedExit = hasPressedExit;
    }

    @Override
    public boolean hasPressedNewGame() {
        return hasPressedNewGame;
    }

    @Override
    public boolean hasPressedAboutScreen() {
        return hasPressedAboutScreen;
    }

    @Override
    public boolean hasPressedHighScoreScreen() {
        return hasPressedHighScoreScreen;
    }

    @Override
    public boolean hasPressedMenu() {
        return hasPressedMenu;
    }

    @Override
    public boolean hasPressedExit() {
        return hasPressedExit;
    }

    @Override
    public void resetKeyPresses() {
        hasPressedNewGame = false;
        hasPressedAboutScreen = false;
        hasPressedHighScoreScreen = false;
        hasPressedMenu = false;
        hasPressedExit = false;
    }
}
