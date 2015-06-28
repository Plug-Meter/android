package plugmeter.plugmeterapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    public static final int UPDATE_FREQ = 10000;//10 SECONDS

    @ViewById
    TextView txtview_cost;

    @ViewById
    Button btn_schedule;

    final Handler h = new Handler();

    @AfterViews
    public void afterViews() {
        updateCost();
    }

    private void updateCost() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Log.i(App.LOGTAG, "updateCost");
                getEstimate();
                h.postDelayed(this, UPDATE_FREQ);
            }
        };

        h.post(r);
    }

    private void getEstimate() {
        App.inst().getNet().get("custo_estimado", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                txtview_cost.setText(getString(R.string.main_onstart));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    String responseText = new String(response, "UTF-8");
                    txtview_cost.setText(convertToCurrency(responseText));
                } catch (UnsupportedEncodingException e) {
                    txtview_cost.setText("UnsupportedEncodingException");
                }
            }

            private String convertToCurrency(String value) {
                Float number = Float.parseFloat(value);
                String converted = String.format("%4.3f", number);
                return getString(R.string.realBRLCurrency) + converted;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                txtview_cost.setText(getString(R.string.main_onFailure));
            }

            @Override
            public void onRetry(int retryNo) {
                txtview_cost.setText(getString(R.string.main_onretry));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(App.LOGTAG, "main onPause");

        h.removeCallbacksAndMessages(null);
    }

    @Click
    public void txtview_cost() {
        getEstimate();
    }

    @Click
    public void btn_schedule() {
        Schedule_.intent(this).start();
    }
}
