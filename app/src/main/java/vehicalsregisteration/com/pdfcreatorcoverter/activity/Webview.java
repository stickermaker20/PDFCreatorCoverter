package vehicalsregisteration.com.pdfcreatorcoverter.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import vehicalsregisteration.com.pdfcreatorcoverter.R;

public class Webview extends AppCompatActivity {
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        webView = findViewById(R.id.webview_privacy_policy);
        webView.loadUrl("");

    }
}
