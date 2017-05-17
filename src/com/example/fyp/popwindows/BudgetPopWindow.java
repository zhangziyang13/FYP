package com.example.fyp.popwindows;

import java.util.ArrayList;

import com.example.fyp.R;
import com.example.fyp.adapter.SelectAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

public class BudgetPopWindow extends PopupWindow {
	private View conentView;
	private Activity context;
	private SelectAdapter selectAdapter;
	private ListView listView;
	private ArrayList<String> dataList = new ArrayList<String>();
	private String dataInput = new String();
	private EditText inputMinBudget;
	private EditText inputMaxBudget;

	public void setDataList(ArrayList<String> dataList) {
		this.dataList = dataList;
		update();
	}

	public BudgetPopWindow(Activity mContext, ArrayList<String> data,
			String input) {
		context = mContext;
		dataList = data;
		dataInput = input;
		String min = (dataInput.split("-"))[0];
		String max = (dataInput.split("-"))[1];
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.pop_window_budget, null);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(conentView);

		listView = (ListView) getContentView().findViewById(R.id.list_budget);
		selectAdapter = new SelectAdapter(context, dataList);
		listView.setAdapter(selectAdapter);
		inputMinBudget = (EditText) getContentView().findViewById(
				R.id.input_min_budget);
		inputMaxBudget = (EditText) getContentView().findViewById(
				R.id.input_max_budget);
		inputMinBudget.setText(min.trim());
		inputMaxBudget.setText(max.trim());

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
