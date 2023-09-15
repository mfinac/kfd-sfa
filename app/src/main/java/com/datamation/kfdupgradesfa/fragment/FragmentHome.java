package com.datamation.kfdupgradesfa.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.astuetz.PagerSlidingTabStrip;
import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.fragment.debtordetails.DeliveryDetailsFragment;
import com.datamation.kfdupgradesfa.view.dashboard.DaySummaryFragment;
import com.datamation.kfdupgradesfa.view.dashboard.DaySummaryFragment_old;
import com.datamation.kfdupgradesfa.view.dashboard.MainDashboardFragment;
import com.datamation.kfdupgradesfa.view.dashboard.OrderFragment;
import com.datamation.kfdupgradesfa.view.dashboard.OutstandingDetailsFragment;
import com.datamation.kfdupgradesfa.view.dashboard.PaymentDetailsFragment;
import com.datamation.kfdupgradesfa.view.dashboard.ReceiptFragment;
import com.datamation.kfdupgradesfa.view.dashboard.ReportFragment;
import com.datamation.kfdupgradesfa.view.dashboard.TransactionDetailsFragment;


public class FragmentHome extends Fragment {
    public Fragment currentFragment;

    private DaySummaryFragment daySummaryFragment;
    private OrderFragment orderFragment;
    private ReceiptFragment receiptFragment;
    private OutstandingDetailsFragment outstandingDetailsFragment;
    private PaymentDetailsFragment paymentDetailsFragment;
    private DeliveryDetailsFragment deliveryDetailsFragment;

    private MainDashboardFragment mainDashboardFragment;

    private TransactionDetailsFragment transactionDetailsFragment;
    private ReportFragment otherDetailsFragment;


    private ViewPager viewPager;
    private DashboardPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        // ImageButton imgbtnCalendar = (ImageButton) view.findViewById(R.id.dashboard_toolbar_icon_calendar);

//        calendarDatePickerDialog = new CalendarDatePickerDialog();
//
//        calendarDatePickerDialog.setOnDateSetListener(new CalendarDatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(CalendarDatePickerDialog calendarDatePickerDialog, int year, int month, int day) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, month, day);
//
//                thisDay = day;
//
//                long millis = calendar.getTimeInMillis();
//
////                new GetDashboardData(millis).execute();
//                try {
//                    setDashboardDetails(millis);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "Error loading data. Please try again", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

//        imgbtnCalendar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currentFragment != null) {
//                    if (currentFragment instanceof MainDashboardFragment) {
//                        ((MainDashboardFragment) currentFragment).showCalendar();
//                    } else if (currentFragment instanceof DaySummaryFragment) {
//                        ((DaySummaryFragment) currentFragment).showCalendar();
//                    } else if (currentFragment instanceof InvoiceDetailsFragment) {
//                        ((InvoiceDetailsFragment) currentFragment).showCalendar();
//                    } else if (currentFragment instanceof PaymentDetailsFragment) {
//                        ((PaymentDetailsFragment) currentFragment).showCalendar();
//                    }
//                }
//            }
//        });

        // ImageButton imgbtnSync = (ImageButton) view.findViewById(R.id.dashboard_toolbar_icon_sync);
//        imgbtnSync.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currentFragment != null) {
//                    if (currentFragment instanceof MainDashboardFragment) {
//                        ((MainDashboardFragment) currentFragment).sync();
//                    } else if (currentFragment instanceof DaySummaryFragment) {
//                        ((DaySummaryFragment) currentFragment).refresh();
//                    } else if (currentFragment instanceof InvoiceDetailsFragment) {
//                        ((InvoiceDetailsFragment) currentFragment).refresh();
//                    } else if (currentFragment instanceof PaymentDetailsFragment) {
//                        ((PaymentDetailsFragment) currentFragment).refresh();
//                    }
//                }
//            }
//        });

        viewPager = (ViewPager) view.findViewById(R.id.dashboard_vp);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.dashboard_tab_strip);

        pagerAdapter = new DashboardPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        Resources resources = getResources();

        //  tabStrip.setBackgroundColor(resources.getColor(R.color.theme_color));
        tabStrip.setTextColor(resources.getColor(android.R.color.black));
        tabStrip.setIndicatorColor(resources.getColor(R.color.blue_c));
        tabStrip.setDividerColor(resources.getColor(R.color.half_black));
        tabStrip.setViewPager(viewPager);
        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("TAG_HOME"));
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(FragmentHome.this).attach(FragmentHome.this).commit();
                }
                if (position == 1) {
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("TAG_SUMMERY"));
                }
                if (position == 2) {
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("TAG_ORDER"));
                }

                if (position == 3) {
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("TAG_RECEIPT"));

                }

//                if (position == 4) {
//                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("TAG_TRANSACTION"));
//                }
                if (position == 4) {
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent("TAG_REPORT"));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return view;
    }

    private class DashboardPagerAdapter extends FragmentPagerAdapter {

        private String[] titles = {"Main", "Summary", "Orders", "Receipt", "Reports"};
//        private String[] titles = {"Main", "Summary", "Orders", "Receipt", "Transactions", "Reports"};

        public DashboardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case 0:

                    if (mainDashboardFragment == null)
                        mainDashboardFragment = new MainDashboardFragment();
                    return mainDashboardFragment;
                case 1:
                    if (daySummaryFragment == null) daySummaryFragment = new DaySummaryFragment();
                    return daySummaryFragment;
                case 2:
                    if (orderFragment == null) orderFragment = new OrderFragment();
                    return orderFragment;
                case 3:
                    if (receiptFragment == null) receiptFragment = new ReceiptFragment();
                    return receiptFragment;

//                case 4:
//                    if (transactionDetailsFragment == null)
//                        transactionDetailsFragment = new TransactionDetailsFragment();
//                    return transactionDetailsFragment;

                case 4:
                    if (otherDetailsFragment == null) otherDetailsFragment = new ReportFragment();
                    return otherDetailsFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
