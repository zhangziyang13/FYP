package com.example.fyp.tool;

import com.example.fyp.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ProgressAnmi extends ProgressDialog {
	private AnimationDrawable mAnimation;
	private Context mContext;
	private ImageView mImageView;
	private int mResid;

	public ProgressAnmi(Context context, int id) {
		super(context);
		this.mContext = context;
		this.mResid = id;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initData() {

		mImageView.setBackgroundResource(mResid);
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		mImageView.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();

			}
		});
	}

	private void initView() {
		setContentView(R.layout.progress_dialog);
		mImageView = (ImageView) findViewById(R.id.loading_image);
	}
}
