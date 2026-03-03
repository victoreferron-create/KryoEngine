package engine;

public class Main {

    public static boolean isRunning = true;

    public static void main(String[] args) {

        MainLoop.setTPS(20);
        MainLoop.mainloop(Main::test);

    }

    public static void test() {
        System.out.println("Elo");
    }


}
