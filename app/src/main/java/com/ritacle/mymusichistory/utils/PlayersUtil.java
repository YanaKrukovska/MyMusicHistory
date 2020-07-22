package com.ritacle.mymusichistory.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.util.LinkedList;
import java.util.List;

public class PlayersUtil {

    private Context context;

    public PlayersUtil(Context context) {
        this.context = context;
    }

    public List<ApplicationInfo> findPlayers() {
        List<ApplicationInfo> allApplications = context.getPackageManager().getInstalledApplications(0);
        List<ApplicationInfo> result = new LinkedList<>();
        for (int i = 0; i < allApplications.size(); i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (allApplications.get(i).category == ApplicationInfo.CATEGORY_AUDIO) {
                    result.add(allApplications.get(i));
                }
            }
        }
        return result;
    }

    public String getApplicationName(ApplicationInfo resolveInfo) {
        return (String) context.getPackageManager().getApplicationLabel(resolveInfo);
    }

    public Drawable getApplicationIcon(ApplicationInfo resolveInfo) {
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(resolveInfo.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }

}
