package com.example.fyp.popwindows;

import java.util.ArrayList;

import com.example.fyp.R;
import com.example.fyp.adapter.SelectAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

public class SearchPopWindow extends PopupWindow {
	private View conentView;
	private Activity context;
	private SelectAdapter selectAdapter;
	private ListView listView;
	private ArrayList<String> dataList = new ArrayList<String>();

	public void setDataList(ArrayList<String> dataList) {
		this.dataList = dataList;
		update();
	}

	public SearchPopWindow(Activity mContext, ArrayList<String> data) {
		context = mContext;
		dataList = data;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.pop_window_search, null);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(conentView);

		listView = (ListView) getContentView().findViewById(
				R.id.list_pop_window);
		selectAdapter = new SelectAdapter(context, dataList);
		listView.setAdapter(selectAdapter);
		this.setWidth(w);
		this.setHeight(h);
		this.setAnimationStyle(R.style.popwin_anim_style);
		this.setFocusable(true);
		this.setTouchable(true);
		this.setOutsideTouchable(true);
		this.update();
	}

	public void showPopupWindow(View parent) {
		this.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
}
