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
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener

{

    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;

    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // this line tells OpenStreetMap about our app.
        // If you miss this out, you might get banned from OSM servers
        Configuration.getInstance().load
                (this, PreferenceManager.getDefaultSharedPreferences(this));

        mv = (MapView)findViewById(R.id.map1);

        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(17);
        mv.getController().setCenter(new GeoPoint(51.504,0.027)); // West Silvertown

        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);

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

        try
        {

            BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath()+"/poi.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] components = line.split(",");

                if (components.length == 5) {

                    OverlayItem currentPoi = new OverlayItem(components[0], components[2], new GeoPoint(Double.parseDouble(components[4]),
                            Double.parseDouble(components[3])));
                    //reads lon & lat as string so convert to a double using Double.parseDouble
                    items.addItem(currentPoi);
                }
            }
        }
        catch (IOException e) {
            new AlertDialog.Builder(this).setMessage("ERROR: " + e).setPositiveButton("OK", null).show();
        }


        OverlayItem westSilvertown = new OverlayItem("West Silvertown", "My hometown formerly known as Britannia Village", new GeoPoint(51.5040, 0.027));
        OverlayItem cityAirport = new OverlayItem("London City Airport", "The capital's airport", new GeoPoint(51.504, 0.0524));

        items.addItem(westSilvertown);
        items.addItem(new OverlayItem("West Silvertown", "Britannia Village", new GeoPoint(51.504,0.027)));

        items.addItem(cityAirport);
        items.addItem(new OverlayItem("London City Airport", "The capital's airport", new GeoPoint(51.504, 0.0524)));

        mv.getOverlays().add(items);


    }


    @Override
    public void onClick(View view) {
        EditText latitudeEditText = (EditText) findViewById(R.id.latitude);
        double latitude = Double.parseDouble(latitudeEditText.getText().toString());

        EditText longitudeEditText = (EditText) findViewById(R.id.longitude);
        double longitude = Double.parseDouble(longitudeEditText.getText().toString());

        mv.getController().setCenter(new GeoPoint(latitude,longitude));
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.poi_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {

        if(item.getItemId() == R.id.setlocation)
        {
            Intent intent = new Intent(this,SetLocation.class);
            startActivityForResult(intent,2);
            return true;
        }
        return false;
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent intent)
    {

        if(requestCode==1)
        {

            if (resultCode==RESULT_OK)
            {
                Bundle extras=intent.getExtras();
                boolean cyclemap = extras.getBoolean("com.example.cyclemap");
                if(cyclemap==true)
                {
                    mv.setTileSource(TileSourceFactory.CYCLEMAP);
                }

                else{
                    mv.setTileSource(TileSourceFactory.MAPNIK);
                }
            }
        }


        else if(requestCode==2)


        {
            Bundle extras=intent.getExtras();

            double latitude = extras.getDouble("com.example.latitude");
            double longitude = extras.getDouble("com.example.longitude");
            mv.getController().setCenter(new GeoPoint(latitude,longitude));
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