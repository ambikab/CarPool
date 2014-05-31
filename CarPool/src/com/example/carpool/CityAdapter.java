package com.example.carpool;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.carpool.model.City;

public class CityAdapter extends ArrayAdapter<City>{

	ArrayList<City> cities;

	boolean isEdit;

	public CityAdapter(Context context, int textViewResourceId, ArrayList<City> cities, boolean isEdit) {
		super(context, textViewResourceId, cities);
		this.cities = cities;
		this.isEdit = isEdit;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.layout_city_list, null);
		}
		City city = cities.get(position);
		if (city != null) {
			TextView cityId = (TextView) v.findViewById(R.id.cityId);
			TextView cityName = (TextView) v.findViewById(R.id.cityName);
			cityId.setText(city.getCityId() + "");
			cityName.setText(city.getCityName());                            
		}
		return v;
	}
} 