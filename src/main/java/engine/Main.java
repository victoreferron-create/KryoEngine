package engine;

import core.GameContainer;
import gameloader.GameLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        // verify if the user has put the required arguments in the command line
        if (args.length != 1) {
            System.err.println("Invalid argument count: " + args.length + ". Requires 1 argument");
            return;
        }
        // network test

        URL gameURL;

        try {
            gameURL = new File(args[0]).toURI().toURL();

            try (GameContainer gameContainer = GameLoader.loadGame(gameURL)) {
                if (gameContainer == null) return;

                MainLoop.setTPS(40);

                MainLoop.mainloop(() -> {
                    MainLoop.tick(gameContainer);

                    if (gameContainer.getGame().getIsStopped()) {
                        MainLoop.stop();
                    }
                });

            } catch (Exception e) {
                System.err.println("An unknown error has occurred:\n " + e.getMessage());
            }
        } catch (MalformedURLException e) {
            System.err.println("The provided path could not be converted to a URL: " + args[0]);
        }

    }
}
