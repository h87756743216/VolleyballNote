package com.example.volleyballnotes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class DBConnector {
	private static String php_file_url = null, result = null,DB=null;
	private static String[] php_post = null;
	private static String[] user_input = null;
	private static String one_line = null;
	public static void SendPostRequest(String url, String[] PHP_post_value,
			String[] data) {

		try {
			//set up the HTTP Post connect
			Toolbox.LogCenter("DBConnector","php file url", url);
			HttpPost httpPost = new HttpPost(url);
			//post variable should store by ArrayList<NameValuePair>
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			//Parse the array
			for (int i = 0; i < PHP_post_value.length; i++) {
				Toolbox.LogCenter("DBConnector","post variable", PHP_post_value[i] + ":" + data[i]);
				params.add(new BasicNameValuePair(PHP_post_value[i], data[i]));
			}
			//send HTTP request
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			//get HTTP response
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			//get the string
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
			StringBuilder builder = new StringBuilder();
			String line = null;
			//build the response string
			while ((line = bufReader.readLine()) != null) {
				builder.append(line + "\n");
			}
			inputStream.close();
			result = builder.toString(); 
		} catch (Exception e) {
			Log.e("SendPostRequest", e.toString());
		}
	}
	public static void SentSQLcmdOneLine(String url,String database,String one_line_SQLcmd) {
		try {
			Toolbox.LogCenter("DBConnector","php file url", url);
			Toolbox.LogCenter("DBConnector","database", database);
			Toolbox.LogCenter("DBConnector","one_line_SQLcmd", one_line_SQLcmd);
			HttpPost httpPost = new HttpPost(url);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("AccountDatabase", database));
			params.add(new BasicNameValuePair("SQLcommand", one_line_SQLcmd));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();
			BufferedReader bufReader = new BufferedReader(
					new InputStreamReader(inputStream, "utf-8"), 8);
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = bufReader.readLine()) != null) {
				builder.append(line + "\n");
			}
			inputStream.close();
			result = builder.toString();
		} catch (Exception e) {
			Log.e("log_tagDB", e.toString());
		}
	}
	public static String SQLCommandOneline(String url,String database,String cmd) {
		one_line = cmd;
		php_file_url = url;
		DB=database;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				SentSQLcmdOneLine(php_file_url,DB,one_line);
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.i("log_tagGD", e.toString());
			e.printStackTrace();
		}
		return result;
	}
	public static String SQLCommand(String url, String[] PHP_post_value,String[] data) {
		php_post = PHP_post_value;
		user_input = data;
		php_file_url = url;
		//run the Internet service at thread
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				SendPostRequest(php_file_url, php_post, user_input);

			}
		});
		thread.start();
		//wait the thread to finish
		try {
			thread.join();
		} catch (InterruptedException e) {
			Log.i("Wait thread", e.toString());
			e.printStackTrace();
		}
		return result;
	}

	
}