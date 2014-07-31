package com.victor.homelaunchvic.menu.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.victor.homelaunchvic.R;

public class MenuThirdHtmlFragment extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_statistical_list);
		// initView("http://www.baidu.com/");
		initView(getIntent().getStringExtra("url"));
	}

	private void initView(String url) {
		final WebView web = (WebView) findViewById(R.id.web_statistical);
		WebSettings settings = web.getSettings();
		settings.setAllowFileAccess(true);
		web.setWebViewClient(new WebViewClient() {// 取消webview上的文本链接，避免点击跳到浏览器上
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				web.clearCache(true);
				web.clearFormData();
				web.clearHistory();
			}

			@Override
			public void onPageFinished(WebView viewe, String url) {
				super.onPageFinished(viewe, url);
				web.clearCache(true);
				web.clearFormData();
				web.clearHistory();

			}

		});
		// try {
		// url = URLEncoder.encode(url, "utf-8");
		// } catch (UnsupportedEncodingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// try {
		web.loadUrl(url);
		// } catch (Exception e) {
		//
		// }
	}

}
