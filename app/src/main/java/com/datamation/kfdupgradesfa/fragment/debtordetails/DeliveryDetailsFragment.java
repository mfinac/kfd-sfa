package com.datamation.kfdupgradesfa.fragment.debtordetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.datamation.kfdupgradesfa.R;
import com.datamation.kfdupgradesfa.controller.OrderController;
import com.datamation.kfdupgradesfa.helpers.SharedPref;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.vo.DateData;


public class DeliveryDetailsFragment extends Fragment {

    ArrayList<String> devDateList;
    public SharedPref pref;
    MCalendarView calendarView;
    int year;
    int month;
    int day;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delivery_details, container, false);

        pref = SharedPref.getInstance(getActivity());
        calendarView = ((MCalendarView) view.findViewById(R.id.calendar));

        devDateList = new OrderController(getActivity()).getDeliveryDates(pref.getSelectedDebCode());

        ArrayList<DateData> dates = new ArrayList<>();

        for (int i = 0; i < devDateList.size(); i++) {

            String dateParts[] = devDateList.get(i).split("-");

            year = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]);
            day = Integer.parseInt(dateParts[2]);

            dates.add(new DateData(year, month, day));

        }

        for (int i = 0; i < dates.size(); i++) {

            calendarView.markDate(dates.get(i).getYear(), dates.get(i).getMonth(), dates.get(i).getDay());//mark multiple dates with this code.
        }

        calendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                Toast.makeText(getActivity(), String.format("%d-%d", year, month), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

}
