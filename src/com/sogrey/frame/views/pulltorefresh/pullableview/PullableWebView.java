package com.sogrey.frame.views.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class PullableWebView extends WebView implements Pullable {
	private PullableStatus mPullableStatus = PullableStatus.BOTH;

	public PullableWebView(Context context) {
		super(context);
	}

	public PullableWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		if (mPullableStatus == PullableStatus.BOTH
				|| mPullableStatus == PullableStatus.PULL_DOWN) {
			if (getScrollY() == 0)
				return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canPullUp() {
		if (mPullableStatus == PullableStatus.BOTH
				|| mPullableStatus == PullableStatus.PULL_UP) {
			if (getScrollY() >= getContentHeight() * getScale()
					- getMeasuredHeight())
				return true;
		}
		return false;
	}

	@Override
	public void setPullStatus(PullableStatus pullableStatus) {
		mPullableStatus = pullableStatus;
	}
}
