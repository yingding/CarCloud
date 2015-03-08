package com.example.carcloud;

import android.app.IntentService;
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
    private Camera.Parameters p;
    private int count;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        ConnectivityManager manager = (ConnectivityManager) 
                        getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        cam = Camera.open();
        p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        boolean hasFlash = this.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        count = 0;
        while(true)
        {
            try {
                if (count > 5) break;
                Thread.sleep(3000);
                count++;
                
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
                       
        }
        this.stopSelf();
        return START_STICKY; 
    }  
    
    private void flipLight() {
        if (cam == null) {
            Log.v("carservice","cam is null!");
            return;
        }
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
        //this.stopSelf();        
    }

}
