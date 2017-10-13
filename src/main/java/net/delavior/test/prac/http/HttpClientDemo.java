package net.delavior.test.prac.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientDemo {
	private static final int CONN_REQ_TIMEOUT = 5000;
	private static final int CONN_TIMEOUT = 5000;
	private static final int SOCKET_TIMEOUT = 5000;

	public void postForm(Map<String, String> map) {
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			Iterator<String> keyIter = map.keySet().iterator();
			while (keyIter.hasNext()) {
				String key = keyIter.next();
				String val = map.get(key);
				nameValuePairs.add(new BasicNameValuePair(key, val));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs);
			post(formEntity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void postString(String requestMessage) {
		try {
			StringEntity stringEntity = new StringEntity(requestMessage);
			post(stringEntity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void post(HttpEntity requestEntity) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		InputStream in = null;
		try {
			// httpClient = HttpClients.createDefault();
			CookieStore cookieStore = new BasicCookieStore();
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONN_REQ_TIMEOUT)
			        .setConnectTimeout(CONN_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT)
			        .setCookieSpec(CookieSpecs.STANDARD).build();
			HttpHost proxyHost = new HttpHost("127.0.0.1", 1080);
			HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxyHost, DefaultSchemePortResolver.INSTANCE);
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(new AuthScope("127.0.0.1", 1080),
			        new UsernamePasswordCredentials("admin", "admin"));
			HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(3, true);
			httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setDefaultCookieStore(cookieStore)
			        .setRoutePlanner(routePlanner).setDefaultCredentialsProvider(credentialsProvider)
			        .setRetryHandler(retryHandler).build();
			String url = "127.0.0.1:8080";
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(requestEntity);
			httpPost.addHeader("Content-Type", "application/json");
			BasicClientCookie cookie = new BasicClientCookie("cookieName", "cookieValue");
			cookie.setDomain("127.0.0.1");
			cookie.setPath("/");
			cookie.setVersion(0);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, 2099);
			cookie.setExpiryDate(calendar.getTime());
			response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			String result = EntityUtils.toString(responseEntity);
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println(statusCode + "===" + result);
			in = responseEntity.getContent();
			List<Cookie> cookies = cookieStore.getCookies();
			if (!cookies.isEmpty()) {
				for (int i = 0; i < cookies.size(); i++) {
					Cookie eachCookie = cookies.get(i);
					System.out.println(eachCookie.getName() + "---" + eachCookie.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
