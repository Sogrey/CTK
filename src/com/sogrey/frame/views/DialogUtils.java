/**
 * @author Sogrey
 * @date 2015年5月26日
 */
package com.sogrey.frame.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sogrey.ctk.R;

/**
 * 对话框<br>
 * for example:<br>
 * 
 * <pre>
 * DialogUtils dialog = new DialogUtils(this, R.style.CircularDialog) {
 * 
 * 	&#064;Override
 * 	public void ok() {
 * 		Toast.makeText(MainActivity.this, &quot;你确定进入&quot;, Toast.LENGTH_SHORT).show();
 * 	}
 * 
 * 	&#064;Override
 * 	public void cancle() {
 * 		Toast.makeText(MainActivity.this, &quot;你确定取消&quot;, Toast.LENGTH_SHORT).show();
 * 	}
 * };
 * dialog.show();
 * TextView tv = new TextView(this);
 * tv.setText(&quot;首次游戏有丰厚奖励哦!&quot;);
 * dialog.setContent(tv);
 * dialog.setDialogTitle(&quot;提示&quot;);
 * dialog.setDialogCancleBtn(&quot;下次吧&quot;);
 * dialog.setDialogOkBtn(&quot;现在就去&quot;);
 * dialog.setDialogCancleBtnColor(getResources().getColor(R.color.s_green));
 * dialog.setDialogOkBtnColor(getResources().getColor(R.color.s_red));
 * </pre>
 * 
 * styles.xml<br>
 * 
 * 圆角对话框 <br>
 * 
 * <pre>
 *     <style name="CircularDialog" parent="@android:style/Theme.Dialog">
 *         <item name="android:windowFrame">@null< /item>
 *         <item name="android:windowIsFloating">true< /item>
 *         <item name="android:windowIsTranslucent">false< /item>
 *         <item name="android:windowNoTitle">true< /item>
 *         <item name="android:windowBackground">@drawable/dialog_bg< /item>
 *         <item name="android:backgroundDimEnabled">true< /item>
 *     < /style>
 * </pre>
 * 
 * @author Sogrey
 * @date 2015年5月26日
 */
public abstract class DialogUtils extends Dialog {

	private TextView mTxtTitle;
	private Button mBtnOk;
	private Button mBtnCancle;
	private LinearLayout mLytContent;
	private Context mContext;
	private LinearLayout mTxtTitleLine;
	private View mViewCancleSpace;
	private Button mBtnIgnore;
	private View mViewIgnoreSpace;

	protected DialogUtils(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		mContext = context;
	}

	public DialogUtils(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	public DialogUtils(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_utils_lyt);
		initView();
	}

	private void initView() {
		mTxtTitle = (TextView) findViewById(R.id.txt_dialog_title);
		mTxtTitleLine = (LinearLayout) findViewById(R.id.lyt_dialog_title_line);
		mTxtTitle.setVisibility(View.GONE);
		mTxtTitleLine.setVisibility(View.GONE);
		
		mLytContent = (LinearLayout) findViewById(R.id.lyt_dialog_content);
		mBtnCancle = (Button) findViewById(R.id.btn_dialog_cancle);
		mViewCancleSpace = findViewById(R.id.view_dialog_cancle);
		mBtnOk = (Button) findViewById(R.id.btn_dialog_ok);
		mBtnIgnore = (Button) findViewById(R.id.btn_dialog_ignore);
		mViewIgnoreSpace = findViewById(R.id.view_dialog_ignore);
		mBtnIgnore.setVisibility(View.GONE);
		mViewIgnoreSpace.setVisibility(View.GONE);
		mBtnCancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancle();
				toCancle();
			}
		});
		mBtnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ok();
				toCancle();
			}
		});
		mBtnIgnore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ignore();
				toCancle();
			}
		});
	}

	/**
	 * 忽略<br>
	 * @author Sogrey
	 * @date 2015-12-2上午10:32:14
	 */
	public abstract void ignore();

	/**
	 * 确定事件处理<br>
	 * 需主动调用 toCancle() 方法
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public abstract void ok();

	/**
	 * 取消事件处理<br>
	 * 需主动调用 toCancle() 方法
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public abstract void cancle();

	/**
	 * 对话框取消
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void toCancle() {
		this.cancel();
	}

	/**
	 * 是否可取消
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setCancleable(boolean isCancle) {
		this.setCancelable(isCancle);
	}

	/**
	 * 确定按钮是否可用
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setOkBtnClickable(boolean isClickable) {
		mBtnOk.setClickable(isClickable);
	}

	/**
	 * 取消按钮是否可用
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setCancleBtnClickable(boolean isClickable) {
		mBtnCancle.setClickable(isClickable);
	}
	/**
	 * 忽略按钮是否可用
	 * @author Sogrey
	 * @date 2015-12-2上午10:52:13
	 * @param isClickable
	 */
	public void setIgnoreBtnClickable(boolean isClickable) {
		mBtnCancle.setClickable(isClickable);
	}

	/**
	 * 移除对话框布局
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void removeContentView() {
		mLytContent.removeAllViews();
	}

	/**
	 * 设置对话框布局
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setContent(View v) {
		if (mLytContent.getChildCount() > 0) {
			mLytContent.removeAllViews();
		}
		mLytContent.addView(v, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	/**
	 * 设置对话框布局
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setContent(int layoutId) {
		View v = ((Activity) this.mContext).getLayoutInflater().inflate(
				layoutId, null);
		setContent(v);
	}

	/**
	 * 设置对话框标题颜色
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogTitleColor(int color) {
		mTxtTitle.setTextColor(color);
	}

	/**
	 * 设置对话框标题
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogTitle(String title) {
		if (!TextUtils.isEmpty(title)) {
			mTxtTitle.setText(title);
			mTxtTitle.setVisibility(View.VISIBLE);
			mTxtTitleLine.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置对话框标题
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogTitle(int titleId) {
		if (titleId != 0) {
			mTxtTitle.setText(titleId);
			mTxtTitle.setVisibility(View.VISIBLE);
			mTxtTitleLine.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置对话框取消按钮颜色
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogCancleBtnColor(int color) {
		mBtnCancle.setTextColor(color);
	}

	/**
	 * 设置对话框取消按钮文本
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogCancleBtn(String cancle) {
		mBtnCancle.setText(cancle);
	}

	/**
	 * 设置对话框取消按钮文本
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogCancleBtn(int cancleId) {
		mBtnCancle.setText(cancleId);
	}

	/**
	 * 设置对话框确定按钮颜色
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogOkBtnColor(int color) {
		mBtnOk.setTextColor(color);
	}

	/**
	 * 设置对话框确定按钮文本
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogOkBtn(String ok) {
		mBtnOk.setText(ok);
	}

	/**
	 * 设置对话框确定按钮文本
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogOkBtn(int ok) {
		mBtnOk.setText(ok);
	}
	/**
	 * 获取对话框确定按钮文本
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public String getDialogOkBtnText() {
		return mBtnOk.getText().toString();
	}
	/**
	 * 
	 * 设置对话框确定按钮颜色
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogIgnoreBtnColor(int color) {
		mBtnIgnore.setTextColor(color);
	}
	
	/**
	 * 设置对话框确定按钮文本
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogIgnoreBtn(String ok) {
		mBtnIgnore.setText(ok);
	}
	
	/**
	 * 设置对话框确定按钮文本
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public void setDialogIgnoreBtn(int ok) {
		mBtnIgnore.setText(ok);
	}

	/**
	 * 获取对话框确定按钮文本
	 * 
	 * @author Sogrey
	 * @date 2015年6月11日
	 */
	public String getDialogIgnoreBtnText() {
		return mBtnIgnore.getText().toString();
	}
	
	public void setDialogIgnoreBtnVisibility(boolean isVisibility){
		if (isVisibility) {
			mBtnIgnore.setVisibility(View.VISIBLE);
			mViewIgnoreSpace.setVisibility(View.VISIBLE);
		}else{
			mBtnIgnore.setVisibility(View.GONE);
			mViewIgnoreSpace.setVisibility(View.GONE);
		}
	}
}
