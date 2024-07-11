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
import com.datamation.kfdupgradesfa.controller.AreaController;
import com.datamation.kfdupgradesfa.controller.BankController;
import com.datamation.kfdupgradesfa.controller.BrandController;
import com.datamation.kfdupgradesfa.controller.CompanyDetailsController;
import com.datamation.kfdupgradesfa.controller.CostController;
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
import com.datamation.kfdupgradesfa.controller.GroupController;
import com.datamation.kfdupgradesfa.controller.ItemController;
import com.datamation.kfdupgradesfa.controller.ItemLocController;
import com.datamation.kfdupgradesfa.controller.ItemPriceController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.OutstandingController;
import com.datamation.kfdupgradesfa.controller.ReasonController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReferenceDetailDownloader;
import com.datamation.kfdupgradesfa.controller.ReferenceSettingController;
import com.datamation.kfdupgradesfa.controller.RepDebtorController;
import com.datamation.kfdupgradesfa.controller.RouteController;
import com.datamation.kfdupgradesfa.controller.RouteDetController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.controller.SupplierController;
import com.datamation.kfdupgradesfa.controller.TownController;
import com.datamation.kfdupgradesfa.controller.TypeController;
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
    SharedPref pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_download, container, false);
        pref = SharedPref.getInstance(getActivity());

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
                            new itemsDownload(SharedPref.getInstance(getActivity()).getLoginUser().getFREP_CODE()).execute();
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

                            new freeDownload(SharedPref.getInstance(getActivity())
                                    .getLoginUser().getFREP_CODE()).execute();

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

                            new routeDownload(SharedPref.getInstance(getActivity()).getLoginUser().getFREP_CODE()).execute();

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
                            Integer ordHedListCount = new OrderController(getActivity()).getAllDayBeforeUnSyncOrdHed(new SalRepController(getActivity()).getCurrentRepCode());
                            Integer receiptlistCount = new ReceiptController(getActivity()).getAllDayBeforeUnSyncRecHedCount(new SalRepController(getActivity()).getCurrentRepCode());
                            Integer npHedListCount = new DayNPrdHedController(getActivity()).getAllDayBeforeUnSyncNonPrdCount(new SalRepController(getActivity()).getCurrentRepCode());

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
        private String repcode;
        int totalRecords = 0;

        public itemsDownload(String repcode) {
            this.repcode = repcode;
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
                        ItemController itemController = new ItemController(getActivity());
                        itemController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Items, networkFunctions.getItems(repcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }


                    /*****************end item **********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Item Download complete...");
                        }
                    });
                    return true;
                } else {
                    //errors.add("Please enter correct username and password");
                    return false;
                }
            }
//            catch (IOException e) {
//                e.printStackTrace();
//
//                return false;
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//
//                return false;
//            }
            catch (NumberFormatException | IOException e) {
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
        private String repcode;


        public freeDownload(String repcode) {
            this.repcode = repcode;
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



                    FreeMslabController freeMslabController = new FreeMslabController(getActivity());
                    freeMslabController.deleteAll();

                    /*****************FreeHed**********************************************************************/
//

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });
                    // Processing freehed
                    try {

                        FreeHedController freeHedController = new FreeHedController(getActivity());
                        freeHedController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Freehed, networkFunctions.getFreeHed(repcode));
                    } catch (Exception e) {

                        throw e;
                    }
                    // Processing freehed

                    /*****************end freeHed**********************************************************************/
                    /*****************Freedet**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });

                    try {

                        FreeDetController freeDetController = new FreeDetController(getActivity());
                        freeDetController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Freedet, networkFunctions.getFreeDet(repcode));

                    } catch (Exception e) {
                        throw e;
                    }


                    /*****************end freedet**********************************************************************/
                    /*****************freedeb**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });

                    // Processing freedeb
                    try {

                        FreeDebController freeDebController = new FreeDebController(getActivity());
                        freeDebController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Freedeb, networkFunctions.getFreeDebs(repcode));
                    } catch (Exception e) {

                        throw e;
                    }



                    /*****************end freedeb**********************************************************************/
                    /*****************FreeItem*********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });

                    // Processing freeItem
                    try {

                        FreeItemController freeItemController = new FreeItemController(getActivity());
                        freeItemController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Freeitem, networkFunctions.getFreeItems());
                    } catch (Exception e) {

                        throw e;
                    }


                    /*****************end freeItem**********************************************************************/

                    /*****************Freemslab - kaveesha -11-06-2020 *********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Processing downloaded data (free)...");
                        }
                    });
                    // Processing freeMslab
                    try {

                        FreeMslabController freemSlabController = new FreeMslabController(getActivity());
                        freemSlabController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Freemslab, networkFunctions.getFreeMslab());
                    } catch (Exception e) {
                        throw e;
                    }

                    return true;
                } else {

                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();

                return false;
            }  catch (NumberFormatException e) {
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
        private String repcode;

        public routeDownload(String repcode) {
            this.repcode = repcode;
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
                            pdialog.setMessage("Downloading route details...");
                        }
                    });
                    // Processing route

                    try {
                        RouteController routeController = new RouteController(getActivity());
                        routeController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Route, networkFunctions.getRoutes(repcode));
                    } catch (Exception e) {
                        e.printStackTrace();

                        throw e;
                    }



                    // Processing route

                    /*****************end route**********************************************************************/
                    /*****************Route det**********************************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading route details...");
                        }
                    });

                    try {
                        RouteDetController routeDetController = new RouteDetController(getActivity());
                        routeDetController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.RouteDet, networkFunctions.getRouteDets(repcode));
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

                    /*****************end route det**********************************************************************/


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
        private String repcode;
        int totalRecords = 0;

        public outstandingDownload(String repcode) {
            this.repcode = repcode;
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

                    /*****************fDdbNoteWithCondition*****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Outsatnding...");
                        }
                    });

                    try {
                        OutstandingController outstandingController = new OutstandingController(getActivity());
                        outstandingController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.fddbnote, networkFunctions.getFddbNotes(repcode));
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Outstanding Download complete...");
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
                            pdialog.setMessage("Downloading banks...");
                        }
                    });
                    /*****************banks**********************************************************************/

                    try {
                        BankController bankController = new BankController(getActivity());
                        bankController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Bank, networkFunctions.getBanks());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Towns...");
                        }
                    });

                    // Processing towns
                    try {
                        TownController townController = new TownController(getActivity());
                        townController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Towns, networkFunctions.getTowns());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Groups - kaveesha - 2020/10/05 *****************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Groups...");
                        }
                    });

                    // Processing group
                    try {
                        GroupController groupController = new GroupController(getActivity());
                        groupController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Groups, networkFunctions.getGroups());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Brands - kaveesha - 2020/10/06 *****************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Brands...");
                        }
                    });

                    // Processing brands
                    try {
                        BrandController brandController = new BrandController(getActivity());
                        brandController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Brand, networkFunctions.getBrands());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Area Details...");
                        }
                    });

                    // Processing area
                    try {
                        AreaController areaController = new AreaController(getActivity());
                        areaController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Area, networkFunctions.getArea());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Cost...");
                        }
                    });
                    // Processing Cost

                    try {
                        CostController costController = new CostController(getActivity());
                        costController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Cost, networkFunctions.getCost(repcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Supplier**********************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Supplier...");
                        }
                    });

                    // Processing Supplier

                    try {
                        SupplierController supplierController = new SupplierController(getActivity());
                        supplierController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Supplier, networkFunctions.getSupplier(repcode));
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Items downloaded\nDownloading reasons...");
                        }
                    });
                    // Processing reasons

                    try {
                        ReasonController reasonController = new ReasonController(getActivity());
                        reasonController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Reason, networkFunctions.getReasons());
                    } catch (IOException e) {
                        e.printStackTrace();
                        errors.add(e.toString());
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Settings...");
                        }
                    });
                    // Processing company settings
                    try {
                        ReferenceSettingController settingController = new ReferenceSettingController(getActivity());
                        settingController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Settings, networkFunctions.getReferenceSettings());
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /*****************Branches*****************************************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading data (reference details)...");
                        }
                    });


                    // Processing Branches
                    try {
                        ReferenceDetailDownloader branchController = new ReferenceDetailDownloader(getActivity());
                        branchController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Reference, networkFunctions.getReferences(repcode));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    /***************** Types - *************************************************/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading Types....");
                        }
                    });

                    // Processing Types
                    try {
                        TypeController typeController = new TypeController(getActivity());
                        typeController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Type, networkFunctions.getTypes());
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("Downloading SalRep...");
                        }
                    });

                    try {
                        SalRepController salRepController = new SalRepController(getActivity());
                        salRepController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.SalRep, networkFunctions.getSalRep(repcode.trim(), pref.getUserPwd().trim()));
                    } catch (Exception e) {
                        errors.add(e.toString());
                        throw e;
                    }

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
        private String repcode;
        int totalRecords = 0;

        public stockDownload(String repcode) {
            this.repcode = repcode;
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

                    try {
                        ItemLocController itemLocController = new ItemLocController(getActivity());
                        itemLocController.deleteAllItemLoc();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.ItemLoc, networkFunctions.getItemLocations(repcode));
                    } catch (Exception e) {

                        throw e;
                    }

                    // Processing stock

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
            }  catch (NumberFormatException e) {
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
        private String repcode;
        int totalRecords = 0;

        public priceDownload(String repcode) {
            this.repcode = repcode;
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
                            pdialog.setMessage("Items downloaded\nDownloading Item Pri...");
                        }
                    });
                    // Processing Item Pri

                    try {
                        ItemPriceController itemPriceController = new ItemPriceController(getActivity());
                        itemPriceController.deleteAllItemPri();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.ItemPri, networkFunctions.getItemPrices(repcode));
                    } catch (IOException e) {
                        e.printStackTrace();
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
            }  catch (NumberFormatException e) {
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
                            pdialog.setMessage("Downloading Customers...");
                        }
                    });

                    try {
                        CustomerController customerController = new CustomerController(getActivity());
                        customerController.deleteAll();
                        UtilityContainer.download(getActivity(), TaskTypeDownload.Customers, networkFunctions.getCustomer(repcode));
                    } catch (Exception e) {
                       e.printStackTrace();
                        throw e;
                    }
                    // Processing customer

                    /*****************end sutomer *************************************************/

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdialog.setMessage("get Customer Download complete...");
                        }
                    });
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
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
