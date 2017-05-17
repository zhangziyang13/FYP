package com.example.fyp.popwindows;

import java.util.ArrayList;

import com.example.fyp.R;
import com.example.fyp.adapter.SelectAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

public class SelectPopWindow extends PopupWindow {
	private View conentView;
	private SelectAdapter selectAdapter;
	private ListView listView;
	private Activity context;
	private ArrayList<String> dataList = new ArrayList<String>();

	public void setDataList(ArrayList<String> dataList) {
		this.dataList = dataList;
		update();
	}

	public SelectPopWindow(Activity mContext, ArrayList<String> data, int num) {
		context = mContext;
		dataList = data;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.pop_window_select, null);
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(conentView);

		listView = (ListView) getContentView().findViewById(R.id.select_tips);
		selectAdapter = new SelectAdapter(context, dataList);
		listView.setAdapter(selectAdapter);
		View listItem = selectAdapter.getView(0, null, listView);
		listItem.measure(0, 0);
		int h = num * listItem.getMeasuredHeight()
				+ (listView.getDividerHeight() * (num + 1));

		this.setWidth(w);
		this.setHeight(h);
		this.setFocusable(true);
		this.setTouchable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
	}

	public void showPopupWindow(View parent) {
		this.showAsDropDown(parent);
	}
}
