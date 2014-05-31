package com.example.carpool;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.carpool.model.Ride;
import com.example.carpool.util.DisplayUIUtil;

/**
 * 
 * @author ambika_b
 *
 */
public class RideAdapter extends ArrayAdapter<Ride>{

	ArrayList<Ride> rides;
	
	boolean isEdit;

	public RideAdapter(Context context, int textViewResourceId, ArrayList<Ride> rides, boolean isEdit) {
		super(context, textViewResourceId, rides);
		this.rides = rides;
		this.isEdit = isEdit;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.layout_ride_results, null);
		}
		Ride ride = rides.get(position);
		if (ride != null) {
			TextView from = (TextView) v.findViewById(R.id.rFrom);
			TextView to = (TextView) v.findViewById(R.id.rTo);
			TextView timeStamp = (TextView) v.findViewById(R.id.rTimeStamp);
			TextView rideId = (TextView) v.findViewById(R.id.rId);
			ImageButton imgButton = (ImageButton) v.findViewById(R.id.detailsBtn);
			if (isEdit)
				imgButton.setBackgroundResource(R.drawable.ic_action_edit);
			rideId.setText(ride.getRideId() + "");
			from.setText(ride.getFrom());                            
			to.setText(ride.getTo());
			timeStamp.setText(DisplayUIUtil.getUiTime(ride.getFromTime()));
		}
		return v;
	}
}
