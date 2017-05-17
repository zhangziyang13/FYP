package com.example.fyp.function;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.example.fyp.R;
import com.example.fyp.adapter.ConfirmAdapter;
import com.example.fyp.beans.Service;
import com.example.fyp.tool.Constant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Confirm extends Activity {

	private ListView finishList;
	private ConfirmAdapter confirmAdapter;
	private TextView finishTotalPrice;
	private Button finishConfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		init();
	}

	private void init() {
		finishList = (ListView) findViewById(R.id.finish_list);
		finishTotalPrice = (TextView) findViewById(R.id.finish_totalprice);
		finishConfirm = (Button) findViewById(R.id.finish_confirm);
		confirmAdapter = new ConfirmAdapter(this, getServices());
		finishList.setAdapter(confirmAdapter);
		finishTotalPrice.setText(" ￥" + getTotalPrice());
		finishConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Confirm.this, Finish.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private ArrayList<Service> getServices() {
		ArrayList<Service> services = new ArrayList<Service>();
		for (Entry<String, Service> entry : Constant.cartList.entrySet()) {
			Service detail = entry.getValue();
			services.add(detail);
		}
		return services;
	}

	private double getTotalPrice() {
		double price = 0;
		for (Service service : Constant.cartList.values()) {
			price += (service.getPrice().doubleValue()) * (service.getNumber());
		}
		return price;
	}
}
