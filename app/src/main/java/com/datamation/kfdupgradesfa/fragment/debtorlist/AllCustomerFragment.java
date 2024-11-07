package com.datamation.kfdupgradesfa.fragment.debtorlist;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.CustomerAdapter;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.DayNPrdDetController;
import com.datamation.kfdupgradesfa.controller.DayNPrdHedController;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReceiptDetController;
import com.datamation.kfdupgradesfa.controller.RepDebtorController;
import com.datamation.kfdupgradesfa.controller.RouteDetController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Customer;
import com.datamation.kfdupgradesfa.model.Debtor;
import com.datamation.kfdupgradesfa.view.CustomerListActivity;
import com.datamation.kfdupgradesfa.view.DebtorDetailsActivity;
import com.datamation.kfdupgradesfa.view.OrderActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AllCustomerFragment extends Fragment {

    View view;
    ListView lvCustomers;
    SharedPref mSharedPref;
    ArrayList<Debtor> customerList;
    CustomerAdapter customerAdapter;
    private Debtor debtor;
    String routeCode = "";
    SearchView mSearchDeb;
    boolean isAnyActiveOrder = false;
    boolean isAnyActiveReceipt = false;
    boolean isAnyActiveNp = false;
    String debCode1 = null;
    String debCode2 = null;
    String debCode3 = null;
    String debName1;
    String debName2;
    String debName3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_all_customer, container, false);

        lvCustomers = (ListView) view.findViewById(R.id.all_cus_lv);
        mSharedPref = new SharedPref(getActivity());

        mSearchDeb = (SearchView) view.findViewById(R.id.et_all_deb_search);


        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {
                int i = Settings.Global.getInt(getActivity().getContentResolver(), Settings.Global.AUTO_TIME, 0);
                if (i > 0)
                {
                    debtor = customerList.get(position);
                    String debcode = debtor.getFDEBTOR_CODE();
                    SalRepController ds = new SalRepController(getActivity());

                    boolean allowselect = new RepDebtorController(getActivity()).getCheckAllowDebtor(debcode, ds.getCurrentRepCode());

                    //Check active transactions and prevent the user selecting another customer
                    try {
                        debCode1 = new OrderController(getActivity()).getDebCodeByActiveOrder();
                        debName1 = new CustomerController(getActivity()).getCusNameByCode(debCode1);
                        debCode2 = new ReceiptController(getActivity()).getDebCodeByActiveReceipt();
                        debName2 = new CustomerController(getActivity()).getCusNameByCode(debCode2);
                        debCode3 = new DayNPrdHedController(getActivity()).getDebCodeByActiveNPs();
                        debName3 = new CustomerController(getActivity()).getCusNameByCode(debCode3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (debCode1 == null || debCode1.isEmpty()) {
                        isAnyActiveOrder = true;
                    } else if (debCode1.equals(debcode)) {
                        isAnyActiveOrder = true;
                    }

                    if (debCode2 == null || debCode2.isEmpty()) {
                        isAnyActiveReceipt = true;
                    } else if (debCode2.equals(debcode)) {
                        isAnyActiveReceipt = true;
                    }

                    if (debCode3 == null || debCode3.isEmpty()) {
                        isAnyActiveNp = true;
                    } else if (debCode3.equals(debcode)) {
                        isAnyActiveNp = true;
                    }

                    if (isAnyActiveOrder && isAnyActiveReceipt && isAnyActiveNp) {
                        try {
                            Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
                            intent.putExtra("outlet", debtor);
                            mSharedPref.clearPrefWithoutSyncDate();
                            mSharedPref.setSelectedDebCode(debtor.getFDEBTOR_CODE());
                            mSharedPref.setSelectedDebName(debtor.getFDEBTOR_NAME());
                            mSharedPref.setSelectedDebRouteCode(new RouteDetController(getActivity()).getRouteCodeByDebCode(debtor.getFDEBTOR_CODE()));
                            mSharedPref.setSelectedDebtorPrilCode(debtor.getFDEBTOR_PRILLCODE());
                            startActivity(intent);
                            getActivity().finish();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    } else {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Alert!!!");

                        if (!isAnyActiveOrder && debCode1 != null && !debCode1.isEmpty()) {
                            alertDialogBuilder.setMessage("You have already selected " + debName1 + " Customer Before!!!");
                        } else if (!isAnyActiveReceipt && debCode2 != null && !debCode2.isEmpty()) {
                            alertDialogBuilder.setMessage("You have already selected " + debName2 + " Customer Before!!!");
                        } else if (!isAnyActiveNp && debCode3 != null && !debCode3.isEmpty()) {
                            alertDialogBuilder.setMessage("You have already selected " + debName3 + " Customer Before!!!");
                        }

                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }

// Helper method to show error dialog

//                } else {
//                    Toast.makeText(getActivity(), "Please tick the 'Automatic Date and Time' option to continue..", Toast.LENGTH_LONG).show();
//                    /* Show Date/time settings dialog */
//                    startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), 0);
//                }
            }
        });
        mSearchDeb.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                customerList.clear();
                customerList = new CustomerController(getActivity()).getAllCustomers(query);
                lvCustomers.setAdapter(new CustomerAdapter(getActivity(), customerList));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                customerList.clear();
                customerList = new CustomerController(getActivity()).getAllCustomers(newText);
                lvCustomers.setAdapter(new CustomerAdapter(getActivity(), customerList));

                return true;
            }
        });

        new getAllCustomer("").execute();
        return view;
    }

    private class getAllCustomer extends AsyncTask<String, Integer, Boolean> {
        int totalRecords = 0;
        CustomProgressDialog pdialog;
        private String keyWord;

        public getAllCustomer(String keyWord) {
            this.keyWord = keyWord;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new CustomProgressDialog(getActivity());
            pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pdialog.setMessage("Authenticating...");
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {

            try {
                customerList = new CustomerController(getActivity()).getAllCustomers("");

                return true;

            } catch (Exception e) {
                e.printStackTrace();

                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            if (result) {
                customerAdapter = new CustomerAdapter(getActivity(), customerList);
                lvCustomers.setAdapter(customerAdapter);
            }
            pdialog.setMessage("Finalizing data");
            pdialog.setMessage("Download Completed..");
            if (result) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }
            } else {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }

            }
        }

    }

    public boolean isValidateCustomer(Customer customer) {

        if (customer.getCusStatus().equals("I")) {
            errorDialog("Status Error", "Selected debtor is inactive to continue...");
            return false;
        } else if (customer.getCreditPeriod().equals("N")) {
            errorDialog("Period Error", "Credit period expired for Selected debtor...");
            return false;
        } else if (Double.parseDouble(customer.getCreditLimit()) == 0.00) {
            errorDialog("Limit Error", "Credit limit exceed for Selected debtor...");
            return false;
        } else if (customer.getCreditStatus().equals("N")) {
            errorDialog("Credit Status Error", "Credit status not valid for Selected debtor...");
            return false;
        } else {
            return true;
        }
    }

    public void errorDialog(String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.info);


        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        alertD.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
