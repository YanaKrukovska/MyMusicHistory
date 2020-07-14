package com.ritacle.mymusichistory.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.io.File;
import java.util.List;

public class PlayersUtil {

    private Context context;

    public PlayersUtil(Context context) {
        this.context = context;
    }

    public List<ResolveInfo> findPlayers() {
        Intent resolve_intent = new Intent();
        resolve_intent.setAction(android.content.Intent.ACTION_VIEW);
        resolve_intent.setDataAndType(Uri.fromFile(new File("/some/path/to/a/file")), "audio/*");
        return context.getPackageManager().queryIntentActivities(resolve_intent, 0);
    }

}
