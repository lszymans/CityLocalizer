package pl.lukaszszymansk.citylocalizer.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import pl.lukaszszymansk.citylocalizer.R;
import pl.lukaszszymansk.citylocalizer.interfaces.Progressive;
import pl.lukaszszymansk.citylocalizer.services.UtilsService;

import static pl.lukaszszymansk.citylocalizer.R.id.webView;


public class WebPageActivity extends AppCompatActivity implements Progressive {

    private static final String EXTRA_WEBPAGE_URL = "EXTRA_WEBPAGE_URL";
    private static final String EXTRA_WEBPAGE_CONTENT = "EXTRA_WEBPAGE_CONTENT";
    private static final String EXTRA_WEBPAGE_TITLE = "EXTRA_WEBPAGE_TITLE";

    public static Intent getStartIntent(Context context, String url, String title, String content) {
        Intent intent = new Intent(context, WebPageActivity.class);
        intent.putExtra(EXTRA_WEBPAGE_URL, url);
        intent.putExtra(EXTRA_WEBPAGE_TITLE, title);
        intent.putExtra(EXTRA_WEBPAGE_CONTENT, content);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        Bundle extras = getIntent().getExtras();
        String url = extras.getString(EXTRA_WEBPAGE_URL);
        String title = extras.getString(EXTRA_WEBPAGE_TITLE);
        String content = extras.getString(EXTRA_WEBPAGE_CONTENT);

        setupActionBar(title);

        WebView webView = (WebView) findViewById(R.id.webView);

        if (!TextUtils.isEmpty(url)) {
            loadUrlToWebView(webView, url);
        } else if (!TextUtils.isEmpty(content)) {
            webView.loadData(content, "text/html", "UTF-8");
        }
    }

    private void setupActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if(title != null) {
                setTitle(title);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartProgress() {
        UtilsService.swapControls(this, R.id.progressBar, webView);
    }

    @Override
    public void onStopProgress() {
        UtilsService.swapControls(this, webView, R.id.progressBar);
    }

    private void loadUrlToWebView(WebView webView, String url) {
        onStartProgress();

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                onStopProgress();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                onStopProgress();
            }
        });

        webView.loadUrl(url);
    }
}
