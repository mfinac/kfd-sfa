package com.datamation.kfdupgradesfa.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.api.ApiCllient;
import com.datamation.kfdupgradesfa.api.ApiInterface;
import com.datamation.kfdupgradesfa.controller.AreaController;
import com.datamation.kfdupgradesfa.controller.BankController;
import com.datamation.kfdupgradesfa.controller.BrandController;
import com.datamation.kfdupgradesfa.controller.CompanyDetailsController;
import com.datamation.kfdupgradesfa.controller.CostController;
import com.datamation.kfdupgradesfa.controller.CustomerController;
import com.datamation.kfdupgradesfa.controller.DownloadController;
import com.datamation.kfdupgradesfa.controller.FInvhedL3Controller;
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
import com.datamation.kfdupgradesfa.controller.LocationsController;
import com.datamation.kfdupgradesfa.controller.OutstandingController;
import com.datamation.kfdupgradesfa.controller.PaymentController;
import com.datamation.kfdupgradesfa.controller.PaymentDetailController;
import com.datamation.kfdupgradesfa.controller.PaymentHeaderController;
import com.datamation.kfdupgradesfa.controller.PushMsgHedDetController;
import com.datamation.kfdupgradesfa.controller.ReasonController;
import com.datamation.kfdupgradesfa.controller.ReferenceDetailDownloader;
import com.datamation.kfdupgradesfa.controller.ReferenceSettingController;
import com.datamation.kfdupgradesfa.controller.RepDebtorController;
import com.datamation.kfdupgradesfa.controller.RepTrgDetController;
import com.datamation.kfdupgradesfa.controller.RepTrgHedController;
import com.datamation.kfdupgradesfa.controller.RouteController;
import com.datamation.kfdupgradesfa.controller.RouteDetController;
import com.datamation.kfdupgradesfa.controller.SalRepController;
import com.datamation.kfdupgradesfa.controller.SupplierController;
import com.datamation.kfdupgradesfa.controller.TownController;
import com.datamation.kfdupgradesfa.controller.TypeController;
import com.datamation.kfdupgradesfa.helpers.NetworkFunctions;
import com.datamation.kfdupgradesfa.model.Area;
import com.datamation.kfdupgradesfa.model.Bank;
import com.datamation.kfdupgradesfa.model.Brand;
import com.datamation.kfdupgradesfa.model.CompanyBranch;
import com.datamation.kfdupgradesfa.model.CompanySetting;
import com.datamation.kfdupgradesfa.model.Control;
import com.datamation.kfdupgradesfa.model.Cost;
import com.datamation.kfdupgradesfa.model.Debtor;
import com.datamation.kfdupgradesfa.model.FInvhedL3;
import com.datamation.kfdupgradesfa.model.FddbNote;
import com.datamation.kfdupgradesfa.model.FinvDetL3;
import com.datamation.kfdupgradesfa.model.FreeDeb;
import com.datamation.kfdupgradesfa.model.FreeDet;
import com.datamation.kfdupgradesfa.model.FreeHed;
import com.datamation.kfdupgradesfa.model.FreeItem;
import com.datamation.kfdupgradesfa.model.FreeMslab;
import com.datamation.kfdupgradesfa.model.Group;
import com.datamation.kfdupgradesfa.model.Item;
import com.datamation.kfdupgradesfa.model.ItemLoc;
import com.datamation.kfdupgradesfa.model.ItemPri;
import com.datamation.kfdupgradesfa.model.Locations;
import com.datamation.kfdupgradesfa.model.Payment;
import com.datamation.kfdupgradesfa.model.PushMsgHedDet;
import com.datamation.kfdupgradesfa.model.Reason;
import com.datamation.kfdupgradesfa.model.PayReceiptDet;
import com.datamation.kfdupgradesfa.model.PayReceiptHed;
import com.datamation.kfdupgradesfa.model.RepCredentials;
import com.datamation.kfdupgradesfa.model.RepDebtor;
import com.datamation.kfdupgradesfa.model.RepTrgDet;
import com.datamation.kfdupgradesfa.model.RepTrgHed;
import com.datamation.kfdupgradesfa.model.Route;
import com.datamation.kfdupgradesfa.model.RouteDet;
import com.datamation.kfdupgradesfa.model.SalRep;
import com.datamation.kfdupgradesfa.model.Supplier;
import com.datamation.kfdupgradesfa.model.Town;
import com.datamation.kfdupgradesfa.model.Type;
import com.datamation.kfdupgradesfa.settings.TaskTypeDownload;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.datamation.kfdupgradesfa.helpers.SQLiteBackUp;
import com.datamation.kfdupgradesfa.helpers.SQLiteRestore;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * common functions
 */

public class UtilityContainer {


    //---------------------------------------------------------------------------------------------------------------------------------------------------

    public static void showSnackBarError(View v, String message, Context context) {
        Snackbar snack = Snackbar.make(v, "" + message, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        view.setBackgroundColor(Color.parseColor("#CB4335"));
        TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        snack.show();

    }

    /*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public static void mLoadFragment(Fragment fragment, Context context) {

        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //ft.setCustomAnimations(R.anim.enter, R.anim.exit_to_right);
        ft.replace(R.id.fragmentContainer, fragment, fragment.getClass().getSimpleName());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }

    protected static void sacaSnackbar(Context context, View view, String s) {

    }

    public static void ClearReturnSharedPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("returnKeyRoute");
        editor.remove("returnKeyCostCode");
        editor.remove("returnKeyLocCode");
        editor.remove("returnKeyTouRef");
        editor.remove("returnKeyAreaCode");
        editor.remove("returnKeyRouteCode");
        editor.remove("returnKeyTourPos");
        editor.remove("returnkeyCustomer");
        editor.remove("returnkeyReasonCode");
        editor.remove("returnKeyRepCode");
        editor.remove("returnKeyDriverCode");
        editor.remove("returnKeyHelperCode");
        editor.remove("returnKeyLorryCode");
        editor.remove("returnKeyReason");

        editor.commit();
    }

    public static void ClearReceiptSharedPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("ReckeyPayModePos");
        editor.remove("ReckeyPayMode");
        editor.remove("isHeaderComplete");
        editor.remove("ReckeyHeader");
        editor.remove("ReckeyRecAmt");
        editor.remove("ReckeyRemnant");
        editor.remove("ReckeyCHQNo");
        editor.remove("Rec_Start_Time");
        editor.commit();
    }

    public static void ClearCustomerSharedPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("selected_out_id");
        editor.remove("selected_out_name");
        editor.remove("selected_out_route_code");
        editor.remove("selected_pril_code");
        editor.commit();
    }

    public static void ClearDBName(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("Dist_DB");
        editor.commit();
    }


    //-----------------------------------------------------------------------------------------------------------------------------------------------------

    public static void mPrinterDialogbox(final Context context) {

        SharedPref mSharedPref;
        mSharedPref = new SharedPref(context);

        View promptView = LayoutInflater.from(context).inflate(R.layout.settings_printer_layout, null);
        final EditText serverURL = (EditText) promptView.findViewById(R.id.et_mac_address);

        String printer_mac_shared_pref = "";
        printer_mac_shared_pref = new SharedPref(context).getGlobalVal("printer_mac_address");

        if (!TextUtils.isEmpty(printer_mac_shared_pref)) {
            serverURL.setText(printer_mac_shared_pref);
            Toast.makeText(context, "MAC Address Already Exists", Toast.LENGTH_LONG).show();
        }

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(promptView)
                .setTitle("Printer MAC Address")
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                Button bOk = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button bClose = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);


                bOk.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (serverURL.length() > 0) {

                            if (validate(serverURL.getText().toString().toUpperCase())) {
                                //SharedPreferencesClass.setLocalSharedPreference(context, "printer_mac_address", serverURL.getText().toString().toUpperCase());
                                new SharedPref(context).setGlobalVal("printer_mac_address", serverURL.getText().toString().toUpperCase());
                                Toast.makeText(context, "Saved Successfully", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(context, "Enter Valid MAC Address", Toast.LENGTH_LONG).show();
                            }
                        } else
                            Toast.makeText(context, "Type in the MAC Address", Toast.LENGTH_LONG).show();
                    }
                });

                bClose.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    public static boolean validate(String mac) {
        Pattern p = Pattern.compile("^([a-fA-F0-9]{2}[:-]){5}[a-fA-F0-9]{2}$");
        Matcher m = p.matcher(mac);
        return m.find();
    }


    public static void mSQLiteDatabase(final Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.settings_sqlite_database_layout);
        dialog.setTitle("SQLite Backup/Restore");

        final Button b_backups = (Button) dialog.findViewById(R.id.b_backups);
        final Button b_restore = (Button) dialog.findViewById(R.id.b_restore);

        b_backups.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SQLiteBackUp backUp = new SQLiteBackUp(context);
                backUp.exportDB();
                dialog.dismiss();
            }
        });

        b_restore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mLoadFragment(new SQLiteRestore(), context);
//                FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.main_container, new FragmentTools());
//                ft.addToBackStack(null);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.commit();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void download(final Context context, TaskTypeDownload task, String jsonString) {
        NetworkFunctions networkFunctions = new NetworkFunctions(context);
        JSONObject jsonObject = null;
        int totalRecords = 0;

        try {
            jsonObject = new JSONObject(jsonString);

        } catch (JSONException e) {
            Log.e("JSON ERROR>>>>>", e.toString());
        }
        switch (task) {
            case Controllist: {

                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Control> downloadedList = new ArrayList<Control>();
                    CompanyDetailsController companyController = new CompanyDetailsController(context);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        downloadedList.add(Control.parseControlDetails(jsonArray.getJSONObject(i)));
                    }
                    companyController.createOrUpdateFControl(downloadedList);
                    new DownloadController(context).createOrUpdateDownload("" + downloadedList.size(), "" + totalRecords, "Control Info");


                } catch (JSONException | NumberFormatException e) {

                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
         case SalRep: {

             try {
                 JSONArray jsonArray = jsonObject.getJSONArray("data");
                 ArrayList<SalRep> downloadedList = new ArrayList<SalRep>();
                 SalRepController salRepController = new SalRepController(context);
                 for (int i = 0; i < jsonArray.length(); i++) {
                     downloadedList.add(SalRep.parseSalRep(jsonArray.getJSONObject(i)));
                 }
                 salRepController.CreateOrUpdateSalRep(downloadedList);

             } catch (JSONException | NumberFormatException e) {

                 try {
                     throw e;
                 } catch (JSONException e1) {
                     Log.e("JSON ERROR>>>>>", e.toString());
                 }
             }
         }
         break;
            case Customers: {

                try {
                    CustomerController customerController = new CustomerController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();


                    ArrayList<Debtor> downloadedList = new ArrayList<Debtor>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        downloadedList.add(Debtor.parseOutlet(jsonArray.getJSONObject(i)));
                    }
                    customerController.InsertOrReplaceDebtor(downloadedList);
                    new DownloadController(context).createOrUpdateDownload("" + downloadedList.size(), "" + totalRecords, "Debtor Info");
                    Log.d("InsertOrReplaceDebtor", "succes");
//                    SharedPref.getInstance(context).setIsFirstInstallation(true);

                } catch (JSONException | NumberFormatException e) {

                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Settings: {

                try {
                    ReferenceSettingController settingController = new ReferenceSettingController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<CompanySetting> downloadedList = new ArrayList<CompanySetting>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        downloadedList.add(CompanySetting.parseSettings(jsonArray.getJSONObject(i)));
                    }
                    settingController.createOrUpdateFCompanySetting(downloadedList);
                    new DownloadController(context).createOrUpdateDownload("" + downloadedList.size(), "" + totalRecords, "Company Setting Info");

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Reference: {

                try {
                    ReferenceDetailDownloader branchController = new ReferenceDetailDownloader(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<CompanyBranch> downloadedList = new ArrayList<CompanyBranch>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        downloadedList.add(CompanyBranch.parseSettings(jsonArray.getJSONObject(i)));
                    }
                    branchController.createOrUpdateFCompanyBranch(downloadedList);
                    new DownloadController(context).createOrUpdateDownload("" + downloadedList.size(), "" + totalRecords, "Company Branch Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }

            }
            break;
            case Type: {

                try {
                    TypeController typeController = new TypeController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Type> downloadedlist = new ArrayList<Type>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        downloadedlist.add(Type.parseType(jsonArray.getJSONObject(i)));
                    }
                    typeController.CreateOrUpdateType(downloadedlist);
                    new DownloadController(context).createOrUpdateDownload("" + downloadedlist.size(), "" + totalRecords, "Type Info");

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Groups: {

                try {
                    GroupController groupController = new GroupController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Group> arrayList = new ArrayList<Group>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(Group.parseGroup(jsonArray.getJSONObject(i)));
                    }
                    groupController.CreateOrUpdateGroup(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Group Info");

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Brand: {

                try {
                    BrandController brandController = new BrandController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Brand> downloadedlist = new ArrayList<Brand>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        downloadedlist.add(Brand.parseBrand(jsonArray.getJSONObject(i)));
                    }
                    brandController.CreateOrUpodateBrand(downloadedlist);
                    new DownloadController(context).createOrUpdateDownload("" + downloadedlist.size(), "" + totalRecords, "Brand Info");

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Items: {

                try {
                    ItemController itemController = new ItemController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Item> downloadedList = new ArrayList<Item>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        downloadedList.add(Item.parseItem(jsonArray.getJSONObject(i)));
                    }
                    itemController.InsertOrReplaceItems(downloadedList);
                    new DownloadController(context).createOrUpdateDownload("" + downloadedList.size(), "" + totalRecords, "Item Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case ItemLoc: {

                try {
                    ItemLocController itemLocController = new ItemLocController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<ItemLoc> downloadedList = new ArrayList<ItemLoc>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        downloadedList.add(ItemLoc.parseItemLocs(jsonArray.getJSONObject(i)));
                    }
                    itemLocController.InsertOrReplaceItemLoc(downloadedList);
                    new DownloadController(context).createOrUpdateDownload("" + downloadedList.size(), "" + totalRecords, "Item Location Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case ItemPri: {

                try {
                    ItemPriceController itemPriceController = new ItemPriceController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<ItemPri> downloadedList = new ArrayList<ItemPri>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        downloadedList.add(ItemPri.parseItemPrices(jsonArray.getJSONObject(i)));
                    }
                    itemPriceController.InsertOrReplaceItemPri(downloadedList);
                    new DownloadController(context).createOrUpdateDownload("" + downloadedList.size(), "" + totalRecords, "Item Price Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Reason: {

                // Processing reasons
                try {
                    ReasonController reasonController = new ReasonController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Reason> arrayList = new ArrayList<Reason>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(Reason.parseReason(jsonArray.getJSONObject(i)));
                    }
                    Log.d("befor add reason tbl>>>", arrayList.toString());
                    reasonController.createOrUpdateReason(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Reason Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Bank: {

                // Processing bank
                try {
                    BankController bankController = new BankController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Bank> arrayList = new ArrayList<Bank>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(Bank.parseBank(jsonArray.getJSONObject(i)));
                    }
                    bankController.createOrUpdateBank(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Bank Info");

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Route: {
                try {
                    RouteController routeController = new RouteController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Route> arrayList = new ArrayList<Route>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(Route.parseRoute(jsonArray.getJSONObject(i)));
                    }
                    routeController.createOrUpdateFRoute(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Route Info");

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case RouteDet: {

                try {
                    RouteDetController routeDetController = new RouteDetController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<RouteDet> arrayList = new ArrayList<RouteDet>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(RouteDet.parseRoute(jsonArray.getJSONObject(i)));
                    }
                    routeDetController.InsertOrReplaceRouteDet(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Route Detail Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;

//            MMS-2022/01/24

            case RepTrgHed: {

                try {
                    RepTrgHedController repTrgHedController = new RepTrgHedController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<RepTrgHed> arrayList = new ArrayList<RepTrgHed>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(RepTrgHed.parseRepTrgHed(jsonArray.getJSONObject(i)));
                    }
                    repTrgHedController.InsertOrReplaceRepTrgHed(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Rep Target Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case RepTrgDet: {

                try {
                    RepTrgDetController repTrgDetController = new RepTrgDetController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<RepTrgDet> arrayList = new ArrayList<RepTrgDet>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(RepTrgDet.parseRepTrgDet(jsonArray.getJSONObject(i)));
                    }
                    repTrgDetController.InsertOrReplaceRepTrgDet(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Rep Target Detail Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }

            case Cost: {

                try {
                    CostController costController = new CostController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();


                    ArrayList<Cost> arrayList = new ArrayList<Cost>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(Cost.parseCost(jsonArray.getJSONObject(i)));
                    }
                    costController.CreateOrUpdateCost(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Cost Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Supplier: {

                try {
                    SupplierController supplierController = new SupplierController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Supplier> arrayList = new ArrayList<Supplier>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(Supplier.parseSupplier(jsonArray.getJSONObject(i)));
                    }
                    supplierController.CreateOrUpdateSupplier(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Supplier Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case RepDebtor: {

                try {
                    RepDebtorController repDebtorController = new RepDebtorController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();


                    ArrayList<RepDebtor> arrayList = new ArrayList<RepDebtor>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(RepDebtor.parseRepDebtor(jsonArray.getJSONObject(i)));
                    }
                    repDebtorController.CreateOrUpdateRepDebtor(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Rep debtors Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Area: {

                try {
                    AreaController areaController = new AreaController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Area> arrayList = new ArrayList<Area>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(Area.parseArea(jsonArray.getJSONObject(i)));
                    }
                    areaController.CreateOrUpdateArea(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Area Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Locations: {

                try {
                    LocationsController locationsController = new LocationsController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Locations> arrayList = new ArrayList<Locations>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(Locations.parseLocs(jsonArray.getJSONObject(i)));
                    }
                    locationsController.createOrUpdateFLocations(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Location Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Towns: {

                try {
                    TownController townController = new TownController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Town> arrayList = new ArrayList<Town>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(Town.parseTown(jsonArray.getJSONObject(i)));
                    }
                    townController.createOrUpdateFTown(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Town Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Freemslab: {

                try {
                    FreeMslabController freemSlabController = new FreeMslabController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();


                    ArrayList<FreeMslab> arrayList = new ArrayList<FreeMslab>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FreeMslab.parseFreeMslab(jsonArray.getJSONObject(i)));
                    }
                    freemSlabController.createOrUpdateFreeMslab(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Free MS Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Freehed: {

                try {
                    FreeHedController freeHedController = new FreeHedController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<FreeHed> arrayList = new ArrayList<FreeHed>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FreeHed.parseFreeHed(jsonArray.getJSONObject(i)));
                    }
                    freeHedController.createOrUpdateFreeHed(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Free Hed Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Freedet: {

                try {
                    FreeDetController freeDetController = new FreeDetController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<FreeDet> arrayList = new ArrayList<FreeDet>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FreeDet.parseFreeDet(jsonArray.getJSONObject(i)));
                    }
                    freeDetController.createOrUpdateFreeDet(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Free Details Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Freedeb: {

                try {
                    FreeDebController freeDebController = new FreeDebController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<FreeDeb> arrayList = new ArrayList<FreeDeb>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FreeDeb.parseFreeDeb(jsonArray.getJSONObject(i)));
                    }
                    freeDebController.createOrUpdateFreeDeb(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Free Debtor Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Freeitem: {

                try {
                    FreeItemController freeItemController = new FreeItemController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<FreeItem> arrayList = new ArrayList<FreeItem>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FreeItem.parseFreeItem(jsonArray.getJSONObject(i)));
                    }
                    freeItemController.createOrUpdateFreeItem(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Free Item Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case fddbnote: {

                try {
                    OutstandingController outstandingController = new OutstandingController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<FddbNote> arrayList = new ArrayList<FddbNote>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FddbNote.parseFddbnote(jsonArray.getJSONObject(i)));
                    }
                    outstandingController.createOrUpdateFDDbNote(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "DB Note Info");
                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case PushMsgHedDet: {

                try {
                    PushMsgHedDetController pushMsgHedDetController = new PushMsgHedDetController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<PushMsgHedDet> arrayList = new ArrayList<PushMsgHedDet>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(PushMsgHedDet.parsePMsg(jsonArray.getJSONObject(i)));
                    }
                    pushMsgHedDetController.createOrUpdatePMsgHedDet(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Push Message Hed Info");

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case CusPRecHed: {

                try {
                    PaymentHeaderController cusPRecHedController = new PaymentHeaderController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<PayReceiptHed> arrayList = new ArrayList<PayReceiptHed>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(PayReceiptHed.parseRecHed(jsonArray.getJSONObject(i)));

                    }
                    cusPRecHedController.createOrUpdateCusPRecHed(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Customer Receipt Hed Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case CusPRecDet: {

                try {
                    PaymentDetailController cusPRecDetController = new PaymentDetailController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<PayReceiptDet> arrayList = new ArrayList<PayReceiptDet>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(PayReceiptDet.parseRecDet(jsonArray.getJSONObject(i)));

                    }
                    cusPRecDetController.createOrUpdateCusPRecDet(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Customer Receipt Details Info");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case invL3hed: {

                try {
                    FInvhedL3Controller invl3hedController = new FInvhedL3Controller(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<FInvhedL3> arrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FInvhedL3.parseInvoiceHeds(jsonArray.getJSONObject(i)));

                    }
                    invl3hedController.createOrUpdateFinvHedL3(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Last Three Invoice Hed");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case invL3det: {

                try {
                    FinvDetL3Controller invl3detController = new FinvDetL3Controller(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<FinvDetL3> arrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(FinvDetL3.parseInvoiceDets(jsonArray.getJSONObject(i)));

                    }
                    invl3detController.createOrUpdateFinvDetL3(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Last Three Invoice Det");


                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }
            }
            break;
            case Payments: {

                try {
                    PaymentController paymentController = new PaymentController(context);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalRecords = jsonArray.length();

                    ArrayList<Payment> arrayList = new ArrayList<Payment>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList.add(Payment.parsePayment(jsonArray.getJSONObject(i)));

                    }
                    paymentController.CreateOrUpdatePayments(arrayList);
                    new DownloadController(context).createOrUpdateDownload("" + arrayList.size(), "" + totalRecords, "Customer Payments Info");

                } catch (JSONException | NumberFormatException e) {
                    try {
                        throw e;
                    } catch (JSONException e1) {
                        Log.e("JSON ERROR>>>>>", e.toString());
                    }
                }


                Thread thread = new Thread() {
                    public void run() {
                        Looper.prepare();//Call looper.prepare()

                        Handler mHandler = new Handler() {
                            public void handleMessage(Message msg) {
                                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();

                            }
                        };

                        Looper.loop();
                    }
                };
                thread.start();
            }


            break;
            default:
                break;
        }
    }

}
