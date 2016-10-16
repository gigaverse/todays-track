package yambritton.hackrice.todays_track;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Filter extends AppCompatActivity {
    private VideoView video;
    boolean bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottom = true;
        setContentView(R.layout.activity_filter);
        View decorView = getWindow().getDecorView();//fullscreen and pretty
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);




        video  = (VideoView) findViewById(R.id.videoView5);
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
                Log.e("intent","filter intent missing!");
    }
    public void back(View view){
        video.stopPlayback();
        finish();
    }
    public void change(View view){
        ImageView art = (ImageView) findViewById(R.id.imageView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        if(bottom)
        params.gravity = Gravity.TOP;
        else
            params.gravity = Gravity.BOTTOM;
        bottom=!bottom;
        params.height=200;
        params.width=440;
        art.setLayoutParams(params);
    }
}
