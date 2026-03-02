package engine;

import core.GameContainer;
import core.IKryoGame;
import gameloader.GameLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {
    public static void main(String[] args) {
        String gamePath = null;

        try {
            gamePath = args[0];
        } catch (IndexOutOfBoundsException e) {
            System.err.println("No path was given");
            return;
        }

        File gameLocation = null;

        try {
            gameLocation = new File(gamePath);
        } catch (NullPointerException npe) {
            System.err.println("No path has been given!");
            return;
        }

        if (!gameLocation.exists()) {
            System.err.println("Invalid path!");
            return;
        }

        URL gameURL;

        try {
            gameURL = gameLocation.toURI().toURL();
        } catch (MalformedURLException e) {
            System.err.println("An internal error has occurred: " + e.getMessage());
            return;
        }

        GameContainer gameContainer = null;
        URLClassLoader gameClassLoader;
        IKryoGame game;
        try {
            gameContainer = GameLoader.loadGame(gameURL);
            game = gameContainer.getGame();
            gameClassLoader = gameContainer.getLoader();
        } catch (IOException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException |
                 InstantiationException | IllegalAccessException e) {
            System.err.println("An internal error has occurred: " + e.getMessage());
        }

        try {
            gameContainer.close();
        } catch (Exception e) {
            System.err.println("An internal error has occurred: " + e.getMessage());
        }
    }

}
