package com.example.rkhan2019.masterdetailflow;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.*;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.*;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rkhan2019.masterdetailflow.R;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.Arrays;


public class detailFragment extends Fragment {

    String title;
    String body;
    String[] viewTitles;
    Button saveButton;
    int index;
    SharedPreferences sp;

    private detailFragmentInterface mCallback;

    public detailFragment() {
    }

    public static detailFragment newInstance(String ttl, String bdy, String[] viewTitles){
        return new detailFragment(ttl, bdy, viewTitles);
    }

    public detailFragment(String ttl, String bdy, String[] vt){
        title = ttl;
        body = bdy;
        viewTitles = vt;
        index = Arrays.asList(viewTitles).indexOf(title);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (detailFragmentInterface) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + "Must implement detailFragmentInterface");
        }
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mRootView = inflater.inflate(R.layout.detail_layout, container, false);
        final EditText top = mRootView.findViewById(R.id.editText);
        EditText bottom = mRootView.findViewById(R.id.editText2);
        sp = getActivity().getSharedPreferences("com.example.rkhan2019.masterdetailflow", 0);

        saveButton = mRootView.findViewById(R.id.button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = top.getText().toString();
                viewTitles[index] = title;
                Log.i("tag",viewTitles[0]);
                Gson gson = new Gson();
                String gsonStr = gson.toJson(viewTitles);
                sp.edit().putString("title", gsonStr).commit();
                Log.i("tag", sp.getString("title", "null"));
//                mCallback.goBackandUpdate();
            }
        });

        top.setText(title);
        bottom.setText(body);

        top.setBackgroundColor(Color.parseColor("#99ffcc"));
        bottom.setBackgroundColor(Color.parseColor("#fadadd"));
        return mRootView;
    }

    public interface detailFragmentInterface{

        public void goBackandUpdate();
    }

}
