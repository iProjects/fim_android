package com.tech.nyax.myapplication10;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.ImageReader;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;

import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.Manifest;

import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;

import android.widget.Toast;

public class camera2_activity extends AppCompatActivity {

    private final static String TAG = camera2_activity.class.getSimpleName();
    //a variable to store a reference to the Image View at the main.xml file
    private ImageView img_photo;
    //a variable to store a reference to the Surface View at the main.xml file
    private SurfaceView sv;
    //a bitmap to display the captured image
    private Bitmap bmp;
    //Camera variables
    //a surface holder
    private SurfaceHolder sHolder;
    //a variable to control the camera
    private Camera mCamera;
    //the camera parameters
    private Parameters parameters;
    Button btntake_photo, btnbrowse_image;
    public final int REQUEST_SELECT_PICTURE = 0x01;
    public final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static String TEMP_PHOTO_PREFIX_FILE_NAME = "photo_";
    Uri img_photoCaptureUri;
    File mFileTemp;
    // Unique request code
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 3;
    // Unique request code
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 4;
    // Unique request code
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 5;
    // Unique request code
    private static final int SELECT_PICTURE_REQUEST_CODE = 6;
    // Unique request code
    private static final int TAKE_PICTURE_REQUEST_CODE = 7;
    String mCurrentPhotoPath;
    Uri mImageCaptureUri;
    // reusable string object
    private static StringBuilder _string_builder = new StringBuilder();
    private static final String COLON_SEPARATOR = ":";
    private static final String IMAGE = "image";

    /*** Camera2 API ***/
    // The MAX_PREVIEW_SIZE guaranteed by Camera2 API is 1920x1080
    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;

    // A CameraDevice represent one physical device's camera. In this attribute, we save the ID of the current CameraDevice
    private String mCameraId;
    // This is the view (TextureView) that we'll be using to "draw" the preview of the Camera
    private TextureView mTextureView;
    // The CameraCaptureSession for camera preview
    private CameraCaptureSession mCaptureSession;
    // A reference to the opened CameraDevice
    private CameraDevice mCameraDevice;
    // The Size of camera preview.
    private Size mPreviewSize;

    // An additional thread for running tasks that shouldn't block the UI
    private HandlerThread mBackgroundThread;
    // A Handler for running tasks in the background
    private Handler mBackgroundHandler;
    // An ImageReader that handles still image capture
    private ImageReader mImageReader;
    // CaptureRequest.Builder for the camera preview
    private CaptureRequest.Builder mPreviewRequestBuilder;
    // CaptureRequest generated by mPreviewRequestBuilder
    private CaptureRequest mPreviewRequest;
    // A Semaphore to prevent the app from exiting before closing the camera.
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    // Constant ID of the permission request
    private static final int REQUEST_CAMERA_PERMISSION = 8;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener;
    private CameraDevice.StateCallback mStateCallback;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera2_layout);
        Log.e(TAG, "camera2_activity onCreate");

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        mTextureView = findViewById(R.id.mTextureView);

        //get the Image View at the main.xml file
        img_photo = findViewById(R.id.img_photo);
        //get the Surface View at the main.xml file
        sv = findViewById(R.id.surfaceView);

        btntake_photo = findViewById(R.id.btntake_photo);
        btntake_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    _string_builder = new StringBuilder();
                    _string_builder.append("launching camera2...\n");

                    utilz.getInstance(getApplicationContext()).globalloghandler(_string_builder.toString(), TAG, 1, 1);

                    // openCamera();

                    // dispatchTakePictureIntent();

                    startBackgroundThread();
                    // When the screen is turned off and turned back on, the SurfaceTexture is already
                    // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
                    // a camera and start preview from here (otherwise, we wait until the surface is ready in
                    // the SurfaceTextureListener).
                    if (mTextureView.isAvailable()) {
                        openCamera(mTextureView.getWidth(), mTextureView.getHeight());
                    } else {
                        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
                    }

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        try {

            check_Runtime_Permissions();

    /* TextureView.SurfaceTextureListener handles several lifecycle events on a TextureView. In this case, we're
    listening to those events. When the SurfaceTexture is ready, we initialize the camera. When it size changes, we
    setup the preview coming from the camera accordingly */
            mSurfaceTextureListener = new TextureView.SurfaceTextureListener()

            {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
                    openCamera(width, height);
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
                    configureTransform(width, height);
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture texture) {
                }
            };

            // CameraDevice.StateCallback is called when CameraDevice changes its state
            mStateCallback = new CameraDevice.StateCallback()

            {
                @Override
                public void onOpened(@NonNull CameraDevice cameraDevice) {
// This method is called when the camera is opened. We start camera preview here.
                    mCameraOpenCloseLock.release();
                    mCameraDevice = cameraDevice;
                    createCameraPreviewSession();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                    mCameraOpenCloseLock.release();
                    cameraDevice.close();
                    mCameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice cameraDevice, int error) {
                    mCameraOpenCloseLock.release();
                    cameraDevice.close();
                    mCameraDevice = null;
                    finish();
                }
            };

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
// When the screen is turned off and turned back on, the SurfaceTexture is already
// available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
// a camera and start preview from here (otherwise, we wait until the surface is ready in
// the SurfaceTextureListener).
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    void check_Runtime_Permissions() {
        try {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("WRITE_EXTERNAL_STORAGE PERMISSION GRANTED", TAG, 1, 1);
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("READ_EXTERNAL_STORAGE PERMISSION GRANTED", TAG, 1, 1);
            }

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);

            } else {
                //Permission granted
                utilz.getInstance(getApplicationContext()).globalloghandler("CAMERA PERMISSION GRANTED", TAG, 1, 1);
            }

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:

                for (int i = 0; i < grantResults.length; i++) {
                    //perms.put(permissions[i], grantResults[i]);
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] granted.", TAG, 1, 1);
                    } else {
                        // permissions not granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] not granted.", TAG, 1, 0);
                    }
                }

                break;
            case READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:

                for (int i = 0; i < grantResults.length; i++) {
                    //perms.put(permissions[i], grantResults[i]);
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] granted.", TAG, 1, 1);
                    } else {
                        // permissions not granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] not granted.", TAG, 1, 0);
                    }
                }

                break;
            case CAMERA_PERMISSION_REQUEST_CODE:

                for (int i = 0; i < grantResults.length; i++) {
                    //perms.put(permissions[i], grantResults[i]);
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] granted.", TAG, 1, 1);
                    } else {
                        // permissions not granted.
                        utilz.getInstance(getApplicationContext()).globalloghandler("permission [ " + permissions[i] + " ] not granted.", TAG, 1, 0);
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // By convention RESULT_OK means that whatever was done executed successfully 
        //check if the requestCode matches the one we used.
        Bitmap bitmap;
        if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // image selection successful
                utilz.getInstance(getApplicationContext()).globalloghandler("image selection successful.", TAG, 1, 1);
                // Get the result from the returned Intent
                try {
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
                        Drawable drawable = new BitmapDrawable(bitmapScaled);
                        img_photo.setImageDrawable(drawable);
                        img_photo.setVisibility(View.VISIBLE);
                    } catch (Exception ex) {
                        utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                    }
                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            } else if (resultCode == RESULT_CANCELED) {
                // image selection failure
                utilz.getInstance(getApplicationContext()).globalloghandler("image selection failure.", TAG, 1, 0);
            }

        } else if (requestCode == TAKE_PICTURE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // camera take photo successful
                utilz.getInstance(getApplicationContext()).globalloghandler("camera take photo successful.", TAG, 1, 1);
                try {
                    Bitmap bitmappicture = MediaStore.Images.Media.getBitmap(getContentResolver(),
                            img_photoCaptureUri);
                    img_photo.setImageBitmap(bitmappicture);
                    img_photo.setVisibility(View.VISIBLE);

                    handleBigCameraPhoto();

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            } else if (resultCode == RESULT_CANCELED) {
                // camera take photo failure
                utilz.getInstance(getApplicationContext()).globalloghandler("camera take photo failure.", TAG, 1, 0);
            }


        }
    }

    private void handleBigCameraPhoto() {
        try {
            if (mCurrentPhotoPath != null) {
                setPic();
                galleryAddPic();
            }
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    private void setPic() {
        try {
            /* There isn't enough memory to open up more than a couple camera photos */
            /* So pre-scale the target bitmap into which the file is decoded */
            /* Get the size of the ImageView */
            int targetW = img_photo.getWidth();
            int targetH = img_photo.getHeight();
            /* Get the size of the image */
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            /* Figure out which way needs to be reduced less */
            int scaleFactor = 2;
            if ((targetW > 0) && (targetH > 0)) {
                scaleFactor = Math.max(photoW / targetW, photoH / targetH);
            }
            /* Set bitmap options to scale the image decode target */
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Matrix matrix = new Matrix();
            matrix.postRotate(getRotation());
            /* Decode the JPEG file into a Bitmap */
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                    false);
            /* Associate the Bitmap to the ImageView */
            img_photo.setImageBitmap(bitmap);
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    private float getRotation() {
        try {
            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90f;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180f;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270f;
                default:
                    return 0f;
            }
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            Log.e(TAG, "getRotation", ex);
            return 0f;
        }
    }

    private void galleryAddPic() {
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public void initTempFile() {
        try {

            initTempFile();

            String state = Environment.getExternalStorageState();

            utilz.getInstance(getApplicationContext()).globalloghandler("state [ " + state + " ]", TAG, 1, 1);

            if (Environment.MEDIA_MOUNTED.equals(state)) {

                mFileTemp = new File(Environment.getExternalStorageDirectory() + File.separator
                        + getResources().getString(R.string.app_foldername) + File.separator
                        + getResources().getString(R.string.app_photos)
                        , TEMP_PHOTO_PREFIX_FILE_NAME
                        + System.currentTimeMillis() + ".jpg");
                mFileTemp.getParentFile().mkdirs();

                utilz.getInstance(getApplicationContext()).globalloghandler("mFileTemp [ " + mFileTemp.toString() + " ]", TAG, 1, 1);

            } else {

                mFileTemp = new File(getFilesDir() + File.separator
                        + getResources().getString(R.string.app_foldername)
                        + File.separator + getResources().getString(R.string.app_pictures)
                        , TEMP_PHOTO_PREFIX_FILE_NAME + System.currentTimeMillis() + ".jpg");
                mFileTemp.getParentFile().mkdirs();

                utilz.getInstance(getApplicationContext()).globalloghandler("mFileTemp [ " + mFileTemp.toString() + " ]", TAG, 1, 1);

            }
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public void openCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();

            utilz.getInstance(getApplicationContext()).globalloghandler("state [ " + state.toString() + " ]", TAG, 1, 1);

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else {
                // mImageCaptureUri = utilz.getInstance(getApplicationContext()).Photos_internal_storage();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);

            utilz.getInstance(getApplicationContext()).globalloghandler("mImageCaptureUri [ " + mImageCaptureUri.toString() + " ]", TAG, 1, 1);

            startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE);

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public void openGallery() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                utilz.getInstance(getApplicationContext()).globalloghandler("READ_EXTERNAL_STORAGE permission required.", TAG, 1, 0);

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            } else {

                utilz.getInstance(getApplicationContext()).globalloghandler("READ_EXTERNAL_STORAGE permission granted.", TAG, 1, 1);

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), SELECT_PICTURE_REQUEST_CODE);
            }
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST_CODE);
                }
            } else {

            }
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    private File createImageFile() throws IOException {
        try {
            File image = null;
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = TEMP_PHOTO_PREFIX_FILE_NAME + "_" + timeStamp + "_";
            File storageDir = getAlbumDir();
            if (storageDir != null) {

                utilz.getInstance(getApplicationContext()).globalloghandler("storageDir [ " + storageDir.toString() + " ]", TAG, 1, 1);

                image = File.createTempFile(
                        imageFileName, /* prefix */
                        ".jpg", /* suffix */
                        storageDir /* directory */
                );
                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoPath = image.getAbsolutePath();

                utilz.getInstance(getApplicationContext()).globalloghandler("[ " + mCurrentPhotoPath.toString() + " ]", TAG, 1, 1);

            }
            return image;
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return null;
        }
    }

    private File getAlbumDir() {
        try {
            File storageDir = null;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

                storageDir = new File(Environment.getExternalStorageDirectory()
                        + "/dcim/"
                        + "photos");
                if (!storageDir.mkdirs()) {

                    if (!storageDir.exists()) {

                        utilz.getInstance(getApplicationContext()).globalloghandler("failed to create directory", TAG, 1, 0);

                        Log.e(TAG, "failed to create directory");
                        return null;
                    }
                }
            } else {

                utilz.getInstance(getApplicationContext()).globalloghandler("External storage is not mounted READ/WRITE.", TAG, 1, 0);

                Log.e(TAG, "External storage is not mounted READ/WRITE.");
            }
            return storageDir;
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return null;
        }
    }

    @Nullable
    public Bitmap getBitmap(@NonNull Uri bitmapUri, int maxDimen) throws FileNotFoundException {
        InputStream is = getApplicationContext().getContentResolver().openInputStream(bitmapUri);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, getBitmapOptions(bitmapUri, maxDimen));
        int imgRotation = getImageRotationDegrees(bitmapUri);
        int endRotation = (imgRotation < 0) ? -imgRotation : imgRotation;
        endRotation %= 360;
        endRotation = 90 * (endRotation / 90);
        if (endRotation > 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(endRotation);
            Bitmap tmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m,
                    true);
            if (tmp != null) {
                bitmap.recycle();
                bitmap = tmp;
            }
        }
        return bitmap;
    }

    private BitmapFactory.Options getBitmapOptions(Uri uri, int imageMaxDimen) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (imageMaxDimen > 0) {
            options.inJustDecodeBounds = true;
            //decodeImage(null, uri, options);
            options.inSampleSize = calculateScaleFactor(options, imageMaxDimen);
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //addInBitmapOptions(options);
        }
        return options;
    }

    private int calculateScaleFactor(@NonNull BitmapFactory.Options bitmapOptionsMeasureOnly, int
            imageMaxDimen) {
        int inSampleSize = 1;
        if (bitmapOptionsMeasureOnly.outHeight > imageMaxDimen || bitmapOptionsMeasureOnly.outWidth >
                imageMaxDimen) {
            final int halfHeight = bitmapOptionsMeasureOnly.outHeight / 2;
            final int halfWidth = bitmapOptionsMeasureOnly.outWidth / 2;
            while ((halfHeight / inSampleSize) > imageMaxDimen && (halfWidth / inSampleSize) >
                    imageMaxDimen) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public int getImageRotationDegrees(@NonNull Uri imgUri) {
        int photoRotation = ExifInterface.ORIENTATION_UNDEFINED;
        try {
            boolean hasRotation = false;
//If image comes from the gallery and is not in the folder DCIM (Scheme: content://)
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor cursor = getApplicationContext().getContentResolver().query(imgUri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.getColumnCount() > 0 && cursor.moveToFirst()) {
                    photoRotation = cursor.getInt(cursor.getColumnIndex(projection[0]));
                    hasRotation = photoRotation != 0;
                    Log.e(TAG, "Cursor orientation: " + photoRotation);
                }
                cursor.close();
            }
//If image comes from the camera (Scheme: file://) or is from the folder DCIM (Scheme:
            content:
//)
            if (!hasRotation) {
                ExifInterface exif = new ExifInterface(getAbsolutePath(imgUri));
                int exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                switch (exifRotation) {
                    case ExifInterface.ORIENTATION_ROTATE_90: {
                        photoRotation = 90;
                        break;
                    }
                    case ExifInterface.ORIENTATION_ROTATE_180: {
                        photoRotation = 180;
                        break;
                    }
                    case ExifInterface.ORIENTATION_ROTATE_270: {
                        photoRotation = 270;
                        break;
                    }
                }
                Log.e(TAG, "Exif orientation: " + photoRotation);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error determining rotation for image" + imgUri, e);
        }
        return photoRotation;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getAbsolutePath(Uri uri) {
//Code snippet edited from: http://stackoverflow.com/a/20559418/2235133
        String filePath = uri.getPath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
// Will return "image:x*"
            String[] wholeID = TextUtils.split(DocumentsContract.getDocumentId(uri), COLON_SEPARATOR);
// Split at colon, use second item in the array
            String type = wholeID[0];
            if (IMAGE.equalsIgnoreCase(type)) {//If it not type image, it means it comes from a remote location, like Google Photos
                String id = wholeID[1];
                String[] column = {MediaStore.Images.Media.DATA};
// where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = getApplicationContext().getContentResolver().
                        query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                column, sel, new String[]{id}, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex);
                    }
                    cursor.close();
                }
                Log.e(TAG, "Fetched absolute path for uri" + uri);
            }
        }
        return filePath;
    }

    /***Camera2 related methods*/
// Those are methods that uses the Camera2 APIs
    private void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
            return;
        }
        setUpCameraOutputs(width, height);
        configureTransform(width, height);
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    // Closes the current camera
    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /* Sets up member variables related to camera */
    private void setUpCameraOutputs(int width, int height) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);
// We don't use a front facing camera in this sample.
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }
// For still image captures, we use the largest available size.
                Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(
                        null, mBackgroundHandler);
                Point displaySize = new Point();
                getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;
                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }
                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }
// Danger! Attempting to use too large a preview size could exceed the camera
// bus' bandwidth limitation, resulting in gorgeous previews but the storage of
// garbage capture data.
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight, largest);
                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
// Currently an NPE is thrown when the Camera2API is used but not supported on the
// device this code runs.
            Toast.makeText(camera2_activity.this, "Camera2 API not supported on this device",
                    Toast.LENGTH_LONG).show();
        }
    }

    // Creates a new CameraCaptureSession for camera preview
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
// We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
// This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);
// We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder
                    = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);
// Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
// The camera is already closed
                            if (null == mCameraDevice) return;
// When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;
                            try {
// Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
// Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        null, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            showToast("Failed");
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Permissions related methods For Android API 23+
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(camera2_activity.this)
                    .setMessage("R string request permission")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(camera2_activity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA_PERMISSION);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .create();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    // Background thread / handler methods
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera2BackgroundThread");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utility methods
     * Given choices of Sizes supported by a camera, choose the smallest one that is at least at large as the respective
     * texture view size, and that is as most as large as the respective max size, and whose aspect ratio matches with the
     * specified value. If doesn't exist, choose the largest one that is at most as large as the respective max size, and
     * whose aspect ratio matches with the specified value
     */
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size
                                                  aspectRatio) {
// Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
// Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }
// Pick the smallest of those big enough. If there is no one big enough, pick the
// largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    // This method congfigures the neccesary Matrix transformation to mTextureView
    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mTextureView || null == mPreviewSize) {
            return;
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    // This method compares two Sizes based on their areas.
    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
// We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    /**
     * Shows a {@link Toast} on the UI thread.
     *
     * @param text The message to show
     */
    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(camera2_activity.this, text, Toast.LENGTH_SHORT).show();
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.home_menu:

                    utilz.getInstance(getApplicationContext()).globalloghandler("launching MainActivity...", TAG, 1, 1);

                    final Intent _MainActivity = new Intent(this, MainActivity.class);
                    startActivity(_MainActivity);
                    return true;
                default:
                    break;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            return false;
        }
    }


}

