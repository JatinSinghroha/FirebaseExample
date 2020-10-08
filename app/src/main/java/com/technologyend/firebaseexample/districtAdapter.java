package com.technologyend.firebaseexample;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;

public class districtAdapter extends ArrayAdapter<districtWiseClass> {


    public districtAdapter(@NonNull Context context, int resource, @NonNull List<districtWiseClass> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.district_wise_items, parent, false);
        }
        TextView disNameTV = (TextView) convertView.findViewById(R.id.disNameTV);
        TextView confirmedTV = (TextView) convertView.findViewById(R.id.confirmedTV);
        TextView recoveredTV = (TextView) convertView.findViewById(R.id.recoveredTV);
        TextView activeTV = (TextView) convertView.findViewById(R.id.activeTV);
        TextView deadTV = (TextView) convertView.findViewById(R.id.deathsTV);

        districtWiseClass myobj = getItem(position);

        disNameTV.setText(myobj.getDistrictName());
        confirmedTV.setText(myobj.getConfirmed());
        recoveredTV.setText(myobj.getRecovered());
        activeTV.setText(myobj.getActive());
        deadTV.setText(myobj.getDead());
        return convertView;
    }
}