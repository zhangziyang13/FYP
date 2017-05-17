package com.example.fyp.function;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.cjj.sva.JJSearchView;
import com.cjj.sva.anim.controller.JJAroundCircleBornTailController;
import com.example.fyp.R;
import com.example.fyp.adapter.SelectAdapter;
import com.example.fyp.popwindows.BudgetPopWindow;
import com.example.fyp.popwindows.KeywordPopWindow;
import com.example.fyp.popwindows.SearchPopWindow;
import com.example.fyp.tool.CnToPy;
import com.example.fyp.tool.RecordSQLiteOpenHelper;
import com.example.fyp.tool.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class Search extends Activity {

	private LinearLayout conditionCity;
	private LinearLayout conditionDistance;
	private LinearLayout conditionType;
	private LinearLayout conditionBudget;
	private LinearLayout conditionStore;
	private LinearLayout conditionTraffic;

	private TextView locationCity;
	private ImageView locationIcon;
	private TextView locationDistance;
	private TextView shopType;
	private TextView perBudget;
	private TextView searchInput;
	private CheckBox trafficSelect;
	private JJSearchView searchIcon;

	// popwindow
	private SearchPopWindow cityPopWindow;
	private SearchPopWindow distancePopWindow;
	private SearchPopWindow typePopWindow;
	private BudgetPopWindow budgetPopWindow;
	private KeywordPopWindow storePopWindow;
	private ImageButton quitCity;
	private ImageButton quitDistance;
	private ImageButton quitType;
	private ImageButton quitBudget;
	private ImageButton confirmBudget;
	private ImageButton quitStore;
	private ImageButton clearStore;
	private ListView listCity;
	private ListView listDistance;
	private ListView listType;
	private ListView listBudget;
	private ListView listStore;
	private EditText inputCity;
	private EditText inputDistance;
	private EditText inputType;
	private EditText inputMinBudget;
	private EditText inputMaxBudget;
	private EditText inputStore;
	private ArrayList<String> cityList = Constant.CITYLIST;
	private ArrayList<String> distanceList = Constant.DISTANCELIST;
	private ArrayList<String> typeList = Constant.TYPELIST;
	private ArrayList<String> budgetList = Constant.BUDGETLIST;
	private ArrayList<String> historyList = new ArrayList<String>();

	// SQLite
	private RecordSQLiteOpenHelper helper = new RecordSQLiteOpenHelper(this);;
	private SQLiteDatabase db;

	// Locate
	private String city = "suzhou";
	private Location location;
	private LocationManager locationManager;
	private String locationProvider;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x001:
				locationCity.setText(CnToPy.getPingYin(city));
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		init();
		if (Constant.location == null) {
			location(Search.this);
		} else {
			findCity(Constant.location.getLongitude(),
					Constant.location.getLatitude());
		}
	}

	// 初始化控件
	private void init() {
		conditionCity = (LinearLayout) findViewById(R.id.condition_city);
		conditionDistance = (LinearLayout) findViewById(R.id.condition_distance);
		conditionType = (LinearLayout) findViewById(R.id.condition_type);
		conditionBudget = (LinearLayout) findViewById(R.id.condition_budget);
		conditionStore = (LinearLayout) findViewById(R.id.condition_store);
		conditionTraffic = (LinearLayout) findViewById(R.id.condition_traffic);

		locationCity = (TextView) findViewById(R.id.location_city);
		locationIcon = (ImageView) findViewById(R.id.location_icon);
		locationDistance = (TextView) findViewById(R.id.location_distance);
		shopType = (TextView) findViewById(R.id.shop_type);
		perBudget = (TextView) findViewById(R.id.per_budget);
		searchInput = (TextView) findViewById(R.id.search_input);
		trafficSelect = (CheckBox) findViewById(R.id.traffic_select);

		searchIcon = (JJSearchView) findViewById(R.id.search_icon);
		searchIcon.setController(new JJAroundCircleBornTailController());

		// First criteria
		conditionCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cityPopWindow = new SearchPopWindow(Search.this, cityList);

				quitCity = (ImageButton) cityPopWindow.getContentView()
						.findViewById(R.id.quit_pop_window);
				quitCity.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						cityPopWindow.dismiss();
					}
				});
				listCity = (ListView) cityPopWindow.getContentView()
						.findViewById(R.id.list_pop_window);
				listCity.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						locationCity.setText(cityList.get(position));
						cityPopWindow.dismiss();
					}
				});
				inputCity = (EditText) cityPopWindow.getContentView()
						.findViewById(R.id.input_pop_window);
				inputCity.setOnKeyListener(new View.OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (KeyEvent.KEYCODE_ENTER == keyCode
								&& KeyEvent.ACTION_DOWN == event.getAction()) {
							locationCity.setText(inputCity.getText());
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(0,
									InputMethodManager.HIDE_NOT_ALWAYS);
							cityPopWindow.dismiss();
							return true;
						}
						return false;
					}
				});
				cityPopWindow.showPopupWindow(conditionCity);
			}
		});

		// Second criteria
		conditionDistance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				distancePopWindow = new SearchPopWindow(Search.this,
						distanceList);

				quitDistance = (ImageButton) distancePopWindow.getContentView()
						.findViewById(R.id.quit_pop_window);
				quitDistance.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						distancePopWindow.dismiss();
					}
				});
				listDistance = (ListView) distancePopWindow.getContentView()
						.findViewById(R.id.list_pop_window);
				listDistance.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						locationDistance.setText(distanceList.get(position));
						distancePopWindow.dismiss();
					}
				});
				inputDistance = (EditText) distancePopWindow.getContentView()
						.findViewById(R.id.input_pop_window);
				inputDistance.setOnKeyListener(new View.OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (KeyEvent.KEYCODE_ENTER == keyCode
								&& KeyEvent.ACTION_DOWN == event.getAction()) {
							locationDistance.setText("<"
									+ inputDistance.getText() + "km");
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(0,
									InputMethodManager.HIDE_NOT_ALWAYS);
							distancePopWindow.dismiss();
							return true;
						}
						return false;
					}
				});
				distancePopWindow.showPopupWindow(conditionDistance);
			}
		});

		// Third criteria
		conditionType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				typePopWindow = new SearchPopWindow(Search.this, typeList);

				quitType = (ImageButton) typePopWindow.getContentView()
						.findViewById(R.id.quit_pop_window);
				quitType.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						typePopWindow.dismiss();
					}
				});
				listType = (ListView) typePopWindow.getContentView()
						.findViewById(R.id.list_pop_window);
				listType.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						shopType.setText(typeList.get(position));
						typePopWindow.dismiss();
					}
				});
				inputType = (EditText) typePopWindow.getContentView()
						.findViewById(R.id.input_pop_window);
				inputType.setOnKeyListener(new View.OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (KeyEvent.KEYCODE_ENTER == keyCode
								&& KeyEvent.ACTION_DOWN == event.getAction()) {
							shopType.setText(inputType.getText());
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(0,
									InputMethodManager.HIDE_NOT_ALWAYS);
							typePopWindow.dismiss();
							return true;
						}
						return false;
					}
				});
				typePopWindow.showPopupWindow(conditionType);
			}
		});

		// Fourth criteria
		conditionBudget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				budgetPopWindow = new BudgetPopWindow(Search.this, budgetList,
						perBudget.getText().toString());

				inputMinBudget = (EditText) budgetPopWindow.getContentView()
						.findViewById(R.id.input_min_budget);
				inputMaxBudget = (EditText) budgetPopWindow.getContentView()
						.findViewById(R.id.input_max_budget);

				inputMinBudget.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						if (!inputMinBudget.getText().toString().isEmpty()
								&& !inputMaxBudget.getText().toString()
										.isEmpty()) {
							int minBudget = Integer.parseInt(inputMinBudget
									.getText().toString());
							int maxBudget = Integer.parseInt(inputMaxBudget
									.getText().toString());
							if (!(minBudget < maxBudget)) {
								Toast.makeText(Search.this,
										"Enter the right scope",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(Search.this,
									"Enter the right scope", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});

				inputMaxBudget.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						if (!inputMinBudget.getText().toString().isEmpty()
								&& !inputMaxBudget.getText().toString()
										.isEmpty()) {
							int minBudget = Integer.parseInt(inputMinBudget
									.getText().toString());
							int maxBudget = Integer.parseInt(inputMaxBudget
									.getText().toString());
							if (!(minBudget < maxBudget)) {
								Toast.makeText(Search.this,
										"Enter the right scope",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(Search.this,
									"Enter the right scope", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});

				confirmBudget = (ImageButton) budgetPopWindow.getContentView()
						.findViewById(R.id.confirm_budget);
				confirmBudget.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!inputMinBudget.getText().toString().isEmpty()
								&& !inputMaxBudget.getText().toString()
										.isEmpty()) {
							int minBudget = Integer.parseInt(inputMinBudget
									.getText().toString());
							int maxBudget = Integer.parseInt(inputMaxBudget
									.getText().toString());
							if (minBudget < maxBudget) {
								String budget = inputMinBudget.getText()
										+ " - " + inputMaxBudget.getText();
								perBudget.setText(budget);
								budgetPopWindow.dismiss();
							} else {
								Toast.makeText(Search.this,
										"Enter the right scope",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(Search.this,
									"Enter the right scope", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});

				quitBudget = (ImageButton) budgetPopWindow.getContentView()
						.findViewById(R.id.quit_budget);
				quitBudget.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						budgetPopWindow.dismiss();
					}
				});
				listBudget = (ListView) budgetPopWindow.getContentView()
						.findViewById(R.id.list_budget);
				listBudget.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						perBudget.setText(budgetList.get(position));
						budgetPopWindow.dismiss();
					}
				});

				budgetPopWindow.showPopupWindow(conditionBudget);
			}
		});

		// Fifth criteria
		conditionStore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				queryData();
				storePopWindow = new KeywordPopWindow(Search.this, historyList);

				quitStore = (ImageButton) storePopWindow.getContentView()
						.findViewById(R.id.quit_keyword);
				quitStore.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						storePopWindow.dismiss();
					}
				});

				clearStore = (ImageButton) storePopWindow.getContentView()
						.findViewById(R.id.clear_keyword);
				clearStore.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						deleteData();
						storePopWindow.setDataList(historyList);
					}
				});

				listStore = (ListView) storePopWindow.getContentView()
						.findViewById(R.id.list_keyword);
				listStore.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						searchInput.setText(historyList.get(position));
						storePopWindow.dismiss();
					}
				});
				inputStore = (EditText) storePopWindow.getContentView()
						.findViewById(R.id.input_keyword);
				inputStore.setOnKeyListener(new View.OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (KeyEvent.KEYCODE_ENTER == keyCode
								&& KeyEvent.ACTION_DOWN == event.getAction()) {
							searchInput.setText(inputStore.getText());
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(0,
									InputMethodManager.HIDE_NOT_ALWAYS);
							storePopWindow.dismiss();
							return true;
						}
						return false;
					}
				});
				storePopWindow.showPopupWindow(conditionStore);
			}
		});

		// Sixth criteria
		conditionTraffic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (trafficSelect.isChecked()) {
					trafficSelect.setChecked(false);
				} else {
					trafficSelect.setChecked(true);
				}
			}
		});

		// Search event
		searchIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(searchInput.getText().toString())) {
					Toast.makeText(Search.this, "Enter the keyword",
							Toast.LENGTH_SHORT).show();
				} else {
					searchIcon.resetAnim();
					searchIcon.startAnim();
					insertData(searchInput.getText().toString());

					final Intent intent = new Intent(Search.this, Result.class);
					intent.putExtra("locationCity", locationCity.getText()
							.toString());
					if (!locationDistance.getText().toString().isEmpty()) {
						intent.putExtra("locationDistance", locationDistance
								.getText().toString());
					}
					if (!shopType.getText().toString().isEmpty()) {
						intent.putExtra("shopType", shopType.getText()
								.toString());
					}
					intent.putExtra("perBudget", perBudget.getText().toString());
					intent.putExtra("searchInput", searchInput.getText()
							.toString());
					intent.putExtra("trafficSelect", trafficSelect.isChecked());
					if (location != null) {
						intent.putExtra("longitude", location.getLongitude());
						intent.putExtra("latitude", location.getLatitude());
					}

					Timer timer = new Timer();
					TimerTask tast = new TimerTask() {
						@Override
						public void run() {
							startActivity(intent);
						}
					};
					timer.schedule(tast, 2000);
				}
			}
		});

		// Map
		locationIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Search.this, Map.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
	}

	// SQLite***********************************************************************************************
	private void queryData() {
		historyList = new ArrayList<String>();
		Cursor cursor = helper.getReadableDatabase().rawQuery(
				"select id as _id,name from records order by id desc ", null);
		while (cursor.moveToNext()) {
			historyList.add(cursor.getString(cursor.getColumnIndex("name")));
		}

	}

	private void insertData(String tempName) {
		db = helper.getWritableDatabase();
		db.execSQL("insert or replace into records(name) values('" + tempName
				+ "')");
		db.close();
	}

	private void deleteData() {
		db = helper.getWritableDatabase();
		db.execSQL("delete from records");
		db.close();
		historyList = new ArrayList<String>();
	}

	// Locate***********************************************************************************************
	private void location(Context mcontext) {
		locationManager = (LocationManager) mcontext
				.getSystemService(Context.LOCATION_SERVICE);

		getProvider();
		openGPS();
		// Get Location
		location = locationManager.getLastKnownLocation(locationProvider);

		if (location != null) {
			Constant.location = location;
			findCity(location.getLongitude(), location.getLatitude());
		} else {
			Constant.location = location;
			Constant.location.setLatitude(31.274009);
			Constant.location.setLongitude(120.735848);
			Toast.makeText(this, "Locate fail !", Toast.LENGTH_SHORT).show();
		}

		locationManager.requestLocationUpdates(locationProvider, 2000, 10,
				locationListener);
	}

	private void openGPS() {
		if (locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
				|| locationManager
						.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
			return;
		}
		Toast.makeText(this, "Please open GPS !", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		startActivityForResult(intent, 0);
	}

	// Get Location Provider
	private void getProvider() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		locationProvider = locationManager.getBestProvider(criteria, true);
	}

	LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle arg2) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			findCity(location.getLongitude(), location.getLatitude());
		}
	};

	// Find City by longitude and latitude
	public void findCity(double longitude, double latitude) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("callback", "renderReverse");
		params.add("location", latitude + "," + longitude);
		params.add("output", "json");
		params.add("pois", "0");
		params.add("ak", Constant.AK);
		client.post(Constant.FINDCITY, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String result = new String(arg2);
				try {
					JSONObject object = new JSONObject(result);
					JSONObject object2 = (JSONObject) object.get("result");
					JSONObject object3 = (JSONObject) object2
							.get("addressComponent");
					if (!object3.get("city").equals("")) {
						city = object3.get("city").toString().substring(0, 2);
					}
					mHandler.sendEmptyMessage(0x001);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Toast.makeText(Search.this, "Positioning fail !",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
