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
public class Agendamento extends AppCompatActivity {

    @ViewById
    EditText editText;

    @ViewById
    Switch switch1;

    @AfterViews
    public void afterViews() {
        switch1.setChecked(false);
        App.getNet().get("relay", new AsyncHttpResponseHandler() {
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
                Log.i("plugmeter", "agendamento onfailure");
            }
        });
    }

    @Click
    public void button2() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                App.getNet().post("off", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.i("plugmeter", "agendamento success");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i("plugmeter", "agendamento failure");
                    }
                });
            }
        }, Integer.parseInt(editText.getText().toString()) * 1000);
    }

    @Click
    public void switch1() {
        if (!switch1.isChecked()) {
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
}
