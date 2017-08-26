package com.example.nikhil.qrscanner;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

import static com.example.nikhil.qrscanner.R.id.barcode;

public class MainActivity extends AppCompatActivity implements BarcodeRetriever{

    // use a compound button so either checkbox or switch widgets work.

    String savemessage;
    private static final String TAG = "BarcodeMain";

    CheckBox fromXMl;
    SwitchCompat drawRect, autoFocus, supportMultiple, touchBack, drawText, flash, frontCam;

    BarcodeCapture barcodeCapture;

    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "Saved QR Codes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(barcode);
        barcodeCapture.setRetrieval(this);

        fromXMl = (CheckBox) findViewById(R.id.from_xml);
        drawRect = (SwitchCompat) findViewById(R.id.draw_rect);
        autoFocus = (SwitchCompat) findViewById(R.id.focus);
        supportMultiple = (SwitchCompat) findViewById(R.id.support_multiple);
        touchBack = (SwitchCompat) findViewById(R.id.touch_callback);
        drawText = (SwitchCompat) findViewById(R.id.draw_text);
        flash = (SwitchCompat) findViewById(R.id.on_flash);
        frontCam = (SwitchCompat) findViewById(R.id.front_cam);

        File dir = new File(path);
        dir.mkdirs();




        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeCapture.stopScanning();
            }
        });

        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromXMl.isChecked()) {

                } else {


                    barcodeCapture.setShowDrawRect(drawRect.isChecked())
                            .setSupportMultipleScan(supportMultiple.isChecked())
                            .setTouchAsCallback(touchBack.isChecked())
                            .shouldAutoFocus(autoFocus.isChecked())
                            .setShowFlash(flash.isChecked())
                            .setBarcodeFormat(Barcode.ALL_FORMATS)
                            .setCameraFacing(frontCam.isChecked() ? CameraSource.CAMERA_FACING_FRONT : CameraSource.CAMERA_FACING_BACK)
                            .setShouldShowText(drawText.isChecked());
                    barcodeCapture.refresh();
                }
            }
        });

    }


    /*public void SaveM(String str)
    {

        File file = new File (path + "/QRCodes.txt");
        String[] saveText = String.valueOf(str).split(System.getProperty(System.lineSeparator()));

        Save (file, saveText);

        Toast.makeText(getApplicationContext(), "Saved",Toast.LENGTH_SHORT).show();
    }*/


   /* public static void Save(File file, String[] data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    if (i < data.length-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }*/


    @Override
    public void onRetrieved(final Barcode barcode) {
        Log.d(TAG, "Barcode read: " + barcode.displayValue);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("code retrieved")
                        .setMessage(barcode.displayValue);
                builder.show();
            }
        });
        barcodeCapture.stopScanning();


    }




    @Override
    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = "Code selected : " + closetToClick.displayValue + "\n\nother " +
                        "codes in frame include : \n";
                for (int index = 0; index < barcodeGraphics.size(); index++) {
                    Barcode barcode = barcodeGraphics.get(index).getBarcode();
                    message += (index + 1) + ". " + barcode.displayValue + "\n";
                    /*savemessage = barcode.displayValue + "\n";

                    System.out.println(savemessage);*/
                }
               /* SaveM(savemessage);*/

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("code retrieved")
                        .setMessage(message);
                builder.show();

            }
        });

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        for (int i = 0; i < sparseArray.size(); i++) {
            Barcode barcode = sparseArray.valueAt(i);
            Log.e("value", barcode.displayValue);
        }

    }

    @Override
    public void onRetrievedFailed(String reason) {

    }

    @Override
    public void onPermissionRequestDenied() {

    }


}
