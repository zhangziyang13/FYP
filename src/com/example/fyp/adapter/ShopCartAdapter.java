package com.example.fyp.adapter;

import java.util.ArrayList;

import com.example.fyp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShopCartAdapter extends BaseAdapter {
	private ArrayList<String> selectList = new ArrayList<String>();
	private LayoutInflater inflater;
	private Context context;

	public void setSelectList(ArrayList<String> selectList) {
		this.selectList = selectList;
		notifyDataSetChanged();
	}

	public ShopCartAdapter(Context mContext, ArrayList<String> SelectList) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_select, null);
			holder = new Holder();
			holder.selectItem = (TextView) convertView
					.findViewById(R.id.select_item);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.selectItem.setText(selectList.get(position));

		return convertView;
	}

	private class Holder {

		TextView selectItem;
	}
}
