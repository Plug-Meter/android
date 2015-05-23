package plugmeter.plugmeterapp;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.apache.http.Header;

@EActivity(R.layout.activity_setup_wifi)
public class SetupWifi extends AppCompatActivity {

    ProgressDialog pd;

    @AfterViews
    protected void aoCriar() {
        pd = ProgressDialog.show(this, "", "Conectando na rede Plug Meter", true, true);

        App.getNet().connectToPlugMeterAP();

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            int retryNo = 0;

            @Override
            public void run() {
                if (!App.getNet().getWifiName().equals(Network.PM_SSID)) {
                    if (++retryNo < 3) {
                        handler.postDelayed(this, 3000);
                    } else {
                        onFailure();
                    }
                } else {
                    onSucess();
                }
            }
        };
        handler.postDelayed(r, 3000);
    }

    void onSucess() {
        App.getNet().get("visibleNetworks", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void onFailure() {
        Toast.makeText(this, "Não foi possível conectar na rede Plug Meter", Toast.LENGTH_SHORT).show();
        pd.dismiss();
        finish();
    }

}
