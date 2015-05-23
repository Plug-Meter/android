package plugmeter.plugmeterapp;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_setup_wifi)
public class SetupWifi extends AppCompatActivity {

    public static final int MAX_RETRY = 4;
    public static final int RETRY_DELAY = 3000;

    ProgressDialog pd;

    @AfterViews
    public void afterViews() {
        pd = ProgressDialog.show(this, "", getString(R.string.setupwifi_after_views) + Network.PM_SSID, true, true);

        App.getNet().connectToPlugMeterAP();

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            int retryNo = 0;

            @Override
            public void run() {
                if (!App.getNet().getWifiName().equals(Network.PM_SSID)) {
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

    void onSucess() {
        pd.dismiss();

        Toast.makeText(this, getString(R.string.setupwifi_onsuccess) + Network.PM_SSID, Toast.LENGTH_SHORT).show();
    }

    private void onFailure() {
        Toast.makeText(this, getString(R.string.setupwifi_onfailure) + Network.PM_SSID, Toast.LENGTH_SHORT).show();
        pd.dismiss();
        finish();
    }

}
