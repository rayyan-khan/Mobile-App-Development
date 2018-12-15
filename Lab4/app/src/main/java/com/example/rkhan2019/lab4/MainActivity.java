package com.example.rkhan2019.lab4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    public static final String tag = "Lab4";
    int count1 = 0;
    int count2 = 0;
    int count3 = 0;
    int count4 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View v)
    {
        int myId = v.getId();
        String output = "";
        switch(myId)
        {
            case R.id.view:
                count1++;
                output = "View: View 1 -- #Clicks: " + count1;
                break;
            case R.id.view2:
                count2++;
                output = "View: View 2 -- #Clicks: " + count2;
                break;
            case R.id.view3:
                count3++;
                output = "View: View 3 -- #Clicks: " + count3;
                break;
            case R.id.view4:
                count4++;
                output = "View: View 4 -- #Clicks " + count4;
                break;
        }
        Log.i(tag, output);
    }
}

