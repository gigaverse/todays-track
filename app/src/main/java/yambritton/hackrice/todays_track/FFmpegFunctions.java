package yambritton.hackrice.todays_track;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by gigaverse on 10/15/16.
 */

class FFmpegFunctions {

    //INITIALIZE THE BINARY
    public static void FFmpegInit(Context context)
    {
        FFmpeg ffmpeg = FFmpeg.getInstance(context);
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
        }

        //CREATE THE LOCATION THAT ALL FILES WILL BE STORED
        File dir = new File("/storage/emulated/0/todaystrack");
        try{
            if(dir.mkdir()) {
                System.out.println("Directory created");
            } else {
                System.out.println("Directory is not created");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //This will merge the video and audio at the paths provided as parameters
    // -- REMEMBER TO CUT AUDIO LENGTH DOWN TO VIDEO LENGTH --
    public static String mergeAudio(String video, String audio, Context context) throws IOException {
        FFmpeg ffmpeg = FFmpeg.getInstance(context);

        String filename = getFileName(".mp4");

        //TOUCH A FILE SO FFmpeg CAN WRITE TO IT
        touch(filename);

        // FILE SETUP
        String[] cmd = ("-y -i %s -i %s -c:v copy -c:a copy /storage/emulated/0/todaystrack/" + filename).split(" ");
        cmd[2] = video;
        cmd[4] = audio;

        Log.d("inputstring", Arrays.toString(cmd));

        final File tempAudio = new File(audio);
        //ACTUALLY EXECUTE THE COMMAND
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
                    if(tempAudio.getAbsolutePath().contains("todaystrack"))
                    {
                        tempAudio.delete();
                    }
                    MainActivity.refresh();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException f) {
            Log.d("FAILURE", "audio video merge fuckup");
        }

        return filename;
    }

    //This will take an absolute path to an audio file, and trim it at time pos for length time
    public static String splitAudio(String audio, double pos, double time, Context context)
    {
        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        String filename = getFileName(".mp3");

        touch(filename);

        String[] cmd = String.format(Locale.ENGLISH, "-y -ss %.2f -t %.2f -i s /storage/emulated/0/todaystrack/" + filename,pos, time).split(" ");
        cmd[6] = audio;
        Log.d("inputstring", Arrays.toString(cmd));
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
            return "";
        }
        return filename;

    }


    //USE THIS GET PATH
    public static String getPath(Uri contentUri, Context context)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null); //Since manageQuery is deprecated
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index).replaceAll("[ ]+", "\\ ");
    }

    public static String getFileName(String extension)
    {
        //DETERMINE FILE NAME
        GregorianCalendar now = new GregorianCalendar();
        String filename = now.getTime().getTime() + extension;
        return filename;
    }

    public static boolean touch(String filename)
    {
        String content = "hello world";
        File file;
        FileOutputStream outputStream;

        try {
            file = new File("/storage/emulated/0/todaystrack/", filename);
            file.createNewFile();
            return true;
        } catch (IOException e) {
            Log.d("inputstring", "DAMNIT!" + e.toString());
            e.printStackTrace();
            return false;
        }
    }


}


    /*
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
                audioURI = FFmpegFunctions.getPath(data.getData(), this).replaceAll("[ ]+", "\\ ");
                Log.i("inputstring", audioURI);
                MediaPlayer mp = MediaPlayer.create(this, data.getData());
                mp.start();
                // Do something with the contact here (bigger example below)
            }
        }
    }
    */