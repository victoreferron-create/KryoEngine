package gameloader;

import core.GameContainer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import core.*;

public class GameLoader {


    /**
     * WARNING!
     * <br>
     * This method may return {@code null}
     * */
    public static GameContainer loadGame(URL gameURL) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<String> binaryClassNames = new ArrayList<>();

        List<Class<?>> classes = new ArrayList<>();

        URLClassLoader classLoader = new URLClassLoader(new URL[]{gameURL}, GameLoader.class.getClassLoader());
        try (JarInputStream jarIn = new JarInputStream(gameURL.openStream())) {
            ZipEntry entry;
            while ((entry = jarIn.getNextEntry()) != null) {
                if (entry.getName().endsWith(".class") && entry.getName().contains("$")) {
                    binaryClassNames.add(entry.getName().substring(0, entry.getName().length() - 6).replace('/', '.').replace('\\', '.'));
                }
            }

            for (String className : binaryClassNames) {
                classes.add(classLoader.loadClass(className));
            }

            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(annotations.Game.class) && IKryoGame.class.isAssignableFrom(clazz)) {
                    return new GameContainer((IKryoGame) clazz.getDeclaredConstructor().newInstance(), classLoader);
                }
            }
        }

        return null;

    }
}
