/**
 * @author Sogrey
 * @date 2016-1-26上午11:52:51
 */
package com.sogrey.ctk.utils;

import com.baidu.apistore.sdk.ApiCallBack;

import android.content.Context;

/**
 * @author Sogrey
 * @date 2016-1-26上午11:52:51
 */
public class HttpClient {
	/** 单例模式 对象 */
	private static HttpClient sInstance;

	/**
	 * 单例模式 <br>
	 * 一个类最多只能有一个实例 <br>
	 * 1、有一个私有静态成员 <br>
	 * 2、有一个公开静态方法getInstance得到这个私有静态成员 <br>
	 * 3、有一个私有的构造方法（不允许被实例化） <br>
	 */

	public static HttpClient getInstance(Context context) {
		if (sInstance == null) {
			synchronized (HttpClient.class) {
				if (sInstance == null) {
					sInstance = new HttpClient(context);
				}
			}
		}
		return sInstance;
	}

	private HttpClient(Context context) {
	}
	
	public void getData(int flag,ApiCallBack callback,String... params){
		
	}
}
