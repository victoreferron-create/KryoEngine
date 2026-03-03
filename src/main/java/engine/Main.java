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

    public static boolean isRunning = true;

    public static void main(String[] args) {
        String gamePath;

        try {
            gamePath = args[0];
        } catch (IndexOutOfBoundsException e) {
            System.err.println("No path was given");
            return;
        }

        File gameLocation;

        gameLocation = new File(gamePath);

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


        /* MAINLOOP */

        final double TICKS_PER_SECOND = 60.0;
        final double NANOSECONDS_PER_TICK = 1_000_000_000.0 / TICKS_PER_SECOND;

        long lastTime = System.nanoTime();
        double delta = 0;

        while (Main.isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / NANOSECONDS_PER_TICK;
            lastTime = now;

            while (delta >= 1) {
                MainLoop.mainloop(gameContainer);
                delta--;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Main.isRunning = false;
            }
        }

        try {
            gameContainer.close();
        } catch (Exception e) {
            System.err.println("An internal error has occurred: " + e.getMessage());
        }
    }


}
