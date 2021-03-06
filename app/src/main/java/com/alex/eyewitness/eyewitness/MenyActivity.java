package com.alex.eyewitness.eyewitness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Toast;

import com.alex.eyewitness.eyewitness.messages.NotificationInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.graphics.Color.argb;

public class MenyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback , SeekBar.OnSeekBarChangeListener{

    private static GoogleMap googleMap ;
    public static GoogleMap getvMap() {
        return googleMap;
    }

    private NotificationBroadcastReceiver mNotificationBroadcastReceiver = null;
    private IntentFilter mIntentFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meny);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                SeekBar fSeekBar = (SeekBar)findViewById(R.id.seekBar);
                ArrayList <Coordinates> fLastCoords = DBHelper.getInstance(view.getContext()).getLastCoords(fSeekBar.getProgress());
                Double fMinDistanle = CoordinatesWorker.genMinDistance(googleMap .getCameraPosition().target.longitude, googleMap .getCameraPosition().target.latitude,fLastCoords );
                if (fMinDistanle < 0.002){
                    googleMap .addMarker(new MarkerOptions()
                            .title("Pos " + Integer.toString(1))
                            //.snippet("At " + dateFormat.format(fLastCoords.get(i).getInserted()))
                            .position(googleMap .getCameraPosition().target).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }else{
                    googleMap .addMarker(new MarkerOptions()
                            .title("Pos " + Integer.toString(1))
                            //.snippet("At " + dateFormat.format(fLastCoords.get(i).getInserted()))
                            .position(googleMap .getCameraPosition().target).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SeekBar fSeekBar = (SeekBar)findViewById(R.id.seekBar);
        fSeekBar.setOnSeekBarChangeListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startService(new Intent(this, GeoService2.class));


        this.mNotificationBroadcastReceiver = new NotificationBroadcastReceiver(this);
        this.mIntentFilter = new IntentFilter(Constants.NOTIFICATION_BROADCAST_RECEIVER_MESSAGE_RECEIVED);

        Toast.makeText(getBaseContext(), "GeoService2 succesfull start.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mNotificationBroadcastReceiver, this.mIntentFilter);
    }
    @Override
    protected void onPause() {
        if (this.mNotificationBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mNotificationBroadcastReceiver);
           // unregisterReceiver(mNotificationBroadcastReceiver);
            this.mNotificationBroadcastReceiver = null;
        }

        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meny, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            SeekBar fSeekBar = (SeekBar)findViewById(R.id.seekBar);

            stateOnMap(fSeekBar.getProgress());
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        SeekBar fSeekBar = (SeekBar)findViewById(R.id.seekBar);
        stateOnMap(fSeekBar.getProgress());



    }

    private void stateOnMap(int pCountLines) {


        ArrayList <Coordinates> fLastCoords = DBHelper.getInstance(this).getLastCoords(pCountLines);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        googleMap.clear();

        googleMap.setMyLocationEnabled(true);
        LatLng vMyPosition;

        try {
            vMyPosition = new LatLng(fLastCoords.get(0).getLat(), fLastCoords.get(0).getLng());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vMyPosition, 15));
            //int i = 0;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            PolylineOptions vPoly = new PolylineOptions();
            for (int i = 0; i < fLastCoords.size(); i++ ){
                LatLng vPos = new LatLng(fLastCoords.get(i).getLat(), fLastCoords.get(i).getLng());
                Float fAlpha = (float)(fLastCoords.size()-i)/(fLastCoords.size());
                googleMap.addMarker(new MarkerOptions()
                        .title("Pos " + Integer.toString(i))
                        .snippet("At " + dateFormat.format(fLastCoords.get(i).getInserted()))
                        .position(vPos).alpha(fAlpha));

                //CircleOptions co = new CircleOptions();
                //co.center(vPos).radius(fLastCoords.get(i).getAccuracy()).fillColor(0x220011AA).strokeColor(0x110011AA);
               // vMap.addCircle(co);

                vPoly.add(vPos);



                //i++;
            }
            googleMap.addPolyline(vPoly);

        } catch (Exception ex) {
            vMyPosition = new LatLng(0, 0);
        }

        //vMap.setLatLngBoundsForCameraTarget();




    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        stateOnMap(seekBar.getProgress());
        Toast.makeText(getBaseContext(), "Show last " + Integer.toString(seekBar.getProgress()) +" saved steps.", Toast.LENGTH_SHORT).show();
    }

    private class NotificationBroadcastReceiver extends BroadcastReceiver {

        WeakReference<MenyActivity> mMainActivity;
        private Context context;
        private Intent intent;

        public NotificationBroadcastReceiver(MenyActivity mainActivity) {
            this.mMainActivity = new WeakReference<>(mainActivity);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            this.context = context;
            this.intent = intent;
            MenyActivity mainActivity = mMainActivity.get();
            if (mainActivity != null) {
                Bundle extras = intent.getExtras();
                if (extras != null && extras.containsKey(Constants.PARAM_NOTIFICATION_INFO)) {
                    NotificationInfo notificationInfo = (NotificationInfo) extras.getSerializable(Constants.PARAM_NOTIFICATION_INFO);
                    mainActivity.notificationReceived(notificationInfo);
                }
            }
        }

    }

    public void notificationReceived(@NonNull final NotificationInfo notificationInfo)
    {
        //handle the notification in MainActivity

        if (notificationInfo.minDistanle < 0.002){
            googleMap.addMarker(new MarkerOptions()
                    .title("Pos " + Integer.toString(1))
                    //.snippet("At " + dateFormat.format(fLastCoords.get(i).getInserted()))
                    .position(googleMap.getCameraPosition().target).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }else{
            googleMap.addMarker(new MarkerOptions()
                    .title("Pos " + Integer.toString(1))
                    //.snippet("At " + dateFormat.format(fLastCoords.get(i).getInserted()))
                    .position(new LatLng(notificationInfo.latitude, notificationInfo.longetude)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        }

    }
}
