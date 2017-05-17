package com.example.fyp.adapter;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.example.fyp.R;
import com.example.fyp.beans.Service;
import com.example.fyp.tool.Constant;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RightAdapter extends BaseAdapter implements OnClickListener {
	private ArrayList<Service> selectList = new ArrayList<Service>();
	private LayoutInflater inflater;
	private Context context;
	private Handler handler;

	public void setSelectList(ArrayList<Service> selectList) {
		this.selectList = selectList;
		notifyDataSetChanged();
	}

	public RightAdapter(Context mContext, ArrayList<Service> SelectList,
			Handler mHandler) {
		this.selectList = SelectList;
		this.context = mContext;
		this.handler = mHandler;
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
			convertView = inflater.inflate(R.layout.item_service, null);
			holder = new Holder();
			holder.serviceName = (TextView) convertView
					.findViewById(R.id.service_name);
			holder.serviceImage = (ImageView) convertView
					.findViewById(R.id.service_image);
			holder.serviceDescription = (TextView) convertView
					.findViewById(R.id.service_description);
			holder.servicePrice = (TextView) convertView
					.findViewById(R.id.service_price);
			holder.serviceMinus = (ImageView) convertView
					.findViewById(R.id.service_minus);
			holder.serviceAdd = (ImageView) convertView
					.findViewById(R.id.service_add);
			holder.serviceNumber = (TextView) convertView
					.findViewById(R.id.service_number);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		Glide.with(context).load(selectList.get(position).getPicture())
				.placeholder(R.drawable.empty).error(R.drawable.empty)
				.into(holder.serviceImage);
		holder.serviceName.setText(selectList.get(position).getServiceName());
		holder.serviceDescription.setText(selectList.get(position)
				.getDescription());
		holder.servicePrice.setText("￥" + selectList.get(position).getPrice());
		holder.serviceNumber.setText(selectList.get(position).getNumber() + "");
		holder.serviceMinus.setTag(holder);
		holder.serviceMinus.setTag(R.id.service_number,
				selectList.get(position));
		holder.serviceAdd.setTag(holder);
		holder.serviceAdd.setTag(R.id.service_number, selectList.get(position));

		holder.serviceMinus.setOnClickListener(this);
		holder.serviceAdd.setOnClickListener(this);
		return convertView;
	}

	private class Holder {

		TextView serviceName;
		ImageView serviceImage;
		TextView serviceDescription;
		TextView servicePrice;
		ImageView serviceMinus;
		ImageView serviceAdd;
		TextView serviceNumber;
	}

	@Override
	public void onClick(View arg0) {
		Holder holder = (Holder) arg0.getTag();
		Service service = (Service) arg0.getTag(R.id.service_number);
		switch (arg0.getId()) {
		case R.id.service_add: {
			String serviceNumber = holder.serviceNumber.getText().toString()
					.trim();
			service.setNumber(Integer.parseInt(serviceNumber) + 1);
			Constant.cartList.put(service.getId().toString(), service);
			holder.serviceNumber.setText(Integer.parseInt(serviceNumber) + 1
					+ "");
			handler.sendEmptyMessage(0x003);
			break;
		}
		case R.id.service_minus: {
			String serviceNumber = holder.serviceNumber.getText().toString()
					.trim();
			if ("0".equals(serviceNumber)) {
				Toast.makeText(context, "Not less than 0", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			service.setNumber(Integer.parseInt(serviceNumber) - 1);
			if (Integer.parseInt(serviceNumber) - 1 == 0) {
				Constant.cartList.remove(service.getId().toString());
			} else {
				Constant.cartList.put(service.getId().toString(), service);
			}
			holder.serviceNumber.setText(Integer.parseInt(serviceNumber) - 1
					+ "");
			handler.sendEmptyMessage(0x003);
			break;
		}
		default:
			break;
		}
	}
}
