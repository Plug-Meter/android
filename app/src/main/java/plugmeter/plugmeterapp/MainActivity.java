package plugmeter.plugmeterapp;

import android.support.v7.app.AppCompatActivity;
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


    @AfterViews
    public void afterViews() {
        getEstimate();

        /*
        App.getNet().get("", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String current = response.getString("corrente");
                    String rele = response.getString("rele");

                    switch1.setChecked(rele.equals("on"));
                    Log.i("plugmeter", rele);

                    textView.setText(current);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        */
    }
    private String convertToCurrency(String value){
        Float number = Float.parseFloat(value);
        String converted = String.format("%4.3f", number);
        String currency = getString(R.string.realBRLCurrency) + converted;
        return currency;
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
                    String responseText = new String(response, "UTF-8");
                    textView.setText(convertToCurrency(responseText));

                    button.setVisibility(View.GONE);
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
    public void agendar(){
        Agendamento_.intent(this).start();



    }

    @Click
    public void button() {
        SetupWifi_.intent(this).start();
    }
}
