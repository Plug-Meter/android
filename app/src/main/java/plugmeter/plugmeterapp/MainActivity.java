package plugmeter.plugmeterapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

    @ViewById
    Button button;

    @ViewById
    TextView textView;

    @ViewById
    Button agendar;

    final Handler h = new Handler();
    Runnable r;

    @AfterViews
    public void afterViews() {
        updateCost();
    }

    private void updateCost() {
        r = new Runnable() {
            @Override
            public void run() {
                getEstimate();
                Log.i(App.LOGTAG, "runnable");
                h.postDelayed(this, App.UPDATE_FREQ);
            }
        };

        h.post(r);
    }

    @Override
    protected void onPause() {
        super.onPause();

        h.removeCallbacksAndMessages(null);
        Log.i(App.LOGTAG, "onPause");
    }

    private void getEstimate() {
        App.getNet().get("custo_estimado", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                textView.setText(getString(R.string.main_onstart));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    textView.setText(new String(response, "UTF-8"));
                    //button.setVisibility(View.GONE);
                } catch (UnsupportedEncodingException e) {
                    textView.setText("UnsupportedEncodingException");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                textView.setText(getString(R.string.main_onFailure));
                //button.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRetry(int retryNo) {
                textView.setText(getString(R.string.main_onretry));
            }
        });
    }

    @Click
    public void textView() {
        getEstimate();
    }

    @Click
    public void agendar() {
        Agendamento_.intent(this).start();
    }

    @Click
    public void button() {
        SetupWifi_.intent(this).start();
    }
}
