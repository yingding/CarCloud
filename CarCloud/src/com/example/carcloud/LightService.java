package com.example.carcloud;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

public class LightService extends Service {
    static final String MY_SERVICE = "com.example.carcloud.LightService";
    private static String TAG = "carcloud";
    private static Camera cam;
    private static boolean lightState = false;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        ConnectivityManager manager = (ConnectivityManager) 
                        getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        cam = Camera.open();
        boolean hasFlash = this.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        while(true)
        {
            try {
                Thread.sleep(2000);
                
                Boolean isWifi = manager.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                if (isWifi && hasFlash) {
                    flipLight();                    
                } else if (hasFlash) {
                    // if no wi
                    if (!lightState) cam.startPreview();
                }
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //return START_STICKY;            
        }
    }  
    
    private void flipLight() {
        if (cam == null) return;
        if (lightState) {
            lightState = false;
            cam.stopPreview();            
        } else {
            lightState = true;
            cam.startPreview();
        }
    }
 
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (cam != null) {
            cam.stopPreview();
            cam.release();
        }
        super.onDestroy();
        this.stopSelf();
        
    }

}
