package plugmeter.plugmeterapp;

import android.app.Application;

public class App extends Application{

    private static App inst;
    private static Network network;

    public App() {
        inst = this;
    }

    public static App inst() {
        return inst;
    }

    public static Network getNet() {
        if (network == null)
            network = new Network();
        return network;
    }
}
