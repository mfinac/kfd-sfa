package com.datamation.kfdupgradesfa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.model.Customer;
import com.datamation.kfdupgradesfa.model.Debtor;

import java.util.ArrayList;

/**
 * Created by Rashmi on 11/20/2018.
 */

public class CustomerAdapter extends ArrayAdapter<Debtor> {
    Context context;
    ArrayList<Debtor> list;

    public CustomerAdapter(Context context, ArrayList<Debtor> list) {

        super(context, R.layout.row_customer_listview, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, View row, final ViewGroup parent) {

        LayoutInflater inflater = null;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.row_customer_listview, parent, false);

        LinearLayout layout = (LinearLayout) row.findViewById(R.id.linearLayout);
        TextView code = (TextView) row.findViewById(R.id.debCode);
        TextView name = (TextView) row.findViewById(R.id.debName);
        TextView address = (TextView) row.findViewById(R.id.debAddress);


        if(list.get(position).getFDEBTOR_STATUS().equals("B")){
            code.setTextColor(Color.parseColor("#2196F3"));
            name.setTextColor(Color.parseColor("#2196F3"));
            address.setTextColor(Color.parseColor("#2196F3"));

        } else if(list.get(position).getFDEBTOR_STATUS().equals("I")){
            code.setTextColor(Color.parseColor("#F32104"));
            name.setTextColor(Color.parseColor("#F32104"));
            address.setTextColor(Color.parseColor("#F32104"));
        }
        code.setText(list.get(position).getFDEBTOR_CODE());
        name.setText(list.get(position).getFDEBTOR_NAME());
        address.setText(list.get(position).getFDEBTOR_ADD1() + ", " + list.get(position).getFDEBTOR_ADD2() );

        return row;
    }
}