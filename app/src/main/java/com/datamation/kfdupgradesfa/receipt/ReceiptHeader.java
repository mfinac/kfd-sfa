package com.datamation.kfdupgradesfa.receipt;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.afollestad.materialdialogs.MaterialDialog;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.CustomerDebtAdapter;
import com.datamation.kfdupgradesfa.adapter.downloadListAdapter;
import com.datamation.kfdupgradesfa.controller.BankController;
import com.datamation.kfdupgradesfa.controller.CostController;
import com.datamation.kfdupgradesfa.controller.DownloadController;
import com.datamation.kfdupgradesfa.controller.LocationsController;
import com.datamation.kfdupgradesfa.controller.OutstandingController;
import com.datamation.kfdupgradesfa.controller.ReceiptController;
import com.datamation.kfdupgradesfa.controller.ReferenceController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.dialog.CustomKeypadDialogReceipt;
import com.datamation.kfdupgradesfa.fragment.debtorlist.AllCustomerFragment;
import com.datamation.kfdupgradesfa.helpers.ReceiptResponseListener;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Bank;
import com.datamation.kfdupgradesfa.model.Control;
import com.datamation.kfdupgradesfa.model.Cost;
import com.datamation.kfdupgradesfa.model.FddbNote;
import com.datamation.kfdupgradesfa.model.ReceiptHed;
import com.datamation.kfdupgradesfa.settings.ReferenceNum;
import com.datamation.kfdupgradesfa.view.DebtorDetailsActivity;
import com.datamation.kfdupgradesfa.view.ReceiptActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import at.markushi.ui.CircleButton;

public class ReceiptHeader extends Fragment {

    View view;
    TextView customerName, outStandingAmt, txtCompDisc;
    EditText InvoiceNo, currnentDate, manual, remarks, txtReceAmt, txtCHQNO, txtCardNo, txtSlipNo, txtDraftNo, txtAddUser, txtBranchCode, txtBankCode;
    TextView txtCHQDate, txtRecExpireDate;
    TableRow chequeRow, cardRow, cardTypeRow, exDateRow, chequeNoRow, bankRow, depositRow, draftRow, branchRow;
    Spinner spnPayMode, spnBank, spnCardType, spnCostCenter;
    SearchableSpinner spnBank1;
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    String RefNo, payModePos, CosLocation, CostPos;
    SharedPref mSharedPref;
    MyReceiver r;
    FloatingActionButton fb;
    CircleButton fbExit;
    ReceiptActivity mainActivity;
    ReceiptResponseListener listener;
    ArrayList<Cost> costList;
    public SharedPref pref;
    String startTime  ;

    //mithsu//
    public static boolean b ;
    //mithsu//


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sales_management_receipt_header, container, false);
        localSP = getActivity().getSharedPreferences(SETTINGS, 0);
        mSharedPref = new SharedPref(getActivity());
        pref = SharedPref.getInstance(getActivity());
        mainActivity = (ReceiptActivity) getActivity();
        spnPayMode = (Spinner) view.findViewById(R.id.spnRecPayMode);
        //spnBank = (Spinner) view.findViewById(R.id.spnRecBank);
        spnBank1 = (SearchableSpinner) view.findViewById(R.id.spnRecBank);
        spnBank1.setTitle("Select Bank");

        spnCardType = (Spinner) view.findViewById(R.id.spnCardType);
        spnCostCenter = (Spinner) view.findViewById(R.id.spnCostCenter);

        customerName = (TextView) view.findViewById(R.id.customerName);
        outStandingAmt = (TextView) view.findViewById(R.id.rec_outstanding_amt);
        txtCHQDate = (TextView) view.findViewById(R.id.txtRecChequeDate);
        txtRecExpireDate = (TextView) view.findViewById(R.id.txtRecExpireDate);

        InvoiceNo = (EditText) view.findViewById(R.id.txtRecNo);
        currnentDate = (EditText) view.findViewById(R.id.txtRecDate);
        manual = (EditText) view.findViewById(R.id.txtRecManualNo);
        remarks = (EditText) view.findViewById(R.id.txtRecRemarks);
        txtReceAmt = (EditText) view.findViewById(R.id.txtRecAmt);
        txtCHQNO = (EditText) view.findViewById(R.id.txtRecCheque);
        txtCardNo = (EditText) view.findViewById(R.id.txtCardNo);
        txtSlipNo = (EditText) view.findViewById(R.id.txtSlipNo);
        txtDraftNo = (EditText) view.findViewById(R.id.txtDraftNo);
        txtAddUser = (EditText) view.findViewById(R.id.txtAddUser);
        txtBranchCode = (EditText) view.findViewById(R.id.txtBranchCode);
        txtBankCode = (EditText) view.findViewById(R.id.txtBankCode);


        chequeRow = (TableRow) view.findViewById(R.id.chequeRow);
        cardRow = (TableRow) view.findViewById(R.id.cardRow);
        cardTypeRow = (TableRow) view.findViewById(R.id.cardTypeRow);
        chequeNoRow = (TableRow) view.findViewById(R.id.chequeNoRow);
        exDateRow = (TableRow) view.findViewById(R.id.exDateRow);
        bankRow = (TableRow) view.findViewById(R.id.bankRow);
        depositRow = (TableRow) view.findViewById(R.id.depositRow);
        draftRow = (TableRow) view.findViewById(R.id.draftRow);
        branchRow = (TableRow) view.findViewById(R.id.branchRow);

        fb = (FloatingActionButton) view.findViewById(R.id.fab1);
        fbExit = (CircleButton) view.findViewById(R.id.fab_exit);

        //TimeZone tz = TimeZone.getTimeZone("UTC +5:30");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        //df.setTimeZone(tz);
        startTime  = df.format(new Date());

//        mSharedPref.setReceiptHedClicked("0");
//        mSharedPref.setNextClick("0");

        mSharedPref.setReceipttHeaderNextClicked(false);



        outStandingAmt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                outstandingList(SharedPref.getInstance(getActivity()).getSelectedDebCode());
                return false;
            }
        });

        try {

            fbExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                            .content("Do you want to discard the order?")
                            .positiveColor(ContextCompat.getColor(getActivity(), R.color.material_alert_positive_button))
                            .positiveText("Yes")
                            .negativeColor(ContextCompat.getColor(getActivity(), R.color.material_alert_negative_button))
                            .negativeText("No, Exit")
                            .callback(new MaterialDialog.ButtonCallback() {

                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);

                                    ReceiptActivity activity = (ReceiptActivity) getActivity();
                                    new OutstandingController(getActivity()).ClearFddbNoteData();
                                    new ReceiptController(getActivity()).CancelReceiptS(RefNo);

                                    //activity.cusPosition = 0;
                                    activity.selectedDebtor = null;
                                    activity.selectedRecHed = null;
                                    Toast.makeText(getActivity(), "Receipt discarded successfully..!", Toast.LENGTH_SHORT).show();


                                    Intent intent = new Intent(getActivity(), DebtorDetailsActivity.class);
                                    startActivity(intent);

                                    getActivity().finish();


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
            });


        } catch (Exception x){
            Log.e("###", "onCreateView: ",x );
        }



        if(new ReferenceController(getActivity()).IsExistRefNoReceipt(new ReferenceController(getActivity()).getCurrentRefNo(getResources().getString(R.string.RecNumVal)))>0){
            new ReferenceNum(getActivity()).NumValueUpdateAvoidDuplicateReceipt(getResources().getString(R.string.RecNumVal));
            RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.RecNumVal));
        }else{
            RefNo = new ReferenceNum(getActivity()).getCurrentRefNo(getResources().getString(R.string.RecNumVal));
        }


        manual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                b = true;
            }
        });
        //kaveesha - 25-01-2022
        txtCHQNO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                b = true;
            }
        });
        txtBankCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                b = true;
            }
        });
        txtBranchCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                b = true;
            }
        });
        // kaveesha
        remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                b = true;
            }
        });
//        spnPayMode.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                b = true;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        spnCostCenter.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                b = true;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        txtAddUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                b = true;
            }
        });

        txtReceAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                b = true;
                mSharedPref.setIsChangedReceiptAmount(true);
            }
        });


        //mithsu//

        /*------------------------------------------*/
        List<String> payModeList = new ArrayList<String>();
        payModeList.add("-SELECT-");
        payModeList.add("CASH");
        payModeList.add("CHEQUE");
        //payModeList.add("CREDIT CARD");
        //payModeList.add("DIRECT DEPOSIT");
        //payModeList.add("BANK DRAFT");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, payModeList);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPayMode.setAdapter(dataAdapter1);

        /*------------------------------------------*/
        //rashmi -2017-12-12
        BankController BankController = new BankController(getActivity());
        List<Bank> bankList = new ArrayList<>();

        bankList = BankController.getBanks();
        ArrayAdapter<Bank> bankAdapter = new ArrayAdapter<Bank>(getActivity(), android.R.layout.simple_spinner_item, bankList);
        bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnBank1.setAdapter(bankAdapter);

        /*------------------------------------------  */
        //rashmi - 2017-12-13
        List<String> cardTypeList = new ArrayList<String>();
        cardTypeList.add("-SELECT-");
        cardTypeList.add("VISA");
        cardTypeList.add("MASTER");
        cardTypeList.add("OTHER");

        ArrayAdapter<String> cardListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cardTypeList);
        cardListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCardType.setAdapter(cardListAdapter);

        /*-------------------------------------------*/

        //Kaveesha - 12/02/2021
        costList = new CostController(getActivity()).getAllCostCenters();

        List<String> costNames = new ArrayList<String>();
        /* Merge group code with group name to the list */
        for (Cost cost : costList) {
            costNames.add(cost.getFCOST_CODE() + " - " + cost.getFCOST_NAME());
        }

        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, costNames);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCostCenter.setAdapter(dataAdapter3);

        SalRepController repController = new SalRepController(getActivity());
        LocationsController locationsController = new LocationsController(getActivity());

        CosLocation = locationsController.getRepLocation(mSharedPref.getGlobalVal("PrekeyCost"));

        if (CosLocation.isEmpty()) {
            String cost = String.valueOf(getIndexByname(repController.getCurrentCostCode()));

            CostPos = cost;
        } else {
            CostPos = mSharedPref.getGlobalVal("PrekeyCostPos");
        }


        /*-------------------------------------------*/


        payModePos = mSharedPref.getGlobalVal("ReckeyPayModePos");

        if (!(payModePos.equalsIgnoreCase("-SELECT-")) && !(payModePos.equalsIgnoreCase("***")))
//            spnPayMode.setSelection(Integer.parseInt(payModePos));
            spnPayMode.setSelection(spnPayMode.getSelectedItemPosition());

        currentDate();
        InvoiceNo.setText(RefNo);

        /*-*--*--*--*--*--*--*--*-Item group selection-*--*--*--*--*--*--*--*--*--*-*/

        spnPayMode.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payModePos = mSharedPref.getGlobalVal("ReckeyPayModePos");

                if (!(payModePos.equals(String.valueOf(spnPayMode.getSelectedItemPosition())))) {

                    mSharedPref.setGlobalVal("ReckeyPayModePos", String.valueOf(spnPayMode.getSelectedItemPosition()));
                    mSharedPref.setGlobalVal("ReckeyPayMode", spnPayMode.getSelectedItem().toString());


                    //rashmi
                    if (spnPayMode.getSelectedItem().toString().equalsIgnoreCase("cheque")) {

                        chequeRow.setVisibility(View.VISIBLE);
                        chequeNoRow.setVisibility(View.VISIBLE);
                        bankRow.setVisibility(View.VISIBLE);
                        branchRow.setVisibility(View.VISIBLE);

                        cardRow.setVisibility(View.GONE);
                        cardTypeRow.setVisibility(View.GONE);
                        exDateRow.setVisibility(View.GONE);
                        depositRow.setVisibility(View.GONE);
                        draftRow.setVisibility(View.GONE);
                        b = true;//kaveesha - 25-01-2022
                    } else if (spnPayMode.getSelectedItem().toString().equalsIgnoreCase("credit card")) {
                        cardRow.setVisibility(View.VISIBLE);
                        cardTypeRow.setVisibility(View.VISIBLE);
                        exDateRow.setVisibility(View.VISIBLE);

                        chequeRow.setVisibility(View.GONE);
                        chequeNoRow.setVisibility(View.GONE);
                        bankRow.setVisibility(View.GONE);
                        branchRow.setVisibility(View.VISIBLE);
                        depositRow.setVisibility(View.GONE);
                        draftRow.setVisibility(View.GONE);

                    } else if (spnPayMode.getSelectedItem().toString().equalsIgnoreCase("direct deposit")) {

                        depositRow.setVisibility(View.VISIBLE);

                        chequeRow.setVisibility(View.GONE);
                        chequeNoRow.setVisibility(View.GONE);
                        bankRow.setVisibility(View.GONE);
                        cardRow.setVisibility(View.GONE);
                        branchRow.setVisibility(View.VISIBLE);
                        cardTypeRow.setVisibility(View.GONE);
                        exDateRow.setVisibility(View.GONE);
                        draftRow.setVisibility(View.GONE);

                    } else if (spnPayMode.getSelectedItem().toString().equalsIgnoreCase("bank draft")) {

                        draftRow.setVisibility(View.VISIBLE);

                        chequeRow.setVisibility(View.GONE);
                        chequeNoRow.setVisibility(View.GONE);
                        bankRow.setVisibility(View.GONE);
                        cardRow.setVisibility(View.GONE);
                        branchRow.setVisibility(View.VISIBLE);
                        cardTypeRow.setVisibility(View.GONE);
                        exDateRow.setVisibility(View.GONE);
                        depositRow.setVisibility(View.GONE);

                    } else {
                        chequeRow.setVisibility(View.GONE);
                        chequeNoRow.setVisibility(View.GONE);
                        bankRow.setVisibility(View.GONE);
                        cardRow.setVisibility(View.GONE);
                        cardTypeRow.setVisibility(View.GONE);
                        exDateRow.setVisibility(View.GONE);
                        depositRow.setVisibility(View.GONE);
                        draftRow.setVisibility(View.GONE);
                        branchRow.setVisibility(View.GONE);
                        b = true;//kaveesha - 25-01-2022
                    }
                }
                mSharedPref.setGlobalVal("ReckeyPayModePos", String.valueOf(spnPayMode.getSelectedItemPosition()));
                mSharedPref.setGlobalVal("ReckeyPayMode", spnPayMode.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/


        fb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSharedPref.setGlobalVal("isHeaderComplete", "1");
                mSharedPref.setReceipttHeaderNextClicked(true);
                if (txtReceAmt.getText().toString().equals("") || txtReceAmt.getText().toString() == "") {
                    Toast.makeText(getActivity(), "Received amount can't be empty", Toast.LENGTH_LONG).show();
                    txtReceAmt.requestFocus();
                } else {
                    if ((Double.parseDouble(txtReceAmt.getText().toString().replaceAll(",", "")) > 0) && !spnPayMode.getSelectedItem().toString().equalsIgnoreCase("-SELECT-")) {
//commented by rashmi -2019/05/06 because over payment allocated allow
//                        if(Double.parseDouble(txtReceAmt.getText().toString().replaceAll(",", "")) > Double.parseDouble(outStandingAmt.getText().toString().replaceAll(",", "")))
//                        {
//                            Toast.makeText(getActivity(), "Received amount can't be greater than the outstanding amount", Toast.LENGTH_LONG).show();
//                            txtReceAmt.requestFocus();
//                        }
//                        else
//                        {


                        //mithsu//
                        if (b) {
                            if (spnPayMode.getSelectedItemPosition() == 1) {
                                if (Double.parseDouble(txtReceAmt.getText().toString().replaceAll(",", "")) > 0) {

                                    SaveReceiptHeader();
                                    mSharedPref.setGlobalVal("ReckeyHeader", "1");
                                    mSharedPref.setReceiptHedClicked("1");
                                    listener.moveToDetailsRece(1);
                                } else {
                                    Toast.makeText(getActivity(), "Please fill in Received amount", Toast.LENGTH_LONG).show();
                                    txtReceAmt.requestFocus();
                                }
                            } else if (spnPayMode.getSelectedItemPosition() == 2) {
                                if (!txtCHQDate.getText().equals("-SELECT DATE-") && Double.parseDouble(txtReceAmt.getText().toString().replaceAll(",", "")) > 0) {
                                    if (txtCHQNO.getText().length() < 6) {
                                        Toast.makeText(getActivity(), "Invalid Cheque No!", Toast.LENGTH_LONG).show();
                                        txtCHQNO.requestFocus();
                                    } else if ((txtBankCode.getText().length() < 4)) {
                                        android.widget.Toast.makeText(getActivity(), "Invalid Bank Code!", android.widget.Toast.LENGTH_LONG).show();
                                        txtBankCode.requestFocus();
                                    } else if (txtBranchCode.getText().length() < 3) {
                                        android.widget.Toast.makeText(getActivity(), "Invalid Branch Code!", android.widget.Toast.LENGTH_LONG).show();
                                        txtBranchCode.requestFocus();
                                    } else {
                                        SaveReceiptHeader();
                                        mSharedPref.setGlobalVal("ReckeyHeader", "1");
                                        mSharedPref.setReceiptHedClicked("1");
                                        listener.moveToDetailsRece(1);
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Fill in Received amount, Chq date..!", Toast.LENGTH_LONG).show();
                                    txtCHQNO.requestFocus();
                                }

                            } else if (spnPayMode.getSelectedItemPosition() == 3) {
                                if (txtCardNo.getText().length() > 4 && txtRecExpireDate.getText().length() > 4 && Double.parseDouble(txtReceAmt.getText().toString().replaceAll(",", "")) > 0) {
                                    if (txtCardNo.getText().length() < 2) {
                                        Toast.makeText(getActivity(), "Invalid Credit Card No!", Toast.LENGTH_LONG).show();
                                        txtCardNo.requestFocus();
                                    } else {
                                        SaveReceiptHeader();
                                        mSharedPref.setGlobalVal("ReckeyHeader", "1");
                                        listener.moveToDetailsRece(1);
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Fill in Received amount, Credit Card No, Expire Date..!", Toast.LENGTH_LONG).show();
                                    txtCardNo.requestFocus();
                                }

                            } else if (spnPayMode.getSelectedItemPosition() == 4) {
                                if (txtSlipNo.getText().length() > 1 && Double.parseDouble(txtReceAmt.getText().toString().replaceAll(",", "")) > 0) {
                                    if (txtSlipNo.getText().length() < 2) {
                                        Toast.makeText(getActivity(), "Invalid Slip No!", Toast.LENGTH_LONG).show();
                                        txtSlipNo.requestFocus();
                                    } else {
                                        SaveReceiptHeader();
                                        mSharedPref.setGlobalVal("ReckeyHeader", "1");
                                        listener.moveToDetailsRece(1);
                                    }

                                } else {
                                    Toast.makeText(getActivity(), "Fill in Received amount, Slip No..!", Toast.LENGTH_LONG).show();
                                    txtCardNo.requestFocus();
                                }

                            } else {
                                if (txtDraftNo.getText().length() < 2) {
                                    Toast.makeText(getActivity(), "Invalid Draft No!", Toast.LENGTH_LONG).show();
                                    txtDraftNo.requestFocus();
                                } else {
                                    SaveReceiptHeader();
                                    mSharedPref.setGlobalVal("ReckeyHeader", "1");
                                    listener.moveToDetailsRece(1);
                                }
                            }

                            b = false;
                        } else {//kaveesha - 25-01-2022
                            Toast.makeText(getActivity(), "Already clicked", Toast.LENGTH_LONG).show();
                        }
                        //mithsu//


                        //  }
                    } else {
                        Toast.makeText(getActivity(), "Please fill received amount and payment mode", Toast.LENGTH_LONG).show();
                    }
                }
            }


        });

        /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

//        txtReceAmt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                ReceiptActivity activity = new ReceiptActivity();
//                if (hasFocus) {
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (imm != null) {
//                        imm.showSoftInput(txtReceAmt, InputMethodManager.SHOW_IMPLICIT);
//                    }
//
//                    txtReceAmt.selectAll();
//                    txtReceAmt.setText(txtReceAmt.getText().toString().replaceAll(",", ""));
//                    if(txtReceAmt.getText().toString().contains(","))
//                    {
//                        activity.ReceivedAmt = Double.parseDouble(txtReceAmt.getText().toString().replace(",", ""));
//                    }
//
//                    if(txtReceAmt.getText().toString() == null || txtReceAmt.getText().toString().isEmpty() || txtReceAmt.getText().toString().equals("") || txtReceAmt.getText().toString() == "" )
//                    {
//                        Toast.makeText(getActivity(), "Please fill in Received amount", Toast.LENGTH_LONG).show();
//                        txtReceAmt.requestFocus();
//                    }
//                    else
//                    {
//                        SaveReceiptHeader();
//                    }
//
//                } else {
//                    if (txtReceAmt.getText().length() > 0) {
//                        txtReceAmt.setText(String.format("%,.2f", Double.parseDouble(txtReceAmt.getText().toString().replaceAll(",", ""))));
//                        if(txtReceAmt.getText().toString().contains(","))
//                        {
//                            activity.ReceivedAmt = Double.parseDouble(txtReceAmt.getText().toString().replace(",", ""));
//                        }
//                        if(txtReceAmt.getText().toString() == null || txtReceAmt.getText().toString().isEmpty() || txtReceAmt.getText().toString().equals("") || txtReceAmt.getText().toString() == "" )
//                        {
//                            Toast.makeText(getActivity(), "Please fill in Received amount", Toast.LENGTH_LONG).show();
//                            txtReceAmt.requestFocus();
//                        }
//                        else
//                        {
//                            SaveReceiptHeader();
//                        }
//                    }
//                }
//            }
//        });

        /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        txtReceAmt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ReceiptActivity activity = new ReceiptActivity();
                //---------------------------------------------------------------------------------------------------------
                CustomKeypadDialogReceipt keypadPrice = new CustomKeypadDialogReceipt(getActivity(), true, new CustomKeypadDialogReceipt.IOnOkClickListener() {
                    @Override
                    public void okClicked(double value) {

                        activity.ReceivedAmt = value;
                        txtReceAmt.setText("" + activity.ReceivedAmt);
                        mSharedPref.setGlobalVal("ReckeyRecAmt", "" + value);
                        SaveReceiptHeader();

                    }
                });
//                keypadPrice.show();

//                keypadPrice.setHeader("Enter Received Amount");
//                if(preProduct.getPREPRODUCT_CHANGED_PRICE().equals("0")){
//                keypadPrice.loadValue(activity.ReceivedAmt);
                //  txtReceAmt.selectAll();


            }
        });

        /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/




        /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

        txtCHQDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (spnPayMode.getSelectedItemPosition() == 2) {
                    datetimepicker();
                }
            }
        });

        txtRecExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnPayMode.getSelectedItemPosition() == 3) {
                    datetimepickerForCardExpire();
                }
            }
        });
        customerName.setText(mSharedPref.getSelectedDebName());
        outStandingAmt.setText(String.format("%,.2f", new OutstandingController(getActivity()).getDebtorBalance(SharedPref.getInstance(getActivity()).getSelectedDebCode())));
        manual.setEnabled(true);
        remarks.setEnabled(true);
        spnPayMode.setEnabled(true);
        txtCHQNO.setEnabled(true);
        txtCHQDate.setEnabled(true);
        txtBranchCode.setEnabled(true);
        txtCHQDate.setEnabled(true);
        txtCHQDate.setEnabled(true);
        txtReceAmt.setEnabled(true);


        if (mainActivity.selectedRecHed != null) {

            manual.setText(mainActivity.selectedRecHed.getFPRECHED_MANUREF());
            remarks.setText(mainActivity.selectedRecHed.getFPRECHED_REMARKS());

            if (mainActivity.selectedRecHed.getFPRECHED_PAYTYPE().equals("CA")) {
                spnPayMode.setSelection(1);
            } else {
                spnPayMode.setSelection(2);

                txtCHQNO.setText(mainActivity.selectedRecHed.getFPRECHED_CHQNO());
                txtCHQDate.setText(mainActivity.selectedRecHed.getFPRECHED_CHQDATE());
                txtReceAmt.setText(mainActivity.selectedRecHed.getFPRECHED_TOTALAMT());
            }
        }

        return view;
    }

    public int getIndexByname(String pName) {
        for (Cost _item : costList) {
            if (_item.getFCOST_CODE().equals(pName))
                return costList.indexOf(_item);
        }
        return -1;
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    /* current date */
    private void currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        currnentDate.setText(dateFormat.format(date));
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    /* Current time */
    private String currentTime() {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(cal.getTime());
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void SaveReceiptHeader() {

        ReceiptActivity activity = new ReceiptActivity();
        mSharedPref.setGlobalVal("ReckeyRemnant", "0");
        new OutstandingController(getActivity()).ClearFddbNoteData();
        BankController bnk = new BankController(getContext());
        ReceiptHed recHed = new ReceiptHed();

        if (activity.selectedRecHed != null) {
            recHed = activity.selectedRecHed;
        }

        recHed.setFPRECHED_REFNO(RefNo);
        recHed.setFPRECHED_DEBCODE(SharedPref.getInstance(getActivity()).getSelectedDebCode());
        recHed.setFPRECHED_ADDDATE(currnentDate.getText().toString());
        recHed.setFPRECHED_MANUREF(manual.getText().toString());
        recHed.setFPRECHED_REMARKS(remarks.getText().toString());
        recHed.setFPRECHED_ADDMACH(localSP.getString("MAC_Address", "No MAC Address").toString());
        recHed.setFPRECHED_ADDUSER(new SalRepController(getActivity()).getCurrentRepCode());
        recHed.setFPRECHED_CURCODE("LKR");
        recHed.setFPRECHED_CURRATE("1.00");
        String RCstr = spnCostCenter.getSelectedItem().toString();
        String[] RCarrOfCostCd = RCstr.split("-", 2);
        mSharedPref.setGlobalVal("PrekeyCost", RCarrOfCostCd[0]);
//        mSharedPref.getGlobalVal("PrekeyCost")
        recHed.setFPRECHED_COSTCODE(RCarrOfCostCd[0]);
        recHed.setFPRECHED_ISACTIVE("1");
        recHed.setFPRECHED_ISDELETE("0");
        recHed.setFPRECHED_ISSYNCED("0");
        recHed.setFPRECHED_START_TIME(startTime);
        recHed.setFPRECHED_REPCODE(new SalRepController(getActivity()).getCurrentRepCode());
        recHed.setFPRECHED_TXNDATE(currnentDate.getText().toString());
        recHed.setFPRECHED_TXNTYPE("42");
        recHed.setFPRECHED_TOTALAMT(String.valueOf(Double.parseDouble(txtReceAmt.getText().toString().replaceAll(",", ""))));
        recHed.setFPRECHED_BTOTALAMT(String.valueOf(Double.parseDouble(txtReceAmt.getText().toString().replaceAll(",", ""))));
        recHed.setFPRECHED_SALEREFNO("");
        recHed.setFPRECHED_ADDUSER_NEW(txtAddUser.getText().toString());

        mSharedPref.setGlobalVal("ReckeyRecAmt", txtReceAmt.getText().toString());

        if (spnPayMode.getSelectedItemPosition() == 1) {
            recHed.setFPRECHED_PAYTYPE("CA");
            recHed.setFPRECHED_BANKCODE("");
            recHed.setFPRECHED_BRANCHCODE("");
            recHed.setFPRECHED_CUSBANK("");
            recHed.setFPRECHED_CHQNO("");
            recHed.setFPRECHED_CHQDATE("");
        } else if (spnPayMode.getSelectedItemPosition() == 2) {
            // recHed.setFPRECHED_CUSBANK(spnBank1.getSelectedItem().toString().split(":")[0].trim());
            mSharedPref.setGlobalVal("KeyBankCode", txtBankCode.getText().toString());
            mSharedPref.setGlobalVal("KeyBranchCode", txtBranchCode.getText().toString());
            // mSharedPref.setGlobalVal("KeyBankName",spnBank1.getSelectedItem().toString().trim());
            // recHed.setFPRECHED_CUSBANK(bnk.getBankByName(spnBank1.getSelectedItem().toString()).getFBANK_BANK_CODE());
            recHed.setFPRECHED_PAYTYPE("CH");
            recHed.setFPRECHED_BANKCODE(txtBankCode.getText().toString());
            recHed.setFPRECHED_BRANCHCODE(txtBranchCode.getText().toString());
            recHed.setFPRECHED_CHQNO(txtCHQNO.getText().toString());
            recHed.setFPRECHED_CHQDATE(txtCHQDate.getText().toString());
            mSharedPref.setGlobalVal("ReckeyCHQNo", txtCHQNO.getText().toString());
        } else if (spnPayMode.getSelectedItemPosition() == 3) {
            recHed.setFPRECHED_CUSBANK(bnk.getBankByName(spnBank1.getSelectedItem().toString()).getFBANK_BANK_CODE());
            recHed.setFPRECHED_PAYTYPE("CC");
            recHed.setFPRECHED_BANKCODE("");
            recHed.setFPRECHED_BRANCHCODE("");
            recHed.setFPRECHED_CHQNO(txtCardNo.getText().toString());
            recHed.setFPRECHED_CHQDATE(txtRecExpireDate.getText().toString());
            mSharedPref.setGlobalVal("ReckeyCHQNo", txtCardNo.getText().toString());
        } else if (spnPayMode.getSelectedItemPosition() == 4) {
            recHed.setFPRECHED_CUSBANK("");
            recHed.setFPRECHED_PAYTYPE("DD");
            recHed.setFPRECHED_BANKCODE("");
            recHed.setFPRECHED_BRANCHCODE("");
            recHed.setFPRECHED_CHQNO(txtSlipNo.getText().toString());
            recHed.setFPRECHED_CHQDATE("");
            mSharedPref.setGlobalVal("ReckeyCHQNo", txtSlipNo.getText().toString());
        } else {
            recHed.setFPRECHED_CUSBANK("");
            recHed.setFPRECHED_PAYTYPE("BD");
            recHed.setFPRECHED_BANKCODE("");
            recHed.setFPRECHED_BRANCHCODE("");
            recHed.setFPRECHED_CHQNO(txtDraftNo.getText().toString());
            recHed.setFPRECHED_CHQDATE("");
            mSharedPref.setGlobalVal("ReckeyCHQNo", txtDraftNo.getText().toString());
        }

        activity.selectedRecHed = recHed;
        if (txtReceAmt.getText().toString().contains(",")) {
            activity.ReceivedAmt = Double.parseDouble(txtReceAmt.getText().toString().replace(",", ""));
            mSharedPref.setGlobalVal("ReckeyRecAmt", txtReceAmt.getText().toString().replace(",", ""));
        } else {
            activity.ReceivedAmt = Double.parseDouble(txtReceAmt.getText().toString());
            mSharedPref.setGlobalVal("ReckeyRecAmt", txtReceAmt.getText().toString().replace(",", ""));
            activity.selectedRecHed = recHed;
        }
        SharedPref.getInstance(getActivity()).setGlobalVal("Rec_Start_Time", currentTime());
        ArrayList<ReceiptHed> RecHedList = new ArrayList<ReceiptHed>();
        RecHedList.add(activity.selectedRecHed);

        new ReceiptController(getActivity()).createOrUpdateRecHedS(RecHedList);

    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void datetimepicker() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_picker);
        final DatePicker dtp = (DatePicker) dialog.findViewById(R.id.dpResult);
        dtp.setCalendarViewShown(false);
        dialog.setCancelable(true);
        Button button = (Button) dialog.findViewById(R.id.btnok);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int month = dtp.getMonth() + 1;
                int year = dtp.getYear();
                int date = dtp.getDayOfMonth();
                String chdate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", date);
                Date date1 = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date2 = new Date();
                try {
                    Date date11 = new Date();
                    date1 = (Date) dateFormat.parse(dateFormat.format(date11));
                    date2 = (Date) dateFormat.parse(chdate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();

                cal1.setTime(date1);
                cal2.setTime(date2);

                if ((cal2.before(cal1))) {
                    System.out.println("Date1 is before Date2 detail");
                    Toast.makeText(getActivity(), "Invalid Date.Please select a Future Date than Current Date.", Toast.LENGTH_SHORT).show();
                    txtCHQDate.setText("");

                } else {

                    txtCHQDate.setText(chdate);
                    b = true;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/
    //rashmi -2017-12-13
    public void datetimepickerForCardExpire() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_picker);
        final DatePicker dtp = (DatePicker) dialog.findViewById(R.id.dpResult);
        dtp.setCalendarViewShown(false);
        dialog.setCancelable(true);
        Button button = (Button) dialog.findViewById(R.id.btnok);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int month = dtp.getMonth() + 1;
                int year = dtp.getYear();
                int date = dtp.getDayOfMonth();
                String chdate = String.format("%02d", month) + "/" + year;
                Date date1 = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date2 = new Date();
                try {
                    Date date11 = new Date();
                    date1 = (Date) dateFormat.parse(dateFormat.format(date11));
                    date2 = (Date) dateFormat.parse(chdate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();

                cal1.setTime(date1);
                cal2.setTime(date2);

                if ((cal2.before(cal1))) {
                    System.out.println("Date1 is before Date2 detail");
                    Toast.makeText(getActivity(), "Invalid Date.Please select a Future Date than Current Date.", Toast.LENGTH_SHORT).show();
                    txtRecExpireDate.setText("");

                } else {

                    txtRecExpireDate.setText(chdate);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /*------------------------------------------------------------------------------------------*/
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mRefreshHeader();
        }
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r, new IntentFilter("TAG_HEADER"));
    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public void mRefreshHeader() {

        Log.d("123456", "454544545");
        ReceiptActivity mainActivity = new ReceiptActivity();
        // if (mSharedPref.getGlobalVal("ReckeyCustomer").equals("1")) {
        payModePos = mSharedPref.getGlobalVal("ReckeyPayModePos");

        if (!(payModePos.equalsIgnoreCase("-SELECT-")))
            spnPayMode.setSelection(Integer.parseInt(payModePos));

        Log.d("Customer", mSharedPref.getSelectedDebName());

        // }


    }

    /*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mnu_exit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (ReceiptResponseListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onButtonPressed");
        }
    }

    public void outstandingList( String DebCode)
    {
        final Dialog sDialog = new Dialog(getActivity());
        sDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sDialog.setCancelable(false);
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.setContentView(R.layout.outdanding_dialog);

        ListView outstanding_List = (ListView) sDialog.findViewById(R.id.download_listview);
        ArrayList<FddbNote> outstandingList = new OutstandingController(getActivity()).getDebtInfo(DebCode);
        outstanding_List.setAdapter(new CustomerDebtAdapter(getActivity(),outstandingList));

        sDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
            }
        });

        sDialog.show();
    }


}
