package com.sogrey.frame.views.pulltorefresh.pullableview;

/**
 * 刷新状态<br>
 * {@link CanPullStatus.PULL_UP}只可上拉刷新<br>
 * {@link CanPullStatus.PULL_DOWN}只可下拉刷新<br>
 * {@link CanPullStatus.BOTH}可上拉下拉刷新<br>
 * {@link CanPullStatus.NEITHER}禁止上拉下拉刷新<br>
 * @author Sogrey
 * @date 2015-8-18 下午5:01:25
 */
public enum PullableStatus
{
	PULL_UP, 
	PULL_DOWN, 
	BOTH,
	NEITHER
}
