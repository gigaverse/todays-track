package yambritton.hackrice.todays_track;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import static yambritton.hackrice.todays_track.R.id.videoView;

public class MainActivity extends AppCompatActivity {
    private VideoView video;
    private String[] mToolTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Uri videoUri;
    private Uri audioUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //loops video player
        video  = (VideoView) findViewById(videoView);
        video.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.bg);
        //video.start();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                video.start();
            }
        });

        //populating the tool drawer
        mToolTitles = getResources().getStringArray(R.array.tools);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mToolTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        //mDrawerLayout.openDrawer(mDrawerList);


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    public void pickMedia(View view){
        mDrawerLayout.openDrawer(mDrawerList);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        // Check which request we're responding to
        if (requestCode == 0) {
            // Make sure the request for video was successful
            if (resultCode == RESULT_OK) {
                Log.d("data", data.getData().toString());
                VideoView video = (VideoView) findViewById(videoView);
                video.setVideoURI(data.getData());
                videoUri=data.getData();
                video.start();
            }
        }
        if (requestCode == 1) {
            // Make sure the request for audio was successful
            if (resultCode == RESULT_OK) {
                Log.d("data", data.getData().toString());
                audioUri=data.getData();
                //TODO play new audio
            }
        }
        if(requestCode==2){//returning from trim video
            //TODO replace videoUri with new one
            video.start();
        }
        if(requestCode==3){//returning from trim audio
            //TODO replace audioUri with new one
            video.start();
        }
        if(requestCode==4){//returning from trim audio
            //TODO apply filter to the video
            video.start();
        }
        if(requestCode==5){//returning from volume adjustment
            video.start();
            //TODO replace with new video- Audio will be hardcoded at this point
        }
    }
    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerList.clearChoices();
        Intent intent;
        switch(position){
            case 0://choose video
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , 0);
                break;
            case 1://choose audio
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , 1);
                break;
            case 2://trim video
                if(videoUri!=null) {
                    intent = new Intent(this, trim_video.class);
                    intent.putExtra("VIDEO", videoUri.toString());
                    Log.d("videoUri","this: "+videoUri.toString());
                    startActivityForResult(intent,2);
                }
                else
                    Toast.makeText(this, "Select a video first!", Toast.LENGTH_SHORT).show();
                break;
            case 3://trim audio
                if(audioUri!=null) {
                    intent = new Intent(this, TrimAudio.class);
                    intent.putExtra("AUDIO", videoUri.toString());
                    video.stopPlayback();
                    startActivityForResult(intent, 4);
                }
                else
                    Toast.makeText(this, "Select a song first!", Toast.LENGTH_SHORT).show();
                break;
            case 4://now playing filter
                if(videoUri!=null) {
                    intent = new Intent(this, Filter.class);//gotta change this
                    intent.putExtra("VIDEO", videoUri.toString());
                    Log.d("videoUri","this: "+videoUri.toString());
                    video.stopPlayback();
                    startActivityForResult(intent, 3);
                }
                else
                    Toast.makeText(this, "Select a video first!", Toast.LENGTH_SHORT).show();
                break;
            case 5://adjust volume
                if(videoUri!=null) {
                    intent = new Intent(this, volume.class);
                    intent.putExtra("VIDEO", videoUri.toString());
                    Log.d("videoUri","this: "+videoUri.toString());
                    startActivityForResult(intent, 5);
                }
                else
                    Toast.makeText(this, "Select a video first!", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("touch","listened");
            selectItem(position);
        }
    }
}
