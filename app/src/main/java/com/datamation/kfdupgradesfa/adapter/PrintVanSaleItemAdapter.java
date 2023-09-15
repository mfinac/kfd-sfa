package com.datamation.kfdupgradesfa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.controller.InvDetController;
import com.datamation.kfdupgradesfa.controller.ItemController;
import com.datamation.kfdupgradesfa.model.InvDet;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PrintVanSaleItemAdapter extends ArrayAdapter<InvDet> {
    Context context;
    ArrayList<InvDet> list;
    String refno;
    BigDecimal disc;

    public PrintVanSaleItemAdapter(Context context, ArrayList<InvDet> list) {

        super(context, R.layout.row_printitems_listview, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {

        LayoutInflater inflater = null;
        View row = null;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.row_printitems_listview, parent, false);

        TextView variantcode = (TextView) row.findViewById(R.id.printvariantcode);
        TextView articleno = (TextView) row.findViewById(R.id.printarticleno);
        TextView unitprice = (TextView) row.findViewById(R.id.unitprice);
        TextView printindex = (TextView) row.findViewById(R.id.printindexVan);
        TextView disper = (TextView) row.findViewById(R.id.printdisper);
        TextView description = (TextView) row.findViewById(R.id.printdescription);
        TextView qty = (TextView) row.findViewById(R.id.printqty);
        TextView discamt = (TextView) row.findViewById(R.id.printdiscamt);
        TextView amount = (TextView) row.findViewById(R.id.printlineamount);
        TextView sizedet = (TextView) row.findViewById(R.id.printsize);

        description.setText(""+new ItemController(context).getItemNameByCode(list.get(position).getFINVDET_ITEM_CODE().trim()));
        variantcode.setText(""+list.get(position).getFINVDET_ITEM_CODE());
        articleno.setText(""+list.get(position).getFINVDET_ARTICLENO());
        unitprice.setText(""+list.get(position).getFINVDET_SELL_PRICE());
        amount.setText(""+list.get(position).getFINVDET_AMT());
        disper.setText(""+list.get(position).getFINVDET_DIS_PER());
        qty.setText(""+list.get(position).getFINVDET_QTY());
        discamt.setText(""+list.get(position).getFINVDET_DIS_AMT());
        sizedet.setText(""+ new InvDetController(this.context).getSizecodeString(list.get(position).getFINVDET_ITEM_CODE(), list.get(position).getFINVDET_REFNO(),list.get(position).getFINVDET_ARTICLENO()));

        position = position + 1;
        String pos = Integer.toString(position);
        printindex.setText(pos + ". ");

        return row;
    }
}
