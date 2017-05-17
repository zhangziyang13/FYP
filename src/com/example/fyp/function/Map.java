package com.example.fyp.function;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.fyp.R;
import com.example.fyp.tool.Constant;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Map extends Activity {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Location location;
	private ImageButton mapQuit, mapConfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.map);
		init();
		baidu();
	}

	private void init() {
		location = Constant.location;
		mMapView = (MapView) findViewById(R.id.bmapView);
		mapQuit = (ImageButton) findViewById(R.id.map_quit);
		mapConfirm = (ImageButton) findViewById(R.id.map_confirm);
		mapQuit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Map.this, Search.class);
				startActivity(intent);
			}
		});
		mapConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Constant.location = location;
				Intent intent = new Intent(Map.this, Search.class);
				startActivity(intent);
			}
		});
	}

	private void baidu() {
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		LatLng loca = new LatLng(location.getLatitude(),
				location.getLongitude());
		MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory
				.newLatLng(loca);
		mBaiduMap.setMapStatus(mapstatusUpdatePoint);
		MapStatusUpdate mapstatusUpdate = MapStatusUpdateFactory.zoomTo(19);
		mBaiduMap.setMapStatus(mapstatusUpdate);
		mBaiduMap.setMyLocationEnabled(true);
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.mark_icon);
		OverlayOptions options = new MarkerOptions().position(loca)
				.icon(bitmap).period(10).draggable(true);
		mBaiduMap.addOverlay(options);
		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {

			@Override
			public void onMarkerDragStart(Marker marker) {

			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				LatLng latLng = marker.getPosition();
				location.setLatitude(latLng.latitude);
				location.setLongitude(latLng.longitude);
			}

			@Override
			public void onMarkerDrag(Marker marker) {

			}
		});
	}
}
