package com.example.rkhan2019.masterdetailflow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.*;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.*;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public class MasterFragment extends Fragment {

    SharedPreferences sp;
    View mRootView;
    LinearLayout linearLayout;
    Gson gson;

    String[] viewTitles = {"Title1", "Title2", "Title3"};

    public MasterFragment() {
    }

    public static MasterFragment newInstance() {
        return new MasterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.main_layout, container, false);
        LinearLayout linearLayout = mRootView.findViewById(R.id.linear_layout);
        Gson gson = new Gson();
        FragmentActivity activity = getActivity();
        final SharedPreferences sp = activity.getSharedPreferences("com.example.rkhan2019.masterdetailflow", Context.MODE_PRIVATE);
        if (sp.contains("title")) {
            String gsonTitles = sp.getString("title", "0");
            viewTitles = gson.fromJson(gsonTitles, viewTitles.getClass());
            Log.i("tag", "pulled");
            for (String title : viewTitles) {
                Log.i("title", title);
            }
        }

        for (int title = 0; title < viewTitles.length; title++) {
            TextView textView = new TextView(getContext());
            textView.setText(viewTitles[title]);
            textView.setTextSize(40);
            textView.setTag(title);

            linearLayout.addView(textView);
            if (title % 2 == 1) {
                textView.setBackgroundColor(Color.parseColor("#ADD8E6"));
            } else {
                textView.setBackgroundColor(Color.parseColor("#FFB6C1"));
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    detailFragment detFragment = detailFragment.newInstance(viewTitles[tag], "body", viewTitles);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.frame, detFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
        return mRootView;
    }

    public void updateDisplay() {

        sp = getActivity().getSharedPreferences("com.example.rkhan2019.masterdetailflow", Context.MODE_PRIVATE);

        if (sp.contains("title")) {
            String gsonTitles = sp.getString("title", "0");
            viewTitles = gson.fromJson(gsonTitles, viewTitles.getClass());
            Log.i("tag", "pulled");
            for (String title : viewTitles) {
                Log.i("title", title);
            }
        }

        for (int title = 0; title < viewTitles.length; title++) {
            TextView textView = new TextView(getContext());
            textView.setText(viewTitles[title]);
            textView.setTextSize(40);
            textView.setTag(title);

            linearLayout.addView(textView);
            if (title % 2 == 1) {
                textView.setBackgroundColor(Color.parseColor("#ADD8E6"));
            } else {
                textView.setBackgroundColor(Color.parseColor("#FFB6C1"));
            }
        }

    }
}