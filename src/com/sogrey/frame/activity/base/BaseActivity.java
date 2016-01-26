package com.sogrey.frame.activity.base;

import java.util.LinkedHashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sogrey.ctk.R;
import com.sogrey.ctk.app.BaseApplication;
import com.sogrey.frame.utils.ToastUtil;
import com.sogrey.frame.views.DialogUtils;

/**
 * 基本Activity类;
 * 
 * @author Yu L.
 * 
 */
public abstract class BaseActivity extends FragmentActivity {

	private ActivityManager manager = ActivityManager.getActivityManager();

	private LinkedHashMap<String, Boolean> mLinkedMap;
	private String mLoginId = "";
	private DialogUtils mDailogUtils;
	public Context mcontext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mcontext = this;
		manager.putActivity(this);
		if (mLinkedMap == null) {
			mLinkedMap = new LinkedHashMap<String, Boolean>();
		}
		int layoutResID = setLayout();
		if (0 != layoutResID)
			setContentView(layoutResID);
		initViews();
		initDatas();
	}
	@Override
	protected void onResume() {
		super.onResume();
//		if(getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
//		 {
//		 //隐藏软键盘
//		 getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//		 getWindow().getAttributes().softInputMode=WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;
//		 } 
		
		WindowManager.LayoutParams params = getWindow().getAttributes();
        if (params.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            // 隐藏软键盘
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        }
		
//		// 隐藏输入法
//		InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//		// 显示或者隐藏输入法
//		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 设置布局xml文件ID
	 * 
	 * @author Sogrey
	 * @date 2015年5月19日
	 */
	public abstract int setLayout();

	/**
	 * 布局控件初始化
	 * 
	 * @author Sogrey
	 * @date 2015年5月19日
	 */
	public abstract void initViews();

	/**
	 * 设置数据
	 * 
	 * @author Sogrey
	 * @date 2015年5月19日
	 */
	public abstract void initDatas();

	// 退出提示;
	public void showExitDialog() {
		if (mDailogUtils != null && mDailogUtils.isShowing()) {
			mDailogUtils.dismiss();
		}
		if (mDailogUtils == null) {
			mDailogUtils = new DialogUtils(this, R.style.CircularDialog) {

				@Override
				public void ok() {
					exit();
					toCancle();
				}

				@Override
				public void cancle() {
					toCancle();
				}

				@Override
				public void ignore() {
				}
			};
			mDailogUtils.show();
			TextView tv = new TextView(this);
			tv.setText("确定退出程序？");
			tv.setGravity(Gravity.CENTER);
			mDailogUtils.setContent(tv);
			mDailogUtils.setDialogTitle("退出");
			mDailogUtils.setDialogCancleBtn("取消");
			mDailogUtils.setDialogOkBtn("确定");
			mDailogUtils.setDialogCancleBtnColor(getResources().getColor(
					R.color.s_dark_green));
			mDailogUtils.setDialogOkBtnColor(getResources().getColor(
					R.color.s_dark_red));
		} else {
			mDailogUtils.show();
		}
	}

	public void exit() {
		if (mLinkedMap != null) {
			mLinkedMap.clear();
			mLinkedMap = null;
		}
		manager.exit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		manager.removeActivity(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (TextUtils.equals("", mLoginId)) {
				if (System.currentTimeMillis()-BaseApplication.getInstance().dataFrist<2000) {
					exit();
				}else{
					ToastUtil.showToast(mcontext, "再次点击返回键退出");
					BaseApplication.getInstance().dataFrist = System.currentTimeMillis();
				}
//				showExitDialog();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void putNetWorkFlag(String key, boolean val) {
		if (mLinkedMap == null) {
			mLinkedMap = new LinkedHashMap<String, Boolean>();
		}
		mLinkedMap.put(key, val);
	}

	public boolean getNetWorkFlag(String key, boolean val) {
		if (mLinkedMap == null) {
			mLinkedMap = new LinkedHashMap<String, Boolean>();
		}
		if (mLinkedMap.containsKey(key)) {
			val = mLinkedMap.get(key);
		} else {
			val = false;
		}
		return val;
	}

	public void clearAllNetWorkFlag() {
		if (mLinkedMap == null) {
			mLinkedMap = new LinkedHashMap<String, Boolean>();
		}
		mLinkedMap.clear();
		mLinkedMap = null;
	}

	public void setLoginId(String content) {
		mLoginId = content;
	}
}
