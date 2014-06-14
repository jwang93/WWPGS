package com.jay.inspire;


import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import com.jay.inspire.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LinearLayout linearLayout = (LinearLayout)findViewById(R.layout.activity_main);
		
		
	    final TextView quote = (TextView)findViewById(R.id.quote);

		new LongRunningGetIO().execute();


	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	public void refresh(View view) {
		Button b = (Button)findViewById(R.id.my_button);
		Log.i("main", "entered onClick");
		b.setClickable(false);
		new LongRunningGetIO().execute();
	}
	
	private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
		
		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
	       InputStream in = entity.getContent();
	         StringBuffer out = new StringBuffer();
	         int n = 1;
	         while (n>0) {
	             byte[] b = new byte[4096];
	             n =  in.read(b);
	             if (n>0) out.append(new String(b, 0, n));
	         }
	         return out.toString();
	    }
		
		@Override
		protected String doInBackground(Void... params) {
			 HttpClient httpClient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
             HttpGet httpGet = new HttpGet("http://www.iheartquotes.com/api/v1/random?format=json&source=paul_graham");
             String text = null;
             String quote = "";
             try {
                   HttpResponse response = httpClient.execute(httpGet, localContext);
                   HttpEntity entity = response.getEntity();
                   text = getASCIIContentFromEntity(entity);
                   JSONObject jObject = new JSONObject(text);
                   quote = jObject.getString("quote");

             } catch (Exception e) {
            	 return e.getLocalizedMessage();
             }
             
             
             return quote;
		}	
		
		protected void onPostExecute(String results) {
			if (results!=null) {
				TextView tv = (TextView)findViewById(R.id.quote);
				tv.setText(Html.fromHtml(processString(results)));
			}
			Button b = (Button)findViewById(R.id.my_button);
			b.setClickable(true);

		}
		
		private String processString(String unprocessed) {
			
			int end = unprocessed.indexOf("Paul Graham");
			
			if (end == -1) return unprocessed;
			return unprocessed.substring(0, end);
					
			
		}
    }


	
	
	

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
