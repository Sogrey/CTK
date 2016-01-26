/**
 * @author Sogrey
 * @date 2016-1-26上午11:52:51
 */
package com.sogrey.ctk.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.sogrey.frame.utils.LogUtil;
import com.sogrey.frame.utils.NetUtils;

/**
 * @author Sogrey
 * @date 2016-1-26上午11:52:51
 */
public class HttpClient {
	private static final String TAG = "HttpClient";
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

	private Context mContext;

	private HttpClient(Context context) {
		mContext = context;
	}

	public void getData(int flag, HttpResponsListener _listener,
			String... params) {
		if (!NetUtils.isConnected(mContext)) {
			// ToastUtil.showToast(mContext, "暂无网络");
			if (_listener != null) {
				_listener.onError(flag, -99, "暂无网络", null);
			}
			return;
		}
		switch (flag) {
		case 1:
			//TODO 
			break;

		default:
			GatDataFromNetwork(flag, ApiStoreSDK.GET,
					"http://apis.baidu.com/apistore/weatherservice/citylist",
					_listener, params);
			break;
		}
	}

	/**
	 * 参数列表
	 * 
	 * @author Sogrey
	 * @param flag
	 * @date 2016-1-26下午2:42:44
	 * @param url
	 * @param url2
	 * @param params
	 * @return
	 */
	private Parameters getParameters(int flag, String method, String url,
			String... params) {
		Parameters para = new Parameters();
		if (params == null || params.length < 1) {
			return para;
		}
		// URL 编码
//		for (int i = 0; i < params.length; i++) {
//			try {
//				params[i] = URLEncoder.encode(params[i], "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//		}

		switch (flag) {
		case 1:

			break;

		default:
			para.put("cityname", params[0]);
			break;
		}

		// 打印
		LogUtil.e(TAG, method + "请求，url=" + url);
		LogUtil.e(TAG, "↓↓↓↓↓↓↓↓↓↓参数列表↓↓↓↓↓↓↓↓↓↓");
		Iterator<Map.Entry<String, String>> it = para.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = it.next();
			LogUtil.e(TAG, entry.getKey() + " = " + entry.getValue());
		}
		LogUtil.e(TAG, "↑↑↑↑↑↑↑↑↑↑参数列表↑↑↑↑↑↑↑↑↑↑");
		return para;
	}

	/**
	 * 调用网络接口获取数据
	 * 
	 * @author Sogrey
	 * @date 2016-1-26下午2:20:51
	 * @param flag
	 *            接口请求标识，区分请求的那个接口
	 * @param method
	 *            接口请求方法{@linkplain ApiStoreSDK.GET} 和
	 *            {@linkplain ApiStoreSDK.POST}
	 * @param url
	 *            接口地址
	 * @param params
	 *            参数列表
	 * @param callback
	 *            接口回调方法
	 */
	private void GatDataFromNetwork(final int flag, String method, String url,
			final HttpResponsListener _listener, String... params) {

		Parameters para = getParameters(flag, method, url, params);

		ApiStoreSDK.execute(url, method, para, new ApiCallBack() {
			@Override
			public void onSuccess(int status, String responseString) {
				LogUtil.i("sdkdemo", "onSuccess>>>" + responseString);
				if (_listener != null) {
					_listener.onSuccess(flag, status, responseString);
				}
			}

			@Override
			public void onComplete() {
				LogUtil.i("sdkdemo", "onComplete");
				if (_listener != null) {
					_listener.onComplete(flag);
				}
			}

			@Override
			public void onError(int status, String responseString, Exception e) {
				LogUtil.i("sdkdemo", "onError, status: " + status
						+ ",responseString: " + responseString);
				LogUtil.i("sdkdemo",
						"errMsg: " + (e == null ? "" : e.getMessage()));
				if (_listener != null) {
					_listener.onError(flag, status, responseString, e);
				}
			}

		});
	}

	public interface HttpResponsListener {
		/**
		 * 请求成功
		 * 
		 * @author Sogrey
		 * @date 2016-1-26下午2:26:35
		 * @param flag
		 *            接口请求标识，区分请求的那个接口
		 * @param status
		 * @param responseString
		 */
		public void onSuccess(int flag, int status, String responseString);

		/**
		 * 请求完成（完成在最后）
		 * 
		 * @author Sogrey
		 * @date 2016-1-26下午2:32:37
		 * @param flag
		 */
		public void onComplete(int flag);

		/**
		 * 请求失败
		 * 
		 * @author Sogrey
		 * @date 2016-1-26下午2:29:50
		 * @param flag
		 * @param status
		 *            <pre>
		 * -1 没有检测到当前网络； 
		 * -3 没有进行初始化; 
		 * -4 传参错误
		 * 其他数值是http状态码，或服务器响应的errNum，请查阅响应字符串responseString
		 * </pre>
		 * @param responseString
		 *            响应数据
		 * @param e
		 *            异常
		 */
		public void onError(int flag, int status, String responseString,
				Exception e);
	}

}
