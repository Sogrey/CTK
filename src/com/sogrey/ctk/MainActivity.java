package com.sogrey.ctk;

import android.os.Bundle;

import com.sogrey.ctk.http.HttpClient;
import com.sogrey.ctk.http.HttpClient.HttpResponsListener;
import com.sogrey.frame.activity.base.BaseActivity;
import com.sogrey.frame.utils.ToastUtil;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ToastUtil.showToast(this, "开始");
		HttpClient mHttp= HttpClient.getInstance(this);
		mHttp.getData(0, _listener, "陕西");
	}

	private HttpResponsListener _listener = new HttpResponsListener() {
		
		@Override
		public void onSuccess(int flag, int status, String responseString) {
			// TODO Auto-generated method stub
			ToastUtil.showToast(MainActivity.this, responseString);
		}
		//□■
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
	public int setLayout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initDatas() {
		// TODO Auto-generated method stub
		
	}
}
