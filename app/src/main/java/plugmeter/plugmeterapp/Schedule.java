package plugmeter.plugmeterapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

@EActivity(R.layout.activity_agendamento)
public class Schedule extends AppCompatActivity {

    @ViewById
    EditText etxt_timerValue;

    @ViewById
    Switch switch1;

    @AfterViews
    public void afterViews() {
        switch1.setChecked(true);

        App.inst().getNet().get("relay", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody, "UTF-8");
                    switch1.setChecked(result.equals("on"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(App.LOGTAG, "Schedule onFailure");
            }
        });
    }

    @Click
    public void btn_schedule() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                App.inst().getNet().post("off", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.i(App.LOGTAG, "Schedule onSuccess");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i(App.LOGTAG, "Schedule onFailure");
                    }
                });
            }
        }, Integer.parseInt(etxt_timerValue.getText().toString()) * 1000);
    }

    @Click
    public void switch1() {
        final String postPath = switch1.isChecked() ? "off" : "on";

        App.inst().getNet().post(postPath, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(App.LOGTAG, postPath + " onSuccess");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(App.LOGTAG, postPath + " onFailure");
            }
        });
    }
}
