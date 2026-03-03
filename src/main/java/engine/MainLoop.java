package engine;

import core.GameContainer;
import core.IKryoGame;
import org.jetbrains.annotations.NotNull;
import world.Level;
import world.PhysicalGameObject;

public class MainLoop {

    private static double nsPerTick = 1_000_000_000 / 60.0;
    private static boolean isRunning = true;

    public static void setTPS(double tps) {
        MainLoop.nsPerTick = 1_000_000_000.0 / tps;
    }


    public static void tick(@NotNull GameContainer gameContainer) {
        IKryoGame game = gameContainer.getGame();

        Level level = game.getCurrentLevel();

        for (PhysicalGameObject object : level.getLevelContents()) {
            object.tick();
        }
    }



    public static void mainloop(Runnable task) {
        long lastTime = System.nanoTime();
        double delta = 0;

        while (MainLoop.isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / MainLoop.nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                task.run();
                delta--;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                MainLoop.stop();
            }
        }
    }

    public static void stop() {
        MainLoop.isRunning = false;
    }
}
