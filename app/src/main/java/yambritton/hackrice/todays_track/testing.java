package yambritton.hackrice.todays_track;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

public class testing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Button videobutton = (Button) findViewById(R.id.videobutton);

        videobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  select video

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
