package plugmeter.plugmeterapp;

import android.app.Application;

public class App extends Application {

    private static App inst;
    private static Network network;

    public static String LOGTAG = "pmlogtag";

    public App() {
        inst = this;
    }

    public static App inst() {
        return inst;
    }

    public Network getNet() {
        if (network == null)
            network = new Network();

        return network;
    }
}
