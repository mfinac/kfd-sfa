package com.datamation.kfdupgradesfa.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.controller.FreeMslabController;
import com.datamation.kfdupgradesfa.controller.ItemController;
import com.datamation.kfdupgradesfa.controller.OrderDetailController;
import com.datamation.kfdupgradesfa.controller.PreProductController;
import com.datamation.kfdupgradesfa.dialog.CustomKeypadDialog;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.OrderDetail;
import com.datamation.kfdupgradesfa.model.PreProduct;
import com.datamation.kfdupgradesfa.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;

//mithsu//
public class PreOrderAdapter extends RecyclerView.Adapter<PreOrders> {
    Context context;
    public static ArrayList<Product> list;
    String ref = null;
    public SharedPref mSharedPref;

    public PreOrderAdapter(Context context, final ArrayList<Product> list, String refno) {
        this.context = context;
        this.list = list;
        this.ref = refno;
        this.mSharedPref = SharedPref.getInstance(context);
    }

    @Override
    public PreOrders onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewObj = inflater.inflate(R.layout.row_product_item_responsive_layout, viewGroup, false);
        return new PreOrders(viewObj);
    }

    @Override
    public void onBindViewHolder(final PreOrders preOrders, final int i) {
        final Product product = list.get(i);

        try {
            if (Integer.parseInt(product.getFPRODUCT_QOH()) == 0) {
                preOrders.itemBonus.setTextColor(Color.parseColor("#FF5252"));
                preOrders.pack.setTextColor(Color.parseColor("#FF5252"));
                preOrders.ItemName.setTextColor(Color.parseColor("#FF5252"));
                preOrders.Price.setTextColor(Color.parseColor("#FF5252"));
                preOrders.HoQ.setTextColor(Color.parseColor("#FF5252"));
                preOrders.lblQty.setTextColor(Color.parseColor("#FF5252"));
                preOrders.lblCase.setTextColor(Color.parseColor("#FF5252"));
            } else {
                preOrders.itemBonus.setTextColor(Color.parseColor("#4682B4"));
                preOrders.pack.setTextColor(Color.parseColor("#4682B4"));
                preOrders.ItemName.setTextColor(Color.parseColor("#4682B4"));
                preOrders.Price.setTextColor(Color.parseColor("#4682B4"));
                preOrders.HoQ.setTextColor(Color.parseColor("#4682B4"));
                preOrders.lblQty.setTextColor(Color.parseColor("#4682B4"));
                preOrders.lblCase.setTextColor(Color.parseColor("#4682B4"));
            }

        } catch (NumberFormatException ne) {
            ne.printStackTrace();
        }

        preOrders.itemBonus.setText(new FreeMslabController(context).getFreeDetails(product.getFPRODUCT_ITEMCODE(), mSharedPref.getSelectedDebCode()));
        preOrders.pack.setText("");
        preOrders.ItemName.setText(product.getFPRODUCT_ITEMCODE() + " : " + product.getFPRODUCT_ITEMNAME() + " || " + product.getFPRODUCT_PACK());
        preOrders.Price.setText(product.getFPRODUCT_PRICE());
        preOrders.HoQ.setText(product.getFPRODUCT_QOH());
        preOrders.lblQty.setText(product.getFPRODUCT_QTY());
        preOrders.lblCase.setText("0");

        /*Change colors*/
        if (Double.parseDouble(preOrders.lblQty.getText().toString()) > 0)
            preOrders.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));
        else
            preOrders.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


        preOrders.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.generic_item_name, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Generic Item Name");
                alertDialogBuilder.setView(promptView);

                TextView itemName = (TextView) promptView.findViewById(R.id.itemName);

                String genericItemName = new ItemController(context).getGenericItemName(list.get(i).getFPRODUCT_ITEMCODE());
                itemName.setText(genericItemName);

                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();
                return false;
            }
        });


        preOrders.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPref(context).setDiscountClicked("0");
                int qty = Integer.parseInt(preOrders.lblQty.getText().toString());
                qty = qty - 1;
                if (qty >= 0) {
                    preOrders.lblQty.setText(qty + "");
//                    String mQoh = new DecimalFormat("#.##").format(Double.parseDouble(product.getFPRODUCT_PRICE()) * qty);
//                    preOrders.Price.setText(mQoh);
                } else {
                    qty = Integer.parseInt(preOrders.lblQty.getText().toString());
                    Toast.makeText(context, "Cannot allow minus values"
                            , Toast.LENGTH_SHORT).show();
                    preOrders.lblQty.setText(preOrders.lblQty.getText().toString());
                }
                product.setFPRODUCT_QTY(preOrders.lblQty.getText().toString());

                qty = Integer.parseInt(preOrders.lblQty.getText().toString());
                new PreProductController(context).insertOrUpdatePreProductsNew(product);

                //    }
                ArrayList<OrderDetail> toSaveOrderDetails = new OrderDetailController(context).mUpdatePrsSalesNew(product, ref);
                if (new OrderDetailController(context).createOrUpdateOrdDet(toSaveOrderDetails) > 0) {
                    Log.d("ORDER_DETAILS", "Order det saved successfully...");
                } else {
                    Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
                }
                /*Change colors*/
                if (qty == 0)
                    preOrders.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));
            }
        });

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        preOrders.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPref(context).setDiscountClicked("0");
                int qty = Integer.parseInt(preOrders.lblQty.getText().toString());
                mSharedPref.setHeaderNextClicked("1");
                preOrders.lnStripe.setBackground(context.getResources()
                        .getDrawable(R.drawable.custom_textbox_new));

                if (qty < (Double.parseDouble(preOrders.HoQ.getText().toString()))) {
                    qty = qty + 1;
//                    String mQoh = new DecimalFormat("#.##").format(Double.parseDouble(product.getFPRODUCT_PRICE()) * qty);
//                    preOrders.Price.setText(mQoh+"");

                    preOrders.lblQty.setText(qty + "");

                    product.setFPRODUCT_QTY(preOrders.lblQty.getText().toString());
                    new PreProductController(context).insertOrUpdatePreProductsNew(product);
                    ArrayList<OrderDetail> toSaveOrderDetails = new OrderDetailController(context).mUpdatePrsSalesNew(product, ref);

                    if (new OrderDetailController(context).createOrUpdateOrdDet(toSaveOrderDetails) > 0) {
                        Log.d("ORDER_DETAILS", "Order det saved successfully...");
                    } else {
                        Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
                    }
                } else {
                    Toast.makeText(context, "Exceeds available  stock"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        preOrders.btnPlus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        preOrders.btnPlus.setBackground(context.getResources()
                                .getDrawable(R.drawable.icon_plus_d));
                    }
                    break;

                    case MotionEvent.ACTION_UP: {
                        preOrders.btnPlus.setBackground(context.getResources()
                                .getDrawable(R.drawable.icon_plus));
                    }
                    break;
                }
                return false;
            }
        });

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        preOrders.btnMinus.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        preOrders.btnMinus.setBackground(context.getResources()
                                .getDrawable(R.drawable.icon_minus_d));
                    }
                    break;

                    case MotionEvent.ACTION_UP: {
                        preOrders.btnMinus.setBackground(context.getResources()
                                .getDrawable(R.drawable.icon_minus));
                    }
                    break;
                }
                return false;
            }
        });

        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        //--------------------------------------------------------------------------------------------------------------------------
        preOrders.lblQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomKeypadDialog keypad = new CustomKeypadDialog(context, false
                        , new CustomKeypadDialog.IOnOkClickListener() {
                    @Override
                    public void okClicked(double value) {
                        double distrStock = Double.parseDouble(product.getFPRODUCT_QOH());
                        int enteredQty = (int) value;
                        Log.d("<>+++++", "" + distrStock);
                        mSharedPref.setHeaderNextClicked("1");
                        new SharedPref(context).setDiscountClicked("0");

                        if (enteredQty > (int) distrStock) {
                            preOrders.lblQty.setText("0");
                            Toast.makeText(context, "Exceeds available  stock"
                                    , Toast.LENGTH_SHORT).show();
                        } else {
                            preOrders.lblQty.setText(String.valueOf(enteredQty));

                            product.setFPRODUCT_QTY(preOrders.lblQty.getText().toString());

                            ArrayList<OrderDetail> toSaveOrderDetails = new OrderDetailController(context).mUpdatePrsSales(product, ref);
                            if (new OrderDetailController(context).createOrUpdateOrdDet(toSaveOrderDetails) > 0) {
                                Log.d("ORDER_DETAILS", "Order det saved successfully...");
                            } else {
                                Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
                            }
                        }

                        //*Change colors*//**//*
                        if (Integer.parseInt(preOrders.lblQty.getText().toString()) > 0) {
                            preOrders.lnStripe.setBackground(context.getResources()
                                    .getDrawable(R.drawable.custom_textbox_new));
                        } else {
                            preOrders.lnStripe.setBackground(context.getResources()
                                    .getDrawable(R.drawable.custom_textbox));
                        }

                    }
                });

                keypad.show();

                keypad.setHeader("SELECT QUANTITY");
                keypad.loadValue(Double.parseDouble(product.getFPRODUCT_QTY()));
                if (Integer.parseInt(preOrders.lblQty.getText().toString()) > 0) {
                    preOrders.lnStripe.setBackground(context.getResources()
                            .getDrawable(R.drawable.custom_textbox_new));
                } else {
                    preOrders.lnStripe.setBackground(context.getResources()
                            .getDrawable(R.drawable.custom_textbox));
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

class PreOrders extends RecyclerView.ViewHolder {
    LinearLayout lnStripe;
    TextView itemBonus, pack, ItemName, Price, HoQ, lblQty, lblCase;
    ImageButton btnPlus, btnMinus;

    public PreOrders(View itemView) {
        super(itemView);
        lnStripe = itemView.findViewById(R.id.lnProductStripe);
        itemBonus = itemView.findViewById(R.id.row_bonus);
        pack = itemView.findViewById(R.id.row_pack);
        ItemName = itemView.findViewById(R.id.row_itemname);
        Price = itemView.findViewById(R.id.row_price);
        HoQ = itemView.findViewById(R.id.row_qoh);
        lblQty = itemView.findViewById(R.id.et_qty);
        lblCase = itemView.findViewById(R.id.et_case);
        btnPlus = itemView.findViewById(R.id.btnAddition);
        btnMinus = itemView.findViewById(R.id.btnSubtract);
    }

}
//mithsu//

//public class PreOrderAdapter extends BaseAdapter {
//    private LayoutInflater inflater;
//    Context context;
//    ArrayList<Product> list;
//    String ref = null;
//    public SharedPref mSharedPref;
//
//    public PreOrderAdapter(Context context, final ArrayList<Product> list, String refno) {
//        this.inflater = LayoutInflater.from(context);
//        this.context = context;
//        this.list = list;
//        this.ref = refno;
//        this.mSharedPref = SharedPref.getInstance(context);
//    }
//
//    @Override
//    public int getCount() {
//        if (list != null) {
//            return list.size();
//        }
//        return 0;
//    }
//
//    @Override
//    public Product getItem(int position) {
//        return list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        final PreOrderAdapter.ViewHolder viewHolder;
//
//        if (convertView == null) {
//            viewHolder = new PreOrderAdapter.ViewHolder();
//            convertView = inflater.inflate(R.layout.row_product_item_responsive_layout, parent, false);
//
//            viewHolder.lnStripe = (LinearLayout) convertView.findViewById(R.id.lnProductStripe);
//            viewHolder.itemBonus = (TextView) convertView.findViewById(R.id.row_bonus);
//            viewHolder.pack = (TextView) convertView.findViewById(R.id.row_pack);
//            viewHolder.ItemName = (TextView) convertView.findViewById(R.id.row_itemname);
//            viewHolder.Price = (TextView) convertView.findViewById(R.id.row_price);
//            viewHolder.HoQ = (TextView) convertView.findViewById(R.id.row_qoh);
//            viewHolder.lblQty = (TextView) convertView.findViewById(R.id.et_qty);
//            viewHolder.lblCase = (TextView) convertView.findViewById(R.id.et_case);
//            viewHolder.btnPlus = (ImageButton) convertView.findViewById(R.id.btnAddition);
//            viewHolder.btnMinus = (ImageButton) convertView.findViewById(R.id.btnSubtract);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (PreOrderAdapter.ViewHolder) convertView.getTag();
//        }
//        final Product product = getItem(position);
//
//        viewHolder.itemBonus.setText(new FreeMslabController(context).getFreeDetails(product.getFPRODUCT_ITEMCODE(), mSharedPref.getSelectedDebCode()));
//        viewHolder.pack.setText(product.getFPRODUCT_PACK());
//        // viewHolder.unit.setText("Units("+product.getPREPRODUCT_UNIT()+")");
//        viewHolder.ItemName.setText(product.getFPRODUCT_ITEMCODE() + " : " + product.getFPRODUCT_ITEMNAME());
//        viewHolder.Price.setText(product.getFPRODUCT_PRICE());
//        viewHolder.HoQ.setText(product.getFPRODUCT_QOH());
//        // viewHolder.lblQty.setText(""+(Integer.parseInt(product.getPREPRODUCT_QTY())+Integer.parseInt(product.getPREPRODUCT_BALQTY())));
//        viewHolder.lblQty.setText(product.getFPRODUCT_QTY());
//        // Log.d(">>>qtysettext",">>>"+(Integer.parseInt(product.getPREPRODUCT_QTY())+Integer.parseInt(product.getPREPRODUCT_BALQTY())));
//        viewHolder.lblCase.setText("0");
//
//        /*Change colors*/
//        if (Double.parseDouble(viewHolder.lblQty.getText().toString()) > 0)
//            viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));
//        else
//            viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));
//
//        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
//
//        convertView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                LayoutInflater layoutInflater = LayoutInflater.from(context);
//                View promptView = layoutInflater.inflate(R.layout.generic_item_name, null);
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                alertDialogBuilder.setTitle("Generic Item Name");
//                alertDialogBuilder.setView(promptView);
//
//                TextView itemName = (TextView) promptView.findViewById(R.id.itemName);
//
//                String genericItemName = new ItemController(context).getGenericItemName(list.get(position).getFPRODUCT_ITEMCODE());
//                itemName.setText(genericItemName);
//
//                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                    public void onClick(final DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//                AlertDialog alertD = alertDialogBuilder.create();
//                alertD.show();
//                return false;
//            }
//        });
//
//
//        viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SharedPref(context).setDiscountClicked("0");
//                int qty = Integer.parseInt(viewHolder.lblQty.getText().toString());
//                qty = qty - 1;
//                if (qty >= 0) {
//                    viewHolder.lblQty.setText(qty + "");
//                } else {
//                    qty = Integer.parseInt(viewHolder.lblQty.getText().toString());
//                    Toast.makeText(context, "Cannot allow minus values", Toast.LENGTH_SHORT).show();
//                    viewHolder.lblQty.setText(viewHolder.lblQty.getText().toString());
//                }
////                if ((new SharedPref(context).generateOrderId()== new SharedPref(context).getEditOrderId())) {
////
////                        list.get(position).setPREPRODUCT_QTY(viewHolder.lblQty.getText().toString());
////                        list.get(position).setPREPRODUCT_BALQTY("0");
////
////                }else{
//                //      list.get(position).setPREPRODUCT_BALQTY(""+(Integer.parseInt(viewHolder.lblQty.getText().toString())-Integer.parseInt(product.getPREPRODUCT_QTY())));
//                list.get(position).setFPRODUCT_QTY(viewHolder.lblQty.getText().toString());
//
//                qty = Integer.parseInt(viewHolder.lblQty.getText().toString());
//
//                //    }
//                ArrayList<OrderDetail> toSaveOrderDetails = new OrderDetailController(context).mUpdatePrsSales(list.get(position), ref);
//                if (new OrderDetailController(context).createOrUpdateOrdDet(toSaveOrderDetails) > 0) {
//                    Log.d("ORDER_DETAILS", "Order det saved successfully...");
//                } else {
//                    Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
//                }
//                /*Change colors*/
//                if (qty == 0)
//                    viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));
//            }
//        });
//
//        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
//
//        viewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new SharedPref(context).setDiscountClicked("0");
//                int qty = Integer.parseInt(viewHolder.lblQty.getText().toString());
//                mSharedPref.setHeaderNextClicked("1");
//                viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));
//
//                if (qty < (Double.parseDouble(viewHolder.HoQ.getText().toString()))) {
//                    qty = qty + 1;
//                    viewHolder.lblQty.setText(qty + "");
////                            if(new SharedPref(context).generateOrderId()== new SharedPref(context).getEditOrderId()){
////                                list.get(position).setPREPRODUCT_QTY(viewHolder.lblQty.getText().toString());
////                                list.get(position).setPREPRODUCT_BALQTY("0");
////                                Log.d(">>>qtysettextplus",">>>"+(Integer.parseInt(product.getPREPRODUCT_QTY())+" Balqty"+Integer.parseInt(product.getPREPRODUCT_BALQTY())));
////
////                            }else{
//                    //  list.get(position).setPREPRODUCT_BALQTY(""+(Integer.parseInt(viewHolder.lblQty.getText().toString())-Integer.parseInt(product.getPREPRODUCT_QTY())));
//                    list.get(position).setFPRODUCT_QTY(viewHolder.lblQty.getText().toString());
//
////                                Log.d(">>>qtysettextplus",">>>"+(Integer.parseInt(product.getPREPRODUCT_QTY())+" Balqty"+Integer.parseInt(product.getPREPRODUCT_BALQTY())));
////                                Log.d(">>>balqtyinplus",">>>"+list.get(position).getPREPRODUCT_BALQTY());
//
//                    //       }
//                    ArrayList<OrderDetail> toSaveOrderDetails = new OrderDetailController(context).mUpdatePrsSales(list.get(position), ref);
//                    if (new OrderDetailController(context).createOrUpdateOrdDet(toSaveOrderDetails) > 0) {
//                        Log.d("ORDER_DETAILS", "Order det saved successfully...");
//                    } else {
//                        Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
//                    }
//                } else {
//                    Toast.makeText(context, "Exceeds available  stock", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
//
//        viewHolder.btnPlus.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        viewHolder.btnPlus.setBackground(context.getResources().getDrawable(R.drawable.icon_plus_d));
//                    }
//                    break;
//
//                    case MotionEvent.ACTION_UP: {
//                        viewHolder.btnPlus.setBackground(context.getResources().getDrawable(R.drawable.icon_plus));
//                    }
//                    break;
//                }
//                return false;
//            }
//        });
//
//        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
//
//        viewHolder.btnMinus.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        viewHolder.btnMinus.setBackground(context.getResources().getDrawable(R.drawable.icon_minus_d));
//                    }
//                    break;
//
//                    case MotionEvent.ACTION_UP: {
//                        viewHolder.btnMinus.setBackground(context.getResources().getDrawable(R.drawable.icon_minus));
//                    }
//                    break;
//                }
//                return false;
//            }
//        });
//
//        /*-*-*-*-*-*-*--*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
//
//
//        //--------------------------------------------------------------------------------------------------------------------------
//        viewHolder.lblQty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomKeypadDialog keypad = new CustomKeypadDialog(context, false, new CustomKeypadDialog.IOnOkClickListener() {
//                    @Override
//                    public void okClicked(double value) {
//                        double distrStock = Double.parseDouble(product.getFPRODUCT_QOH());
//                        int enteredQty = (int) value;
//                        Log.d("<>+++++", "" + distrStock);
//                        mSharedPref.setHeaderNextClicked("1");
//                        new SharedPref(context).setDiscountClicked("0");
//
//                        if (enteredQty > (int) distrStock) {
//                            viewHolder.lblQty.setText("0");
//                            Toast.makeText(context, "Exceeds available  stock", Toast.LENGTH_SHORT).show();
//                        } else {
//                            viewHolder.lblQty.setText(String.valueOf(enteredQty));
////                                    if(new SharedPref(context).generateOrderId()== new SharedPref(context).getEditOrderId()){
////                                        list.get(position).setPREPRODUCT_QTY(viewHolder.lblQty.getText().toString());
////                                        list.get(position).setPREPRODUCT_BALQTY("0");
////                                    }else{
//                            //    list.get(position).setPREPRODUCT_BALQTY(""+(Integer.parseInt(viewHolder.lblQty.getText().toString())-Integer.parseInt(product.getPREPRODUCT_QTY())));
//                            list.get(position).setFPRODUCT_QTY(viewHolder.lblQty.getText().toString());
//
//                            //     Log.d(">>>balqtyinqtydlg",">>>"+list.get(position).getPREPRODUCT_BALQTY());
//
//                            //            }
//                            //   have a logic for qty - and qty +
//
//                            ArrayList<OrderDetail> toSaveOrderDetails = new OrderDetailController(context).mUpdatePrsSales(list.get(position), ref);
//                            if (new OrderDetailController(context).createOrUpdateOrdDet(toSaveOrderDetails) > 0) {
//                                Log.d("ORDER_DETAILS", "Order det saved successfully...");
//                            } else {
//                                Log.d("ORDER_DETAILS", "Order det saved unsuccess...");
//                            }
//                        }
//
//                        //*Change colors*//**//*
//                        if (Integer.parseInt(viewHolder.lblQty.getText().toString()) > 0) {
//                            viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));
//                        } else {
//                            viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));
//                        }
//
//                    }
//                });
//
//                keypad.show();
//
//                keypad.setHeader("SELECT QUANTITY");
//                keypad.loadValue(Double.parseDouble(product.getFPRODUCT_QTY()));
//                if (Integer.parseInt(viewHolder.lblQty.getText().toString()) > 0) {
//                    viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox_new));
//                } else {
//                    viewHolder.lnStripe.setBackground(context.getResources().getDrawable(R.drawable.custom_textbox));
//                }
//
//            }
//        });
//
//        return convertView;
//    }
//
//    private static class ViewHolder {
//        LinearLayout lnStripe;
//        TextView itemBonus;
//        TextView unit;
//        TextView pack;
//        TextView ItemName;
//        TextView Price;
//        TextView HoQ;
//        TextView lblQty;
//        TextView lblCase;
//        ImageButton btnPlus;
//        ImageButton btnMinus;
//
//    }
//}


