package com.datamation.kfdupgradesfa.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.downloadListAdapter;
import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.controller.CompanyDetailsController;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.DayNPrdHedController;
import com.datamation.kfdupgradesfa.controller.DownloadController;
import com.datamation.kfdupgradesfa.controller.FInvhedL3Controller;
import com.datamation.kfdupgradesfa.controller.FItenrDetController;
import com.datamation.kfdupgradesfa.controller.FItenrHedController;
import com.datamation.kfdupgradesfa.controller.FinvDetL3Controller;
import com.datamation.kfdupgradesfa.controller.FreeDebController;
import com.datamation.kfdupgradesfa.controller.FreeDetController;
import com.datamation.kfdupgradesfa.controller.FreeHedController;
import com.datamation.kfdupgradesfa.controller.FreeItemController;
import com.datamation.kfdupgradesfa.controller.FreeMslabController;
import com.datamation.kfdupgradesfa.controller.ItemController;
import com.datamation.kfdupgradesfa.controller.ItemLocController;
import com.datamation.kfdupgradesfa.controller.ItemPriceController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.OutstandingController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReferenceDetailDownloader;
import com.datamation.kfdupgradesfa.controller.ReferenceSettingController;
import com.datamation.kfdupgradesfa.controller.RepDebtorController;
import com.datamation.kfdupgradesfa.controller.RouteController;
import com.datamation.kfdupgradesfa.controller.RouteDetController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Control;
import com.datamation.kfdupgradesfa.model.Debtor;
import com.datamation.kfdupgradesfa.model.FddbNote;
import com.datamation.kfdupgradesfa.model.FreeDeb;
import com.datamation.kfdupgradesfa.model.FreeDet;
import com.datamation.kfdupgradesfa.model.FreeHed;
import com.datamation.kfdupgradesfa.model.FreeItem;
import com.datamation.kfdupgradesfa.model.FreeMslab;
import com.datamation.kfdupgradesfa.model.Item;
import com.datamation.kfdupgradesfa.model.ItemLoc;
import com.datamation.kfdupgradesfa.model.ItemPri;
import com.datamation.kfdupgradesfa.model.Route;
import com.datamation.kfdupgradesfa.model.RouteDet;
import com.datamation.kfdupgradesfa.settings.TaskTypeDownload;
import com.datamation.kfdupgradesfa.utils.NetworkUtil;
import com.datamation.kfdupgradesfa.utils.UtilityContainer;
import com.datamation.kfdupgradesfa.view.ActivityHome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FragmentCategoryWiseDownload extends Fragment {

    private View view;
    private TextView downItems, downFree, downRoute, downOutstanding, downPrice, downStock, downOthers, downCustomer;
    NetworkFunctions networkFunctions;
    ApiInterface apiInterface;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private long timeInMillis;

    ArrayList<Control> downloadList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_download, container, false);

        getActivity().setTitle("Category Wise Download");
        networkFunctions = new NetworkFunctions(getActivity());
        //initializations
        downItems = (TextView) view.findViewById(R.id.items_download);
        downFree = (TextView) view.findViewById(R.id.free_download);
        downRoute = (TextView) view.findViewById(R.id.route_download);
        downOutstanding = (TextView) view.findViewById(R.id.outstanding_download);
        downPrice = (TextView) view.findViewById(R.id.price_download);
        downStock = (TextView) view.findViewById(R.id.stock_download);
        downOthers = (TextView) view.findViewById(R.id.other_download);
        downCustomer = (TextView) view.findViewById(R.id.customer_download);

        downItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mithsu", "AAA " + SharedPref.getInstance(getActivity()).getLoginUser().getFREP_CODE());

                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());
                if (connectionStatus == true) {

                    if (isAllUploaded(getActivity())) {

                        try {
                            //mithsu//
                            new itemsDownload(SharedPref.getInstance(getActivity())
                                    .getLoginUser().getFREP_CODE()).execute();
                            //mithsu//
                        } catch (Exception e) {
                            Log.e("## ErrorInItemDown ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });

        downFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());
                if (connectionStatus == true) {

                    if (isAllUploaded(getActivity())) {

                        try {
                            //mithsu//
                            new freeDownload(SharedPref.getInstance(getActivity())
                                    .getLoginUser().getFREP_CODE()).execute();
                            //mithsu//
                            // new freeDownload(SharedPref.getInstance(getActivity()).getLoginUser().getRepCode()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInItemDown ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        downRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());
                if (connectionStatus == true) {

                    if (isAllUploaded(getActivity())) {

                        try {
                            //mithsu//
                            new routeDownload(SharedPref.getInstance(getActivity())
                                    .getLoginUser().getFREP_CODE()).execute();
                            //mithsu//
                            // new routeDownload(SharedPref.getInstance(getActivity()).getLoginUser().getRepCode()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInItemDown ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        downOutstanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());
                if (connectionStatus == true) {

                    if (isAllUploaded(getActivity())) {

                        try {
                            //mithsu//
                            new outstandingDownload(SharedPref.getInstance(getActivity())
                                    .getLoginUser().getFREP_CODE()).execute();
                            //mithsu//
                            //  new outstandingDownload(SharedPref.getInstance(getActivity()).getLoginUser().getRepCode()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInOutstanding##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        downCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());

                if (connectionStatus == true) {
                    if (isAllUploaded(getActivity())) {
                        try {
                                new customerDownload(SharedPref.getInstance(getActivity()).getUserId()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInCustomer ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        downOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());

                if (connectionStatus == true) {
                    if (isAllUploaded(getActivity())) {
                        try {
                            //mithsu//
                            Integer ordHedListCount = new OrderController(getActivity()).getAllDayBeforeUnSyncOrdHed();
                            Integer receiptlistCount = new ReceiptController(getActivity()).getAllDayBeforeUnSyncRecHedCount();
                            Integer npHedListCount = new DayNPrdHedController(getActivity()).getAllDayBeforeUnSyncNonPrdCount();

                            if (ordHedListCount > 0 || receiptlistCount > 0 || npHedListCount > 0) {
                                Toast.makeText(getActivity(), "Please upload all transaction details", Toast.LENGTH_LONG).show();
                            } else {
                                new OtherDownload(SharedPref.getInstance(getActivity())
                                        .getLoginUser().getFREP_CODE()).execute();
                            }

                            //mithsu//
                            // new OtherDownload(SharedPref.getInstance(getActivity()).getLoginUser().getRepCode()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInOtherDown ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        //mithsu//
        downStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());

                if (connectionStatus == true) {
                    if (isAllUploaded(getActivity())) {
                        try {
                            new stockDownload(SharedPref.getInstance(getActivity())
                                    .getLoginUser().getFREP_CODE()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInStockDown ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        downPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());

                if (connectionStatus == true) {
                    if (isAllUploaded(getActivity())) {
                        try {
                            new priceDownload(SharedPref.getInstance(getActivity())
                                    .getLoginUser().getFREP_CODE()).execute();
                        } catch (Exception e) {
                            Log.e("## ErrorInPriceDown ##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        downCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean connectionStatus = NetworkUtil.isNetworkAvailable(getActivity());

                if (connectionStatus == true) {
                    if (isAllUploaded(getActivity())) {
                        try {
                            new customerDownload(SharedPref.getInstance(getActivity())
                                    .getUserId()).execute();
                        } catch (Exception e) {
                            Log.e("##ErrorInCustomerDown##", e.toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Upload All Transactions",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        //mithsu//

        //DISABLED BACK NAVIGATION
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("", "keyCode: " + keyCode);
                ActivityHome.navigation.setVisibility(View.VISIBLE);

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(getActivity(), "Back button disabled!", Toast.LENGTH_SHORT).show();
                    return true;
                } else if ((keyCode == KeyEvent.KEYCODE_HOME)) {

                    getActivity().finish();

                    return true;

                } else {
                    return false;
                }
            }
        });


        return view;
    }


    private boolean isAllUploaded(Context context) {
        Boolean allUpload = true;


        return allUpload;
    }

    //item download asynctask
    private class itemsDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String debcode;
        int totalRecords = 0;

        public itemsDownload(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading Items...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {
                    new DownloadController(getActivity()).deleteAll();
                    /*****************Item *****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading item data...");
                        }
                    });

                    String item = "";
                    try {
                        item = networkFunctions.getItems(debcode);
                        // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing item
                    try {
                        JSONObject itemJSON = new JSONObject(item);
                        JSONArray itemJSONArray = itemJSON.getJSONArray("data");
                        totalRecords = itemJSONArray.length();

                        ArrayList<Item> itemList = new ArrayList<Item>();

                        for (int i = 0; i < itemJSONArray.length(); i++) {
                            itemList.add(Item.parseItem(itemJSONArray.getJSONObject(i)));
                        }

                        ItemController itemController = new ItemController(getActivity());
                        itemController.deleteAll();
                        itemController.InsertOrReplaceItems(itemList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + itemList.size(), "" + totalRecords, "Item Info");


                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end item **********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Download complete...");
                        }
                    });
                    return true;
                } else {
                    //errors.add("Please enter correct username and password");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            } catch (JSONException e) {
                e.printStackTrace();

                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            pdialog.setMessage("Finalizing itembundle data");
            pdialog.setMessage("Download Completed..");

            downloadList = new ArrayList<>();
            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }

            String category = ("'Item Info'");
            for (Control c : new DownloadController(getActivity()).getAllDownloadByCategoryName(category)) {
                downloadList.add(c);
            }
            mDownloadResult(downloadList);
        }
    }

    //free download asynctask
    private class freeDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String debcode;


        public freeDownload(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading Free...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {
                    new DownloadController(getActivity()).deleteAll();
                    FreeHedController freeHedController = new FreeHedController(getActivity());
                    freeHedController.deleteAll();
                    FreeDetController freedetController = new FreeDetController(getActivity());
                    freedetController.deleteAll();
                    FreeDebController freedebController = new FreeDebController(getActivity());
                    freedebController.deleteAll();
                    FreeItemController freeitemController = new FreeItemController(getActivity());
                    freeitemController.deleteAll();
                    FreeMslabController freeMslabController = new FreeMslabController(getActivity());
                    freeMslabController.deleteAll();

                    /*****************FreeHed**********************************************************************/
                    String freehed = "";
                    int totalHedRecords = 0;
                    try {
                        freehed = networkFunctions.getFreeHed(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }


                    // Processing freehed
                    try {
                        JSONObject freeHedJSON = new JSONObject(freehed);
                        JSONArray freeHedJSONArray = freeHedJSON.getJSONArray("data");
                        totalHedRecords = freeHedJSONArray.length();
                        ArrayList<FreeHed> freeHedList = new ArrayList<FreeHed>();

                        for (int i = 0; i < freeHedJSONArray.length(); i++) {
                            freeHedList.add(FreeHed.parseFreeHed(freeHedJSONArray.getJSONObject(i)));
                        }
                        freeHedController.createOrUpdateFreeHed(freeHedList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + freeHedList.size(), "" + totalHedRecords, "Free Hed Info");

                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end freeHed**********************************************************************/
                    /*****************Freedet**********************************************************************/
                    String freedet = "";
                    int totalDetRecords = 0;
                    try {
                        freedet = networkFunctions.getFreeDet(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    try {
                        JSONObject freedetJSON = new JSONObject(freedet);
                        JSONArray freedetJSONArray = freedetJSON.getJSONArray("data");
                        totalDetRecords = freedetJSONArray.length();
                        ArrayList<FreeDet> freedetList = new ArrayList<FreeDet>();

                        for (int i = 0; i < freedetJSONArray.length(); i++) {
                            freedetList.add(FreeDet.parseFreeDet(freedetJSONArray.getJSONObject(i)));
                        }
                        freedetController.createOrUpdateFreeDet(freedetList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + freedetList.size(), "" + totalDetRecords, "Free Det Info");

                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end freedet**********************************************************************/
                    /*****************freedeb**********************************************************************/
                    String freedeb = "";
                    int totalDebRecords = 0;

                    try {
                        freedeb = networkFunctions.getFreeDebs(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }


                    // Processing freedeb
                    try {
                        JSONObject freedebJSON = new JSONObject(freedeb);
                        JSONArray freedebJSONArray = freedebJSON.getJSONArray("data");
                        totalDebRecords = freedebJSONArray.length();
                        ArrayList<FreeDeb> freedebList = new ArrayList<FreeDeb>();

                        for (int i = 0; i < freedebJSONArray.length(); i++) {
                            freedebList.add(FreeDeb.parseFreeDeb(freedebJSONArray.getJSONObject(i)));
                        }
                        freedebController.createOrUpdateFreeDeb(freedebList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + freedebList.size(), "" + totalDebRecords, "Free Deb Info");

                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end freedeb**********************************************************************/
                    /*****************FreeItem*********************************************************************/
                    String freeitem = "";
                    int totalItemRecords = 0;
                    try {
                        freeitem = networkFunctions.getFreeItems();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing freeItem
                    try {
                        JSONObject freeitemJSON = new JSONObject(freeitem);
                        JSONArray freeitemJSONArray = freeitemJSON.getJSONArray("data");
                        totalItemRecords = freeitemJSONArray.length();
                        ArrayList<FreeItem> freeitemList = new ArrayList<FreeItem>();

                        for (int i = 0; i < freeitemJSONArray.length(); i++) {
                            freeitemList.add(FreeItem.parseFreeItem(freeitemJSONArray.getJSONObject(i)));
                        }
                        freeitemController.createOrUpdateFreeItem(freeitemList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + freeitemList.size(), "" + totalItemRecords, "Free Item Info");

                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end freeItem**********************************************************************/

                    /*****************Freemslab - kaveesha -11-06-2020 *********************************************************************/
                    String freemslab = "";
                    int totalMsRecords = 0;
                    try {
                        freemslab = networkFunctions.getFreeMslab();
                    } catch (IOException e) {

                        throw e;
                    }

                    // Processing Freemslab
                    try {
                        JSONObject freemslabJSON = new JSONObject(freemslab);
                        JSONArray freemslabJSONArray = freemslabJSON.getJSONArray("data");
                        totalMsRecords = freemslabJSONArray.length();
                        ArrayList<FreeMslab> freeMslabsList = new ArrayList<FreeMslab>();

                        for (int i = 0; i < freemslabJSONArray.length(); i++) {
                            freeMslabsList.add(FreeMslab.parseFreeMslab(freemslabJSONArray.getJSONObject(i)));
                        }
                        freeMslabController.createOrUpdateFreeMslab(freeMslabsList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + freeMslabsList.size(), "" + totalMsRecords, "Free Ms Info");

                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);
                        throw e;
                    }
                    return true;
                } else {
                    //errors.add("Please enter correct username and password");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            } catch (JSONException e) {
                e.printStackTrace();

                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            pdialog.setMessage("Finalizing free data");
            pdialog.setMessage("Download Completed..");
            downloadList = new ArrayList<>();

            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }

            String categoryList = "'Free Hed Info','Free Det Info','Free Deb Info','Free Item Info','Free MS Info'";

            for (Control c : new DownloadController(getActivity()).getAllDownloadByCategoryName(categoryList)) {
                downloadList.add(c);

            }
            mDownloadResult(downloadList);
        }
    }

    //route download asynctask
    private class routeDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String debcode;

        public routeDownload(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading routes...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {
                    new DownloadController(getActivity()).deleteAll();
                    RouteController routeController = new RouteController(getActivity());
                    routeController.deleteAll();
                    RouteDetController routedetController = new RouteDetController(getActivity());
                    routedetController.deleteAll();
                    FItenrHedController fItenrHedController = new FItenrHedController(getActivity());
                    fItenrHedController.deleteAll();
                    FItenrDetController fItenrDetController = new FItenrDetController(getActivity());
                    fItenrDetController.deleteAll();
                    /*****************route*****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading route data...");
                        }
                    });

                    String route = "";
                    int totalRouteRecords = 0;
                    try {
                        route = networkFunctions.getRoutes(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (routes)...");
                        }
                    });

                    // Processing route
                    try {
                        JSONObject routeJSON = new JSONObject(route);
                        JSONArray routeJSONArray = routeJSON.getJSONArray("data");
                        totalRouteRecords = routeJSONArray.length();
                        ArrayList<Route> routeList = new ArrayList<Route>();

                        for (int i = 0; i < routeJSONArray.length(); i++) {
                            routeList.add(Route.parseRoute(routeJSONArray.getJSONObject(i)));
                        }
                        routeController.createOrUpdateFRoute(routeList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + routeList.size(), "" + totalRouteRecords, "Route Info");

                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end route**********************************************************************/
                    /*****************Route det**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading route details...");
                        }
                    });

                    String routedet = "";
                    int totalRouteDetRecords = 0;
                    try {
                        routedet = networkFunctions.getRouteDets(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (routes)...");
                        }
                    });

                    // Processing route
                    try {
                        JSONObject routeJSON = new JSONObject(routedet);
                        JSONArray routeJSONArray = routeJSON.getJSONArray("data");
                        totalRouteDetRecords = routeJSONArray.length();
                        ArrayList<RouteDet> routeList = new ArrayList<RouteDet>();

                        for (int i = 0; i < routeJSONArray.length(); i++) {
                            routeList.add(RouteDet.parseRoute(routeJSONArray.getJSONObject(i)));
                        }
                        routedetController.InsertOrReplaceRouteDet(routeList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + routeList.size(), "" + totalRouteDetRecords, "Route Detail Info");

                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    /*****************end route det**********************************************************************/


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Download complete...");
                        }
                    });
                    return true;
                } else {
                    //errors.add("Please enter correct username and password");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            } catch (JSONException e) {
                e.printStackTrace();

                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            pdialog.setMessage("Finalizing Route data");
            pdialog.setMessage("Download Completed..");
            downloadList = new ArrayList<>();

            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }

            String category = "'Route Info','Route Detail Info'";
            for (Control c : new DownloadController(getActivity()).getAllDownloadByCategoryName(category)) {
                downloadList.add(c);

            }
            mDownloadResult(downloadList);
        }
    }

    //outstanding download asynctask
    private class outstandingDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String debcode;
        int totalRecords = 0;

        public outstandingDownload(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading outstanding...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {
                    new DownloadController(getActivity()).deleteAll();
                    OutstandingController outstandingController = new OutstandingController(getActivity());
                    outstandingController.deleteAll();
                    /*****************fDdbNoteWithCondition*****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading outstanding data...");
                        }
                    });

                    /*****************fddbnote*****************************************************************************/

                    String fddbnote = "";
                    try {
                        fddbnote = networkFunctions.getFddbNotes(debcode);
                        // Log.d(LOG_TAG, "OUTLETS :: " + outlets);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (outstanding details)...");
                        }
                    });

                    // Processing fddbnote
                    try {
                        JSONObject fddbnoteJSON = new JSONObject(fddbnote);
                        JSONArray fddbnoteJSONArray = fddbnoteJSON.getJSONArray("data");
                        totalRecords = fddbnoteJSONArray.length();
                        ArrayList<FddbNote> fddbnoteList = new ArrayList<FddbNote>();

                        for (int i = 0; i < fddbnoteJSONArray.length(); i++) {
                            fddbnoteList.add(FddbNote.parseFddbnote(fddbnoteJSONArray.getJSONObject(i)));
                        }
                        outstandingController.createOrUpdateFDDbNote(fddbnoteList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + fddbnoteList.size(), "" + totalRecords, "DB Note Info");

                    } catch (JSONException | NumberFormatException e) {

//                        ErrorUtil.logException("LoginActivity -> Authenticate -> doInBackground() # Process Routes and Outlets",
//                                e, routes, BugReport.SEVERITY_HIGH);

                        throw e;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Download complete...");
                        }
                    });
                    return true;
                } else {
                    //errors.add("Please enter correct username and password");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            } catch (JSONException e) {
                e.printStackTrace();

                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            pdialog.setMessage("Finalizing item data");
            pdialog.setMessage("Download Completed..");
            downloadList = new ArrayList<>();

            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }

            String category = "'DB Note Info'";

            for (Control c : new DownloadController(getActivity()).getAllDownloadByCategoryName(category)) {
                downloadList.add(c);

            }
            mDownloadResult(downloadList);
        }
    }

    //other download - 11-06-2020
    private class OtherDownload extends AsyncTask<String, Integer, Boolean> {

        private String repcode;
        CustomProgressDialog pdialog;
        //  private Handler mHandler;
        private List<String> errors = new ArrayList<>();

        public OtherDownload(String repcode) {
            this.repcode = repcode;
            this.pdialog = new CustomProgressDialog(getActivity());
            // mHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            // mHandler = new Handler(Looper.getMainLooper());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Authenticating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            apiInterface = ApiCllient.getClient(getActivity()).create(ApiInterface.class);
            //   mHandler = new Handler(Looper.getMainLooper());

            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null && SharedPref.getInstance(getActivity()).isLoggedIn()) {
                    new DownloadController(getActivity()).deleteAll();
                    /*****************company details**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading data(Company details)...");
                        }
                    });

                    // Processing controls
                    try {
                        new CompanyDetailsController(getActivity()).deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Controllist, networkFunctions.getCompanyDetails(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    // ************************Settings**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading data (Company Settings)...");
                        }
                    });
                    // Processing Settings

                    try {
                        ReferenceSettingController settingController = new ReferenceSettingController(getActivity());
                        settingController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Settings, networkFunctions.getReferenceSettings());
                    } catch (IOException e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Reference**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Reference details...");
                        }
                    });

                    // Processing banks
                    try {
                        ReferenceDetailDownloader referenceDetaileControlle = new ReferenceDetailDownloader(getActivity());
                        referenceDetaileControlle.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Reference, networkFunctions.getReferences(repcode));
                    } catch (IOException e) {
                        errors.add(e.toString());
                        throw e;
                    }
                    /*****************RepDebtor**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Rep Allocated Debtors...");
                        }
                    });

                    // Processing RepDebtors

                    try {
                        RepDebtorController repDebtorController = new RepDebtorController(getActivity());
                        repDebtorController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.RepDebtor, networkFunctions.getRepDebtor(repcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }
                    /*****************invHedL3**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (Last three invoices hed)...");
                        }
                    });

                    try {

                        FInvhedL3Controller finvHedl3Controller = new FInvhedL3Controller(getActivity());
                        finvHedl3Controller.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.invL3hed, networkFunctions.getLastThreeInvHed(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************invDetL3**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (Last three invoices det)...");
                        }
                    });

                    try {

                        FinvDetL3Controller finvdetl3Controller = new FinvDetL3Controller(getActivity());
                        finvdetl3Controller.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.invL3det, networkFunctions.getLastThreeInvDet(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Completed...");
                        }
                    });

                    /*****************end sync**********************************************************************/

                    return true;
                } else {

                    errors.add("SharedPref.getInstance(getActivity()).getLoginUser() = null OR !SharedPref.getInstance(getActivity()).isLoggedIn()");
                    Log.d("ERROR>>>>>", "Login USer" + SharedPref.getInstance(getActivity()).getLoginUser().toString() + " IS LoggedIn --> " + SharedPref.getInstance(getActivity()).isLoggedIn());
                    return false;
                }
            } catch (Exception e) {

                e.printStackTrace();
                errors.add(e.toString());

                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            SharedPref.getInstance(getActivity()).setGlobalVal("SyncDate", dateFormat.format(new Date(timeInMillis)));
            pdialog.setMessage("Finalizing data");
            pdialog.setMessage("Download Completed..");
            downloadList = new ArrayList<>();


            if (result) {
                SharedPref.getInstance(getActivity()).setOtherDownload(true);
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }
            } else {
                SharedPref.getInstance(getActivity()).setOtherDownload(false);
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }
            }

            String category = "'Control Info','Company Setting Info','Company Branch Info','Rep debtors Info','Last Three Invoice Hed','Last Three Invoice Det'";

            for (Control c : new DownloadController(getActivity()).getAllDownloadByCategoryName(category)) {
                downloadList.add(c);
            }
            mDownloadResult(downloadList);
        }
    }

    //mithsu//
    //stock download asynctask
    private class stockDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String debcode;
        int totalRecords = 0;

        public stockDownload(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading Stock...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null &&
                        SharedPref.getInstance(getActivity()).isLoggedIn()) {

                    /*****************Stock *******************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading stock data...");
                        }
                    });

                    String stock = "";
                    try {
                        stock = networkFunctions.getItemLocations(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing stock
                    try {
                        JSONObject stockJSON = new JSONObject(stock);
                        JSONArray stockJSONArray = stockJSON.getJSONArray("data");
                        Log.d("mithsu", "doInBackground: " + stockJSONArray);
                        totalRecords = stockJSONArray.length();
                        ArrayList<ItemLoc> stockList = new ArrayList<ItemLoc>();

                        for (int i = 0; i < stockJSONArray.length(); i++) {
                            stockList.add(ItemLoc.parseItemLocs(stockJSONArray.getJSONObject(i)));
                        }

                        //mithsu//
                        ItemLocController itemLocController = new ItemLocController(getActivity());
                        itemLocController.deleteAllItemLoc();
                        itemLocController.InsertOrReplaceItemLoc(stockList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + stockList.size(), "" + totalRecords, "Item Loc Info");
//                        UtilityContainer.download(getActivity(), TaskTypeDownload.ItemLoc,
//                                networkFunctions.getItemLocations(SharedPref
//                                        .getInstance(getActivity())
//                                        .getLoginUser().getFREP_CODE()));

                        //mithsu//

                    } catch (JSONException | NumberFormatException e) {
                        throw e;
                    }
                    /*****************end stock ***************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Download complete...");
                        }
                    });
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            } catch (JSONException e) {
                e.printStackTrace();

                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            pdialog.setMessage("Finalizing stockbundle data");
            pdialog.setMessage("Download Completed..");
            downloadList = new ArrayList<>();

            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }

            String category = "'Item Loc Info'";
            for (Control c : new DownloadController(getActivity()).getAllDownloadByCategoryName(category)) {
                downloadList.add(c);

            }
            mDownloadResult(downloadList);
        }
    }

    //price download asynctask
    private class priceDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String debcode;
        int totalRecords = 0;

        public priceDownload(String debcode) {
            this.debcode = debcode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading Prices...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null &&
                        SharedPref.getInstance(getActivity()).isLoggedIn()) {

                    /*****************Price *******************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Prices data...");
                        }
                    });

                    String price = "";
                    try {
                        price = networkFunctions.getItemPrices(debcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing price
                    try {
                        JSONObject priceJSON = new JSONObject(price);
                        JSONArray priceJSONArray = priceJSON.getJSONArray("data");
                        totalRecords = priceJSONArray.length();
                        ArrayList<ItemPri> priceList = new ArrayList<ItemPri>();

                        for (int i = 0; i < priceJSONArray.length(); i++) {
                            priceList.add(ItemPri.parseItemPrices(priceJSONArray.getJSONObject(i)));
                        }

                        ItemPriceController itemPriceController = new ItemPriceController(getActivity());
                        itemPriceController.deleteAllItemPri();
                        itemPriceController.InsertOrReplaceItemPri(priceList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + priceList.size(), "" + totalRecords, "Item Price Info");
//
//                        UtilityContainer.download(getActivity(), TaskTypeDownload.ItemPri,
//                                networkFunctions.getItemLocations(SharedPref
//                                        .getInstance(getActivity())
//                                        .getLoginUser().getFREP_CODE()));

                    } catch (JSONException | NumberFormatException e) {
                        throw e;
                    }
                    /*****************end price ***************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Download complete...");
                        }
                    });
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            } catch (JSONException e) {
                e.printStackTrace();

                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            pdialog.setMessage("Finalizing pricesbundle data");
            pdialog.setMessage("Download Completed..");
            downloadList = new ArrayList<>();

            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }

            String category = "'Item Price Info'";
            for (Control c : new DownloadController(getActivity()).getAllDownloadByCategoryName(category)) {
                downloadList.add(c);

            }
            mDownloadResult(downloadList);
        }
    }

    //customer download asynctask
    private class customerDownload extends AsyncTask<String, Integer, Boolean> {
        CustomProgressDialog pdialog;
        private String repcode;
        int totalRecords = 0;

        public customerDownload(String repcode) {
            this.repcode = repcode;
            this.pdialog = new CustomProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Downloading Customer...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                if (SharedPref.getInstance(getActivity()).getLoginUser() != null &&
                        SharedPref.getInstance(getActivity()).isLoggedIn()) {

                    /*****************Customer ****************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading customer data...");
                        }
                    });

                    String customer = "";
                    try {
//                        customer = networkFunctions.getItems(debcode);
                        customer = networkFunctions.getCustomer(repcode);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    // Processing customer
                    try {
                        JSONObject customerJSON = new JSONObject(customer);
                        JSONArray customerJSONArray = customerJSON
                                .getJSONArray("data");
                        totalRecords = customerJSONArray.length();
                        ArrayList<Debtor> customerList = new ArrayList<Debtor>();

                        for (int i = 0; i < customerJSONArray.length(); i++) {
                            customerList.add(Debtor.parseOutlet(customerJSONArray.getJSONObject(i)));
                        }

                        CustomerController customerController = new CustomerController(getActivity());
                        customerController.deleteAll();
                        customerController.InsertOrReplaceDebtor(customerList);
                        new DownloadController(getActivity()).createOrUpdateDownload("" + customerList.size(), "" + totalRecords, "Debtor Info");

//                        UtilityContainer.download(getActivity(), TaskTypeDownload.Customers,
//                                networkFunctions.getCustomer(SharedPref
//                                        .getInstance(getActivity())
//                                        .getLoginUser().getFREP_CODE()));

                    } catch (JSONException | NumberFormatException e) {
                        throw e;
                    }
                    /*****************end sutomer *************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Download complete...");
                        }
                    });
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            } catch (JSONException e) {
                e.printStackTrace();

                return false;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            pdialog.setMessage("Finalizing customerbundle data");
            pdialog.setMessage("Download Completed..");
            downloadList = new ArrayList<>();

            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }

            String category = "'Debtor Info'";

            for (Control c : new DownloadController(getActivity()).getAllDownloadByCategoryName(category)) {
                downloadList.add(c);

            }
            mDownloadResult(downloadList);
        }
    }
    //mithsu//


    public void mDownloadResult(ArrayList<Control> downlodaList) {
        final Dialog sDialog = new Dialog(getActivity());
        sDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sDialog.setCancelable(false);
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.setContentView(R.layout.download_dialog);

        ListView download_list = (ListView) sDialog.findViewById(R.id.download_listview);
//        ArrayList<Control> downlodaList = new DownloadController(getActivity()).getAllDownload();

        download_list.setAdapter(new downloadListAdapter(getActivity(), downlodaList));


        sDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
                downloadList.clear();
                Intent intent = new Intent(getActivity(),ActivityHome.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        sDialog.show();
    }
}
