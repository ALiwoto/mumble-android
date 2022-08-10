package se.lublin.mumla.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;

import java.io.IOException;

public class SoundUtils {
    private static final float DEFAULT_VOL = 1.0f;

    /**
     * Play a sound resource based on resource ID.
     * @param context The application context containing the resource
     * @param resId The resource ID to play
     */
    public static void playSoundResource(Context context, int resId) throws IOException {
        playSoundResource(context, resId, DEFAULT_VOL, DEFAULT_VOL);
    }

    /**
     * Play a sound resource based on resource ID.
     * @param context The application context containing the resource
     * @param resId The resource ID to play
     * @param leftVol Left volume level (0.0f - 1.0f)
     * @param rightVol Right volume level (0.0f - 1.0f)
     */
    public static void playSoundResource(Context context, int resId,
                                         float leftVol, float rightVol) throws IOException {
        AssetFileDescriptor resFd = context.getResources().openRawResourceFd(resId);

        if (resFd != null) {
            MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp){
                    mp.release();
                    try {
                        resFd.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp){
                    mp.start();
                }
            };
            MediaPlayer mPlayer = new MediaPlayer();

            mPlayer.setOnCompletionListener(completionListener);
            mPlayer.setOnPreparedListener(preparedListener);
            mPlayer.setDataSource(resFd.getFileDescriptor(),
                    resFd.getStartOffset(), resFd.getLength());
            mPlayer.setVolume(leftVol, rightVol);
            mPlayer.prepareAsync();
            //mPlayer.start();
        }
    }
}
