package de.philipphock.android.lib.services;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.util.Log;

public class ServiceUtil {
	public static final boolean isServiceRunning(Context c,Class<?> s){
		
	    return isServiceRunning(c, s.getCanonicalName());
	}
	
	public static final boolean isServiceRunning(Context c,String fullClassName){
		
	    ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (fullClassName.equals(service.service.getClassName())) {
	        	Log.d("Service",service.service.getClassName()+" FOUND");
	            return true;
	        }
	    }
	    return false;
	}
}
