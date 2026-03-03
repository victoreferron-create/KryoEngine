package engine;

import core.GameContainer;
import core.IKryoGame;
import world.Level;
import world.PhysicalGameObject;

public class MainLoop {
    public static void mainloop(GameContainer gameContainer) {
        IKryoGame game = gameContainer.getGame();

        Level level = game.getCurrentLevel();

        for (PhysicalGameObject object : level.getLevelContents()) {
            object.tick();
        }
    }
}
