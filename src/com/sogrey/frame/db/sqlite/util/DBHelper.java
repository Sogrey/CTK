package com.sogrey.frame.db.sqlite.util;

import android.content.Context;

import com.sogrey.frame.db.ahibernate.util.MyDBHelper;

public class DBHelper extends MyDBHelper {
	private static final Class<?>[] clazz = {/*XXX.class*/};// 要初始化的表

	public DBHelper(Context context) {
		super(context, "Download.db", null, 2, clazz);
	}

}
