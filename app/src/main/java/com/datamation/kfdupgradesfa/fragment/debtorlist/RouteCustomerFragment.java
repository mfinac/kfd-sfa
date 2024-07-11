package com.datamation.kfdupgradesfa.fragment.debtorlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.CustomerAdapter;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.RepDebtorController;
import com.datamation.kfdupgradesfa.controller.RouteDetController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Customer;
import com.datamation.kfdupgradesfa.model.Debtor;
import com.datamation.kfdupgradesfa.view.DebtorDetailsActivity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class RouteCustomerFragment extends Fragment {

    View view;
    ListView lvCustomers;
    SharedPref mSharedPref;
    ArrayList<Debtor> customerList;
    CustomerAdapter customerAdapter;
    private Debtor debtor;
    String routeCode = "";
    SearchView mSearchDeb;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_route_customer, container, false);

        lvCustomers = (ListView) view.findViewById(R.id.route_cus_lv);
        mSharedPref = new SharedPref(getActivity());
        mSearchDeb = (SearchView) view.findViewById(R.id.et_all_deb_search);

        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {

                int i = Settings.Global.getInt(getActivity().getContentResolver(), Settings.Global.AUTO_TIME, 0);

                if (i > 0) {

                    debtor = customerList.get(position);
                    String debcode = debtor.getFDEBTOR_CODE();
                    SalRepController ds = new SalRepController(getActivity());
                    boolean allowselect = new RepDebtorController(getActivity()).getCheckAllowDebtor(debcode, ds.getCurrentRepCode());

                    if (debtor.getFDEBTOR_STATUS().equals("B")) {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Access Denied")
                                .setContentText("Black Listed Customer :" + debtor.getFDEBTOR_NAME().trim())
                                .setConfirmText("Ok")
                                .showCancelButton(false)

                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })
                                .show();
                    } else if (!allowselect) {

                        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Access Denied")
                                .setContentText("Access Denied for this Customer :" + debtor.getFDEBTOR_NAME().trim())
                                .setConfirmText("Ok")
                                .showCancelButton(false)

                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                })

                                .show();
                    } else {


                        try {
                            debtor = customerList.get(position);
//                            mSharedPref.clearPref();
                            mSharedPref.clearPrefWithoutSyncDate();
                            mSharedPref.setSelectedDebCode(debtor.getFDEBTOR_CODE());
                            mSharedPref.setSelectedDebName(debtor.getFDEBTOR_NAME());
                            mSharedPref.setSelectedDebRouteCode(new RouteDetController(getActivity()).getRouteCodeByDebCode(debtor.getFDEBTOR_CODE()));
                            mSharedPref.setSelectedDebtorPrilCode(debtor.getFDEBTOR_PRILLCODE());
                            Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
                            intent.putExtra("outlet", debtor);
                            intent.putExtra("allCusFrag", 88);
                            startActivity(intent);
                            getActivity().finish();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Please tick the 'Automatic Date and Time' option to continue..", Toast.LENGTH_LONG).show();
                    startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), 0);
                }
            }
        });

        mSearchDeb.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                customerList.clear();
                customerList = new CustomerController(getActivity()).getRouteCustomers("", query);
                lvCustomers.setAdapter(new CustomerAdapter(getActivity(), customerList));


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                customerList.clear();
                customerList = new CustomerController(getActivity()).getRouteCustomers("", newText);
                lvCustomers.setAdapter(new CustomerAdapter(getActivity(), customerList));


                return true;
            }
        });

        new getRouteCustomer().execute();

        return view;
    }

    private class getRouteCustomer extends AsyncTask<String, Integer, Boolean> {
        int totalRecords = 0;
        CustomProgressDialog pdialog;

        public getRouteCustomer() {
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
                customerList = new CustomerController(getActivity()).getRouteCustomers("", "");

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
            errorDialog("Limit Error", "Not enough credit limit for Selected debtor...");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
