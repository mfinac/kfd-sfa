package com.datamation.kfdupgradesfa.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.controller.BaseUrlController;
import com.datamation.kfdupgradesfa.controller.OrderDetailController;
import com.datamation.kfdupgradesfa.controller.ReferenceController;
import com.datamation.kfdupgradesfa.dialog.CustomProgressDialog;
import com.datamation.kfdupgradesfa.helpers.OrderResponseListener;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Debtor;
import com.datamation.kfdupgradesfa.model.Order;
import com.datamation.kfdupgradesfa.order.OrderDetailFragment;
import com.datamation.kfdupgradesfa.order.OrderHeaderFragment;
import com.datamation.kfdupgradesfa.order.OrderMainFragment;
import com.datamation.kfdupgradesfa.order.OrderSummaryFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.datamation.kfdupgradesfa.utils.CustomViewPager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderActivity<extras> extends AppCompatActivity implements OrderResponseListener {
    private OrderMainFragment orderMainFragment;
    private OrderHeaderFragment orderHeaderFragment;
    private OrderDetailFragment orderDetailFragment;
    private OrderSummaryFragment orderSummaryFragment;
    public CustomViewPager viewPager;
    Context context;
    public Order selectedPreHed = null;
    SharedPref pref;
    boolean status = false;
    String refNo;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sales);
        context = this;
        pref = SharedPref.getInstance(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("SALES ORDER (APP VERSION-"+getVersionCode()+") "+new BaseUrlController(context).getActiveConnectionName(context));


        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.presale_tab_strip);
        viewPager = (CustomViewPager) findViewById(R.id.presale_viewpager);

        //   slidingTabStrip.setBackgroundColor(getResources().getColor(R.color.theme_color));
        slidingTabStrip.setTextColor(getResources().getColor(android.R.color.black));
        slidingTabStrip.setIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        slidingTabStrip.setDividerColor(getResources().getColor(R.color.half_black));

        refNo = new ReferenceController(context).getCurrentRefNo(getResources().getString(R.string.NumVal));

        PreSalesPagerAdapter adapter = new PreSalesPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        slidingTabStrip.setViewPager(viewPager);
        viewPager.disableScroll(true);

        viewPager.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });


        viewPager.addOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if (position == 2)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TAG_PRE_SUMMARY"));
                else if (position == 0)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TAG_PRE_HEADER"));
                else if (position == 1)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TAG_PRE_DETAILS"));


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Intent dataIntent = getIntent();
        Debtor debtor = dataIntent.getParcelableExtra("outlet");

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void moveBackToFragment(int index) {
        if (index == 0) {
            viewPager.setCurrentItem(0);
        }

        if (index == 1) {
            viewPager.setCurrentItem(1);
        }

        if (index == 2) {
            viewPager.setCurrentItem(2);
        }
    }

    @Override
    public void moveNextToFragment(int index) {
        if (index == 0) {
            viewPager.setCurrentItem(0);
        }

        if (index == 1) {
            viewPager.setCurrentItem(1);
        }

        if (index == 2) {
            viewPager.setCurrentItem(2);
        }
    }

    private class PreSalesPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = {"ORDER HEADER", "ORDER DETAILS", "ORDER SUMMARY"};


        public PreSalesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
//                case 0:
//                    if(orderMainFragment == null) orderMainFragment = new OrderMainFragment();
//                    return orderMainFragment;
                case 0:
                    if (orderHeaderFragment == null)
//                        refNo = new ReferenceController(context).getCurrentRefNo(getResources().getString(R.string.NumVal));
//                        new ReferenceController(context).findUsageOfrefNo(refNo);
                        orderHeaderFragment = new OrderHeaderFragment();
                    return orderHeaderFragment;
                case 1:

                    if (orderDetailFragment == null)
                        orderDetailFragment = new OrderDetailFragment();
                    return orderDetailFragment;
                case 2:

                    if (orderSummaryFragment == null)
                        orderSummaryFragment = new OrderSummaryFragment();
                    return orderSummaryFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }


    }
    public String getVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}




