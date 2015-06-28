package plugmeter.plugmeterapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class SetupWifi {

    public static final int MAX_RETRY = 4;
    public static final int RETRY_DELAY = 3000;
    private Context context;

    ProgressDialog pd;

    public SetupWifi(Context paramContext) {
        context = paramContext;

        pd = ProgressDialog.show(App.inst(), "", context.getString(R.string.setupwifi_after_views) + Network.PM_SSID, true, true);

        App.inst().getNet().connectToPlugMeterAP();

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            int retryNo = 0;

            @Override
            public void run() {
                if (!App.inst().getNet().getWifiName().equals(Network.PM_SSID)) {
                    if (++retryNo < MAX_RETRY) {
                        handler.postDelayed(this, RETRY_DELAY);
                    } else {
                        onFailure();
                    }
                } else {
                    onSucess();
                }
            }
        };
        handler.postDelayed(r, RETRY_DELAY);
    }

    private void onSucess() {
        pd.dismiss();
        Toast.makeText(context, context.getString(R.string.setupwifi_onsuccess) + Network.PM_SSID, Toast.LENGTH_SHORT).show();
    }

    private void onFailure() {
        pd.dismiss();
        Toast.makeText(context, context.getString(R.string.setupwifi_onfailure) + Network.PM_SSID, Toast.LENGTH_SHORT).show();
    }

}
