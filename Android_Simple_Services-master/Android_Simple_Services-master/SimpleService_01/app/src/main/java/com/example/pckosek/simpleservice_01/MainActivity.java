package com.example.pckosek.simpleservice_01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /* -------------------------------- */
    /*    member variables              */

    private Button mIntentButton;
    private TextView mTextView;

    SimpleServiceReceiver mSimpleServiceReceiver;
    private final static String TAG = "MAIN_ACTIVITY_TAG";

    /* -------------------------------- */
    /*    lifecycle methods             */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign the button
        mIntentButton = findViewById(R.id.b_start_service);
        mIntentButton.setOnClickListener(this);

        mTextView = findViewById(R.id.textView);

        // This receiver is where the service communicates back to us
        mSimpleServiceReceiver = new SimpleServiceReceiver();
    }


    @Override
    public void onResume() {
        super.onResume();

        // BROADCAST RECEIVERS ARE REGISTERED IN onResume

        // local broadcasts are used when your app is the sole listener
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);

        // create a filter for the manager
        //    => i.e. listen to messages with this a specific key from our service class
        IntentFilter filter = new IntentFilter(SimplestService.ACTION_KEY);

        // register your receiver class with the local broadcast receiver
        manager.registerReceiver(
                mSimpleServiceReceiver,
                filter);
    }


    @Override
    public void onPause() {
        super.onPause();

        // BROADCAST RECEIVERS ARE UN-REGISTERED IN onPause - lest bad things happen

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(mSimpleServiceReceiver);
    }


    /* -------------------------------- */
    /*    interface methods             */

    @Override
    public void onClick(View v) {

        Log.i(TAG,"STARTING SERVICE");

        // the intent that will start the service
        Intent intent_from_activity_to_service =  new Intent();

        // define the context and the target (the service that will be started)
        intent_from_activity_to_service.setClass(this, SimplestService.class);

        // go ahead and start the service
        startService(intent_from_activity_to_service);
    }

    /* -------------------------------- */
    /*    CUSTOM HELPER CLASS           */

    // this class handles intents that come back from the worker service
    private class SimpleServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent_from_service) {

            // get any information embedded in the intent
            int secretNumber = intent_from_service.getIntExtra("super_top_secret",-1);

            mTextView.setText("Service Value is: "+secretNumber);
            Log.i(TAG, "DATA_IN: "+secretNumber);

        }
    }

}
