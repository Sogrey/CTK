package com.sogrey.frame.activity.base;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Activity;

/**
 * 管理类;
 * 
 * @author Yu L.
 *
 */
public class ActivityManager {

	private static ActivityManager activityManager;

	public static ActivityManager getActivityManager() {
		if (activityManager == null) {
			activityManager = new ActivityManager();
		}
		return activityManager;
	}

	private final HashMap<String, SoftReference<Activity>> taskMap = new HashMap<String, SoftReference<Activity>>();

	public final void putActivity(Activity atv) {
		taskMap.put(atv.toString(), new SoftReference<Activity>(atv));
	}

	public final void removeActivity(Activity atv) {
		taskMap.remove(atv.toString());
	}

	public final void exit() {
		for (Iterator<Entry<String, SoftReference<Activity>>> iterator = taskMap
				.entrySet().iterator(); iterator.hasNext();) {
			SoftReference<Activity> activityReference = iterator.next()
					.getValue();
			Activity activity = activityReference.get();
			if (activity != null) {
				activity.finish();
			}
		}
		taskMap.clear();
	}
}
