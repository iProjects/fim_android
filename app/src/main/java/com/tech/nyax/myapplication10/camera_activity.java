package com.tech.nyax.myapplication10;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;

import android.net.Uri;

import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.Manifest;

public class camera_activity extends AppCompatActivity implements SurfaceHolder.Callback {

    private final static String TAG = camera_activity.class.getSimpleName();
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
    Button btntake_photo, btnbrowse_image, btntake_photo_camera2;
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

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
        Log.i(TAG, "camera_activity onCreate");

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

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
                    _string_builder.append("launching camera...\n");

                    utilz.getInstance(getApplicationContext()).globalloghandler(_string_builder.toString(), TAG, 1, 1);

                    openCamera();

                    dispatchTakePictureIntent();

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btntake_photo_camera2 = findViewById(R.id.btntake_photo_camera2);
        btntake_photo_camera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    _string_builder = new StringBuilder();
                    _string_builder.append("launching camera2_activity...\n");

                    utilz.getInstance(getApplicationContext()).globalloghandler(_string_builder.toString(), TAG, 1, 1);

                    final Intent camera2_activity = new Intent(getApplicationContext(), camera2_activity.class);
                    startActivity(camera2_activity);

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btnbrowse_image = findViewById(R.id.btnbrowse_image);
        btnbrowse_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    _string_builder = new StringBuilder();
                    _string_builder.append("select photo...\n");

                    openGallery();

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        try {

            //Get a surface
            sHolder = sv.getHolder();
            //add the callback interface methods defined below as the Surface View callbacks
            sHolder.addCallback(this);
            //tells Android that this surface will have its data constantly replaced
            sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            check_Runtime_Permissions();

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
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
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        try {
            //get camera parameters
            parameters = mCamera.getParameters();
            //set camera parameters
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            //sets what code should be executed after the picture is taken
            Camera.PictureCallback mCall = new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    //decode the data obtained by the camera into a Bitmap
                    bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    String filename = Environment.getExternalStorageDirectory()
                            + File.separator + "testimage.jpg";
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(filename);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException ex) {
                            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                        }
                    }
                    //set the img_photo
                    img_photo.setImageBitmap(bmp);
                }
            };
            mCamera.takePicture(null, null, mCall);
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // The Surface has been created, acquire the camera and tell it where
            // to draw the preview.
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);
        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        } finally {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //stop the preview
        mCamera.stopPreview();
        //release the camera
        mCamera.release();
        //unbind the camera from this object
        mCamera = null;
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
                    Log.d(TAG, "Cursor orientation: " + photoRotation);
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
                Log.d(TAG, "Exif orientation: " + photoRotation);
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
                Log.d(TAG, "Fetched absolute path for uri" + uri);
            }
        }
        return filePath;
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



