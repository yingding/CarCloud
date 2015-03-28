package com.example.carcloud;

import android.support.v7.app.ActionBarActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
    private boolean serviceState;
    private ServiceConnection boundStartedServiceConnection;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
        Button b1 = (Button) findViewById(R.id.button1);
        
        boundStartedServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // TODO Auto-generated method stub
                Log.i(MainActivity.class.getSimpleName(), "Service bound");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // TODO Auto-generated method stub
                Log.i(MainActivity.class.getSimpleName(), "Service disconnected");
            }
        };

        b1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (serviceState == true) {
                    serviceState = false;
                    stopService(createExplizitIntent());
                }                
            }
            
        });
        serviceState = true;
        startService(createExplizitIntent());
    }
    
    private Intent createExplizitIntent() {
        Intent explizitIntent = new Intent(LightService.MY_SERVICE);
        ComponentName component = new ComponentName("com.example.carcloud", "LightService");
        explizitIntent.setComponent(component);
        return explizitIntent;
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, LightService.class);
        bindService(intent, boundStartedServiceConnection, Context.BIND_AUTO_CREATE);
    }
 
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(boundStartedServiceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
