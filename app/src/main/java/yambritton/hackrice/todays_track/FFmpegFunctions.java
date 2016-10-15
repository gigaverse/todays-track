package yambritton.hackrice.todays_track;

import android.content.Context;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;

/**
 * Created by MLH Admin on 10/15/2016.
 */

public class FFmpegFunctions {

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
