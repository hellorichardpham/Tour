package com.example.julu.tourbeta;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

import static com.example.julu.tourbeta.R.drawable.be105;
import static com.example.julu.tourbeta.R.id.bottomsheet;
import static com.example.julu.tourbeta.R.id.bottomtest;
import static com.example.julu.tourbeta.R.id.list;
import static com.example.julu.tourbeta.R.id.textViewMajor;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private LocationManager locationManager;
    private BottomSheetLayout bs;
    private SQLiteDatabase myDB = null;
    private String TableName = "testdb4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupBottomSheet();
        setupDatabase();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng defaultPoint = new LatLng(37.000225, -122.063148);
        generateMarkersFromDatabase();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((defaultPoint), 18.01f));

        mMap.setOnMarkerClickListener(this);

    }

    /*
        roomInformation[0] = c.getString(idIndex);
        roomInformation[1] = c.getString(titleIndex);
        roomInformation[2] = c.getString(locationIndex);
        roomInformation[3] = c.getString(majorindex);
        roomInformation[4] = c.getString(imageIndex);
        roomInformation[5] = c.getString(description1Index);
        roomInformation[6] = c.getString(description2Index);
        roomInformation[7] = c.getString(description3Index);
        roomInformation[8] = c.getString(latitude);
        roomInformation[9] = c.getString(longitude);
     */

    @Override
    public boolean onMarkerClick(final Marker marker) {
        View parentView;
        TextView markerTitle, markerLocation, markerMajor, markerDescription1, markerDescription2, markerDescription3;
        // Retrieve the data from the marker.
        Integer tag = (Integer) marker.getTag();
        String[] roomInformation = retrieveRoomInformation(myDB, TableName, tag);
        for(int i = 0; i < roomInformation.length; i++) {
            System.out.printf("RoomInformation[%d]: %s\n", i, roomInformation[i]);
        }
        bs.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.bottom_test, bs, false));
        parentView = bs.getSheetView();

        markerTitle = (TextView) parentView.findViewById(R.id.textViewTitle);
        markerLocation = (TextView) parentView.findViewById(R.id.textViewLocation);
        markerMajor = (TextView) parentView.findViewById(R.id.textViewMajor);
        markerDescription1 = (TextView) parentView.findViewById(R.id.textViewDescription1);
        markerDescription2 = (TextView) parentView.findViewById(R.id.textViewDescription2);
        markerDescription3 = (TextView) parentView.findViewById(R.id.textViewDescription3);

        markerTitle.setText(roomInformation[1]);
        markerLocation.setText(roomInformation[2]);
        markerMajor.setText(roomInformation[3]);
        markerDescription1.setText(roomInformation[5]);
        markerDescription2.setText(roomInformation[6]);
        markerDescription3.setText(roomInformation[7]);

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


    public void setupDatabase() {
        try {
            myDB = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);
            myDB.execSQL("DROP TABLE " + TableName);
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TableName
                    + " (id INT(2), title VARCHAR(30), location VARCHAR(50), major VARCHAR(30), " +
                    "image VARCHAR(200), description1 VARCHAR(200), description2 VARCHAR(200), description3 VARCHAR(200), latitude VARCHAR(15), longitude VARCHAR(15));");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description1, description2, description3, latitude, longitude)"
                    + " VALUES (0,'BE 105: Computer Lab', 'Baskin Engineering 1 Room 105', 'Computer Science', " +
                    "'emptyImage', '105 description1', '105 description2', '105 description3', 37.0002225, -122.063148);");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description1, description2, description3, latitude, longitude)"
                    + " VALUES (1,'BE 300: Game Design Lab', 'Baskin Engineering 1 Room 300', 'Computer Science Game Design', " +
                    "'emptyImage', '300 description1', '300 description2', '300 description3', '37.000419', '-122.062715');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description1, description2, description3, latitude, longitude)"
                    + " VALUES (2,'BE 115: Mechatronics Lab', 'Baskin Engineering 1 Room 115', 'Computer Engineering', " +
                    "'emptyImage', '115 description1', '115 description2', '115 description3', '37.000183', '-122.063545');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description1, description2, description3, latitude, longitude)"
                    + " VALUES (3,'BE xxx: Electrical Engineering 101 Lab', 'Baskin Engineering 1 Room xxx', 'Electrical Engineering', " +
                    "'emptyImage', 'xxx description1', 'xxx description2', 'xxx description3', '37.000358', '-122.063413');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description1, description2, description3, latitude, longitude)"
                    + " VALUES (4,'Graduate Advising Office', 'Oakes Academic Building, Room 221', 'Graduate Division', " +
                    "'emptyImage', 'Graduate description1', 'Graduate description2', 'Graduate description3', '36.9896204', '-122.0649923');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description1, description2, description3, latitude, longitude)"
                    + " VALUES (5,'Graduate Student Housing', '', 'Graduate Division', " +
                    "'emptyImage', 'Graduate Housing description1', 'Graduate Housing description2', 'Graduate Housing description3', '37.0000333', '-122.0642744');");
            System.out.println("Done inserting");
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void generateMarkersFromDatabase() {

        /*
            Computer Sci - DEFAULT
            EE - HUE_VIOLET
            Game Design - HUE_AZURE
         */

        //BitmapDescriptorFactory.HUE_AZURE;
        //BitmapDescriptorFactory.HUE_VIOLET;
        //BitmapDescriptorFactory.HUE_GREEN
        //BitmapDescriptorFactory.MAGENTA
        //BitmapDescriptorFactory.ORANGE
        //BitmapDescriptorFactory.HUE_RED; //Default



        Cursor c = myDB.rawQuery("SELECT * FROM " + TableName, null);
        String[] roomInformation = new String[c.getColumnCount()];
        ArrayList<Marker> listOfMarkers = new ArrayList<>();
        float colorOfMarker;
        while (c.moveToNext()) {
            int idIndex = c.getColumnIndex("id");
            int titleIndex = c.getColumnIndex("title");
            int latitudeIndex = c.getColumnIndex("latitude");
            int longitudeIndex = c.getColumnIndex("longitude");
            int majorIndex = c.getColumnIndex("major");

            String id = c.getString(idIndex);
            String title = c.getString(titleIndex);
            String latitude = c.getString(latitudeIndex);
            String longitude = c.getString(longitudeIndex);
            String major = c.getString(majorIndex);
            colorOfMarker = getMarkerColor(major);

            int tag = Integer.parseInt(id);
            double lat = Double.parseDouble(latitude);
            double lon = Double.parseDouble(longitude);
            LatLng latLong = new LatLng(lat, lon);

            Marker marker =  mMap.addMarker(new MarkerOptions().position(latLong).title(title).icon(BitmapDescriptorFactory.defaultMarker(colorOfMarker)));
            marker.setTag(tag);

            listOfMarkers.add(marker);
        }

        for(int i = 0; i < listOfMarkers.size(); i++) {
            System.out.printf("Marker Title: %s Marker Tag: %d\n", listOfMarkers.get(i).getTitle(), listOfMarkers.get(i).getTag());
        }
    }

    private float getMarkerColor(String major) {
        switch(major) {
            case "Computer Science":
                return BitmapDescriptorFactory.HUE_RED;
            case "Computer Engineering":
                return BitmapDescriptorFactory.HUE_AZURE;
            case "Electrical Engineering":
                return BitmapDescriptorFactory.HUE_GREEN;
            case "Computer Science Game Design":
                return BitmapDescriptorFactory.HUE_MAGENTA;
            case "Graduate Division":
                return BitmapDescriptorFactory.HUE_YELLOW;
            default:
                return BitmapDescriptorFactory.HUE_RED;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();
        System.out.println(msg);
    }

    public String[] retrieveRoomInformation(SQLiteDatabase myDB, String TableName, int index) {
        Cursor c = myDB.rawQuery("SELECT * FROM " + TableName + " WHERE iD = " + index, null);
        String[] roomInformation = new String[c.getColumnCount()];

        if(c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int titleIndex = c.getColumnIndex("title");
            int locationIndex = c.getColumnIndex("location");
            int majorindex = c.getColumnIndex("major");
            int imageIndex = c.getColumnIndex("image");
            int description1Index = c.getColumnIndex("description1");
            int description2Index = c.getColumnIndex("description2");
            int description3Index = c.getColumnIndex("description3");
            int latitudeIndex = c.getColumnIndex("latitude");
            int longitudeIndex = c.getColumnIndex("longitude");

            roomInformation[0] = c.getString(idIndex);
            roomInformation[1] = c.getString(titleIndex);
            roomInformation[2] = c.getString(locationIndex);
            roomInformation[3] = c.getString(majorindex);
            roomInformation[4] = c.getString(imageIndex);
            roomInformation[5] = c.getString(description1Index);
            roomInformation[6] = c.getString(description2Index);
            roomInformation[7] = c.getString(description3Index);
            roomInformation[8] = c.getString(latitudeIndex);
            roomInformation[9] = c.getString(longitudeIndex);

        } else {
            System.out.println("There was an error with the cursor");
        }
        return roomInformation;
    }

    public void setupBottomSheet() {
        bs = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        bs.setShouldDimContentView(false);
        bs.setPeekSheetTranslation(300);
    }


}
