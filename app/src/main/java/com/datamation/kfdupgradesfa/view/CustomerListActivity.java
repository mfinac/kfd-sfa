package com.datamation.kfdupgradesfa.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.controller.BaseUrlController;
import com.datamation.kfdupgradesfa.fragment.debtorlist.AllCustomerFragment;
import com.datamation.kfdupgradesfa.fragment.debtorlist.RouteCustomerFragment;
import com.datamation.kfdupgradesfa.helpers.SharedPref;


import at.markushi.ui.CircleButton;

public class CustomerListActivity extends AppCompatActivity{
    Context context;
    private CircleButton fabNewCust;
    private RouteCustomerFragment routeCustomerFragment;
    private AllCustomerFragment allCustomerFragment;
    SharedPref pref;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_management_pre_sales_customer);
        context = this;
        pref = SharedPref.getInstance(context);

        fabNewCust = (CircleButton) findViewById(R.id.outlet_details_fab_add_new_cus);
        fabNewCust.setColor(ContextCompat.getColor(this, R.color.theme_color));
        fabNewCust.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.addusr));
        Toolbar toolbar = (Toolbar) findViewById(R.id.debtor_list_toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView backIc = (ImageView) toolbar.findViewById(R.id.back);
        //Switch gpsSw = (Switch)toolbar.findViewById(R.id.gps_switch);
        title.setText("CUSTOMER LIST "+ new BaseUrlController(context).getActiveConnectionName(context));
         backIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     new PreProductController(DebtorListActivity.this).mClearTables();
                Intent intent = new Intent(CustomerListActivity.this, ActivityHome.class);
                startActivity(intent);
                finish();
            }
        });
        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.debtor_list_tab_strip);
        ViewPager viewPager = (ViewPager) findViewById(R.id.debtor_list_viewpager);

        //  slidingTabStrip.setBackgroundColor(getResources().getColor(R.color.theme_color));
        slidingTabStrip.setTextColor(getResources().getColor(android.R.color.black));
        slidingTabStrip.setIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        slidingTabStrip.setDividerColor(getResources().getColor(R.color.half_black));

        DebtorListPagerAdapter adapter = new DebtorListPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        slidingTabStrip.setViewPager(viewPager);

        fabNewCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), NewCustomerActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }
    private class DebtorListPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = {"ALL", "ROUTE"};

        public DebtorListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (allCustomerFragment == null)
                        allCustomerFragment = new AllCustomerFragment();
                        return allCustomerFragment;
                case 1:
                    if (routeCustomerFragment == null)
                        routeCustomerFragment = new RouteCustomerFragment();
                        return routeCustomerFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void onBackPressed() {
    }
}




