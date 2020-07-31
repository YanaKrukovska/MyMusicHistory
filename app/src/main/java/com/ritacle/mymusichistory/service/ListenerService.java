package com.ritacle.mymusichistory.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.ritacle.mymusichistory.scrobbling.ListenSender;
import com.ritacle.mymusichistory.scrobbling.PlaybackTracker;
import com.ritacle.mymusichistory.utils.NotificationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ListenerService extends NotificationListenerService
        implements MediaSessionManager.OnActiveSessionsChangedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "Listener Service";
    private List<MediaController> mediaControllers = new ArrayList<>();
    private Map<MediaController, MediaController.Callback> controllerCallbacks = new WeakHashMap<>();
    private PlaybackTracker playbackTracker;
    private SharedPreferences sharedPreferences;

    public ListenSender listenSender;

    @Override
    public void onCreate() {
        Log.d(TAG, "NotificationListenerService started");

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences = settings;
        settings.registerOnSharedPreferenceChangeListener(this);

        NotificationUtil notificationUtil =
                new NotificationUtil(this);

        listenSender = new ListenSender(getApplicationContext());
        playbackTracker = new PlaybackTracker(getApplicationContext(), notificationUtil, listenSender);


        MediaSessionManager mediaSessionManager =
                (MediaSessionManager)
                        getApplicationContext().getSystemService(Context.MEDIA_SESSION_SERVICE);

        ComponentName componentName = new ComponentName(this.getApplicationContext(), this.getClass());
        mediaSessionManager.addOnActiveSessionsChangedListener(this, componentName);
        List<MediaController> initialSessions = mediaSessionManager.getActiveSessions(componentName);
        onActiveSessionsChanged(initialSessions);
    }

    public static boolean isNotificationAccessEnabled(Context context) {
        return NotificationManagerCompat.getEnabledListenerPackages(context)
                .contains(context.getPackageName());
    }

    @Override
    public void onActiveSessionsChanged(List<MediaController> activeMediaControllers) {
        Log.d(TAG, "Active MediaSessions changed");

        excludeDisabledPlayers(activeMediaControllers);

        for (final MediaController controller : activeMediaControllers) {
            String packageName = controller.getPackageName();
            Log.d(TAG, String.format("Listening for events from %s", packageName));

            MediaController.Callback callback =
                    new MediaController.Callback() {
                        @Override
                        public void onPlaybackStateChanged(@NonNull PlaybackState state) {
                            controllerPlaybackStateChanged(controller, state);
                        }

                        @Override
                        public void onMetadataChanged(MediaMetadata metadata) {
                            controllerMetadataChanged(controller, metadata);
                        }
                    };

            controllerCallbacks.put(controller, callback);
            controller.registerCallback(callback);
            controllerPlaybackStateChanged(controller, controller.getPlaybackState());
            controllerMetadataChanged(controller, controller.getMetadata());
        }
        mediaControllers = activeMediaControllers;
    }

    private void excludeDisabledPlayers(List<MediaController> activeMediaControllers) {
        for (int i = 0; i < activeMediaControllers.size(); i++) {
            MediaController player = activeMediaControllers.get(i);
            String playerPackage = "player" + player.getPackageName();
            if (sharedPreferences.getBoolean(playerPackage, false)) {
                if (controllerCallbacks.get(player) != null) {
                    player.unregisterCallback(controllerCallbacks.get(player));
                }
                playbackTracker.handleSessionTermination(player.getPackageName());
                activeMediaControllers.remove(player);
            }
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.startsWith("player.")) {
            final String packageName = key.substring(7);
            if (sharedPreferences.getBoolean(key, true)) {
                Log.d(TAG, "Player enabled, re-registering callbacks");
                onActiveSessionsChanged(mediaControllers);
            } else {
                Log.d(TAG, "Player disabled, stopping any current tracking");
                final Optional<MediaController> optionalController =
                        Iterables.tryFind(
                                mediaControllers, input -> input.getPackageName().equals(packageName));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (optionalController.isPresent()
                            && controllerCallbacks.containsKey(optionalController.get())) {
                        MediaController controller = optionalController.get();
                        controller.unregisterCallback(controllerCallbacks.get(controller));
                        playbackTracker.handleSessionTermination(controller.getPackageName());
                        controllerCallbacks.remove(controller);
                    }
                }
            }
        }
    }

    private void controllerPlaybackStateChanged(MediaController controller, PlaybackState state) {
        Log.d(TAG, "controller playback state changed");
        String controllerPackageName = controller.getPackageName();
        if (sharedPreferences.getBoolean("player." + controllerPackageName, false)) {
            playbackTracker.handlePlaybackStateChange(controllerPackageName, state);
        }
    }

    private void controllerMetadataChanged(MediaController controller, MediaMetadata metadata) {
        Log.d(TAG, "controller metadata changed");
        String controllerPackageName = controller.getPackageName();
        if (sharedPreferences.getBoolean("player." + controllerPackageName, false)) {
            playbackTracker.handleMetadataChange(controllerPackageName, metadata);
        }
    }


}