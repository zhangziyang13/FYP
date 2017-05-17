package com.example.fyp.adapter;

import java.util.ArrayList;

import com.example.fyp.R;
import com.example.fyp.beans.Service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConfirmAdapter extends BaseAdapter {
	private ArrayList<Service> selectList = new ArrayList<Service>();
	private LayoutInflater inflater;
	private Context context;

	public void setSelectList(ArrayList<Service> selectList) {
		this.selectList = selectList;
		notifyDataSetChanged();
	}

	public ConfirmAdapter(Context mContext, ArrayList<Service> SelectList) {
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
			convertView = inflater.inflate(R.layout.item_confirm, null);
			holder = new Holder();
			holder.confirmName = (TextView) convertView
					.findViewById(R.id.confirm_name);
			holder.confirmPrice = (TextView) convertView
					.findViewById(R.id.confirm_price);
			holder.confirmNumber = (TextView) convertView
					.findViewById(R.id.confirm_number);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.confirmName.setText(selectList.get(position).getServiceName());
		holder.confirmPrice.setText("￥" + selectList.get(position).getPrice());
		holder.confirmNumber.setText("* "
				+ selectList.get(position).getNumber());

		return convertView;
	}

	private class Holder {

		TextView confirmName;
		TextView confirmPrice;
		TextView confirmNumber;
	}
}
