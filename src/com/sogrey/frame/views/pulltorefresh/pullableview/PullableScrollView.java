package com.sogrey.frame.views.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable {

	private PullableStatus mPullableStatus = PullableStatus.BOTH;

	public PullableScrollView(Context context) {
		super(context);
	}

	public PullableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		if (mPullableStatus == PullableStatus.BOTH
				|| mPullableStatus == PullableStatus.PULL_DOWN) {
			if (getScrollY() == 0)
				return true;
			else
				return false;
		}
		return false;
	}

	@Override
	public boolean canPullUp() {
		if (mPullableStatus == PullableStatus.BOTH
				|| mPullableStatus == PullableStatus.PULL_UP) {
			if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
				return true;
			else
				return false;
		}
		return false;
	}

	@Override
	public void setPullStatus(PullableStatus pullableStatus) {
		mPullableStatus = pullableStatus;
	}

}
