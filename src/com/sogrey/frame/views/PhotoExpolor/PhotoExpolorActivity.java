package com.sogrey.frame.views.PhotoExpolor;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.sogrey.ctk.R;
import com.sogrey.frame.views.PhotoExpolor.view.ImageViewTouchViewPager;
import com.sogrey.frame.views.imagezoom.ImageViewTouch;
import com.sogrey.frame.views.imagezoom.ImageViewTouch.OnImageViewTouchSingleTapListener;

public class PhotoExpolorActivity extends Activity {
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";
	private int pagerPosition;
	private String[] urls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_photoexpolor);
		
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
		
		ImageView mImgBack = (ImageView) findViewById(R.id.img_imagepre_back);
		mImgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ImageViewTouchViewPager pager = (ImageViewTouchViewPager) findViewById(R.id.pager);
		ImageAdapter adapter = new ImageAdapter(urls);
		pager.setAdapter(adapter);
		
		pager.setCurrentItem(pagerPosition);
	}

	private class ImageAdapter extends PagerAdapter {

		private List<String> list;

		private int total;

		public ImageAdapter(String[] urls) {
			list = new ArrayList<String>();
			if (urls!=null)
			for (int i = 0; i < urls.length; i++) {
				list.add(urls[i]);
			}
			total = list.size();
		}

		@Override
		public int getCount() {
			return total;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == (View) obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View parent = LayoutInflater.from(PhotoExpolorActivity.this)
					.inflate(R.layout.act_photoexpolor_pager_item, null);
			ImageViewTouch mImage = (ImageViewTouch) parent
					.findViewById(R.id.image);
			TextView indicator = (TextView) parent.findViewById(R.id.indicator);
			
			CharSequence text = getString(R.string.viewpager_indicator,
					position + 1, getCount());
			indicator.setText(text);
			
			final ProgressBar progressBar = (ProgressBar) parent
					.findViewById(R.id.loading);
			mImage.setMaxScale(8.0f);
			mImage.setMinScale(1.0f);

			ImageLoader.getInstance().displayImage(list.get(position), mImage,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "下载错误";
								break;
							case DECODING_ERROR:
								message = "图片无法显示";
								break;
							case NETWORK_DENIED:
								message = "网络异常，无法下载";
								break;
							case OUT_OF_MEMORY:
								message = "图片太大无法显示";
								break;
							case UNKNOWN:
								message = "未知的错误";
								break;
							}
							Toast.makeText(PhotoExpolorActivity.this, message,
									Toast.LENGTH_SHORT).show();
							progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							progressBar.setVisibility(View.GONE);
						}
					});

			mImage.setTag(ImageViewTouchViewPager.VIEW_PAGER_OBJECT_TAG
					+ position);

			mImage.setSingleTapListener(new OnImageViewTouchSingleTapListener() {

				@Override
				public void onSingleTapConfirmed() {
					Log.d("##", "##onSingleTapConfirmed");
				}
			});
			((ViewPager) container).addView(parent, 0);
			
			return parent;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
