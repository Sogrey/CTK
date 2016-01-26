package com.sogrey.frame.views.pulltorefresh.pullableview;


public interface Pullable {
	/**
	 * 判断是否可以下拉，如果不需要下拉功能可以直接return false
	 * 
	 * @return true如果可以下拉否则返回false
	 */
	boolean canPullDown();

	/**
	 * 判断是否可以上拉，如果不需要上拉功能可以直接return false
	 * 
	 * @return true如果可以上拉否则返回false
	 */
	boolean canPullUp();

	/**
	 * 设置刷新状态（默认可以上拉下拉）<br>
	 * 
	 * {@link CanPullStatus.PULL_UP}只可上拉刷新<br>
	 * {@link CanPullStatus.PULL_DOWN}只可下拉刷新<br>
	 * {@link CanPullStatus.BOTH}可上拉下拉刷新<br>
	 * {@link CanPullStatus.NEITHER}禁止上拉下拉刷新<br>
	 * 
	 * @author Sogrey
	 * @date 2015-8-18 下午2:30:13
	 * @param canPullStatus
	 */
	public void setPullStatus(PullableStatus PullStatus);
}
