package com.datamation.kfdupgradesfa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Control;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class downloadListAdapter extends ArrayAdapter<Control> {

    Context context;
    ArrayList<Control> list;

    SharedPref sharedPref;

    public downloadListAdapter(Context context, ArrayList<Control> list) {
        super(context, R.layout.row_download_view, list);
        this.context = context;
        this.list = list;
        this.sharedPref = SharedPref.getInstance(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = null;
        View row = null;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        row = inflater.inflate(R.layout.row_download_view, parent, false);

        TextView title = (TextView) row.findViewById(R.id.row_title);
        TextView count = (TextView) row.findViewById(R.id.row_count);


        if (sharedPref.isSyncedSuccess()) {
            title.setText(list.get(position).getFCONTROL_DOWNLOAD_TITLE());
            if (!(list.get(position).getFCONTROL_DOWNLOADCOUNT().equals(list.get(position).getFCONTROL_DOWNLOADEDCOUNT())) || (list.get(position).getFCONTROL_DOWNLOADCOUNT().equals("0"))) {

                count.setTextColor(Color.parseColor("#EE0000"));
                count.setText(list.get(position).getFCONTROL_DOWNLOADCOUNT() + "/" + list.get(position).getFCONTROL_DOWNLOADEDCOUNT());
            } else {
                count.setTextColor(Color.parseColor("#4CAF50"));
                count.setText(list.get(position).getFCONTROL_DOWNLOADCOUNT() + "/" + list.get(position).getFCONTROL_DOWNLOADEDCOUNT());
            }
        } else {
            title.setTextColor(Color.parseColor("#EE0000"));
            title.setText(list.get(position).getFCONTROL_DOWNLOAD_TITLE());
        }

        return row;
    }

}
