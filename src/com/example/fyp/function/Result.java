package com.example.fyp.function;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.fyp.R;
import com.example.fyp.adapter.RecommendAdapter;
import com.example.fyp.adapter.ResultAdapter;
import com.example.fyp.beans.Account;
import com.example.fyp.popwindows.SelectPopWindow;
import com.example.fyp.tool.ProgressAnmi;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class Result extends Activity {

	// popwindow-head
	private LinearLayout view;
	private CheckBox selectPlace;
	private CheckBox selectOrder;
	private CheckBox selectRange;
	private SelectPopWindow selectBox;
	private ListView selectItem;
	private ArrayList<String> placeList = new ArrayList<String>();
	private ArrayList<String> orderList = new ArrayList<String>();
	private ArrayList<String> rangeList = new ArrayList<String>();

	// body
	private ListView searchResult;
	private ListView recommendResult;
	private LinearLayout recommendTitle;
	private ResultAdapter resultAdapter;
	private RecommendAdapter recommendAdapter;
	private ImageView failImage;
	private TextView failText;

	// constant
	private int page = 1;
	private String locationCity = "";
	private String locationDistance = "";
	private String shopType = "";
	private String perBudget = "";
	private String searchInput = "";
	private String longitude = Constant.location.getLongitude() + "";
	private String latitude = Constant.location.getLatitude() + "";
	private String order = "Order";
	private String trafficSelect = "";

	private ProgressAnmi dialog = null;

	private Gson gson = new Gson();
	private ArrayList<Account> resultList = new ArrayList<Account>();
	private ArrayList<Account> recommendList = new ArrayList<Account>();;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x001:
				resultAdapter.setResultList(resultList);
				int totalHeight1 = 0;
				for (int i = 0; i < resultAdapter.getCount(); i++) {
					View listItem = resultAdapter
							.getView(i, null, searchResult);
					listItem.measure(0, 0);
					totalHeight1 += listItem.getMeasuredHeight();
				}
				ViewGroup.LayoutParams params1 = searchResult.getLayoutParams();
				params1.height = totalHeight1
						+ (searchResult.getDividerHeight() * (searchResult
								.getCount() - 1));
				searchResult.setLayoutParams(params1);

				if (recommendList.size() > 0) {
					recommendResult.setVisibility(View.VISIBLE);
					recommendTitle.setVisibility(View.VISIBLE);
					recommendAdapter.setResultList(recommendList);
					int totalHeight2 = 0;
					for (int i = 0; i < recommendAdapter.getCount(); i++) {
						View listItem = recommendAdapter.getView(i, null,
								recommendResult);
						listItem.measure(0, 0);
						totalHeight2 += listItem.getMeasuredHeight();
					}
					ViewGroup.LayoutParams params2 = recommendResult
							.getLayoutParams();
					params2.height = totalHeight2
							+ (recommendResult.getDividerHeight() * (recommendResult
									.getCount() - 1));
					recommendResult.setLayoutParams(params2);
				} else {
					recommendResult.setVisibility(View.GONE);
					recommendTitle.setVisibility(View.GONE);
				}
				break;
			case 0x002:
				searchResult.setVisibility(View.GONE);
				recommendResult.setVisibility(View.GONE);
				recommendTitle.setVisibility(View.GONE);
				failImage.setVisibility(View.VISIBLE);
				failText.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		addData();
		init();

		resultAdapter = new ResultAdapter(Result.this, resultList);
		recommendAdapter = new RecommendAdapter(Result.this, recommendList);
		searchResult.setAdapter(resultAdapter);
		recommendResult.setAdapter(recommendAdapter);

		mainSearch();
	}

	private void addData() {
		placeList = Constant.CITYLIST;
		orderList = Constant.ORDERLIST;
		rangeList = Constant.DISTANCELIST;
		if (getIntent().getStringExtra("locationCity") != null) {
			locationCity = getIntent().getStringExtra("locationCity");
		}
		if (getIntent().getStringExtra("locationDistance") != null) {
			locationDistance = getIntent().getStringExtra("locationDistance");
		} else {
			locationDistance = "<1km";
		}
		if (getIntent().getStringExtra("shopType") != null) {
			shopType = getIntent().getStringExtra("shopType");
		}
		if (getIntent().getStringExtra("perBudget") != null) {
			perBudget = getIntent().getStringExtra("perBudget");
		}
		if (getIntent().getStringExtra("searchInput") != null) {
			searchInput = getIntent().getStringExtra("searchInput");
		}
		if (getIntent().getStringExtra("longitude") != null) {
			longitude = getIntent().getStringExtra("longitude") + "";
		}
		if (getIntent().getStringExtra("latitude") != null) {
			latitude = getIntent().getStringExtra("latitude") + "";
		}
		if (getIntent().getStringExtra("trafficSelect") != null) {
			trafficSelect = getIntent().getStringExtra("trafficSelect") + "";
		}
	}

	private void init() {

		// head
		view = (LinearLayout) findViewById(R.id.selector);
		selectPlace = (CheckBox) findViewById(R.id.select_place);
		selectOrder = (CheckBox) findViewById(R.id.select_order);
		selectRange = (CheckBox) findViewById(R.id.select_range);
		selectItem = (ListView) findViewById(R.id.select_tips);
		selectRange.setText(locationDistance);
		// body
		searchResult = (ListView) findViewById(R.id.search_result);
		recommendResult = (ListView) findViewById(R.id.recommend_result);
		recommendTitle = (LinearLayout) findViewById(R.id.recommend_title);
		failImage = (ImageView) findViewById(R.id.fail_image);
		failText = (TextView) findViewById(R.id.fail_text);

		// First select
		selectPlace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectPlace.isChecked()) {
					if (selectBox == null || !selectBox.isShowing()) {
						selectBox = new SelectPopWindow(Result.this, placeList,
								placeList.size());
						selectBox.showPopupWindow(view);
						WindowManager.LayoutParams params = getWindow()
								.getAttributes();
						params.alpha = 0.5f;
						getWindow().setAttributes(params);
					} else {
						selectBox.setDataList(placeList);
					}
					selectItem = (ListView) selectBox.getContentView()
							.findViewById(R.id.select_tips);
					selectItem
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									locationCity = placeList.get(position);
									selectPlace.setText(placeList.get(position));
									selectBox.dismiss();
									mainSearch();
								}
							});
					selectOrder.setChecked(false);
					selectRange.setChecked(false);
					selectBox.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss() {
							selectPlace.setChecked(false);
							WindowManager.LayoutParams params = getWindow()
									.getAttributes();
							params.alpha = 1f;
							getWindow().setAttributes(params);
						}
					});
				} else {
					selectPlace.setChecked(false);
					selectBox.dismiss();
				}

			}
		});

		// Second select
		selectOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectOrder.isChecked()) {
					if (selectBox == null || !selectBox.isShowing()) {
						selectBox = new SelectPopWindow(Result.this, orderList,
								orderList.size());
						selectBox.showPopupWindow(view);
						WindowManager.LayoutParams params = getWindow()
								.getAttributes();
						params.alpha = 0.5f;
						getWindow().setAttributes(params);
					} else {
						selectBox.setDataList(orderList);
					}
					selectItem = (ListView) selectBox.getContentView()
							.findViewById(R.id.select_tips);
					selectItem
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									order = Constant.ORDER.get(position);
									selectOrder.setText(order);
									selectBox.dismiss();
									mainSearch();
								}
							});
					selectPlace.setChecked(false);
					selectRange.setChecked(false);
					selectBox.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss() {
							selectOrder.setChecked(false);
							WindowManager.LayoutParams params = getWindow()
									.getAttributes();
							params.alpha = 1f;
							getWindow().setAttributes(params);
						}
					});
				} else {
					selectOrder.setChecked(false);
					selectBox.dismiss();
				}
			}
		});

		// Third select
		selectRange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectRange.isChecked()) {
					if (selectBox == null || !selectBox.isShowing()) {
						selectBox = new SelectPopWindow(Result.this, rangeList,
								rangeList.size());
						selectBox.showPopupWindow(view);
						WindowManager.LayoutParams params = getWindow()
								.getAttributes();
						params.alpha = 0.5f;
						getWindow().setAttributes(params);
					} else {
						selectBox.setDataList(rangeList);
					}
					selectItem = (ListView) selectBox.getContentView()
							.findViewById(R.id.select_tips);
					selectItem
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									locationDistance = rangeList.get(position);
									selectRange.setText(rangeList.get(position));
									selectBox.dismiss();
									mainSearch();
								}
							});
					selectPlace.setChecked(false);
					selectOrder.setChecked(false);
					selectBox.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss() {
							selectRange.setChecked(false);
							WindowManager.LayoutParams params = getWindow()
									.getAttributes();
							params.alpha = 1f;
							getWindow().setAttributes(params);
						}
					});
				} else {
					selectRange.setChecked(false);
					selectBox.dismiss();
				}
			}
		});

		// Order online
		searchResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Result.this, Menu.class);
				Account account = resultList.get(position);
				intent.putExtra("id", account.getId() + "");
				intent.putExtra("name", account.getRealname());
				startActivity(intent);
			}
		});

		// Order online
		recommendResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Result.this, Menu.class);
				Account account = recommendList.get(position);
				intent.putExtra("id", account.getId() + "");
				intent.putExtra("name", account.getRealname());
				startActivity(intent);
			}
		});
	}

	public void mainSearch() {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("longitude", longitude);
		params.add("latitude", latitude);
		params.add("range", locationDistance);
		params.add("type", shopType);
		params.add("budget", perBudget);
		params.add("page", page + "");
		params.add("city", locationCity);
		params.add("realname", searchInput);
		params.add("orderBy", order);
		client.post(Constant.MAINSEARCH, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String result = new String(arg2);
						try {
							JSONObject object = new JSONObject(result);
							resultList = gson.fromJson(
									object.optString("rows", "[]"),
									new TypeToken<ArrayList<Account>>() {
									}.getType());
							int total = (int) object.get("total");
							if (total < 10) {
								recommendList = gson.fromJson(
										object.optString("recommend", "[]"),
										new TypeToken<ArrayList<Account>>() {
										}.getType());

							} else {
								recommendList = new ArrayList<>();
							}
							dialog.dismiss();
							mHandler.sendEmptyMessage(0x001);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Toast.makeText(Result.this, "Connection fail",
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						mHandler.sendEmptyMessage(0x002);
					};

					@Override
					public void onStart() {
						dialog = new ProgressAnmi(Result.this, R.anim.loading);
						dialog.show();
						super.onStart();
					}
				});
	}
}
