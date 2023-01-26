package com.erppdghs.erpp.dghs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    protected WebView mainWebView;
    // private ProgressBar mProgress;
    private Context mContext;
    private WebView mWebviewPop;

    private ProgressBar progress;

    private String url = "https://erppdghs.com/web/login";
    private String target_url_prefix = "https://erppdghs.com/web/login";

    public void onBackPressed() {

        if (mainWebView.isFocused() && mainWebView.canGoBack()) {
            mainWebView.goBack();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get main webview
        mainWebView = (WebView) findViewById(R.id.activity_main_webview);

        //progress = (ProgressBar) findViewById(R.id.progressBar);
        //progress.setMax(100);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mainWebView.setWebContentsDebuggingEnabled(true);
        }

        mainWebView.getSettings().setUserAgentString("example_android_app");

        // Cookie manager for the webview
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        // Get outer container
        //mContainer = (FrameLayout) findViewById(R.id.webview_frame);
//        if (!InternetConnection.checkNetworkConnection(this)) {
//            showAlert(this, "No network found",
//                    "Please check your internet settings.");
//        } else {

            // Settings
            WebSettings webSettings = mainWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            //webSettings.setAppCacheEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setSupportMultipleWindows(true);

            mainWebView.setWebViewClient(new MyCustomWebViewClient());
            mainWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

            mainWebView.setWebChromeClient(new MyCustomChromeClient());
            mainWebView.loadUrl(url);
       // }

    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.example_main, menu);
    // return true;
    // }

    private class MyCustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

//            progress.setProgress(0);
 //           progress.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String host = Uri.parse(url).getHost();
            //Toast.makeText(MainActivity.this, host,
            //Toast.LENGTH_SHORT).show();
            if (host.equals(target_url_prefix)) {
                // This is my web site, so do not override; let my WebView load
                // the page
                if (mWebviewPop != null) {
                    mWebviewPop.setVisibility(View.GONE);
                    //mContainer.removeView(mWebviewPop);
                    mWebviewPop = null;
                }
                return false;
            }

            if (host.contains("m.facebook.com") || host.contains("facebook.co")
                    || host.contains("google.co")
                    || host.contains("www.facebook.com")
                    || host.contains(".google.com")
                    || host.contains(".google.co")
                    || host.contains("accounts.google.com")
                    || host.contains("accounts.google.co.in")
                    || host.contains("www.accounts.google.com")
                    || host.contains("www.twitter.com")
                    || host.contains("secure.payu.in")
                    || host.contains("https://secure.payu.in")
                    || host.contains("oauth.googleusercontent.com")
                    || host.contains("content.googleapis.com")
                    || host.contains("ssl.gstatic.com")) {
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch
            // another Activity that handles URLs
            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            //startActivity(intent);
            //return true;
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            progress.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            Log.d("onReceivedSslError", "onReceivedSslError");
            // super.onReceivedSslError(view, handler, error);
        }
    }

//    public void setValue(int progress) {
//        this.progress.setProgress(progress);
//    }

    public void showAlert(Context context, String title, String text) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder.setMessage(text).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        finish();
                    }
                }).create().show();

    }

    private class MyCustomChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(mContext);
            mWebviewPop.setVerticalScrollBarEnabled(false);
            mWebviewPop.setHorizontalScrollBarEnabled(false);
            mWebviewPop.setWebViewClient(new MyCustomWebViewClient());
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.getSettings().setSavePassword(false);
            mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            //mContainer.addView(mWebviewPop);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // TODO Auto-generated method stub
            super.onProgressChanged(view, newProgress);
            //MainActivity.this.setValue(newProgress);
        }

        @Override
        public void onCloseWindow(WebView window) {
            Log.d("onCloseWindow", "called");
        }

    }
}