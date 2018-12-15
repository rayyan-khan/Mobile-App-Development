package com.example.rkhan2019.lab11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /* ------------------------*/
    /*   member variables
     * */

    private final static int BREADCRUMB_OPEN_DOCUMENT = 6560;
    private MyCustomView mMyCustomView;


    /* ------------------------*/
    /*   lifecycle methods
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonLoad = findViewById(R.id.button_load);
        buttonLoad.setOnClickListener(this);

        Button buttonTell = findViewById(R.id.button_tell);
        buttonTell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doSomethingWithBitmap();
                Log.i("ONCLICK", "DO SOMETHING");
            }
        });

        mMyCustomView = findViewById(R.id.mcv_01);
    }

    @Override
    public void onClick(View v) {
        openImageOnDevice();
    }


    /* ------------------------*/
    /*   OPEN DOCUMENT METHODS :
     * *  SOURCE: https://developer.android.com/guide/topics/providers/document-provider#java
     * */

    private void openImageOnDevice(){

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

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
                    mMyCustomView.setBackground(d);
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
        int pixelColorAsInt = c.getPixel(0,0);
        Log.i("BITMAP", pixelColorAsInt+"");
    }
}
