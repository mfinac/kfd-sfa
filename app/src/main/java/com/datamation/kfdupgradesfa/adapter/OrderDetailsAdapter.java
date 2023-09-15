package com.datamation.kfdupgradesfa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.controller.ItemController;
import com.datamation.kfdupgradesfa.model.FreeHed;
import com.datamation.kfdupgradesfa.model.OrderDetail;

import java.util.ArrayList;


public class OrderDetailsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    ArrayList<OrderDetail> list;
    ArrayList<FreeHed> arrayList;
    Context context;
    String debCode;

    public OrderDetailsAdapter(Context context, ArrayList<OrderDetail> list, String debCode){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.debCode = debCode;
    }
    @Override
    public int getCount() {
        if (list != null) return list.size();
        return 0;
    }
    @Override
    public OrderDetail getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_order_details, parent, false);
            viewHolder.lblItem = (TextView) convertView.findViewById(R.id.row_item);
            viewHolder.lblQty = (TextView) convertView.findViewById(R.id.row_cases);
            viewHolder.lblCase = (TextView) convertView.findViewById(R.id.row_pieces);
            viewHolder.lblAMt = (TextView) convertView.findViewById(R.id.row_piece);
            viewHolder.showStatus = (TextView) convertView.findViewById(R.id.row_free_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.lblItem.setText(list.get(position).getFORDDET_ITEM_CODE() + " - " + new ItemController(convertView.getContext()).getItemNameByCode(list.get(position).getFORDDET_ITEM_CODE()));
        viewHolder.lblQty.setText(list.get(position).getFORDDET_QTY());
        viewHolder.lblCase.setText(list.get(position).getFORDDET_PACKSIZE());
        // String sArray[] = new TaxDetController(context).calculateTaxForwardFromDebTax(debCode, list.get(position).getFORDERDET_ITEMCODE(), Double.parseDouble(list.get(position).getFORDERDET_AMT()));
        String amt = String.format("%.2f", Double.parseDouble(list.get(position).getFORDDET_AMT()));
        viewHolder.lblAMt.setText(amt);

        //kaveesha - 02/11/2020 ***********
                if((list.get(position).getFORDDET_TYPE() != null)  && (list.get(position).getFORDDET_TYPE().equals("SA")) ){
                //other products------this procut has't free items
                viewHolder.showStatus.setBackgroundColor(Color.WHITE);
            }else{
                //free item eligible product
                viewHolder.showStatus.setBackground(context.getResources().getDrawable(R.drawable.ic_free_b));
            }

        return convertView;
    }
    private  static  class  ViewHolder{
        TextView lblItem;
        TextView lblQty;
        TextView lblCase;
        TextView lblAMt;
        TextView showStatus;

    }

}
