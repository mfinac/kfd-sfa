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
import com.datamation.kfdupgradesfa.controller.DayNPrdDetController;
import com.datamation.kfdupgradesfa.controller.DayNPrdHedController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReceiptDetController;
import com.datamation.kfdupgradesfa.model.DayNPrdDet;
import com.datamation.kfdupgradesfa.model.DayNPrdHed;
import com.datamation.kfdupgradesfa.model.ReceiptDet;
import com.datamation.kfdupgradesfa.model.ReceiptHed;
import com.datamation.kfdupgradesfa.utils.UtilityContainer;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

public class ExpandableNonPListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<DayNPrdHed> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<DayNPrdHed, List<DayNPrdDet>> _listDataChild;

    public ExpandableNonPListAdapter(Context context, List<DayNPrdHed> listDataHeader,
                                     HashMap<DayNPrdHed, List<DayNPrdDet>> listChildData) {
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

        final DayNPrdDet childText = (DayNPrdDet) getChild(groupPosition, childPosition);

        if (grpview == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grpview = infalInflater.inflate(R.layout.list_items_1, null);
        }

        TextView txtListChild1 = (TextView) grpview.findViewById(R.id.reason);
        TextView txtListChild2 = (TextView) grpview.findViewById(R.id.remark);

        txtListChild1.setText("Reason - "+childText.getNONPRDDET_REASON());
        txtListChild2.setText("Remark - "+childText.getNONPRDDET_REMARK());
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
        final DayNPrdHed headerTitle = (DayNPrdHed) getGroup(groupPosition);
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
        TextView reason = (TextView) convertView.findViewById(R.id.reason);
        TextView stats = (TextView) convertView.findViewById(R.id.status);
        ImageView type = (ImageView) convertView.findViewById(R.id.type);
        ImageView print = (ImageView) convertView.findViewById(R.id.print);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getNONPRDHED_REFNO());

        tot.setVisibility(View.GONE);
        deb.setVisibility(View.VISIBLE);
        reason.setVisibility(View.VISIBLE);

        deb.setText(headerTitle.getNONPRDHED_DEBCODE());
        if(headerTitle.getNONPRDHED_IS_SYNCED().equals("1")){
            //    delete.setBackground(null);
            stats.setText("Synced");
            stats.setTextColor(_context.getResources().getColor(R.color.material_alert_positive_button));
        }else{
            //  delete.setBackground(getResources().getDrawable(R.drawable.icon_minus));
            stats.setText("Not Synced");
            stats.setTextColor(_context.getResources().getColor(R.color.material_alert_negative_button));
        }
        //type.setText(headerTitle.getORDER_TXNTYPE());
        date.setText(headerTitle.getNONPRDHED_TXNDATE());
        reason.setText(headerTitle.getNONPRDHED_REASON());

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(headerTitle.getNONPRDHED_IS_SYNCED().equals("0")){
                    deleteNonPrd(headerTitle.getNONPRDHED_REFNO());
                }else{
                    Toast.makeText(_context,"Cannot delete synced nonproductive details", Toast.LENGTH_LONG).show();

                }

            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printNonPrd(headerTitle.getNONPRDHED_REFNO());
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



    }

    public void deleteNonPrd(final String RefNo) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(_context)
                .content("Do you want to delete this nonproductive details ?")
                .positiveColor(ContextCompat.getColor(_context, R.color.material_alert_positive_button))
                .positiveText("Yes")
                .negativeColor(ContextCompat.getColor(_context, R.color.material_alert_negative_button))
                .negativeText("No, Exit")
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        boolean result = new DayNPrdHedController(_context).restData(RefNo);

                        if (result) {
                            new DayNPrdDetController(_context).deleteOrdDetByRefNo(RefNo);
                            Toast.makeText(_context, "Nonproductive details deleted successfully..!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(_context, "Nonproductive details delete unsuccess..!", Toast.LENGTH_SHORT).show();
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

    public void printNonPrd(final String RefNo) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(_context)
                .content("Do you want to print this Nonproductive details ?")
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
