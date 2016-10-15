package yambritton.hackrice.todays_track;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class testing extends AppCompatActivity {

    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
    String videoURI = "", audioURI = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        Log.d("inputstring", Environment.getExternalStorageDirectory().getAbsolutePath());
        FFmpegInit();

        //GETTING PERMISSION FOR EVERYTHING
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        final Button videobutton = (Button) findViewById(R.id.videobutton);

        videobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  select video
                pickVideo();

            }
        });

        final Button audiobutton = (Button) findViewById(R.id.audiobutton);

        audiobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pickMedia();
            }
        });

        final Button outputbutton = (Button) findViewById(R.id.outputbutton);
        outputbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // generate output
                try {
                    mergeAudio(videoURI, audioURI);
                }
                catch (Exception e)
                {
                    Log.d("inputstring", e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    public void pickVideo(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent , 1);
    }

    public void pickMedia(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent , 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                videoURI = getPath(data.getData()).replaceAll("[ ]+", "\\ ");
                Log.i("inputstring", videoURI);
                VideoView video = (VideoView) findViewById(R.id.videoView);
                video.setVideoURI(data.getData());
                video.start();
                // Do something with the contact here (bigger example below)
            }
        }
        if (requestCode == 2) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                audioURI = getPath(data.getData()).replaceAll("[ ]+", "\\ ");
                Log.i("inputstring", audioURI);
                MediaPlayer mp = MediaPlayer.create(this, data.getData());
                mp.start();
                // Do something with the contact here (bigger example below)
            }
        }
    }
    //This will take an absolute path to an audio file, and trim it at time pos for length time
    protected void splitAudio(String audio, double pos, double time)
    {
        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        String[] cmd = String.format(Locale.ENGLISH, "-y -ss %.2f -t %.2f -i %s output.mp3",pos, time, audio).split(" ");
        try {
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.d("inputstring", "START MUSIC TRIMMER");
                }

                @Override
                public void onProgress(String message) {
                    Log.d("inputstring", message);
                }

                @Override
                public void onFailure(String message) {
                    Log.d("inputstring", "MUSIC TRIMMER FAIL:" + message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.d("inputstring", "MUSIC TRIMMER SUCCESS:" + message);
                }

                @Override
                public void onFinish() {
                    Log.d("inputstring", "END MUSIC TRIMMER");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException f)
        {
            Log.d("FAILURE", "audio trim fuckup");
        }
    }
    //This will merge the video and audio at the paths provided as parameters
    // -- REMEMBER TO CUT AUDIO LENGTH DOWN TO VIDEO LENGTH --
    protected void mergeAudio(String video, String audio) throws IOException
    {
        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        String[] cmd = "-y -i %s -i %s -c:v copy -c:a copy /storage/emulated/0/output.mp4".split(" ");
        String content = "hello world";
        File file;
        FileOutputStream outputStream;
        try {
            file = new File(Environment.getExternalStorageDirectory(), "output.mp4");
            Log.d("inputstring", file.getAbsolutePath());
            outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            Log.d("inputstring", "DAMNIT!" + e.toString());
            e.printStackTrace();
        }
        cmd[2] = video;
        cmd[4] = audio;
        Log.d("inputstring", Arrays.toString(cmd));
        try {
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.d("inputstring", "START");
                }

                @Override
                public void onProgress(String message) {
                    Log.d("inputstring", message);
                }

                @Override
                public void onFailure(String message) {
                    Log.d("inputstring", "VIDEO MIXER FAILURE: " + message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.d("inputstring", "VIDEO MIXER SUCCESS: " + message);
                }

                @Override
                public void onFinish() {
                    Log.d("inputstring", "close");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException f)
        {
            Log.d("FAILURE", "audio video merge fuckup");
        }
    }

    protected void FFmpegInit()
    {
        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
23
    }

    public String getPath(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null); //Since manageQuery is deprecated
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
