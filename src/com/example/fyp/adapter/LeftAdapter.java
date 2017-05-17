package com.example.fyp.adapter;

import java.util.ArrayList;

import com.example.fyp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LeftAdapter extends BaseAdapter {
	private ArrayList<String> selectList = new ArrayList<String>();
	private LayoutInflater inflater;
	private Context context;
	private int selectPositon = 0;

	public void setSelectList(ArrayList<String> selectList) {
		this.selectList = selectList;
		notifyDataSetChanged();
	}

	public LeftAdapter(Context mContext, ArrayList<String> SelectList) {
		this.selectList = SelectList;
		this.context = mContext;
		this.inflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return selectList.size();
	}

	@Override
	public Object getItem(int position) {
		return selectList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setSelectedPosition(int positon) {
		this.selectPositon = positon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_type, null);
			holder = new Holder();
			holder.typeName = (TextView) convertView
					.findViewById(R.id.type_name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.typeName.setText(selectList.get(position));
		if (selectPositon == position) {
			holder.typeName.setTextColor(context.getResources().getColor(
					R.color.skyblue));
		} else {
			holder.typeName.setTextColor(context.getResources().getColor(
					R.color.black));
		}
		return convertView;
	}

	private class Holder {

		TextView typeName;
	}
}
