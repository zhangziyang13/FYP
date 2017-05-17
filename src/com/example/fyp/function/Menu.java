package com.example.fyp.function;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.fyp.R;
import com.example.fyp.adapter.LeftAdapter;
import com.example.fyp.adapter.RightAdapter;
import com.example.fyp.beans.Service;
import com.example.fyp.beans.Type;
import com.example.fyp.tool.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends Activity {

	private ListView leftListView, rightListView;
	private TextView confirmOrder, totalNum, totalPrice, shopName;
	private ImageView back;

	private Gson gson = new Gson();
	private LeftAdapter typeAdapter;
	private RightAdapter serviceAdapter;

	private ArrayList<Type> typeList = new ArrayList<Type>();
	private ArrayList<Service> serviceList = new ArrayList<Service>();
	private ArrayList<String> typeName = new ArrayList<String>();
	private String id = "";
	private String name = "";

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x001:
				Type firstType = typeList.get(0);
				getService(firstType.getId() + "");
				for (int i = 0; i < typeList.size(); i++) {
					typeName.add(typeList.get(i).getTypeName());
				}
				typeAdapter.setSelectList(typeName);
				break;
			case 0x002:
				for (int i = 0; i < serviceList.size(); i++) {
					Service service = serviceList.get(i);
					if (Constant.cartList.get(service.getId().toString()) != null) {
						service.setNumber(Constant.cartList.get(
								service.getId().toString()).getNumber());
					} else {
						service.setNumber(0);
					}
					serviceList.set(i, service);
				}
				serviceAdapter.setSelectList(serviceList);
				break;
			case 0x003:
				totalNum.setText(Constant.cartList.size() + "");
				totalPrice.setText("￥" + getTotalPrice() + "");
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		Constant.cartList.clear();
		init();
		if (getIntent().getStringExtra("id") != null) {
			id = getIntent().getStringExtra("id");
			getType(id);
		}
		if (getIntent().getStringExtra("name") != null) {
			name = getIntent().getStringExtra("name");
			shopName.setText(name);
		}
	}

	private void init() {

		totalNum = (TextView) findViewById(R.id.total_num);
		totalPrice = (TextView) findViewById(R.id.total_price);
		confirmOrder = (TextView) findViewById(R.id.confirm_order);
		shopName = (TextView) findViewById(R.id.shop_name);
		back = (ImageView) findViewById(R.id.back);

		leftListView = (ListView) findViewById(R.id.menu_left);
		rightListView = (ListView) findViewById(R.id.menu_right);
		typeAdapter = new LeftAdapter(this, typeName);
		leftListView.setAdapter(typeAdapter);
		serviceAdapter = new RightAdapter(this, serviceList, mHandler);
		rightListView.setAdapter(serviceAdapter);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		leftListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				typeAdapter.setSelectedPosition(position);
				typeAdapter.notifyDataSetInvalidated();
				getService(typeList.get(position).getId() + "");
			}
		});

		confirmOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Constant.cartList.isEmpty()) {
					Toast.makeText(Menu.this, "NOTHING SELECTED",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(Menu.this, Confirm.class);
				intent.putExtra("totalprice", getTotalPrice() + "");
				intent.putExtra("detail", getServices());
				startActivity(intent);
			}
		});
	}

	private double getTotalPrice() {
		double price = 0;
		for (Service service : Constant.cartList.values()) {
			price += (service.getPrice().doubleValue()) * (service.getNumber());
		}
		return price;
	}

	private ArrayList<Service> getServices() {
		ArrayList<Service> services = new ArrayList<Service>();
		for (Entry<String, Service> entry : Constant.cartList.entrySet()) {
			Service detail = entry.getValue();
			services.add(detail);
		}
		return services;
	}

	private void getType(String id) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("shopID", id);
		client.post(Constant.GETTYPE, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String result = new String(arg2);
				try {
					JSONObject object = new JSONObject(result);
					typeList = gson.fromJson(object.optString("rows", "[]"),
							new TypeToken<ArrayList<Type>>() {
							}.getType());
					if (typeList.size() != 0) {
						mHandler.sendEmptyMessage(0x001);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				arg3.printStackTrace();
				Toast.makeText(Menu.this, "Connection fail", Toast.LENGTH_SHORT)
						.show();
			};
		});
	}

	private void getService(String id) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("typeID", id);
		client.post(Constant.GETSERVICE, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String result = new String(arg2);
						try {
							JSONObject object = new JSONObject(result);
							serviceList = gson.fromJson(
									object.optString("rows", "[]"),
									new TypeToken<ArrayList<Service>>() {
									}.getType());

							mHandler.sendEmptyMessage(0x002);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						arg3.printStackTrace();
						Toast.makeText(Menu.this, "Connection fail",
								Toast.LENGTH_SHORT).show();
					};
				});
	}

}
