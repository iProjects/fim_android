package com.tech.nyax.myapplication10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.Manifest;

public class audio_activity extends AppCompatActivity {

    private final static String TAG = audio_activity.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    Button btnplayaudio, btnplayvideo;
    VideoView _videoView;
    private MediaObserver observer = null;
    // Unique request code
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 3;
    // Unique request code
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 4;
    // Unique request code
    private static final int AUDIO_REQUEST_CODE = 5;
    // Unique request code
    private static final int VIDEO_REQUEST_CODE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_layout);

        getSupportActionBar().setTitle(utilz.getInstance(getApplicationContext()).formatspannablestring(getSupportActionBar().getTitle().toString()));

        _videoView = findViewById(R.id.videoView);

        final MediaPlayer _media_Player = MediaPlayer.create(this, R.raw.get_you_home);

        btnplayaudio = findViewById(R.id.btnplayaudio);
        btnplayaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent _external_intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(_external_intent, AUDIO_REQUEST_CODE);

                    Intent _internal_intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(_internal_intent, AUDIO_REQUEST_CODE);

                    play_audio(_media_Player);
                    audio_player();

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        btnplayvideo = findViewById(R.id.btnplayvideo);
        btnplayvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, VIDEO_REQUEST_CODE);

                    // String _pathToVideo = "";
                    // _videoView.setVideoPath(_pathToVideo);
                    // //Start playing video.
                    // _videoView.start();

                    play_video(_videoView);

                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        });

        try {

            check_Runtime_Permissions();

            /* This will return a list of all MP3 files. Use the list to display data. */
            getAllAudioFromDevice(this);

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    void transfer_audio_to_global_location() {
        try {

            // Generate the URI for the file
            // To share the file we must provide an identifier for the file. This is done by using a URI (Uniform Resource Identifier).
            // We assume the file we want to load is in the documents/ subdirectory
            // of the internal storage
            File documentsPath = new File(getApplicationContext().getFilesDir(), "ngoma");
            File file = new File(documentsPath, "ngoma.pdf");
            // This can also in one line of course:
            // File file = new File(Context.getFilesDir(), "documents/sample.pdf");
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.mydomain.fileprovider", file);

            File myFolder = getFilesDir();
            File myFile = new File(myFolder, "ngoma.bin");
            File _myFile = new File(getFilesDir(), "ngoma.bin");

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

                File _file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "ngoma.txt");

                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "ngoma");
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d(TAG, "failed to create directory");
                    }
                }

                // Access your app's directory in the device's Public documents directory
                File _docs = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "ngoma");
                // Make the directory if it does not yet exist
                _docs.mkdirs();

                File sd = Environment.getExternalStorageDirectory(); // getting phone SD card path
                String backupPath = sd.getAbsolutePath() + "ngoma"; // if you want to set backup in specific folder name
                /* be careful , foldername must initial like this : "/myFolder" . don't forget "/" at begin of folder name you could define foldername like this : "/myOutterFolder/MyInnerFolder" and so on ...
                 */
                File dir = new File(backupPath);
                if (!dir.exists()) // if there was no folder at this path , it create it .
                {
                    dir.mkdirs();
                }

                File directory_music = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)));

                File currentDB = null;
                File backupDB = null;

                currentDB = new File(String.valueOf(R.raw.get_you_home));
                backupDB = new File(directory_music, String.valueOf(R.raw.get_you_home));

                FileChannel source = null;
                FileChannel destination = null;

                try {
                    if (currentDB.exists() && !backupDB.exists()) {
                        source = new FileInputStream(currentDB).getChannel();
                        destination = new FileOutputStream(backupDB).getChannel();
                        destination.transferFrom(source, 0, source.size());
                        source.close();
                        destination.close();
                    }
                } catch (IOException ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }

            }

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public void audio_player() {
        try {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

//mediaPlayer.setDataSource(getResources().openRawResource(R.raw.the_vamps_personal_ft_maggie_lindeman).toString());

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    observer.stop();
                    //progress_bar.setProgress(mp.getCurrentPosition());
                    // TODO Auto-generated method stub
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });

            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    //progress_bar.setSecondaryProgress(percent);
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    btnplayaudio.setEnabled(true);
                }
            });

            observer = new MediaObserver();
            try {
                mediaPlayer.prepare();
            } catch (IOException ex) {
                utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
            }

            mediaPlayer.start();
            new Thread(observer).start();

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public void play_audio(MediaPlayer _media_Player) {
        try {

            if (_media_Player.isPlaying()) {
                _media_Player.reset();
                _media_Player = MediaPlayer.create(getApplicationContext(), R.raw.get_you_home);
            }
            //_media_Player.start();

            // Prepare asynchronously to not block the Main Thread
            _media_Player.prepareAsync();

            // Set callback for when prepareAsync() finishes
            _media_Player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
// Called when the MediaPlayer is ready to play
                    player.start();
                }
            });

            //_media_Player.start();
            _media_Player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
// ... react appropriately ...
// The MediaPlayer has moved to the Error state, must be reset!
// Then return true if the error has been handled
                    return true;
                }
            });

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public void play_video(final VideoView videoView) {
        try {

            //videoView.setVideoURI(Uri.parse("http://example.com/examplevideo.mp4"));
            videoView.requestFocus();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                }
            });
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    videoView.start();
                    mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            MediaController mediaController = new MediaController(getApplicationContext());
                            videoView.setMediaController(mediaController);
                            mediaController.setAnchorView(videoView);
                        }
                    });
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    return false;
                }
            });

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    void transfer_audio_to_global_dir() {
        try {

        } catch (Exception ex) {
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
        }
    }

    public List<AudioModel> getAllAudioFromDevice(final Context context) {
        final List<AudioModel> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.ArtistColumns.ARTIST
        };
        //read files of a specific folder
//        Cursor cs = context.getContentResolver().query(uri,
//                projection,
//                MediaStore.Audio.Media.DATA + " like ? ",
//                new String[]{"%utm%"},
//                null);
        //retrieve all files from device
        Cursor c = context.getContentResolver().query(uri,
                projection,
                null,
                null,
                null);
        if (c != null) {
            while (c.moveToNext()) {
                AudioModel audioModel = new AudioModel();
                String path = c.getString(0);
                String name = c.getString(1);
                String album = c.getString(2);
                String artist = c.getString(3);
                audioModel.setaName(name);
                audioModel.setaAlbum(album);
                audioModel.setaArtist(artist);
                audioModel.setaPath(path);
                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);
                tempAudioList.add(audioModel);
            }
            c.close();
        }
        return tempAudioList;
    }


    private class MediaObserver implements Runnable {
        private AtomicBoolean stop = new AtomicBoolean(false);

        public void stop() {
            stop.set(true);
        }

        @Override
        public void run() {
            while (!stop.get()) {
                // progress_bar.setProgress((int)((double)mediaPlayer.getCurrentPosition() / (double)mediaPlayer.getDuration()*100));
                try {
                    Thread.sleep(200);
                } catch (Exception ex) {
                    utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 0);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mediaPlayer.stop();
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
            utilz.getInstance(getApplicationContext()).globalloghandler(ex.toString(), TAG, 1, 1);
            return false;
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
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // By convention RESULT_OK means that whatever was done executed successfully
        if (resultCode == Activity.RESULT_OK) {
            //check if the requestCode matches the one we used.
            if (requestCode == AUDIO_REQUEST_CODE) {
                // Get the result from the returned Intent
                Uri selectedAudioUri = data.getData();
                String selectedAudioPath = getRealPathFromURI(selectedAudioUri, "Audio");

                utilz.getInstance(getApplicationContext()).globalloghandler("selectedAudioPath [ " + selectedAudioPath + " ]", TAG, 1, 1);

            } else if (requestCode == VIDEO_REQUEST_CODE) {
                Uri selectedVideoUri = data.getData();
                String selectedVideoPath = getRealPathFromURI(selectedVideoUri, "Video");

                utilz.getInstance(getApplicationContext()).globalloghandler("selectedVideoPath [ " + selectedVideoPath + " ]", TAG, 1, 1);

                _videoView.setVideoPath(selectedVideoPath);
                //Start playing video.
                _videoView.start();
                play_video(_videoView);
            }
        }
    }

    public String getRealPathFromURI(Uri uri, String media) {
        if (uri == null) {
            return null;
        }
        switch (media) {
            case "Audio":
                String[] audio_projection = {MediaStore.Audio.Media.DATA};
                Cursor audio_cursor = getContentResolver().query(uri, audio_projection, null, null,
                        null);
                if (audio_cursor != null) {
                    int column_index = audio_cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    audio_cursor.moveToFirst();
                    return audio_cursor.getString(column_index);
                }
                break;
            case "Video":
                String[] video_projection = {MediaStore.Video.Media.DATA};
                Cursor video_cursor = getContentResolver().query(uri, video_projection, null, null,
                        null);
                if (video_cursor != null) {
                    int column_index = video_cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                    video_cursor.moveToFirst();
                    return video_cursor.getString(column_index);
                }
                break;
        }

        return uri.getPath();
    }


}




















