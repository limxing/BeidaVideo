package me.leefeng.beida.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.beida.BaseActivity;
import me.leefeng.beida.R;

/**
 * Created by limxing on 2017/5/20.
 */

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.webview_view)
    WebView webviewView;

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra("url");
        webviewView.loadUrl(url);
    }

    @Override
    protected void initView() {
        WebSettings setting = webviewView.getSettings();
        setting.setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        setting.setJavaScriptEnabled(true);
        webviewView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                promptDialog.showLoading("0 %");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            //处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }
        });
        webviewView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
               if (newProgress==100){
                   promptDialog.dismissImmediately();
               }else{
                   promptDialog.showLoading(newProgress+" %");
               }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleName.setText(title);
            }
        });


    }

    @Override
    protected int getContentView() {
        return R.layout.activity_webview;
    }

    @Override
    protected void doReceive(Intent action) {

    }


    @OnClick(R.id.title_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webviewView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webviewView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webviewView.destroy();
    }
}