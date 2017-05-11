package com.example.a2sebaj04.pointsofinterests;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener

{

    MapView mv;
    ItemizedIconOverlay<OverlayItem> Markers;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button submitButton = (Button) findViewById(R.id.submitButton);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        submitButton.setOnClickListener(this);



        // this line tells OpenStreetMap about our app.
        // If you miss this out, you might get banned from OSM servers


        mv = (MapView)findViewById(R.id.map1);

        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(15);
        mv.getController().setCenter(new GeoPoint(51.504,0.027)); // West Silvertown

        Markers = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);

        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()
        {
            public boolean onItemLongPress(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item)
            {
                Toast.makeText(MainActivity.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.poi_menu, menu);
        return true;
    }



    public void onClick(View view) {
        EditText latitudeEditText = (EditText) findViewById(R.id.latitude);
        double latitude = Double.parseDouble(latitudeEditText.getText().toString());

        EditText longitudeEditText = (EditText) findViewById(R.id.longitude);
        double longitude = Double.parseDouble(longitudeEditText.getText().toString());

        mv.getController().setCenter(new GeoPoint(latitude,longitude));


    }





    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.addpoi) {
            Intent intent = new Intent(this, AddPOI.class);
            startActivityForResult(intent, 0);
            return true;
        }
        /*if(item.getItemId() == R.id.choosemap)
        {
            // react to the menu item being selected...
            Intent intent = new Intent(this,MapChooseActivity.class);
            startActivityForResult(intent,1);
            return true;
        }

        else if(item.getItemId() == R.id.setlocation)
        {
            Intent intent = new Intent(this,SetLocation.class);
            startActivityForResult(intent,2);
            return true;
        }*/

        else if(item.getItemId() == R.id.save)
        {
            // text file should be saved in the directory automatically


            try
            {
                PrintWriter pw = new PrintWriter( new FileWriter( Environment.getExternalStorageDirectory().getAbsolutePath() + "/point.txt"));
                for(int i = 0; i< Markers.size(); i++) {
                    OverlayItem item1 = Markers.getItem(i);
                    pw.println(item1.getTitle() + "," + item1.getSnippet() + "," + item1.getPoint() + "");
                }
                pw.close(); // close the file to ensure data is flushed to file
            }
            catch(IOException e) {
                new AlertDialog.Builder(this).setMessage("ERROR: " + e).
                        setPositiveButton("OK", null).show();
            }

            return true;
        }
        else if(item.getItemId() == R.id.load) {

            return true; // this should load the poi
        }
        return false;
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent intent) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = intent.getExtras();
                String addname = bundle.getString("com.example.edittext1");
                String addtype = bundle.getString("com.example.edittext2");
                String add_des = bundle.getString("com.example.edittext3");


                double longitude = mv.getMapCenter().getLongitude();
                double latitude = mv.getMapCenter().getLatitude();

                OverlayItem Item = new OverlayItem(addname, add_des, new GeoPoint(latitude, longitude));
                Markers.addItem(Item);


            }

            else if (requestCode == 1) {

                if (resultCode == RESULT_OK) {
                    Bundle extras = intent.getExtras();
                    boolean cyclemap = extras.getBoolean("com.example.cyclemap");
                    if (cyclemap == true) {
                        mv.setTileSource(TileSourceFactory.CYCLEMAP);
                    } else {
                        mv.setTileSource(TileSourceFactory.MAPNIK);
                    }
                }
            } else if (requestCode == 2)


            {
                Bundle extras = intent.getExtras();

                double latitude = extras.getDouble("com.example.latitude");
                double longitude = extras.getDouble("com.example.longitude");
                mv.getController().setCenter(new GeoPoint(latitude, longitude));
            }
        }
    }
    public void onStart()
    {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        double lat = Double.parseDouble ( prefs.getString("lat", "50.9") );
        double lon = Double.parseDouble ( prefs.getString("lon", "-1.4") );
        int dzl = Integer.parseInt ( prefs.getString("dzl","12"));





        // do something with the preference data...
    }

}