package com.datamation.kfdupgradesfa.view.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.controller.PaymentController;
import com.datamation.kfdupgradesfa.model.Payment;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static android.graphics.Color.rgb;

/**
 * Used to show the user a list of recorded payments.
 */
public class PaymentDetailsFragment extends Fragment  {

    private static final String LOG_TAG = PaymentDetailsFragment.class.getSimpleName();
    private List<Payment> pinHolders;
    private PaymentListAdapter adapter;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private NumberFormat numberFormat = NumberFormat.getInstance();

    private TextView tvReceiptNo;
    private TextView tvReceiptDate;
    private TextView tvInvoiceNo;
    private TextView tvInvoiceDate;
    private TextView tvPaymentType;
    private TextView tvPaidAmount;
    StickyListHeadersListView pinnedSectionListView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_payment_details, container, false);

        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(true);

        tvReceiptNo = (TextView) rootView.findViewById(R.id.receipt_no);
        tvReceiptDate = (TextView) rootView.findViewById(R.id.receipt_date);
        tvInvoiceDate = (TextView) rootView.findViewById(R.id.invoice_date);
        tvInvoiceNo = (TextView) rootView.findViewById(R.id.invoice_no);
        tvPaymentType = (TextView) rootView.findViewById(R.id.payment_type);
        tvPaidAmount = (TextView) rootView.findViewById(R.id.paid_Amount);

        pinnedSectionListView = (StickyListHeadersListView) rootView.findViewById(R.id.fragment_payment_details_pslv);


        pinHolders = new PaymentController(getActivity()).getPayments();

        adapter = new PaymentListAdapter(getActivity(), pinHolders);
        pinnedSectionListView.setAdapter(adapter);

        return rootView;
    }

    private static class HeaderViewHolder {
        TextView pinLabel;
    }

    private static class ViewHolder {
        ConstraintLayout layout;
        com.datamation.kfdupgradesfa.utils.CustomFont tvReceiptNo;
        com.datamation.kfdupgradesfa.utils.CustomFont tvReceiptDate;
        com.datamation.kfdupgradesfa.utils.CustomFont tvInvoiceDate;
        com.datamation.kfdupgradesfa.utils.CustomFont tvInvoiceNo;
        com.datamation.kfdupgradesfa.utils.CustomFont tvPaymentType;
        com.datamation.kfdupgradesfa.utils.CustomFont tvPaidAmount;

    }

    private class PaymentListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        private LayoutInflater inflater;
        private List<Payment> paymentPinHolders;

        private PaymentListAdapter(Context context, List<Payment> paymentPinHolders) {
            this.paymentPinHolders = paymentPinHolders;
            this.inflater = LayoutInflater.from(context);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getHeaderView(int position, View view, ViewGroup viewGroup) {

            HeaderViewHolder headerViewHolder;
            if (view == null) {
                view = inflater.inflate(R.layout.item_payment_details_header, null, false);

                headerViewHolder = new HeaderViewHolder();

                view.setTag(headerViewHolder);
            } else {
                headerViewHolder = (HeaderViewHolder) view.getTag();
            }



            return view;
        }

        @Override
        public long getHeaderId(int position) {
            return 0;
        }

        @Override
        public int getCount() {
            if (paymentPinHolders != null) return paymentPinHolders.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_payment_details, null, false);

                viewHolder = new ViewHolder();
                viewHolder.layout = (ConstraintLayout) convertView.findViewById(R.id.row_layout);
                viewHolder.tvReceiptNo = (com.datamation.kfdupgradesfa.utils.CustomFont) convertView.findViewById(R.id.row_receipt_no);
                viewHolder.tvReceiptDate = (com.datamation.kfdupgradesfa.utils.CustomFont) convertView.findViewById(R.id.row_receipt_date);
                viewHolder.tvInvoiceNo = (com.datamation.kfdupgradesfa.utils.CustomFont) convertView.findViewById(R.id.row_invoice_no);
                viewHolder.tvInvoiceDate = (com.datamation.kfdupgradesfa.utils.CustomFont) convertView.findViewById(R.id.row_invoice_date);
                viewHolder.tvPaymentType = (com.datamation.kfdupgradesfa.utils.CustomFont) convertView.findViewById(R.id.row_payment_type);
                viewHolder.tvPaidAmount = (com.datamation.kfdupgradesfa.utils.CustomFont) convertView.findViewById(R.id.row_paid_amt);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (position % 2 == 1) {
                convertView.setBackgroundColor(Color.WHITE);
            } else {
                convertView.setBackgroundColor(	rgb(0, 255, 153));
            }

            if (paymentPinHolders != null) {

                viewHolder.tvReceiptNo.setText(paymentPinHolders.get(position).getFPAYMENT_RECEIPT_NO());
                viewHolder.tvReceiptDate.setText(paymentPinHolders.get(position).getFPAYMENT_RECEIPT_DATE());
                viewHolder.tvInvoiceNo.setText(paymentPinHolders.get(position).getFPAYMENT_INVOICE_NO());
                viewHolder.tvInvoiceDate.setText(paymentPinHolders.get(position).getFPAYMENT_INVOICE_DATE());
                viewHolder.tvPaymentType.setText(paymentPinHolders.get(position).getFPAYMENT_TYPE());
                viewHolder.tvPaidAmount.setText(numberFormat.format(Double.parseDouble(paymentPinHolders.get(position).getFPAYMENT_PAID_AMT())));

            }

            viewHolder.tvReceiptNo.setTextColor(getResources().getColor(R.color.mdtp_dark_gray));
            viewHolder.tvReceiptNo.setTypeface(viewHolder.tvReceiptNo.getTypeface(), Typeface.BOLD);
            viewHolder.tvReceiptDate.setTextColor(getResources().getColor(R.color.mdtp_dark_gray));
            viewHolder.tvReceiptDate.setTypeface(viewHolder.tvReceiptDate.getTypeface(), Typeface.BOLD);
            viewHolder.tvInvoiceNo.setTextColor(getResources().getColor(R.color.mdtp_dark_gray));
            viewHolder.tvInvoiceNo.setTypeface(viewHolder.tvInvoiceNo.getTypeface(), Typeface.BOLD);
            viewHolder.tvInvoiceDate.setTextColor(getResources().getColor(R.color.mdtp_dark_gray));
            viewHolder.tvInvoiceDate.setTypeface(viewHolder.tvInvoiceDate.getTypeface(), Typeface.BOLD);
            viewHolder.tvPaymentType.setTextColor(getResources().getColor(R.color.mdtp_dark_gray));
            viewHolder.tvPaymentType.setTypeface(viewHolder.tvPaymentType.getTypeface(), Typeface.BOLD);
            viewHolder.tvPaidAmount.setTextColor(getResources().getColor(R.color.mdtp_dark_gray));
            viewHolder.tvPaidAmount.setTypeface(viewHolder.tvPaidAmount.getTypeface(), Typeface.BOLD);

            return convertView;
        }

    }
}
