package plugmeter.plugmeterapp;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class Network {

    public static final String SERVER_IP = "http://192.168.4.1/";
    public static final String PM_SSID = "Plug Meter 1";
    public static final String PM_PASS = "12345";

    private AsyncHttpClient client;

    public Network() {
        client = new AsyncHttpClient();
    }

    public void get(String path, AsyncHttpResponseHandler responseHandler) {
        client.get(SERVER_IP + path, responseHandler);
    }

    public void post(String path, AsyncHttpResponseHandler responseHandler) {
        client.post(SERVER_IP + path, responseHandler);
    }

    public String getWifiName() {
        WifiManager wifiMgr = (WifiManager) App.inst().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        return wifiInfo.getSSID().substring(1, wifiInfo.getSSID().length() - 1);
    }

    public void connectToAP(String ssid, String passkey) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        //wifiConfig.preSharedKey = String.format("\"%s\"", passkey);

        WifiManager wifiManager = ((WifiManager) App.inst().getSystemService(Context.WIFI_SERVICE));
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

    public void connectToPlugMeterAP() {
        connectToAP(PM_SSID, PM_PASS);
    }
}
