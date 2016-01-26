/**
 * @author Sogrey
 * 2015年3月18日
 */
package com.sogrey.frame.views;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sogrey.ctk.R;
import com.sogrey.ctk.app.BaseApplication;
import com.sogrey.frame.utils.DensityUtils;
import com.sogrey.frame.utils.ToastUtil;

/**
 * @author Sogrey 2015年3月18日
 */
public class POPWindow {

	private static POPWindow mPOPWindow;
	private LinearLayout mLytPoints;
	private Context mContext;
	private int mPageSize = 0;
	private ViewPager mVp;
	private PopupWindow popupWindow;
	private static final int GRDSIZE = 6;

	public synchronized static POPWindow GetSingleTon() {
		if (mPOPWindow == null) {
			mPOPWindow = new POPWindow();
		}
		return mPOPWindow;
	}


	// 关注我们**********************************
	public static final int TYPE_ABOUT_US_WX = 0x1;
	public static final int TYPE_ABOUT_US_WeiBo = 0x2;
	String number = "";

	
	
	/**
	 * 仿对话框（基础）
	 * @author Sogrey
	 * @date 2015-12-31下午2:04:16
	 * @param context
	 * @param title
	 * @param msg
	 * @param btnOk
	 * @param btnCancle
	 * @param mViewContent
	 * @param l
	 * @return
	 */
	public PopupWindow showWindow_BaseDialog(final Context context,String title, View child,String btnOk,String btnCancle,
			@NonNull final View mViewContent,final OnPopDialogBtnClickListener l) {
		// 找到布局文件
		View mViewRoot = LayoutInflater.from(context).inflate(
				R.layout.lyt_pop_dialog, null);
		TextView txtTitle = (TextView) mViewRoot
				.findViewById(R.id.txt_lyt_pop_dialog_title);
		LinearLayout lytRoot = (LinearLayout) mViewRoot
				.findViewById(R.id.lyt_pop_dialog_root);
		LinearLayout lytContent = (LinearLayout) mViewRoot
				.findViewById(R.id.lyt_lyt_pop_dialog_content);
		TextView txtCancle = (TextView) mViewRoot
				.findViewById(R.id.txt_lyt_pop_dialog_cancle);
		TextView txtOk = (TextView) mViewRoot
				.findViewById(R.id.txt_lyt_pop_dialog_ok);
		View vCancle =  mViewRoot
				.findViewById(R.id.v_lyt_pop_dialog_cancle);
		
		
		txtTitle.setText(title);
		if (TextUtils.isEmpty(btnCancle)) {
			txtCancle.setVisibility(View.GONE);
			vCancle.setVisibility(View.GONE);
		}else
			txtCancle.setText(btnCancle);
		txtOk.setText(btnOk);
		

		//添加内容
		lytContent.removeAllViews();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				BaseApplication.wpx*2/3,LinearLayout.LayoutParams.WRAP_CONTENT
				);
		lytContent.addView(child, lp);
		
		// 实例化一个PopuWindow对象
		popupWindow = new PopupWindow(mViewContent);
		
		// 设置弹框的宽度为布局文件的宽
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		// popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
		// 高度随着内容变化
		popupWindow.setHeight(LayoutParams.MATCH_PARENT);
		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击弹框外部，弹框消失
		popupWindow.setOutsideTouchable(true);
		// 设置焦点
		popupWindow.setFocusable(true);
		// 设置所在布局
		popupWindow.setContentView(mViewRoot);
		popupWindow.showAtLocation(mViewContent, Gravity.CENTER, 0, 0);
		OnClickListener _click = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.txt_lyt_pop_dialog_cancle:// 取消
					if (l != null) {
						l.post(popupWindow, 0);
					}
					popupWindow.dismiss();
					break;
				case R.id.txt_lyt_pop_dialog_ok:// 确定
					if (l != null) {
						l.post(popupWindow, 1);
					}
					break;
				case R.id.lyt_pop_dialog_root:
					popupWindow.dismiss();
					break;
				case R.id.txt_lyt_pop_dialog_title:
					break;
					
				default:
					break;
				}
			}
		};
		txtCancle.setOnClickListener(_click);
		txtOk.setOnClickListener(_click);
		lytRoot.setOnClickListener(_click);
		txtTitle.setOnClickListener(_click);
		return popupWindow;
	}
	public interface OnPopDialogBtnClickListener {
		/**
		 * 
		 * @author Sogrey
		 * @date 2015-12-18下午4:33:46
		 * @param index
		 *            0：取消；1：确定
		 */
		void post(PopupWindow pop,int index);
	}
	/**
	 * 普通对话框（消息主体为文本）<br>
	 * <pre>
	 * ╭───────────────────────────────╮
	 * │标题                                                    │
	 * ├──────────────────┤
	 * │消息内容                                            │
	 * ├────────┬─────────┤
	 * │    取消      │   确定           │
	 * ╰───────────────┴───────────────╯
	 * 
	 * </pre>
	 * @author Sogrey
	 * @date 2015-12-31下午2:33:59
	 * @param context
	 * @param title
	 * @param msg
	 * @param btnOk
	 * @param btnCancle
	 * @param mViewContent
	 * @param l
	 * @return
	 */
	public PopupWindow showWindow_Dialog(final Context context,String title, String msg,String btnOk,String btnCancle,
			@NonNull final View mViewContent,final OnPopDialogBtnClickListener l) {
		TextView tv = new TextView(context);
		tv.setClickable(false);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setMinHeight(DensityUtils.dp2px(context, 45));
		tv.setPadding(DensityUtils.dp2px(context, 10), DensityUtils.dp2px(context, 10),
				DensityUtils.dp2px(context, 10), DensityUtils.dp2px(context, 10));
		tv.setTextColor(context.getResources().getColor(R.color.s_dark_gray));
		tv.setTextSize(DensityUtils.sp2px(context, 10));
		tv.setText(msg);
		tv.setSingleLine(false);
		return showWindow_BaseDialog(context, title, tv, btnOk, btnCancle, mViewContent, l);
	}
}
