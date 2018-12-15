package com.example.rkhan2019.lab10_5;

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

    private Button mIntentButton;
    private TextView mTextView;
    ServiceClassReceiver mServiceClassReceiver;
    private final static String TAG = "MAIN_ACTIVITY_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntentButton = findViewById(R.id.buttonId);
        mIntentButton.setOnClickListener(this);

        mTextView = findViewById(R.id.textView1);

        mServiceClassReceiver = new ServiceClassReceiver();
    }

    @Override
    public void onResume(){
        super.onResume();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter(ServiceClass.ACTION_KEY);
        manager.registerReceiver(mServiceClassReceiver, filter);
    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(mServiceClassReceiver);
    }


    //interface methods
    @Override
    public void onClick(View v) {
        Intent intFromActToService = new Intent();
        intFromActToService.setClass(this, ServiceClass.class);
        startService(intFromActToService);
    }

    //helper class
    private class ServiceClassReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intFromService) {

            // get any information embedded in the intent
            int secretNumber = intFromService.getIntExtra("secret_num",-1);

            mTextView.setText("Service Value is: "+secretNumber);
            Log.i(TAG, "RECEIVED: "+secretNumber);

        }
    }

}
