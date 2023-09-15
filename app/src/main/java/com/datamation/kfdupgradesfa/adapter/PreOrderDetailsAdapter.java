package com.datamation.kfdupgradesfa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.controller.ItemController;
import com.datamation.kfdupgradesfa.model.FreeHed;
import com.datamation.kfdupgradesfa.model.OrderDetail;

import java.util.ArrayList;

//Manoj - 2022/01/10 ***********
public class PreOrderDetailsAdapter extends ArrayAdapter<OrderDetail> {
    private LayoutInflater inflater;
    ArrayList<OrderDetail> list;
    Context context;

    public PreOrderDetailsAdapter(Context context, ArrayList<OrderDetail> list) {
        super(context, R.layout.row_preorder_details, list);
        this.list = list;
        this.context = context;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        LayoutInflater inflater = null;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_preorder_details, parent, false);

        TextView lbldescript = (TextView) convertView.findViewById(R.id.row_order_descript);
        TextView lblItem = (TextView) convertView.findViewById(R.id.row_product_item);
        TextView units = (TextView) convertView.findViewById(R.id.row_unit);
        TextView lblQty = (TextView) convertView.findViewById(R.id.row_qty);
        TextView unitprice = (TextView) convertView.findViewById(R.id.row_unitPrice);
        TextView lblAMt = (TextView) convertView.findViewById(R.id.row_amount);
//        TextView disc = (TextView) convertView.findViewById(R.id.row_discription);


        if(list.get(position).getFORDDET_TYPE().equals("SA")){
            lbldescript.setText("Trans : SALES");
        } else {
            lbldescript.setText("Trans : FREE ISSUE DISTRIBUTOR");
        }
        lblItem.setText(position + 1 + "." + new ItemController(convertView.getContext()).getItemNameByCode(list.get(position).getFORDDET_ITEM_CODE()));
        units.setText("U:"+ new ItemController(convertView.getContext()).getPackSizeByItemCode(list.get(position).getFORDDET_ITEM_CODE()));
        lblQty.setText(list.get(position).getFORDDET_QTY());
        unitprice.setText(list.get(position).getFORDDET_SELL_PRICE());
        String amt = String.format("%.2f", Double.parseDouble(list.get(position).getFORDDET_AMT()));
        lblAMt.setText(amt);

        return convertView;
    }


}
