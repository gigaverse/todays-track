package yambritton.hackrice.todays_track;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;


public class volume extends AppCompatActivity {
    private VideoView video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);
        video  = (VideoView) findViewById(R.id.videoView4);
        if(getIntent().getStringExtra("VIDEO")!=null) {
            Log.d("videoUri","this: "+getIntent().getStringExtra("VIDEO"));
            Uri uri = Uri.parse(getIntent().getStringExtra("VIDEO"));
            video.setVideoURI(uri);
            video.start();
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
        }
        else
            Log.e("intent","trim intent missing!");
    }
    public void back(View view){
        video.stopPlayback();
        finish();
        //TODO tell user this will finalize the audio into the video
    }
}
