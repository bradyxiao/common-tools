package com.common.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpHelper {
	
	private static HttpEntity getHttpEntity(final String httpUrl) {
		HttpEntity httpEntity = null;
		HttpGet httpRequest = new HttpGet(httpUrl);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}
			httpEntity = httpResponse.getEntity();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return httpEntity;
	}
	
	
	public static byte[] getBuff(final String httpUrl) {
		HttpEntity httpEntity = getHttpEntity(httpUrl);
		try {
			return httpEntity == null ? null :  EntityUtils.toByteArray(httpEntity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getText(final String httpUrl) {
		HttpEntity httpEntity = getHttpEntity(httpUrl);
		try {
			return httpEntity == null ? null :  EntityUtils.toString(httpEntity, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
