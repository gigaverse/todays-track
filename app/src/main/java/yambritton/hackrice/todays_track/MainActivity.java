package yambritton.hackrice.todays_track;

import android.content.Intent;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is where I put the test buttons
        final Button videobutton = (Button) findViewById(R.id.videobutton);

        videobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  select video

            }
        });

        final Button audiobutton = (Button) findViewById(R.id.audiobutton);

        audiobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // select audio
            }
        });

        final Button outputbutton = (Button) findViewById(R.id.outputbutton);

        outputbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // generate output
            }
        });


    }
    //TODO -- DETERMINE IF THIS SHOULD REALLY BE VOID
    protected void mergeAudio(String video, String audio)
    {
        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        String[] cmd = {String.format("ffmpeg -i %s -i %s -c:v copy -c:a copy output.mp4", video, audio)};
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
            Log.d("FAILURE", "Already been done son");
        }
    }




}
