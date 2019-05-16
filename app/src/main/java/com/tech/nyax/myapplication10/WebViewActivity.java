package com.tech.nyax.myapplication10;

import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.view.View;

public class WebViewActivity extends Activity {

    private final static String TAG = WebViewActivity.class.getSimpleName();

    private WebView webViewDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_view_layout);

        webViewDisplay = findViewById(R.id.WebViewToDisplay);

        WebSettings webSettings = webViewDisplay.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webViewDisplay.getSettings().setBuiltInZoomControls(true);

        if (android.os.Build.VERSION.SDK_INT >= 11) {

            webViewDisplay.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            webViewDisplay.getSettings().setDisplayZoomControls(false);
        }

        //webViewDisplay.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);

        webViewDisplay.loadUrl("http://example.com");

        /*
         * Invoke Javascript function
         */

        webViewDisplay.loadUrl("javascript:testJsFunction('Hello World!')");

    }

    /**
     * Invoking a Javascript function
     */
    public void doSomething() {
        webViewDisplay.loadUrl("javascript:testAnotherFunction('Hello World Again!')");
    }

}