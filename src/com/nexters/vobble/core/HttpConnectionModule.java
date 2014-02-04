package com.nexters.vobble.core;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HttpConnectionModule {
	public static String POST = "post";
    public static String GET = "get";

    private String method = "";
	private String url = "";

	private HttpClient client;
	private HttpPost post;
	private HttpGet get;
	private List<BasicNameValuePair> params;

	public HttpConnectionModule(String method, String url) {
		this.method = method;
		this.url = url;

		client = new DefaultHttpClient();

		if (method.equals(POST)) {
			post = new HttpPost(url);
		} else {
			get = new HttpGet(url);
		}

		params = new ArrayList<BasicNameValuePair>();
	}

	public void addParameter(String key, String value) {
		params.add(new BasicNameValuePair(key, value));
	}
	public String execute() {
        String result = "";
		try {
			HttpEntity resEntity;
			HttpResponse response;
			if (method.equals(POST)) {
                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				post.setEntity(ent);
				response = client.execute(post);
			} else {
				String buf = url + "?";
				for (int i = 0; i < params.size(); i++) {
					buf += params.get(i).getName() + "=" + params.get(i).getValue();
					if (i != params.size() - 1)
						buf += "&";
				}
				get.setURI(new URI(buf));
				response = client.execute(get);
			}
			resEntity = response.getEntity();
			result = EntityUtils.toString(resEntity, "utf8");
		} catch (Exception e) {
			e.printStackTrace();
		}
        return result;
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {

		return url;
	}
}