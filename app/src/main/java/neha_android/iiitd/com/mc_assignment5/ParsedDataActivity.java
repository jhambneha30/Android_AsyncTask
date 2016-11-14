package neha_android.iiitd.com.mc_assignment5;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParsedDataActivity extends AppCompatActivity {
    private Document htmlDocument;
    private String htmlPageUrl = "https://www.iiitd.ac.in/about";
    private TextView mTextViewParsedTitle;
    private TextView mTextViewParsedBody;
    private String htmlTitleInStringFormat;
    private String htmlBodyInStringFormat;
    private Button mButtonGetParsedTitle;
    //private Button mButtonGetParsedBody;
    private Element element;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsed_data);

        mTextViewParsedTitle = (TextView) findViewById(R.id.textViewParsedTitle);
        mButtonGetParsedTitle = (Button) findViewById(R.id.buttonGetParsedTitle);
        mButtonGetParsedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
            }

        });

        mTextViewParsedBody = (TextView) findViewById(R.id.textViewParsedBody);
        //mButtonGetParsedBody = (Button) findViewById(R.id.buttonGetParsedBody);

    }


    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                htmlTitleInStringFormat = htmlDocument.title();
                element = htmlDocument.select("div#node-10").first();
                htmlBodyInStringFormat=element.text();
                System.out.println("-----------------para---------------- : " + htmlBodyInStringFormat);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mTextViewParsedTitle.setText(htmlTitleInStringFormat);
            mTextViewParsedBody.setText(htmlBodyInStringFormat);
        }
    }



}
