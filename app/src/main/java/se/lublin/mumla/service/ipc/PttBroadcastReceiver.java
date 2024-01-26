package se.lublin.mumla.service.ipc;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;

import java.util.Arrays;


public class PttBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = PttBroadcastReceiver.class.getName();

    public interface PttActionProvider {
        void pttDown();
        void pttUp();
        void pttToggle();
    }

    public static final String[] PTT_DOWN_INTENTS = {
            "android.intent.action.PTT.down",
            "android.intent.action.PTT.pressed",
            "com.runbo.ptt.key.down",
            "com.sonim.intent.action.PTT_KEY_DOWN"
        };
    public static final String[] PTT_UP_INTENTS = {
            "android.intent.action.PTT.up",
            "android.intent.action.PTT.released",
            "com.runbo.ptt.key.up",
            "com.sonim.intent.action.PTT_KEY_UP"
        };
    public static final String[] PTT_TOGGLE_INTENTS = {
            "com.honeywell.intent.action.AUDIO_PTT_STATE_CHANGE",
            "com.kodiak.intent.action.PTT_BUTTON",
            "com.kyocera.android.intent.action.PTT_BUTTON",
            "com.kyocera.intent.action.PTT_BUTTON",
            "com.symbol.button.L2"
        };
    public static final String[] PTT_SOS_INTENTS = {
            "android.intent.action.PTT.SOS"
        };
    public static final String[] PTT_GROUP_PREV_INTENTS = {
            "android.intent.action.TALK_GROUP.prev"
    };
    public static final String[] PTT_GROUP_NEXT_INTENTS = {
            "android.intent.action.TALK_GROUP.next"
    };

    PttActionProvider mPttProvider;

    public PttBroadcastReceiver(PttActionProvider provider) {
        mPttProvider = provider;
    }

    public void register(Context context) {
        for (String intent: PTT_DOWN_INTENTS) {
            context.registerReceiver(this, new IntentFilter(intent));
        }
        for (String intent: PTT_UP_INTENTS) {
            context.registerReceiver(this, new IntentFilter(intent));
        }
        for (String intent: PTT_TOGGLE_INTENTS) {
            context.registerReceiver(this, new IntentFilter(intent));
        }
        /*
        for (String intent: PTT_SOS_INTENTS) {
            context.registerReceiver(this, new IntentFilter(intent));
        }
        for (String intent: PTT_GROUP_PREV_INTENTS) {
            context.registerReceiver(this, new IntentFilter(intent));
        }
        for (String intent: PTT_GROUP_NEXT_INTENTS) {
            context.registerReceiver(this, new IntentFilter(intent));
        }
        */
    }

    public void unregister(Context context) {
        try {
            context.unregisterReceiver(this);
        } catch (IllegalArgumentException ignored) {
            //e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Arrays.asList(PTT_DOWN_INTENTS).contains(action)) {
            mPttProvider.pttDown();
        } else if (Arrays.asList(PTT_UP_INTENTS).contains(action)) {
            mPttProvider.pttUp();
        } else if (Arrays.asList(PTT_TOGGLE_INTENTS).contains(action)) {
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

            if (event != null) {
                if (event.getRepeatCount() == 0) {
                    int keyAction = event.getAction();

                    if (keyAction == KeyEvent.ACTION_DOWN) {
                        mPttProvider.pttDown();
                    } else if (keyAction == KeyEvent.ACTION_UP) {
                        mPttProvider.pttUp();
                    }
                }
            } else {
                mPttProvider.pttToggle();
            }
        } /*else if (Arrays.asList(PTT_SOS_INTENTS).contains(action)) {
        } else if (Arrays.asList(PTT_GROUP_PREV_INTENTS).contains(action)) {
        } else if (Arrays.asList(PTT_GROUP_NEXT_INTENTS).contains(action)) {
        }*/ else {
            Log.d(TAG, "Unhandled PTT action: " + action);
        }
    }
}
