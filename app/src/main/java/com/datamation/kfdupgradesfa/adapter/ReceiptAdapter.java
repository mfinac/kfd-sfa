package com.datamation.kfdupgradesfa.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.model.FddbNote;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReceiptAdapter extends ArrayAdapter<FddbNote> {
    Context context;
    ArrayList<FddbNote> list;
    boolean isSummery;
    String refno;
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public ReceiptAdapter(Context context, ArrayList<FddbNote> list, boolean isSummery, String RefNo) {

        super(context, R.layout.row_receipt_details, list);
        this.context = context;
        this.list = list;
        this.isSummery = isSummery;
        this.refno = RefNo;

    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        LayoutInflater inflater = null;
        View row = null;
        long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;


        ReceiptController reched = new ReceiptController(context);
        SalRepController rep = new SalRepController(context);

        int numOfDays=0;
        Date date, cDate;
        long txn = 0;
        long current = 0;
        try {

            String sDate1 = list.get(position).getFDDBNOTE_TXN_DATE();
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
            System.out.println(sDate1);

            Long def = getDateDiff(date1, new Date(), TimeUnit.DAYS);
            numOfDays =   (int) (def / DAY_IN_MILLIS);



        } catch (ParseException e) {
            e.printStackTrace();
        }

//			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
//			Date currentDate = new Date();

        //String curDate =	dateFormat.format(reched.getChequeDate(refno));

        try {
            cDate = (Date) formatter.parse(reched.getChequeDate(refno));
            current = cDate.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.row_receipt_details, parent, false);


//		int numOfDays =   (int) ((System.currentTimeMillis()  - txn) / DAY_IN_MILLIS);


        int datediff = 0;

        if (reched.getChequeDate(refno).length() > 0) {
            datediff = (int) ((current - txn) / DAY_IN_MILLIS);
        } else {
			datediff = numOfDays;
        }



        TextView lblRefNo = (TextView) row.findViewById(R.id.row_refno);
        TextView lblTxnDate = (TextView) row.findViewById(R.id.row_txndate);
        TextView lblDueAmt = (TextView) row.findViewById(R.id.row_dueAmt);
        TextView lblAmt = (TextView) row.findViewById(R.id.row_Amt);
        TextView lblRepName = (TextView) row.findViewById(R.id.repName);
        TextView lblDays = (TextView) row.findViewById(R.id.days);
        TextView lblDatediff = (TextView) row.findViewById(R.id.dateDiff);

        lblRefNo.setText(list.get(position).getFDDBNOTE_REFNO());

        lblTxnDate.setText(list.get(position).getFDDBNOTE_TXN_DATE().toString());

        lblDays.setText("" + numOfDays);
        lblDatediff.setText("" + datediff);

//		if(rep.getSaleRep(list.get(position).getFDDBNOTE_REP_CODE()).equals(null)){
//			lblRepName.setText("Not Set");
//		}else{
//		lblRepName.setText(""+list.get(position).getFDDBNOTE_REPNAME());
//		}
        // if (list.get(position).getFDDBNOTE_ENTER_AMT() != null)
        // lblDueAmt.setText(String.valueOf(Double.parseDouble(list.get(position).getFDDBNOTE_TOT_BAL())));
        // else

        lblRepName.setText("" + list.get(position).getFDDBNOTE_REPNAME());

        if (isSummery) {

            if (list.get(position).getFDDBNOTE_ENTER_AMT() != null) {
                lblDueAmt.setText(String.format("%,.2f", Double.parseDouble(list.get(position).getFDDBNOTE_TOT_BAL())
                        - Double.parseDouble(list.get(position).getFDDBNOTE_ENTER_AMT())));
                lblAmt.setText(String.format("%,.2f", Double.parseDouble(list.get(position).getFDDBNOTE_ENTER_AMT())));
            }

        } else {
            if(list.get(position).getFDDBNOTE_ENTER_AMT().equals("")){
                lblDueAmt.setText(String.format("%,.2f", Double.parseDouble(list.get(position).getFDDBNOTE_TOT_BAL())));
            }else {

            lblDueAmt.setText(String.format("%,.2f", Double.parseDouble(list.get(position).getFDDBNOTE_TOT_BAL())
                    - Double.parseDouble(list.get(position).getFDDBNOTE_ENTER_AMT())));
            }
//            lblDueAmt.setText(String.format("%,.2f", Double.parseDouble(list.get(position).getFDDBNOTE_TOT_BAL())));
            if (list.get(position).getFDDBNOTE_ENTER_AMT() != null) {
                if (list.get(position).getFDDBNOTE_ENTER_AMT().length() > 0)
                    lblAmt.setText(String.format("%,.2f", Double.parseDouble(list.get(position).getFDDBNOTE_ENTER_AMT())));
                else
                    lblAmt.setText("0.00");
            }

        }


        return row;
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.DAYS);
    }
}
