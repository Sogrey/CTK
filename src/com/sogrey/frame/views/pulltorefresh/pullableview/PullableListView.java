package com.sogrey.frame.views.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PullableListView extends ListView implements Pullable {
	private PullableStatus mPullableStatus = PullableStatus.BOTH;

	public PullableListView(Context context) {
		super(context);
	}

	public PullableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		if (mPullableStatus == PullableStatus.BOTH
				|| mPullableStatus == PullableStatus.PULL_DOWN) {
			if (getCount() == 0) {
				// 没有item的时候也可以下拉刷新
				return true;
			} else if (getFirstVisiblePosition() == 0
					&& getChildAt(0).getTop() >= 0) {
				// 滑到ListView的顶部了
				return true;
			} else
				return false;
		}
		return false;
	}

	@Override
	public boolean canPullUp() {
		if (mPullableStatus == PullableStatus.BOTH
				|| mPullableStatus == PullableStatus.PULL_UP) {
			if (getCount() == 0) {
				// 没有item的时候也可以上拉加载
				return true;
			} else if (getLastVisiblePosition() == (getCount() - 1)) {
				// 滑到底部了
				if (getChildAt(getLastVisiblePosition()
						- getFirstVisiblePosition()) != null
						&& getChildAt(
								getLastVisiblePosition()
										- getFirstVisiblePosition())
								.getBottom() <= getMeasuredHeight())
					return true;
			}
		}
		return false;
	}

	@Override
	public void setPullStatus(PullableStatus pullableStatus) {
		mPullableStatus = pullableStatus;
	}
}