package com.example.rkhan2019.masterdetailflow;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.*;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends FragmentActivity implements detailFragment.detailFragmentInterface {

    MasterFragment masterFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = findViewById(R.id.frame);

        MasterFragment masterFragment = new MasterFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame, masterFragment);
        transaction.commit();
    }

    public void goBackandUpdate(){
        masterFragment.updateDisplay();
    }
}
