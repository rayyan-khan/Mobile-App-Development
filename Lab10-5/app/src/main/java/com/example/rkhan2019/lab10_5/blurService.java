package com.example.rkhan2019.lab10_5;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Arrays;

public class blurService extends Service {

    Bitmap bitmap;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void onHandleIntent(Intent intentIn){
        try {

            intentIn.setAction("Blur");
            Uri uri = (Uri)intentIn.getParcelableExtra("uri");
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            respondToActivity();
            bitmap = transformBitmap(bitmap);

        }
        catch(Exception e){

        }

    }

    public int onStartCommand(final Intent intent, int flags, int startId){
        onHandleIntent(intent);
        return 1;
    }

    private void respondToActivity() {
        Intent intentFromService = new Intent();
        intentFromService.setAction("Blur");
        intentFromService.putExtra("bitmap", getImageUri(getApplicationContext(), bitmap));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentFromService);
    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private Bitmap transformBitmap(Bitmap bmp) {

        String TAG = "tag";

        // --------------------------------------------------------
        //      INITIALIZATION

        // INITIALIZE HELPER VARIABLES USED AT VARIOUS POINTS IN THIS METHOD
        int i = 0;
        int j = 0;
        int pixelNo;


        // DEFINE A NEW DIMENSION (TO BE USED AS HEIGHT AND WIDTH) OF THE BITMAP
        //  THEN, RESIZE THE BITMAP
        int new_dimension = 40;
        bmp.reconfigure(new_dimension, new_dimension, bmp.getConfig());

        // EXTRACT PIXEL VALUES FROM THE OLD BITMAP.
        //   -> -> THESE PIXELS ARE NOT USED IN THIS EXAMPLE. JUST FYI.
        int[] argb_in = new int[new_dimension * new_dimension];
        bmp.getPixels(argb_in, 0, new_dimension, 0, 0, new_dimension, new_dimension);

        Log.i(TAG, "Bitmap Width: " + String.valueOf(bmp.getWidth()));
        Log.i(TAG, "Bitmap Height:" + String.valueOf(bmp.getHeight()));


        // DEFINE THE NUMBER OF POINTS IN THE FFT.
        //  FIRST CREATE AN FFT OBJECT
        //  THEN CREATE COMPLEX OBJECTS (THAT WILL HOLD fft'd VALUES)
        //  THE FFT OPERATION USED IN THIS METHOD TRANSFORMS THE OBJECTS IN PLACE
        int N = 128;
        FFT fft = new FFT(N, N);
        Complex2D KERNEL = new Complex2D(N, N);
        Complex2D COLOR = new Complex2D(N, N);

        // DEFINE THE CONVOLUTION KERNEL
        //  KERNEL IS A 7x& GAUUSIAN
        double[][] kernel = {{0.000036, 0.000363, 0.001446, 0.002291, 0.001446, 0.000363, 0.000036},
                {0.000363, 0.003676, 0.014662, 0.023226, 0.014662, 0.003676, 0.000363},
                {0.001446, 0.014662, 0.058488, 0.092651, 0.058488, 0.014662, 0.001446},
                {0.002291, 0.023226, 0.092651, 0.146768, 0.092651, 0.023226, 0.002291},
                {0.001446, 0.014662, 0.058488, 0.092651, 0.058488, 0.014662, 0.001446},
                {0.000363, 0.003676, 0.014662, 0.023226, 0.014662, 0.003676, 0.000363},
                {0.000036, 0.000363, 0.001446, 0.002291, 0.001446, 0.000363, 0.000036}};

        // ASSIGN THE KERNEL VALUES TO AN OBJECT THAT CAN BE FFT'd
        //  THEN TAKE THE FFT OF THE KERNEL
        KERNEL.assign(kernel);
        fft.fft2(KERNEL.real, KERNEL.imag);

        // THE CONVOLVED IMAGE WILL BE INSET kernel.length/2 PIXELS FROM THE EDGE
        int x_inset = 3;
        int y_inset = 3;


        // DEFINE A 40x40 CHECKER PATTERN
        //   (THIS IS THE TEST IMAGE)
        //  THEN, CREATE AN ARRAY TO HOLD THE PIXElS THAT WILL
        //   PUSHED BE THE OUTPUT BITMAP
        double[] color = {255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255., 0., 0., 0., 0., 0., 0., 0., 0., 0., 0., 255., 255., 255., 255., 255., 255., 255., 255., 255., 255.};

        int[] argb_out = new int[new_dimension * new_dimension];

        // SET THE OUTPUT TRANSPARENCY TO ZERO
        Arrays.fill(argb_out, 255 << 24);

        // DEFINE A VALUE THAT WILL BE USED TO FILL ALL PIXELS THAT ARE NOT EXPLICITLY SET
        double all_others = 255.;


        // --------------------------------------------------------
        //      FAST CONVOLUTION


        //  -------------- RED --------------

        // FILL THE COLOR FFT OBJECT WITH THE PIXELS FROM OUR INPUT IMAGE
        COLOR.assignFrom(color, new_dimension, all_others);

        // TAKE THE 2D FFT OF THE RED LAYER OF THE IMAGE
        fft.fft2(COLOR.real, COLOR.imag);
        Log.i(TAG, "RED FFT");

        // MULTIPLY THE FFT OF THE RED LAYER BY THE FFT OF THE KERNEL
        COLOR.complexMultiply(KERNEL.real, KERNEL.imag);

        // INVERSE 2D FFT THE MULTIPLICATION RESULT
        fft.ifft2(COLOR.real, COLOR.imag);
        Log.i(TAG, "RED iFFT");

        // ASSIGN THE CONVOLVED VALUES TO THE RED CHANNEL OF THE OUTPUT BUFFER
        pixelNo = 0;
        for (i = x_inset; i < x_inset + new_dimension; i++) {
            for (j = y_inset; j < y_inset + new_dimension; j++) {

                // CAST AS AN INT AND SHIFT TO THE RED CHANNEL
                argb_out[pixelNo++] |= ((int) COLOR.real[i][j]) << 16;
            }
        }

        //  -------------- GREEN --------------

        COLOR.assignFrom(color, new_dimension, all_others);

        fft.fft2(COLOR.real, COLOR.imag);
        Log.i(TAG, "GREEN FFT");

        COLOR.complexMultiply(KERNEL.real, KERNEL.imag);
        fft.ifft2(COLOR.real, COLOR.imag);
        Log.i(TAG, "GREEN iFFT");
        pixelNo = 0;

        for (i = x_inset; i < x_inset + new_dimension; i++) {
            for (j = y_inset; j < y_inset + new_dimension; j++) {
                argb_out[pixelNo++] |= ((int) COLOR.real[i][j]) << 8;
            }
        }

        //  -------------- BLUE --------------

        COLOR.assignFrom(color, new_dimension, all_others);

        fft.fft2(COLOR.real, COLOR.imag);
        Log.i(TAG, "BLUE FFT");

        COLOR.complexMultiply(KERNEL.real, KERNEL.imag);
        fft.ifft2(COLOR.real, COLOR.imag);
        Log.i(TAG, "BLUE iFFT");

        pixelNo = 0;
        for (i = x_inset; i < x_inset + new_dimension; i++) {
            for (j = y_inset; j < y_inset + new_dimension; j++) {
                argb_out[pixelNo++] |= ((int) COLOR.real[i][j]);
            }
        }

        // ASSIGN THE NEW PIXELS TO THE BITMAP
        bmp.setPixels(argb_out, 0, new_dimension, 0, 0, new_dimension, new_dimension);
        return bmp;
    }
}
