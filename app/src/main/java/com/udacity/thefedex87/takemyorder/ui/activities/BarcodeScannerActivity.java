package com.udacity.thefedex87.takemyorder.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.google.android.gms.vision.barcode.Barcode.QR_CODE;

public class BarcodeScannerActivity extends AppCompatActivity {
    private final int CAMERA_PERMISSION_REQUEST = 1;

    public static final int RC_PHOTO_PICKER = 2;

    @BindView(R.id.camera_preview)
    SurfaceView cameraPreview;

    @BindView(R.id.open_camera)
    ImageView openCamera;

    @BindView(R.id.open_barcode_image)
    Button openBarcodeImage;

    @Inject
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        TakeMyOrderApplication.appComponent().inject(this);

        ButterKnife.bind(this);

        openCamera();

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        openBarcodeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*").putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
    }

    private void openCamera(){
        if (ContextCompat.checkSelfPermission(BarcodeScannerActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(BarcodeScannerActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            createCameraSource();
        }
    }

    private void createCameraSource(){
        cameraPreview.setVisibility(View.VISIBLE);
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {

            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                try{
                    cameraSource.start(cameraPreview.getHolder());
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() > 0){
                    Intent intent = new Intent();
                    intent.putExtra("barcode", barcodes.valueAt(0));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Here call or Open your camera;
                    createCameraSource();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            //Create the barcode reader
            BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).setBarcodeFormats(QR_CODE).build();
            Bitmap bitmap = null;

            //Requested selection of the picture from file chooser
            Uri selectedImageUri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Timber.e("Error parsing Bitmap " + e.getMessage());
                Toast.makeText(this, getString(R.string.error_loading_bitmap), Toast.LENGTH_LONG).show();
                return;
            }

            //Detect the barcode from Bitmap
            Frame myFrame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Barcode> barcodes = barcodeDetector.detect(myFrame);
            if (barcodes.size() == 0){
                //Barcode not found on picture
                Timber.w(getString(R.string.error_no_barcode_found));
                Toast.makeText(this, getString(R.string.error_no_barcode_found), Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent();
                intent.putExtra("barcode", barcodes.valueAt(0));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
