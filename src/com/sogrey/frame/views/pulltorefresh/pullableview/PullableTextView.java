package com.sogrey.frame.views.pulltorefresh.pullableview;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class PullableTextView extends TextView implements Pullable
{

	private PullableStatus mPullableStatus=PullableStatus.BOTH;
	public PullableTextView(Context context)
	{
		super(context);
	}

	public PullableTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		if (mPullableStatus==PullableStatus.BOTH||mPullableStatus==PullableStatus.PULL_DOWN) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (mPullableStatus==PullableStatus.BOTH||mPullableStatus==PullableStatus.PULL_UP) {
			return true;
		}
		return false;
	}

	@Override
	public void setPullStatus(PullableStatus pullableStatus) {
		mPullableStatus= pullableStatus;
	}
	
}
