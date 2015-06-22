package plugmeter.plugmeterapp;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
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
    Switch switch1;

    @AfterViews
    public void afterViews() {
        getCurrent();

        switch1.setChecked(true);
    }

    private void getCurrent() {
        App.getNet().get("current", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                textView.setText(getString(R.string.main_onstart));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    textView.setText(new String(response, "UTF-8"));
                    button.setVisibility(View.GONE);
                } catch (UnsupportedEncodingException e) {
                    textView.setText("UnsupportedEncodingException");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                textView.setText(getString(R.string.main_onFailure));
                button.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRetry(int retryNo) {
                textView.setText(getString(R.string.main_onretry));
            }
        });
    }

    @Click
    public void switch1() {
        if (switch1.isChecked()) {
            App.getNet().post("off", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i("plugmeter", "off onSuccess");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i("plugmeter", "off onFailure");
                }
            });
        } else {
            App.getNet().post("on", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i("plugmeter", "on onSuccess");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i("plugmeter", "on onFailure");
                }
            });
        }
    }

    @Click
    public void textView() {
        getCurrent();
    }

    @Click
    public void button() {
        SetupWifi_.intent(this).start();
    }
}
