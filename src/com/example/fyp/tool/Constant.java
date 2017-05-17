package com.example.fyp.tool;

import java.util.ArrayList;
import java.util.HashMap;

import android.location.Location;

import com.example.fyp.beans.Service;

public class Constant {

	// GOOGLE API
	public static final String API = "AIzaSyAsSUyrkbSpcPhh1bTWvayfyKJiMKsCejI";

	// Baidu AK
	public static final String AK = "naG8GczDniE2hWXeuxy4jXGTQjDvpqi3";

	// Domain
	public static final String MAIN = "http://7c5b17b6.ngrok.io/FYP/";

	public static final String ACCOUNT = MAIN + "account/dataList";
	public static final String MAINSEARCH = MAIN + "account/mainSearch";
	public static final String GETDETAIL = MAIN + "account/getId";
	public static final String GETTYPE = MAIN + "type/dataList";
	public static final String GETSERVICE = MAIN + "service/dataList";

	// Get City API
	public static final String FINDCITY = "http://api.map.baidu.com/geocoder/v2/";

	public static final ArrayList<String> CITYLIST = new ArrayList<String>() {
		{
			add("suzhou");
			add("hangzhou");
			add("shanghai");
		}
	};

	public static final ArrayList<String> DISTANCELIST = new ArrayList<String>() {
		{
			add("<1km");
			add("<2km");
			add("<5km");
			add("<10km");
		}
	};

	public static final ArrayList<String> TYPELIST = new ArrayList<String>() {
		{
			add("Catering");
			add("Shopping");
			add("Entertainment");
		}
	};

	public static final ArrayList<String> BUDGETLIST = new ArrayList<String>() {
		{
			add("0 - 100");
			add("100 - 300");
			add("300 - 500");
		}
	};

	public static final ArrayList<String> ORDERLIST = new ArrayList<String>() {
		{
			add("Closest distance first");
			add("Lowest budget first");
			add("Highest score first");
		}
	};

	public static final ArrayList<String> ORDER = new ArrayList<String>() {
		{
			add("Distance");
			add("Budget");
			add("Score");
		}
	};

	public static Location location;
	public static HashMap<String, Service> cartList = new HashMap<String, Service>();
}
