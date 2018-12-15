package com.example.pckosek.simpleservice_01;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

public class SimplestService extends IntentService {

    /* -------------------------------- */
    /*    member variables              */

    private int mSecretNumber = 10;
    final static String ACTION_KEY = "action_key";

    private final static String TAG = "SIMPLEST_SERVICE_TAG";

    /* -------------------------------- */
    /*    CONSTRUCTOR                   */

    public SimplestService() {

        // SUPER IMPORTANT!!!
        //
        //  The constructor must call super IntentService(String) with a name for the thread

        super("SimplestService");
    }

    /* -------------------------------- */
    /*    REQUIRED CLASS METHODS        */

    @Override
    protected void onHandleIntent(Intent intent_from_activity) {

        // THIS METHOD IS CALLED WHEN onClick in the activity triggers the start of the service
        Log.i(TAG, "GOT MESSAGE!");

        respondToActivity();
    }


    /* -------------------------------- */
    /*    CUSTOM HELPER METHODS         */

    private void respondToActivity() {

        // create a new intent to send data back to the activity
        Intent intent_from_service = new Intent();

        // define a key that will be paired to the intent
        intent_from_service.setAction(ACTION_KEY);

        // optionally attach data to the intent.
        // the key implementation here is weak.
        intent_from_service.putExtra("super_top_secret", mSecretNumber);

        // send the local broadcast
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent_from_service);
    }
}
