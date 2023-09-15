package com.datamation.kfdupgradesfa.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.adapter.ExpandableListAdapter;
import com.datamation.kfdupgradesfa.dialog.AnswersDialogs;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/*
    create by kaveesha - 01/12/2020
 */

public class QuestionsActivity extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Help");

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                if (groupPosition == 0) {
                    if (childPosition == 0) {
                        // AddItems();
                        AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                        answersDialogs.startOrder();
                    } else if (childPosition == 1) {
                        AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                        answersDialogs.AddItems();
                    } else if (childPosition == 2) {
                        AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                        answersDialogs.calculateFreeIssue();
                    } else if (childPosition == 3) {
                        AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                        answersDialogs.saveOrder();
                    } else if (childPosition == 4) {
                        AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                        answersDialogs.editPlaceAnOrder();
                    } else if (childPosition == 5) {
                        AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                        answersDialogs.deletePlaceAnOrder();
                    }
                } else if (groupPosition == 1) {
                    if (childPosition == 0) {
                        AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                        answersDialogs.editOrders();
                    } else if (childPosition == 1) {
                        AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                        answersDialogs.deleteOrders();
                    } else if (childPosition == 2) {
                        AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                        answersDialogs.uploadOrders();
                    } else if (childPosition == 3) {
                        AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                        answersDialogs.checkOrderApprovalOrReject();
                    } else if (childPosition == 4) {
                        AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                        answersDialogs.checkOrderDispatchOrInvoice();
                    }
                } else if (groupPosition == 2) {
                    AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                    answersDialogs.viewOutstanding();
                } else {
                    AnswersDialogs answersDialogs = new AnswersDialogs(QuestionsActivity.this);
                    answersDialogs.viewPayments();
                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mnu_close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.close:
                Intent aboutIntent = new Intent(QuestionsActivity.this, ActivityHome.class);
                startActivity(aboutIntent);
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //nothing (disable backbutton)
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //Adding titels
        listDataHeader.add("How do you place an order?");
        listDataHeader.add("How to view orders?");
        listDataHeader.add("How to view outstanding?");
        listDataHeader.add("How to view payments?");

        List<String> place_an_order = new ArrayList<String>();

        place_an_order.add("• How to start an order process?");
        place_an_order.add("• How to add items?");
        place_an_order.add("• How to calculate free issue items?");
        place_an_order.add("• How to save order?");
        place_an_order.add("• How to edit order?");
        place_an_order.add("• How to discard order?");

        List<String> view = new ArrayList<String>();

        view.add("• How to edit orders?");
        view.add("• How cancel orders?");
        view.add("• How to upload orders?");
        view.add("• How to check your order is approved or rejected?");
        view.add("• How to check your order is dispatch or invoice?");


        List<String> outstanding = new ArrayList<String>();

        outstanding.add("1). You can view outstanding details by tapping outstanding tab in the dashboard.");

        List<String> payments = new ArrayList<String>();

        payments.add("2). You can view payments details by tapping payment tab you can view payments details in the dashboard.");

        listDataChild.put(listDataHeader.get(0), place_an_order); // Header, Child data
        listDataChild.put(listDataHeader.get(1), view);
        listDataChild.put(listDataHeader.get(2), outstanding);
        listDataChild.put(listDataHeader.get(3), payments);

    }
}
