package com.example.rkhan2019.lab10_5;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

public class ServiceClass extends IntentService{

    //variables

    private int mSecretNumber = 23;
    final static String ACTION_KEY = "action_key";

    private final static String TAG = "SIMPLEST_SERVICE_TAG";


    //Constructor
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ServiceClass() {
        super("Service Class");
    }

    //required to implement
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        respondToActivity();
    }

    //helpers
    private void respondToActivity() {

        Log.i("RESPOND TO ACTIVITY", "");

        mSecretNumber = operationQuicksand();
        Intent intentFromService = new Intent();
        intentFromService.setAction(ACTION_KEY);
        intentFromService.putExtra("secret_num", mSecretNumber);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentFromService);
    }

    public int operationQuicksand() {
        int x = 0;
        int y = 0;
        int z = 0;

        while (x<50) {
            x++;
            y = 0;
            while (y<x) {
                y++;
                z += y;
            }
        }
        return z;
    }
}
