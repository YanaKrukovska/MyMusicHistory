package com.ritacle.mymusichistory.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
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

    public String getApplicationName(ResolveInfo resolveInfo) {
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(resolveInfo.activityInfo.packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return (String) (applicationInfo != null ? context.getPackageManager().getApplicationLabel(applicationInfo) : resolveInfo.activityInfo.packageName);
    }

    public Drawable getApplicationIcon(ResolveInfo resolveInfo) {
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(resolveInfo.activityInfo.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }

}
