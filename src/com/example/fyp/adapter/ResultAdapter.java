package com.example.fyp.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fyp.R;
import com.example.fyp.beans.Account;

public class ResultAdapter extends BaseAdapter {

	private ArrayList<Account> resultList = new ArrayList<Account>();
	private LayoutInflater inflater;
	private Context context;

	public void setResultList(ArrayList<Account> resultList) {
		this.resultList = resultList;
		notifyDataSetChanged();
	}

	public ResultAdapter(Context mContext, ArrayList<Account> ResultList) {
		this.resultList = ResultList;
		this.context = mContext;
		this.inflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return resultList.size();
	}

	@Override
	public Object getItem(int position) {
		return resultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_result, null);
			holder = new Holder();
			holder.resultImage = (ImageView) convertView
					.findViewById(R.id.result_image);
			holder.resultItem = (TextView) convertView
					.findViewById(R.id.result_item);
			holder.resultDistance = (TextView) convertView
					.findViewById(R.id.result_distance);
			holder.resultRate = (RatingBar) convertView
					.findViewById(R.id.result_rate);
			holder.resultPrice = (TextView) convertView
					.findViewById(R.id.result_price);
			holder.resultTraffic = (TextView) convertView
					.findViewById(R.id.result_traffic);
			holder.resultEvent = (TextView) convertView
					.findViewById(R.id.result_event);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Glide.with(context).load(resultList.get(position).getPicture())
				.placeholder(R.drawable.empty).error(R.drawable.empty)
				.into(holder.resultImage);
		holder.resultItem.setText(resultList.get(position).getRealname());
		if (resultList.get(position).getOverallRating() != null) {
			holder.resultRate.setRating(resultList.get(position)
					.getOverallRating().floatValue());
		} else {
			holder.resultRate.setRating(0);
		}
		if (resultList.get(position).getBudget() != null) {
			holder.resultPrice.setText("¥"
					+ resultList.get(position).getBudget() + "/per capita");
		} else {
			holder.resultPrice.setText("¥0/per capita");
		}
		holder.resultTraffic.setText(" good");
		holder.resultEvent.setText(" no");
		double distance = resultList.get(position).getDistance();
		int dis = 0;
		if (distance < 1000) {
			dis = (int) distance;
			holder.resultDistance.setText(dis + "m from you");
		} else {
			DecimalFormat df = new DecimalFormat("0.0");
			distance = distance / 1000;
			holder.resultDistance.setText(df.format(distance) + "km from you");
		}
		return convertView;
	}

	private class Holder {
		ImageView resultImage;
		TextView resultItem;
		TextView resultPrice;
		TextView resultDistance;
		RatingBar resultRate;
		TextView resultTraffic;
		TextView resultEvent;
	}

}
