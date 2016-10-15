package yambritton.hackrice.todays_track;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

public class testing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        final Button videobutton = (Button) findViewById(R.id.videobutton);

        videobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  select video
                pickMedia((VideoView) findViewById(R.id.videoView));

            }
        });

        final Button audiobutton = (Button) findViewById(R.id.audiobutton);

        audiobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        final Button outputbutton = (Button) findViewById(R.id.outputbutton);

        outputbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // generate output
            }
        });
    }

    public void pickMedia(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent , 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Log.i("data", data.getData().toString());
                VideoView video = (VideoView) findViewById(R.id.videoView);
                video.setVideoURI(data.getData());
                video.start();
                // Do something with the contact here (bigger example below)
            }
        }
    }

    public static void splitAudio(String audio, int time, Context context)
    {
        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        String[] cmd = String.format("-ss 10 -t 6 -i %s output.mp3", audio).split(" ");
        try {
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {}

                @Override
                public void onFailure(String message) {}

                @Override
                public void onSuccess(String message) {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException f)
        {
            Log.d("FAILURE", "audio trim fuckup");
        }
    }

    public static void mergeAudio(String video, String audio, Context context)
    {
        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        String[] cmd = String.format("-i %s -i %s -c:v copy -c:a copy output.mp4", video, audio).split(" ");
        try {
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {}

                @Override
                public void onFailure(String message) {}

                @Override
                public void onSuccess(String message) {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException f)
        {
            Log.d("FAILURE", "audio video merge fuckup");
        }
    }
}
