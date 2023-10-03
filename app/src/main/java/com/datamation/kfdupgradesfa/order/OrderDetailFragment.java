
package com.datamation.kfdupgradesfa.order;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.PreOrderAdapter;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.ItemController;
import com.datamation.kfdupgradesfa.controller.ItemLocController;
import com.datamation.kfdupgradesfa.controller.ItemPriceController;
import com.datamation.kfdupgradesfa.controller.LocationsController;
import com.datamation.kfdupgradesfa.controller.OrdFreeIssueController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.OrderDetailController;
import com.datamation.kfdupgradesfa.controller.PreProductController;
import com.datamation.kfdupgradesfa.controller.ProductController;
import com.datamation.kfdupgradesfa.controller.ReferenceController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.controller.SupplierController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.helpers.OrderResponseListener;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.helpers.ValueHolder;
import com.datamation.kfdupgradesfa.model.FreeIssue;
import com.datamation.kfdupgradesfa.model.FreeItemDetails;
import com.datamation.kfdupgradesfa.model.Item;
import com.datamation.kfdupgradesfa.model.ItemFreeIssue;
import com.datamation.kfdupgradesfa.model.OrdFreeIssue;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.model.OrderDetail;
import com.datamation.kfdupgradesfa.model.PreProduct;
import com.datamation.kfdupgradesfa.model.Product;
import com.datamation.kfdupgradesfa.model.Supplier;
import com.datamation.kfdupgradesfa.reports.PreSalesReportAdapter;
import com.datamation.kfdupgradesfa.utils.CustomViewPager;
import com.datamation.kfdupgradesfa.view.OrderActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderDetailFragment extends Fragment {

    private static final String TAG = "OrderDetailFragment";
    public View view;
    public SharedPref mSharedPref;
    int totPieces = 0;
    int seqno = 0;
    SweetAlertDialog sweetAlertDialog;
    OrderResponseListener preSalesResponseListener;
    ArrayList<Product> productList = null, allItemList = null;
    int clickCount = 0;
    OrderActivity mainActivity;
    private String refNo, repCode;
    private MyReceiver r;
    private Order tmpsoHed = null;
    //mithsu//
//    ListView lvProducts;
    RecyclerView lvProducts;
    AutoCompleteTextView supSearch, itemSearch;
    MyReceiver rd;
    private FloatingActionButton next;
    OrderResponseListener responseListener;
    ArrayList<Item> loadlist;
    String debCode, strLoccode;
    ImageView remove;



    //PreSalesResponseListener preSalesResponseListener;
    public OrderDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.product_dialog_layout, container, false);
        seqno = 0;
        totPieces = 0;
        mSharedPref = SharedPref.getInstance(getActivity());
        lvProducts = view.findViewById(R.id.lv_product_list);
        //mithsu//

        supSearch = (AutoCompleteTextView) view.findViewById(R.id.search_supplier);
        itemSearch = (AutoCompleteTextView) view.findViewById(R.id.item_search);
        next = (FloatingActionButton) view.findViewById(R.id.fab);
        refNo = new ReferenceController(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal));
        repCode = new SalRepController(getActivity()).getCurrentRepCode();

        lvProducts.setLongClickable(true);
        remove = (ImageView) view.findViewById(R.id.img_remove);
        mainActivity = (OrderActivity) getActivity();
        tmpsoHed = new Order();

        allItemList = new ArrayList<Product>();
        new LoardingProductFromDB().execute();

        mSharedPref.setGlobalVal("preKeyIsFreeClicked", "0");
        mSharedPref.setDiscountClicked("0");
        clickCount = 0;


        //&^&^& MMS-2022/02/01 %$%$//
        final ArrayList<Supplier> splist = new SupplierController(getActivity()).getAllSuppliers();
        final ArrayList<String> supNamesStr = new ArrayList<String>();
        for (Supplier supplier : splist) {
            supNamesStr.add(supplier.getFSUP_CODE() + "- " + supplier.getFSUP_NAME());
        }

        final ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, supNamesStr);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supSearch.setAdapter(dataAdapter3);
        //&^&^& MMS-2022/02/01 %$%$//

        final ArrayList<String> itemNamesStr = new ArrayList<String>();

        supSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                itemSearch.setText("");

            }


            @Override
            public void afterTextChanged(Editable editable) {
                debCode = new OrderController(getActivity()).getRefnoByDebcode(new ReferenceController(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal)));

                if (editable.equals("")) {

                    itemSearch.setText("");


                } else if (!editable.equals("")) {

                    strLoccode = mSharedPref.getGlobalVal("KeyLoc");
                    productList.clear();

                    String str = editable.toString();
                    String[] arrOfStr = str.split("-", 2);

                    productList = new ItemController(getActivity()).getAllItemsBySupplier(arrOfStr[0].trim(), refNo, strLoccode, debCode, mSharedPref.getGlobalVal("KeyCost"));
                    lvProducts.setAdapter(new PreOrderAdapter(getActivity(), productList, refNo));
                    lvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


                    //&^&^& MMS-2022/02/02 %$%$//
                    final ArrayList<Item> itmlist = new ItemController(getActivity()).getAllItemsBySupCode(arrOfStr[0].trim());

                    for (Item item : itmlist) {
                        itemNamesStr.add(item.getFITEM_ITEM_NAME().trim());
                    }
                }


            }
        });


        final ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, itemNamesStr);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSearch.setAdapter(itemAdapter);
        //&^&^& MMS-2022/02/02 %$%$//

        itemSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence itmqry, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable itemquery) {
                debCode = new OrderController(getActivity()).getRefnoByDebcode(new ReferenceController(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal)));
                strLoccode = mSharedPref.getGlobalVal("KeyLoc");

                if (supSearch.getText().length() > 0) {
                    if (!itemquery.equals("")) {


                        ArrayList<Product> prdList = filter(productList, itemquery, itemquery.length());
                        Log.d(">>>prdList", "afterTextChanged: " + productList);
                        lvProducts.setAdapter(new PreOrderAdapter(getActivity(), prdList, refNo));

                    }
                } else {
                    if (!itemquery.equals("")) {

                        ArrayList<Product> prdItmList = filter(productList, itemquery, itemquery.length());
                        lvProducts.setAdapter(new PreOrderAdapter(getActivity(), prdItmList, refNo));
                    }
                }
                lvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (supSearch.getText().length() > 0) {
                    supSearch.setText("");

                }
                if (itemSearch.getText().length() > 0) {

                    itemSearch.setText("");
                }
                new LoardingProductFromDB().execute();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedPref.setDiscountClicked("1");
                new OrderDetailController(getActivity()).DeleteZeroValueData(refNo);
                new CalculateFree(mSharedPref.getSelectedDebCode()).execute();
                preSalesResponseListener.moveNextToFragment(1);
            }
        });

        return view;
    }

    public void mToggleTextbox() {
        Log.d("Detail>>", ">>" + mSharedPref.getHeaderNextClicked());
        if (mSharedPref.getHeaderNextClicked() == "1") {
            debCode = new OrderController(getActivity()).getRefnoByDebcode(new ReferenceController(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal)));

            strLoccode = mSharedPref.getGlobalVal("KeyLoc");

         //  new LoardingProductFromDB().execute();

        } else {
            preSalesResponseListener.moveBackToFragment(0);
            Toast.makeText(getActivity(), "Cannot proceed,Please click arrow button to save header details...", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(rd);
        Log.d("order_detail", "clicked_count" + clickCount);

    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(rd);
        Log.d("order_detail", "clicked_count" + clickCount);

    }

    public void onResume() {
        super.onResume();
        rd = new MyReceiver();
        OrderDetailFragment.this.mToggleTextbox();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(rd, new IntentFilter("TAG_PRE_DETAILS"));
        Log.d("order_detail", "clicked_count" + clickCount);
    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("order_detail", "clicked_count" + clickCount);
            Log.d("order_detail", "clicked_count" + clickCount);
        }
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            preSalesResponseListener = (OrderResponseListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onButtonPressed");
        }
    }

    public class LoardingProductFromDB extends AsyncTask<Object, Object, ArrayList<Product>> {

        CustomProgressDialog pdialog;

        public LoardingProductFromDB() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            sweetAlertDialog.setTitleText("Fetch Data Please Wait.");
            sweetAlertDialog.setCancelable(false);
            //pDialog.show();
         //   pdialog.dismiss();
        }

        @Override
        protected ArrayList<Product> doInBackground(Object... objects) {
            debCode = new OrderController(getActivity()).getRefnoByDebcode(new ReferenceController(getActivity()).getCurrentRefNo(getResources().getString(R.string.NumVal)));

            strLoccode = mSharedPref.getGlobalVal("KeyLoc");
            if (new OrderDetailController(getActivity()).tableHasRecords(refNo)) {
                productList = new OrderDetailController(getActivity()).getAlreadyOrderedItems("", strLoccode, refNo, "TxnType ='21'", mSharedPref.getGlobalVal("KeyCost"));
              //  pdialog.dismiss();
            } else {
                productList = new ItemController(getActivity()).getAllItemFor("TxnType ='21'", refNo, strLoccode, debCode, mSharedPref.getGlobalVal("KeyCost"));
               // allItemList.clear();
                allItemList = productList;

            }
             return productList;
        }


        @Override
        protected void onPostExecute(ArrayList<Product> products) {
            super.onPostExecute(products);

            lvProducts.setAdapter(new PreOrderAdapter(getActivity(), products, refNo));
            //mithsu//
            lvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//            pdialog.dismiss();
            //mithsu//

        }

    }


    private ArrayList<Product> filter(ArrayList<Product> products, Editable query, int len) {
        final String lowerCaseQuery = query.toString().toLowerCase();

        final ArrayList<Product> filteredModelList = new ArrayList<>();
        for (Product product : products) {

            final String itemNm = product.getFPRODUCT_ITEMNAME().toLowerCase();
            if (itemNm.contains(lowerCaseQuery)) {

                String itmNmStr = itemNm.substring(0, len);
                String itmCodeStr = itemNm.substring(0, len);

                Log.d("^^^Str", "filter: " + itmNmStr);

                if (itmNmStr.equals(lowerCaseQuery) || itmCodeStr.equals(lowerCaseQuery)) {
                    filteredModelList.add(product);
                }
            }
        }

        return filteredModelList;
    }

    public class CalculateFree extends AsyncTask<Object, Object, ArrayList<FreeItemDetails>> {
        CustomProgressDialog pdialog;
        private String debcode;

        public CalculateFree(String debcode) {
            this.pdialog = new CustomProgressDialog(getActivity());
            this.debcode = debcode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Calculating promotions.. Please Wait.");
            pdialog.show();

            //pDialog.show();
        }

        @Override
        protected ArrayList<FreeItemDetails> doInBackground(Object... objects) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pdialog.setMessage("Calculating Free...");
                }
            });
            new OrderDetailController(getActivity()).restFreeIssueData(refNo);
            ArrayList<OrderDetail> dets = new OrderDetailController(getActivity()).getSAForFreeIssueCalc(refNo);
            FreeIssue issue = new FreeIssue(getActivity());
            ArrayList<FreeItemDetails> list = issue.getFreeItemsBySalesItem(dets);
            try {

                for (FreeItemDetails freeItemDetails : list) {
                    int freeQty = freeItemDetails.getFreeQty();
                    String costCode = mSharedPref.getCostCode();
                    String locCode = new LocationsController(getActivity()).getRepLocation(costCode);
                    String itemQOH = new ItemLocController(getActivity()).getProductQOH(freeItemDetails.getFreeIssueSelectedItem(), locCode);
                    boolean validQOH = new OrderDetailController(getActivity()).getCheckQOH(refNo, freeItemDetails.getFreeIssueSelectedItem(), freeQty, Integer.parseInt(itemQOH));
                    if (validQOH) {
                        updateFreeIssues(freeItemDetails);
                    } else {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Not Enough Quantity On Hand For FREE ISSUES..!", Toast.LENGTH_LONG).show();
                            }
                        });

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("Exception", e.toString());
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pdialog.setMessage("Calculed Free...");
                }
            });
            return list;
        }


        @Override
        protected void onPostExecute(ArrayList<FreeItemDetails> products) {
            super.onPostExecute(products);

            if (pdialog.isShowing()) {
                pdialog.dismiss();
            }
           // preSalesResponseListener.moveNextToFragment(2);
        }
    }

    private boolean updateFreeIssues(final FreeItemDetails itemDetails) {

        final ArrayList<ItemFreeIssue> itemFreeIssues;
        final String FIRefNo = itemDetails.getRefno();
        final ItemController itemsDS = new ItemController(getActivity());
        itemFreeIssues = itemsDS.getAllFreeItemNameByRefno(itemDetails.getRefno());

        for (ItemFreeIssue itemFreeIssue : itemFreeIssues) {

            seqno++;
            OrderDetail ordDet = new OrderDetail();
            OrderDetailController detDS = new OrderDetailController(getActivity());
            ArrayList<OrderDetail> ordList = new ArrayList<OrderDetail>();
            ItemPriceController priDS = new ItemPriceController(getActivity());

            ordDet.setFORDDET_ID("0");
            ordDet.setFORDDET_AMT("0.00");
            ordDet.setFORDDET_BAL_QTY("0");
            ordDet.setFORDDET_B_AMT("0.00");
            ordDet.setFORDDET_B_DIS_AMT("0.00");
            ordDet.setFORDDET_BP_DIS_AMT("0.00");

            double unitPrice = Double.parseDouble(priDS.getProductPriceByCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE()));

            ordDet.setFORDDET_B_SELL_PRICE(unitPrice + "");
            ordDet.setFORDDET_BT_TAX_AMT("0");
            ordDet.setFORDDET_BT_SELL_PRICE("0");
            ordDet.setFORDDET_CASE("0");
            ordDet.setFORDDET_CASE_QTY("0");
            ordDet.setFORDDET_DIS_AMT("0.00");
            ordDet.setFORDDET_DIS_PER("0.00");
            ordDet.setFORDDET_FREE_QTY(itemDetails.getFreeQty() + "");
            ordDet.setFORDDET_ITEM_CODE(itemFreeIssue.getItems().getFITEM_ITEM_CODE());
            ordDet.setFORDDET_P_DIS_AMT("0.00");
            ordDet.setFORDDET_PRIL_CODE(priDS.getPrilCodeByItemCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE()));
            ordDet.setFORDDET_QTY(itemDetails.getFreeQty() + "");
            ordDet.setFORDDET_DIS_VAL_AMT("0.00");
            ordDet.setFORDDET_REA_CODE("");
            ordDet.setFORDDET_TYPE("FI");
            ordDet.setFORDDET_RECORD_ID("");
            ordDet.setFORDDET_REFNO(refNo);
            long time = System.currentTimeMillis();
            ordDet.setOrderId(time);
            ordDet.setFORDDET_SELL_PRICE(unitPrice + "");
            ordDet.setFORDDET_SEQNO(seqno + "");
            ordDet.setFORDDET_TAX_AMT("0.00");
            ordDet.setFORDDET_TAX_COM_CODE("");
            ordDet.setFORDDET_TIMESTAMP_COLUMN("");
            ordDet.setFORDDET_T_SELL_PRICE("0");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            ordDet.setFORDDET_TXN_DATE(dateFormat.format(date));
            ordDet.setFORDDET_TXN_TYPE("21");
            ordDet.setFORDDET_IS_ACTIVE("1");
            ordDet.setFORDDET_ITEMNAME(new ItemController(getActivity()).getItemNameByCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE()));
            ordDet.setFORDDET_PACKSIZE(new ItemController(getActivity()).getPackSizeByCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE()));
            /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*OrdFreeIssue table update*-*-*-*-*-*-*-*-*-*-*-*-*-*/
            OrdFreeIssue ordFreeIssue = new OrdFreeIssue();
            ordFreeIssue.setOrdFreeIssue_ItemCode(itemFreeIssue.getItems().getFITEM_ITEM_CODE());
            ordFreeIssue.setOrdFreeIssue_Qty(itemDetails.getFreeQty() + "");
            ordFreeIssue.setOrdFreeIssue_RefNo(FIRefNo);
            ordFreeIssue.setOrdFreeIssue_RefNo1(refNo);
            ordFreeIssue.setOrdFreeIssue_TxnDate(dateFormat.format(date));
            new OrdFreeIssueController(getActivity()).UpdateOrderFreeIssue(ordFreeIssue);
            /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*/

            ordList.add(ordDet);

            if (detDS.createOrUpdateOrdDet(ordList) > 0) {

                //free calculate flag should be true
            }
        }
        return true;
    }

}
