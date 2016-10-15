package yambritton.hackrice.todays_track;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //loops video player
        video  = (VideoView) findViewById(videoView);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
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

        mDrawerLayout.openDrawer(mDrawerList);

    }
    public void pickMedia(View view){
        mDrawerLayout.openDrawer(mDrawerList);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Log.d("data", data.getData().toString());
                VideoView video = (VideoView) findViewById(videoView);
                video.setVideoURI(data.getData());
                videoUri=data.getData();
                video.start();
                // Do something with the contact here (bigger example below)
            }
        }
    }
    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
        Intent intent;
        switch(position){
            case 0:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , 1);
                break;
            case 1:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent , 2);
                break;
            case 2:
                if(videoUri!=null) {
                    intent = new Intent(this, trim_video.class);
                    intent.putExtra("VIDEO", videoUri.toString());
                    Log.d("videoUri","this: "+videoUri.toString());
                    startActivityForResult(intent, 3);
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
