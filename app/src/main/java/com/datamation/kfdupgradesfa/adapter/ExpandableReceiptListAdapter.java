package com.datamation.kfdupgradesfa.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.OrderDetailController;
import com.datamation.kfdupgradesfa.controller.PreProductController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReceiptDetController;
import com.datamation.kfdupgradesfa.dialog.PreSalePrintPreviewAlertBox;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.OrderDetail;
import com.datamation.kfdupgradesfa.model.ReceiptDet;
import com.datamation.kfdupgradesfa.model.ReceiptHed;
import com.datamation.kfdupgradesfa.utils.UtilityContainer;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

public class ExpandableReceiptListAdapter extends BaseExpandableListAdapter {

    private NumberFormat numberFormat = NumberFormat.getInstance();
    private Context _context;
    private List<ReceiptHed> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<ReceiptHed, List<ReceiptDet>> _listDataChild;

    public ExpandableReceiptListAdapter(Context context, List<ReceiptHed> listDataHeader,
                                    HashMap<ReceiptHed, List<ReceiptDet>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View grpview, ViewGroup parent) {

        final ReceiptDet childText = (ReceiptDet) getChild(groupPosition, childPosition);

        if (grpview == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grpview = infalInflater.inflate(R.layout.list_items, null);
        }

        TextView txtListChild = (TextView) grpview.findViewById(R.id.itemcode);
        TextView txtListChild1 = (TextView) grpview.findViewById(R.id.qty);
        TextView txtListChild2 = (TextView) grpview.findViewById(R.id.amount);

        txtListChild.setText("Invoice No - "+childText.getFPRECDET_REFNO1());
        txtListChild1.setText("Date - "+childText.getFPRECDET_TXNDATE());
        txtListChild2.setText("Amount - "+numberFormat.format(Double.parseDouble(childText.getFPRECDET_ALOAMT())));
        return grpview;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final ReceiptHed headerTitle = (ReceiptHed) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
            //convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.refno);
        TextView deb = (TextView) convertView.findViewById(R.id.debcode);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView tot = (TextView) convertView.findViewById(R.id.total);
        TextView stats = (TextView) convertView.findViewById(R.id.status);
//        ImageView type = (ImageView) convertView.findViewById(R.id.type);
        TextView type = (TextView) convertView.findViewById(R.id.type);

        ImageView print = (ImageView) convertView.findViewById(R.id.print);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getFPRECHED_REFNO()+" - "+(headerTitle.getFPRECHED_PAYTYPE().equals("CA")?"CASH":"CHEQUE"));
        deb.setVisibility(View.VISIBLE);
        deb.setText(new CustomerController(_context).getCusNameByCode(headerTitle.getFPRECHED_DEBCODE()));

        if(headerTitle.getFPRECHED_STATUS().equals("SYNCED")){
            //    delete.setBackground(null);
            stats.setText("SYNCED");
            stats.setTextColor(_context.getResources().getColor(R.color.material_alert_positive_button));
        }else if(headerTitle.getFPRECHED_STATUS().equals("RECEIVED")){
            //    delete.setBackground(null);
            stats.setText("RECEIVED");
            stats.setTextColor(_context.getResources().getColor(R.color.colorPrimaryDark));
        }else if (headerTitle.getFPRECHED_STATUS().equals("PAID")){
            stats.setText("PAID");
            stats.setTextColor(_context.getResources().getColor(R.color.md_material_blue_800));
        }
        else{
            //  delete.setBackground(getResources().getDrawable(R.drawable.icon_minus));
            stats.setText("NOT SYNCED");
            stats.setTextColor(_context.getResources().getColor(R.color.material_alert_negative_button));
        }
        //type.setText(headerTitle.getORDER_TXNTYPE());
        date.setText(headerTitle.getFPRECHED_TXNDATE());
        tot.setText(headerTitle.getFPRECHED_TOTALAMT());

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(headerTitle.getFPRECHED_ISSYNCED().equals("0")){
                    deleteReceipt(headerTitle.getFPRECHED_REFNO());
                }else{
                    Toast.makeText(_context,"Cannot delete synced orders", Toast.LENGTH_LONG).show();

                }

            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printReceipt(headerTitle.getFPRECHED_REFNO());
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        double grossTotal = 0;


        List<ReceiptHed> searchingDetails = _listDataHeader;


        for (ReceiptHed rechd : searchingDetails) {

            if (rechd != null) {
                grossTotal += Double.parseDouble(rechd.getFPRECHED_TOTALAMT());

            }
        }

//        total.setText(numberFormat.format(grossTotal));

    }

    public void deleteReceipt(final String RefNo) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(_context)
                .content("Do you want to delete this order ?")
                .positiveColor(ContextCompat.getColor(_context, R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(_context, R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        int result = new ReceiptController(_context).CancelReceiptS(RefNo);

                        if (result > 0) {
                            new ReceiptDetController(_context).restDataForMR(RefNo);
//                            new ReceiptController(getActivity()).CancelReceiptS(RefNo);
                            new ReceiptController(_context).CancelReceiptS(RefNo);
                            Toast.makeText(_context, "Receipt deleted successfully..!", Toast.LENGTH_SHORT).show();

//                            preparePreListData(fromDate.getText().toString().trim(), toDate.getText().toString().trim());
                        } else {
                            Toast.makeText(_context, "Receipt delete unsuccess..!", Toast.LENGTH_SHORT).show();
                        }


                        UtilityContainer.ClearReturnSharedPref(_context);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);

                        dialog.dismiss();


                    }
                })
                .build();
        materialDialog.setCanceledOnTouchOutside(false);
        materialDialog.show();
    }

    public void printReceipt(final String RefNo) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(_context)
                .content("Do you want to print this order ?")
                .positiveColor(ContextCompat.getColor(_context, R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(_context, R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

//                        new PreSalePrintPreviewAlertBox(_context, RefNo);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);

                        dialog.dismiss();


                    }
                })
                .build();
        materialDialog.setCanceledOnTouchOutside(false);
        materialDialog.show();
    }

}
