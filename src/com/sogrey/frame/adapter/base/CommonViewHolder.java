/**
 * @author Sogrey
 * @date 2015年5月11日
 */
package com.sogrey.frame.adapter.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 通用ViewHolder
 * 
 * @author Sogrey
 * @date 2015年5月11日
 */
public class CommonViewHolder {

	private SparseArray<View> mView;
	private Context mContext;
	private int mLayoutId;
	private int mPostion;
	private View mConvertView;

	/**
	 * @author Sogrey
	 * 
	 * @date 2015年5月11日
	 */

	private CommonViewHolder(Context context, ViewGroup parent, int layoutId,
			int postion) {
		this.mContext = context;
		this.mLayoutId = layoutId;
		this.mPostion = postion;

		this.mView = new SparseArray<View>();
		mConvertView = LayoutInflater.from(this.mContext).inflate(
				this.mLayoutId, null);
		mConvertView.setTag(this);
	}

	/**
	 * 获取ViewHolder
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @param context
	 *            上下文对象
	 * @param convertView
	 *            the convertView
	 * @param parent
	 *            the ViewGroup
	 * @param layoutId
	 *            the id of the layout
	 * @param position
	 *            the position of the item
	 * @return CommonViewHolder
	 */
	public static CommonViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new CommonViewHolder(context, parent, layoutId, position);
		} else {
			CommonViewHolder holder = (CommonViewHolder) convertView.getTag();
			holder.mPostion = position;
			return holder;
		}
	}

	/**
	 * 通过viewId获取控件对象
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @param viewId
	 *            控件ID
	 * @return View
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		/* 依据key(viewId)获取View控件 */
		View view = mView.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			/* 添加到控件队列：key:viewId,value:View */
			mView.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 获取convertView
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @return convertView
	 */
	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 获取当前条目的position
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @return position
	 */
	public int getPosition() {
		return mPostion;
	}

	/**
	 * 设置Tag
	 * 
	 * @author Sogrey
	 * @date 2015年8月14日
	 * @param viewId
	 * @param tag
	 * @return
	 */
	public CommonViewHolder setTag(int viewId, Object tag) {
		getView(viewId).setTag(tag);
		return this;
	}

	/**
	 * 设置指定key的Tag
	 * 
	 * @author Sogrey
	 * @date 2015年8月14日
	 * @param viewId
	 * @param key
	 * @param tag
	 * @return
	 */
	public CommonViewHolder setTag(int viewId, int key, Object tag) {
		getView(viewId).setTag(key, tag);
		return this;
	}

	/**
	 * 获取Tag 没有设置时为null
	 * 
	 * @author Sogrey
	 * @date 2015年8月14日
	 * @param viewId
	 * @return
	 */
	public Object getTag(int viewId) {
		return getView(viewId).getTag();
	}

	/**
	 * 获取指定key的Tag 没有设置时为null
	 * 
	 * @author Sogrey
	 * @date 2015年8月14日
	 * @param viewId
	 * @param key
	 * @return
	 */
	public Object getTag(int viewId, int key) {
		return getView(viewId).getTag(key);
	}

	/**
	 * 设置可见状态<br>
	 * 
	 * @author Sogrey
	 * @date 2015年8月14日
	 * @param viewId
	 * @param visibility
	 *            {@link View.VISIBLE},{@link View.GONE},{@link View.INVISIBLE}
	 * @return
	 */
	public CommonViewHolder setVisibility(int viewId, int visibility) {
		getView(viewId).setVisibility(visibility);
		return this;
	}

	/**
	 * 获取可见状态
	 * 
	 * @author Sogrey
	 * @date 2015年8月14日
	 * @param viewId
	 * @return {@link View.VISIBLE},{@link View.GONE},{@link View.INVISIBLE}
	 */
	public int getVisibility(int viewId) {
		return getView(viewId).getVisibility();
	}

	/**
	 * 设置是否可点击<br>
	 * 
	 * @author Sogrey
	 * @date 2015年8月14日
	 * @param viewId
	 * @param clickable
	 * @return
	 */
	public CommonViewHolder setClickable(int viewId, boolean clickable) {
		getView(viewId).setClickable(clickable);
		return this;
	}

	/**
	 * 是否可长按
	 * 
	 * @author Sogrey
	 * @date 2015年8月14日
	 * @param viewId
	 * @param clickable
	 * @return
	 */
	public CommonViewHolder setLongClickable(int viewId, boolean clickable) {
		getView(viewId).setLongClickable(clickable);
		return this;
	}

	/**
	 * 设置是否可用
	 * 
	 * @author Sogrey
	 * @date 2015年8月14日
	 * @param viewId
	 * @param enabled
	 * @return
	 */
	public CommonViewHolder setEnabled(int viewId, boolean enabled) {
		getView(viewId).setEnabled(enabled);
		return this;
	}

	/**
	 * 给TextView设置内容
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @param viewId
	 *            id of the TextView
	 * @param content
	 *            the content to set
	 * @return CommonViewHolder
	 */
	public CommonViewHolder setText_TextView(int viewId, String content) {
		((TextView) getView(viewId)).setText(content);
		return this;
	}

	/**
	 * 给EditText设置内容
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @param viewId
	 *            id of the EditText
	 * @param content
	 *            the content to set
	 * @return CommonViewHolder
	 */
	public CommonViewHolder setText_EditText(int viewId, String content) {
		((EditText) getView(viewId)).setText(content);
		return this;
	}

	/**
	 * 为ImageView设置资源-Bitmap
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @param viewId
	 *            id of the ImageView
	 * @param bm
	 *            the Bitmap to set
	 * @return CommonViewHolder
	 */
	public CommonViewHolder setImg_ImageView(int viewId, Bitmap bm) {
		((ImageView) getView(viewId)).setImageBitmap(bm);
		return this;
	}

	/**
	 * 为ImageView设置资源-Drawable
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @param viewId
	 *            id of the ImageView
	 * @param drawable
	 *            the Drawable to set
	 * @return CommonViewHolder
	 */
	public CommonViewHolder setImg_ImageView(int viewId, Drawable drawable) {
		((ImageView) getView(viewId)).setImageDrawable(drawable);
		return this;
	}

	/**
	 * 为ImageView设置资源-ResourceId
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @param viewId
	 *            id of the ImageView
	 * @param resId
	 *            the id of resource to set
	 * @return CommonViewHolder
	 */
	public CommonViewHolder setImg_ImageView(int viewId, int resId) {
		((ImageView) getView(viewId)).setImageResource(resId);
		return this;
	}

	/**
	 * 为ImageView设置资源-Uri
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @param viewId
	 *            id of the ImageView
	 * @param uri
	 *            the Uri to set
	 * @return CommonViewHolder
	 */
	public CommonViewHolder setImg_ImageView(int viewId, Uri uri) {
		((ImageView) getView(viewId)).setImageURI(uri);
		return this;
	}

	/**
	 * 为ImageView设置资源-Url(网络路径)
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @param viewId
	 *            id of the ImageView
	 * @param url
	 *            the url of resource to set
	 * @return CommonViewHolder
	 */
	public CommonViewHolder setImg_ImageView(int viewId, String url) {
		ImageLoader.getInstance()
				.displayImage(url, (ImageView) getView(viewId));
		return this;
	}

	/**
	 * 设置CheckBox选中与否
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @param viewId
	 *            the id of the CheckBox
	 * @param b
	 *            is checked
	 * @return
	 */
	public CommonViewHolder setChecked_CheckBox(int viewId, boolean b) {
		((CheckBox) getView(viewId)).setChecked(b);
		return this;
	}

	/**
	 * 设置RadioButton选中与否
	 * 
	 * @author Sogrey
	 * @date 2015年5月11日
	 * @param viewId
	 *            the id of the RadioButton
	 * @param b
	 *            is checked
	 * @return
	 */
	public CommonViewHolder setChecked_RadioButton(int viewId, boolean b) {
		((RadioButton) getView(viewId)).setChecked(b);
		return this;
	}

	/**
	 * 设置评星数
	 * 
	 * @author Sogrey
	 * @date 2015-10-26下午5:28:13
	 * @param rtbSoftlistItem
	 * @param ratingbar_size
	 */
	public CommonViewHolder setSize_RatingBar(int viewId, int ratingbar_size) {
		((RatingBar) getView(viewId)).setProgress(ratingbar_size);
		return this;
	}

	/**
	 * 设置评星（float）
	 * 
	 * @author Sogrey
	 * @date 2015-11-13上午11:06:17
	 * @param viewId
	 * @param rating
	 * @return
	 */
	public CommonViewHolder setRating_RatingBar(int viewId, float rating) {
		((RatingBar) getView(viewId)).setRating(rating);
		return this;
	}

	/**
	 * 设置View背景drawable
	 * 
	 * @author Sogrey
	 * @date 2015年5月27日
	 * @param viewId
	 * @param background
	 *            Drawable资源
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public CommonViewHolder setBackGround_View(int viewId, Drawable background) {
		getView(viewId).setBackground(background);
		return this;
	}

	/**
	 * 设置View背景颜色
	 * 
	 * @author Sogrey
	 * @date 2015年5月27日
	 * @param viewId
	 * @param color
	 *            颜色值
	 * @return
	 */
	public CommonViewHolder setBackGroundColor_View(int viewId, int color) {
		getView(viewId).setBackgroundColor(color);
		return this;
	}

	/**
	 * 设置View背景drawable
	 * 
	 * @author Sogrey
	 * @date 2015年5月27日
	 * @param viewId
	 * @param background
	 *            Drawable资源
	 * @return
	 */
	@Deprecated
	public CommonViewHolder setBackGroundDrawable_View(int viewId,
			Drawable background) {
		getView(viewId).setBackgroundDrawable(background);
		return this;
	}

	/**
	 * 设置View背景资源
	 * 
	 * @author Sogrey
	 * @date 2015年5月27日
	 * @param viewId
	 * @param resid
	 *            资源ID
	 * @return
	 */
	public CommonViewHolder setBackGroundResource_View(int viewId, int resid) {
		getView(viewId).setBackgroundResource(resid);
		return this;
	}

	/**
	 * 设置View 点击事件
	 * 
	 * @author Sogrey
	 * @date 2015年7月8日
	 * @param viewId
	 *            the id of the view
	 * @param l
	 *            the OnLongClickListener of view
	 * @return
	 */
	public CommonViewHolder setOnclickListener(int viewId, OnClickListener l) {
		setOnclickListener(getView(viewId), l);
		return this;
	}

	/**
	 * 设置View 点击事件
	 * 
	 * @author Sogrey
	 * @date 2015年7月8日
	 * @param v
	 *            the view is clicked
	 * @param l
	 *            the OnLongClickListener of view
	 * @return
	 */
	public CommonViewHolder setOnclickListener(View v, OnClickListener l) {
		v.setOnClickListener(l);
		return this;
	}

	/**
	 * 设置View 长按事件
	 * 
	 * @author Sogrey
	 * @date 2015年7月8日
	 * @param viewId
	 *            the id of the view
	 * @param l
	 *            the OnLongClickListener of view
	 * @return
	 */
	public CommonViewHolder setOnLongclickListener(int viewId,
			OnLongClickListener l) {
		setOnLongclickListener(getView(viewId), l);
		return this;
	}

	/**
	 * 设置View 长按事件
	 * 
	 * @author Sogrey
	 * @date 2015年7月8日
	 * @param v
	 *            the view is clicked
	 * @param l
	 *            the OnLongClickListener of view
	 * @return
	 */
	public CommonViewHolder setOnLongclickListener(View v, OnLongClickListener l) {
		v.setOnLongClickListener(l);
		return this;
	}

}
