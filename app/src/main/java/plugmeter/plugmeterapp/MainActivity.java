package plugmeter.plugmeterapp;

import android.support.v7.app.AppCompatActivity;
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
    TextView textView;

    @AfterViews
    public void afterViews() {
        App.getNet().get("current", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                textView.setText("onStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    textView.setText(new String(response, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    textView.setText("UnsupportedEncodingException");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                textView.setText("Plug Meter não encontrado\nToque para configurar");
            }

            @Override
            public void onRetry(int retryNo) {
                textView.setText("onRetry");
            }
        });
    }

    @Click
    public void textView() {
        SetupWifi_.intent(this).start();
    }
}
