package com.sogrey.ctk.app;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.baidu.apistore.sdk.ApiStoreSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sogrey.ctk.BuildConfig;
import com.sogrey.ctk.R;
import com.sogrey.ctk.utils.Constant;
import com.sogrey.frame.utils.FileUtil;

/**
 * 全局管理类;
 * 
 * @author Yu L.
 * 
 */
public class BaseApplication extends Application {

	// 屏幕像素;
	public static int wpx = 0;
	public static int hpx = 0;

	private static BaseApplication sInstance;

	private static Context applicationContext;

	public static final String TAG = "BaseApplication";

	public long dataFrist = 0l;
	@Override
	public void onCreate() {
		ApiStoreSDK.init(this, Constant.api_key/*您的apikey*/);
		super.onCreate();
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		WindowManager mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mWm.getDefaultDisplay().getMetrics(mDisplayMetrics);
		wpx = mDisplayMetrics.widthPixels;
		hpx = mDisplayMetrics.heightPixels;
		sInstance = this;
		applicationContext = this.getApplicationContext();

		// 极光
//		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
//		JPushInterface.init(this); // 初始化 JPush

		ImageLoader.getInstance().init(getSimpleConfig());
		if (!BuildConfig.DEBUG)
			CrashHandler.getInstance().init(applicationContext);// 初始化异常监控

	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static Context getContext() {
		return applicationContext;
	}

	public static synchronized BaseApplication getInstance() {
		return sInstance;
	}

	/**
	 * 比较常用的配置方案
	 * 
	 * @return
	 */
	private ImageLoaderConfiguration getSimpleConfig() {
		// 设置缓存的路径
		File cacheDir = null;
		try {
			cacheDir = new File(
					FileUtil.getImageDownloadDir(applicationContext));
		} catch (Exception e) {
			cacheDir = getCacheDir();// 文件所在目录
		}
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.pic_empty)
				.showImageOnFail(R.drawable.pic_err)
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).memoryCacheExtraOptions(480, 800)
				// 即保存的每个缓存文件的最大长宽
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// 线程池中线程的个数
				.denyCacheImageMultipleSizesInMemory()
				// 禁止缓存多张图片
				.memoryCache(new LRULimitedMemoryCache(40 * 1024 * 1024))
				// 缓存策略
				.memoryCacheSize(15 * 1024 * 1024)
				// 设置内存缓存的大小
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// 缓存文件名的保存方式
				.tasksProcessingOrder(QueueProcessingType.LIFO) // 工作队列
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.discCache(new UnlimitedDiscCache(cacheDir)) // 自定义缓存路径
				.writeDebugLogs() // Remove for release app
				.build();
		return config;
	}
}
