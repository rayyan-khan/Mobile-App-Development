package com.example.rkhan2019.lab10_5;

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
import android.os.ParcelFileDescriptor;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mImageVIew;
    private Button buttonGet;
    private Button buttonBlur;
    ServiceClassReceiver mServiceClassReceiver;
    private final static String TAG = "MAIN_ACTIVITY_TAG";

    private final static int BREADCRUMB_OPEN_DOCUMENT = 6560;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageVIew = findViewById(R.id.imageview);
        buttonGet = findViewById(R.id.buttonget);
        buttonBlur = findViewById(R.id.buttonblur);

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageOnDevice();
            }
        });

        buttonBlur.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i("tag", "click");
                final Intent blurIntent = new Intent();
                blurIntent.setClass(getApplicationContext(), blurService.class);
                startService(blurIntent);
                Bitmap bmp = ((BitmapDrawable)mImageVIew.getDrawable()).getBitmap();
                Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/2, bmp.getHeight()/2, false);
                mImageVIew.setImageBitmap(Bitmap.createScaledBitmap(bmp2, bmp2.getWidth(), bmp2.getHeight(), false));
            }
        });

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
        Log.i("ON CLICK", "");
    }

    //helper class
    private class ServiceClassReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intFromService) {

            Uri uri = intFromService.getData();
            try {
                Bitmap b = getBitmapFromUri(uri);
                Log.i("IN TRY", "");
                mImageVIew.setImageBitmap(b);
            } catch (IOException e) {
                Log.e("ERROR","ERROR READING URI FOR BITMAP");
            }
            Log.i(TAG, "RECEIVED: ");

        }
    }


    private void openImageOnDevice() {

        Log.i("OPENING IMAGE", "");

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

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

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
                    //Drawable d = new BitmapDrawable(getResources(), b);

                    Log.i("IN ON ACTIVITY RESULT", "");

                    mImageVIew.setImageBitmap(b);
                } catch (IOException e) {
                    Log.e("ERROR","ERROR READING URI FOR BITMAP");
                }
            }
        }
        else if(requestCode == 1){
            Uri uri = data.getData();

            Log.i("IMAGE STUFF", "Uri: " + uri.toString());

            try {
                Bitmap b = getBitmapFromUri(uri);
                //Drawable d = new BitmapDrawable(getResources(), b);

                Log.i("IN ON ACTIVITY RESULT", "");

                mImageVIew.setImageBitmap(b);
            } catch (IOException e) {
                Log.e("ERROR","ERROR READING URI FOR BITMAP");
            }
        }
    }

}
