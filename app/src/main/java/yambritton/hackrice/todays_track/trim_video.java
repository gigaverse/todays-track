package yambritton.hackrice.todays_track;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import static yambritton.hackrice.todays_track.R.id.videoView;
import static yambritton.hackrice.todays_track.R.id.videoView2;

public class trim_video extends AppCompatActivity {
    private VideoView video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trim_video);

        View decorView = getWindow().getDecorView();//fullscreen and pretty
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        video  = (VideoView) findViewById(videoView2);
        if(getIntent().getStringExtra("VIDEO")!=null) {
            Log.d("videoUri","this: "+getIntent().getStringExtra("VIDEO").toString());
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
        Log.d("trimV","retuning");
        video.stopPlayback();
        finish();
    }
}
