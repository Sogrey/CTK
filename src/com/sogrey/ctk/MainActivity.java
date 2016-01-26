package com.sogrey.ctk;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.sogrey.ctk.http.HttpClient;
import com.sogrey.ctk.http.HttpClient.HttpResponsListener;
import com.sogrey.frame.utils.ToastUtil;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ToastUtil.showToast(this, "开始");
		HttpClient mHttp= HttpClient.getInstance(this);
		mHttp.getData(0, _listener, "beijing");
	}

	private HttpResponsListener _listener = new HttpResponsListener() {
		
		@Override
		public void onSuccess(int flag, int status, String responseString) {
			// TODO Auto-generated method stub
			ToastUtil.showToast(MainActivity.this, responseString);
		}
		
		@Override
		public void onError(int flag, int status, String responseString, Exception e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onComplete(int flag) {
			// TODO Auto-generated method stub
			
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
