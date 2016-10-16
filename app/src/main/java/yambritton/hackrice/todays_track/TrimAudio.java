package yambritton.hackrice.todays_track;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import static yambritton.hackrice.todays_track.R.id.videoView2;

public class TrimAudio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trim_audio);
        //video  = (VideoView) findViewById(videoView2);TODO: Find equivalent for audio
        if(getIntent().getStringExtra("AUDIO")!=null) {
            Log.d("videoUri","this: "+getIntent().getStringExtra("AUDIO"));
            Uri uri = Uri.parse(getIntent().getStringExtra("AUDIO"));
            /*video.setVideoURI(uri);
            video.start();
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {TODO: again, set up for audio
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            */
            //TODO implement audio trimming
        }
        else
            Log.e("intent","trim intent missing!");

    }
    public void back(View view){
        finishActivity(4);
    }
}
