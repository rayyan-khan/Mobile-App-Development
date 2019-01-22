package com.example.rkhan2019.lab11;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileDescriptor;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements  MyCustomView.cvinterface {

    /* ------------------------*/
    /*   member variables
     * */

    private final static int BREADCRUMB_OPEN_DOCUMENT = 6560;
    private MyCustomView mMyCustomView;

    SimpleServiceReceiver mSimpleServiceReceiver;
    private final static String TAG = "MAIN_ACTIVITY_TAG";

    /* ------------------------*/
    /*   lifecycle methods
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLoad = findViewById(R.id.button_load);
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageOnDevice();
            }
        });

        Button buttonTell = findViewById(R.id.button_tell);
        buttonTell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("OnClick", "CLICKED");

                doSomethingWithBitmap();
            }
        });

        mMyCustomView = findViewById(R.id.mcv);

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

    /* ------------------------*/
    /*   interface methods
     * */


    /* ------------------------*/
    /*   OPEN DOCUMENT METHODS :
     * *  SOURCE: https://developer.android.com/guide/topics/providers/document-provider#java
     * */

    private void openImageOnDevice() {


        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file browser
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");

        startActivityForResult(intent, BREADCRUMB_OPEN_DOCUMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == BREADCRUMB_OPEN_DOCUMENT && resultCode == Activity.RESULT_OK) {

            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            Uri uri = null;
            if (data != null) {
                uri = data.getData();

                Log.i("IMAGE STUFF", "Uri: " + uri.toString());

                try {
                    Bitmap b = getBitmapFromUri(uri);
                    Drawable d = new BitmapDrawable(getResources(), b);
                    mMyCustomView.setImageBitmap(b);
                } catch (IOException e) {
                    Log.e("ERROR","ERROR READING URI FOR BITMAP");
                }
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void doSomethingWithBitmap(){
        Bitmap c = mMyCustomView.getCanvasBitmap();

        // Proof that big bitmaps can take the trip!
        //  Bitmap c = Bitmap.createBitmap(2048, 4096, Bitmap.Config.ARGB_8888);

        Log.i("doSomethingWithBitMap", "Doing something");

        final BitmapTransferEnum transferEnum = BitmapTransferEnum.INSTANCE;
        transferEnum.setData(c);

        // the intent that will start the service
        Intent intent_from_activity_to_service =  new Intent();
        intent_from_activity_to_service.putExtra(BitmapTransferEnum.KEY, transferEnum.INSTANCE);

        intent_from_activity_to_service.setClass(this, SimplestService.class);

        // go ahead and start the service
        startService(intent_from_activity_to_service);
    }



    @Override
    public void doIt(float x, float y) {
        // called by touching screen
    }

    /* -------------------------------- */
    /*    CUSTOM HELPER CLASS           */

    // this class handles intents that come back from the worker service
    private class SimpleServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent_from_service) {

            Bundle extras = intent_from_service.getExtras();

            Log.i("ON RECEIVED", extras + "");

            if (extras != null) {
                if (extras.containsKey(BitmapTransferEnum.KEY)) {

                    Log.i(TAG, "Contains enum");
                    BitmapTransferEnum bitmapTransferEnum = (BitmapTransferEnum) extras.getSerializable(BitmapTransferEnum.KEY);
                    Bitmap b = bitmapTransferEnum.INSTANCE.getData();

//                    Drawable d = new BitmapDrawable(getResources(), b);
                    mMyCustomView.setImageBitmap(b);
//                    mMyCustomView.setBackground(d);

                }
            }
        }
    }
}