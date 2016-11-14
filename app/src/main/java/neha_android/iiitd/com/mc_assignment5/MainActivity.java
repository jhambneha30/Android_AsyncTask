package neha_android.iiitd.com.mc_assignment5;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button mGetDataButton;
    private Button mParsedDataButton;
    private Button mClearButton;
    protected ProgressDialog dialog;

    //Code reference: https://developer.android.com/training/basics/network-ops/connecting.html
    private static final String DEBUG_TAG = "HttpExample";
    //private EditText urlText;
    private TextView mTextViewOp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewOp= (TextView) findViewById((R.id.textViewOutput));
        mClearButton=(Button) findViewById(R.id.buttonClear);
        mParsedDataButton =(Button) findViewById(R.id.buttonParsedData);
        mGetDataButton = (Button) findViewById(R.id.getDataBtn);
        // When user clicks button, calls AsyncTask.
        // Before attempting to fetch the URL, makes sure that there is a network connection.
        mGetDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // fetch data
                    new GetServerData().execute("https://www.iiitd.ac.in/about");
                } else {
                    // display error
                    Toast.makeText(getApplicationContext(),"Not connected to the network!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Takes to another activity where data is displayed without html tags using jsoup
        mParsedDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent parsedIntent = new Intent(view.getContext(), ParsedDataActivity.class);
                startActivity(parsedIntent);
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewOp.setText("");
            }
        });


    }


    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class GetServerData extends AsyncTask<String, Void, String> {

        // Call after onPreExecute method
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            mTextViewOp.setText(result);
        }

        // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            // Only display the first 200 characters of the retrieved web page content

            int lenForActivity = 1000;
            int lenForConsole = 10000;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsStringForDisplay = readIt(is, lenForActivity);
                String contentAsStringForConsole = readIt(is, lenForConsole);
                Log.d(DEBUG_TAG, "The content retrieved is: " + contentAsStringForConsole);
                return contentAsStringForDisplay;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        // Reads an InputStream and converts it to a String.
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }
}
