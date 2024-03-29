package com.maalouf.webbrowser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.maalouf.webbrowser.R;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class WebBrowser extends Activity {
	
	EditText searchBar;
	Button searchButton;
	WebView webView;
	
	String searchContent;
	
	// the handler that gets the data from the thread and the displays the data
	final Handler loadContent = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			webView.loadData(String.valueOf(msg.obj), "text/html", "UTF-8");
			//return false;
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        searchBar = (EditText) findViewById(R.id.editText1); 
        searchButton = (Button) findViewById(R.id.button1);
        webView = (WebView) findViewById(R.id.webView1);
        
        
        searchButton.setOnClickListener(new View.OnClickListener() {
	  /* when the buttons gets click it opens a new connection using a new thread which grabs the 
	   * the HTML data puts it in a Message Object and passes it to the UI thread*/
       
			@Override
			public void onClick(View v) {
				
				Thread getURL = new Thread(){
				
			    @Override
				public void run(){
			
					if (isNetworkActive()){
						
						URL url = null;
						
						try {
							url = new URL(searchBar.getText().toString());
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(
											url.openStream()));
							
							String response = "", tmpResponse = "";
							
							tmpResponse = reader.readLine();
							while (tmpResponse != null){
								response = response + tmpResponse;
								tmpResponse = reader.readLine();
							}
							
							Message msg = loadContent.obtainMessage();
							
						
							msg.obj = response;
							
							loadContent.sendMessage(msg);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				}
			};
			
			getURL.start();
			
				
			}
		});
    }
    // Method that does the connection   
    public boolean isNetworkActive(){
    		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    		if (networkInfo != null && networkInfo.isConnected()) {
    			return true;
    		} else {
    			return false;
    		}
    }
}
