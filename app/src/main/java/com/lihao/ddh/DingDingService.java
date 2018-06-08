package com.lihao.ddh;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class DingDingService extends Service {
    private static final String TAG = "DingDingService";

    public DingDingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onCreate()
    {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
    {
        Log.i(TAG, "onStartCommand: ");
        try {
            PowerManager.WakeLock localWakeLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "bright");
            localWakeLock.acquire();
            localWakeLock.release();
            doStartApplicationWithPackageName("com.alibaba.android.rimet");

        }catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return super.onStartCommand(paramIntent, paramInt1, paramInt2);
    }

    private void doStartApplicationWithPackageName(String packagename) {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packagename, 0);
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(packageInfo.packageName);
            ResolveInfo resolveInfo = getPackageManager().queryIntentActivities(intent, 0).iterator().next();

            if (resolveInfo != null) {

                Intent localIntent = new Intent();
                localIntent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(localIntent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
