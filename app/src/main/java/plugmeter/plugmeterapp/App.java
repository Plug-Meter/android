package plugmeter.plugmeterapp;

import android.app.Application;

public class App extends Application {

    private static App inst;
    private static Network network;
    private static boolean relayState;

    public App() {
        inst = this;
    }

    public static App inst() {
        return inst;
    }

    public static boolean getRelayState() {
        setRelayState(network.getRelayState());

        return relayState;
    }

    public static void setRelayState(boolean state) {
        relayState = state;
    }

    public static Network getNet() {
        if (network == null)
            network = new Network();
        return network;
    }
}
