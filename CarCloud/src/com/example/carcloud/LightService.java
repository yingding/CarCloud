package com.example.carcloud;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * use tag:Tag in LogCat to show only the Log.x output
 * reference https://gist.github.com/Alp-Phone/56d22b36714e3339be05
 * @author Yingding Wang
 *
 */
public class LightService extends Service {
    static final String MY_SERVICE = "com.example.carcloud.LightService";
    private static String TAG = "carcloud";
    private static Camera cam;
    private volatile boolean lightState = false;
    private Camera.Parameters p;
    private int count;
    private IBinder serviceBinder;
    private volatile boolean isTermated;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        // serving();
        return serviceBinder;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();   
        Log.i(TAG, "Service onCreate()");
        serviceBinder = new BoundStartedServiceBinder();
        this.isTermated = false;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        serving();
        //this.stopSelf();
        return START_STICKY; 
    }  
    
    private void serving() {
        ConnectivityManager manager = (ConnectivityManager) 
                getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        boolean hasFlash = this.getPackageManager()
        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        count = 0;
        Log.i(TAG,"service started in onStartCommand");
        while(!isTermated) {
            try { 
                if (count > 5) break;     
                Boolean isWifi = manager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
                if (isWifi && hasFlash) {
                    if (cam == null) initCam();
                    flipLight();
                    // if the cam is not released and connected again, 
                    // startPreview is not possible after stopPreview
                    
                } else if (hasFlash) {
                    // if no wi
                    if (!lightState) {
                      if (cam == null) initCam();
                      cam.startPreview();
                    }
                }               
                Thread.sleep(2000);
                count++;        
            } catch (InterruptedException e) {
                //e.printStackTrace();
                if (cam != null) {
                    cam.stopPreview();
                    cam.release();
                }
                this.isTermated = true;
                Log.i(TAG, "Service interrupted!");
            }
               
        }
        if (cam != null) {
            cam.stopPreview();
            cam.release();
        }
    }
    
    private void initCam() {
        cam = Camera.open();
        p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
    }
    
    private void flipLight() {
        if (cam == null) {
            Log.v("carservice","cam is null!");
            return;
        }
        Log.i(TAG, "flipLight");
        if (lightState) {            
            cam.stopPreview();
            lightState = false;
            Log.i(TAG,"light is off");
            cam.release();
        } else {
            if (cam == null) initCam();
            cam.startPreview();
            lightState = true;
            Log.i(TAG,"light is on");
        }
    }
 
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (cam != null) {
            cam.stopPreview();
            cam.release();
        }
        Log.i(TAG, "Service Destroyed");
        super.onDestroy();
        //this.stopSelf();        
    }
    
    public class BoundStartedServiceBinder extends Binder {
    }
}
