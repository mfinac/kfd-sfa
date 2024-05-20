package com.datamation.kfdupgradesfa.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.controller.BaseUrlController;
import com.datamation.kfdupgradesfa.controller.ReceiptDetController;
import com.datamation.kfdupgradesfa.helpers.ReceiptResponseListener;
import com.datamation.kfdupgradesfa.helpers.SharedPref;
import com.datamation.kfdupgradesfa.model.Customer;

import com.datamation.kfdupgradesfa.model.ReceiptHed;

import com.datamation.kfdupgradesfa.receipt.ReceiptDetails;
import com.datamation.kfdupgradesfa.receipt.ReceiptHeader;
import com.datamation.kfdupgradesfa.receipt.ReceiptSummary;


public class ReceiptActivity extends AppCompatActivity implements ReceiptResponseListener {
    private ReceiptHeader headerFragment;
    private ReceiptDetails detailFragment;
    private ReceiptSummary salesManagementFragment;

    private ViewPager viewPager;
    public Customer selectedDebtor = null;
    public ReceiptHed selectedRecHed = null;
    public Toolbar toolbar;
    //  public PayReceiptHed selectedRecHed = null;
    SharedPref pref;
    public Double ReceivedAmt = 0.0;
    Context context;
    boolean status = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_sales);
        context = this;
        pref = SharedPref.getInstance(context);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.inflateMenu(R.menu.mnu_exit);
        toolbar.setTitle("RECEIPT(APP VERSION-"+getVersionCode()+") "+new BaseUrlController(context).getActiveConnectionName(context));

        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.presale_tab_strip);
        viewPager = (ViewPager) findViewById(R.id.presale_viewpager);

        // slidingTabStrip.setBackgroundColor(getResources().getColor(R.color.theme_color));
        slidingTabStrip.setTextColor(getResources().getColor(android.R.color.black));
        slidingTabStrip.setIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        slidingTabStrip.setDividerColor(getResources().getColor(R.color.half_black));

        PreSalesPagerAdapter adapter = new PreSalesPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        viewPager.setPageMargin(pageMargin);
        slidingTabStrip.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 2)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TAG_SUMMARY"));
                else if (position == 0)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TAG_HEADER"));
                else if (position == 1)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("TAG_DETAILS"));

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        status = new ReceiptDetController(getApplicationContext()).isAnyActiveReceipt(pref.getSelectedDebCode());
    }

    private class PreSalesPagerAdapter extends FragmentPagerAdapter {

        private final String[] titles = {"HEADER", "RECEIPT DETAILS", "RECEIPT SUMMARY"};

        public PreSalesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    if(headerFragment == null) headerFragment = new ReceiptHeader();
                    return headerFragment;
                case 1:
                    if(detailFragment == null) detailFragment = new ReceiptDetails();

                    return detailFragment;
                case 2:
                    if(salesManagementFragment == null) salesManagementFragment = new ReceiptSummary();
                    return salesManagementFragment;
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
    public void moveToDetailsRece(int index) {

        if (index == 0)
        {
            viewPager.setCurrentItem(0);
        }

        if (index == 1)
        {
            viewPager.setCurrentItem(1);
        }

        if (index == 2)
        {
            viewPager.setCurrentItem(2);
        }


    }

    @Override
    public void moveBackToDetailsRece(int index) {
        if (index == 0)
        {
            viewPager.setCurrentItem(0);
        }

        if (index == 1)
        {
            viewPager.setCurrentItem(1);
        }

        if (index == 2)
        {
            viewPager.setCurrentItem(2);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (status)
            viewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(this,"Back button disabled until finish transaction",Toast.LENGTH_SHORT).show();
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
}
