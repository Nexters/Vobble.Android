package com.nexters.vobble.network;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {
	
	private static AsyncHttpClient client;
	
	private static AsyncHttpClient getClient() {
		if(client == null) {
			client = new AsyncHttpClient();
		}
		
		return client;
	}
	
	public static void get(String url, Map<String, String> headers, RequestParams params, AsyncHttpResponseHandler handler) {
		setHeaders(headers);
		getClient().get(url, params, handler);
	}

	public static void post(String url, Map<String, String> headers, RequestParams params, AsyncHttpResponseHandler handler) {
		setHeaders(headers);
		getClient().post(url, params, handler);
	}
	
	public static void put(String url, Map<String, String> headers, RequestParams params, AsyncHttpResponseHandler handler) {
		setHeaders(headers);
		getClient().put(url, params, handler);
	}
	
	public static void delete(String url, AsyncHttpResponseHandler handler) {
		getClient().delete(url, handler);
	}
	
	public static void setHeaders(Map<String, String> headers) {
		if(headers == null) {
			return;
		}
		
		for(Map.Entry<String, String> entry : headers.entrySet()) {
			getClient().addHeader(entry.getKey(), entry.getValue());
		}
	}
}
