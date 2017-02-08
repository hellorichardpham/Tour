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

import static com.example.julu.tourbeta.R.id.bottomsheet;
import static com.example.julu.tourbeta.R.id.bottomtest;
import static com.example.julu.tourbeta.R.id.textViewMajor;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private LocationManager locationManager;
    private BottomSheetLayout bs;
    public View descriptionView;
    public LayoutInflater inflater;
    public TextView tv;
    private SQLiteDatabase myDB = null;
    private String TableName = "testdb1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bs = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        bs.setShouldDimContentView(false);
        System.out.println(bs.getPeekSheetTranslation());
        bs.setPeekSheetTranslation(300);




        try {
            myDB = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);

            myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TableName
                    + " (id INT(2), title VARCHAR(30), location VARCHAR(50), major VARCHAR(30), " +
                    "image VARCHAR(200), description1 VARCHAR(200), description2 VARCHAR(200), description3 VARCHAR(200));");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description1, description2, description3)"
                    + " VALUES (0,'BE 105: Computer Lab', 'Baskin Engineering 1 Room 105', 'Computer Science', 'emptyImage', '105 description1', '105 description2', '105 description3');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description1, description2, description3)"
                    + " VALUES (1,'BE 300: Game Design Lab', 'Baskin Engineering 1 Room 300', 'Computer Science Game Design', 'emptyImage', '300 description1', '300 description2', '300 description3');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description1, description2, description3)"
                    + " VALUES (2,'BE 115: Mechatronics Lab', 'Baskin Engineering 1 Room 115', 'Computer Engineering', 'emptyImage', '115 description1', '115 description2', '115 description3');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description1, description2, description3)"
                    + " VALUES (3,'BE xxx: Electrical Engineering 101 Lab', 'Baskin Engineering 1 Room xxx', 'Electrical Engineering', 'emptyImage', 'xxx description1', 'xxx description2', 'xxx description3');");

        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng be105 = new LatLng(37.000225, -122.063148);
        LatLng gameDesignLab = new LatLng(37.000419, -122.062715);
        LatLng mechatronics = new LatLng(37.000183, -122.063545);
        LatLng EE101Lab = new LatLng(37.000358, -122.063413);
        /*
            Computer Sci - DEFAULT
            EE - HUE_VIOLET
            Game Design - HUE_AZURE
         */
        Marker marker_BE105_computer_lab =  mMap.addMarker(new MarkerOptions().position(be105).title("BE 105: Computer Lab"));
        Marker marker_BE300_game_design_lab = mMap.addMarker(new MarkerOptions().position(gameDesignLab).title("BE 300: Game Design Lab")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        Marker marker_BE115_mechatronics = mMap.addMarker(new MarkerOptions().position(mechatronics).title("BE 115: Mechatronics Lab")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        Marker marker_EE101_lab = mMap.addMarker(new MarkerOptions().position(EE101Lab).title("BE 1xx: EE101 Lab")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        marker_BE105_computer_lab.setTag(0);
        marker_BE300_game_design_lab.setTag(1);
        marker_BE115_mechatronics.setTag(2);
        marker_EE101_lab.setTag(3);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((be105), 18.01f));

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
        switch(tag) {
            case 0:
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

                break;
            case 1:
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

                break;
            case 2:
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

                break;
            case 3:
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

                break;
        }
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


    @Override
    public void onLocationChanged(Location location) {

        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();
        System.out.println(msg);
    }

    public String[] retrieveRoomInformation(SQLiteDatabase myDB, String TableName, int index) {
        Cursor c = myDB.rawQuery("SELECT * FROM " + TableName + " WHERE iD = " + index, null);
        String[] roomInformation = new String[8];
        if(c.moveToFirst()) {
            int idIndex = c.getColumnIndex("id");
            int titleIndex = c.getColumnIndex("title");
            int locationIndex = c.getColumnIndex("location");
            int majorindex = c.getColumnIndex("major");
            int imageIndex = c.getColumnIndex("image");
            int description1Index = c.getColumnIndex("description1");
            int description2Index = c.getColumnIndex("description2");
            int description3Index = c.getColumnIndex("description3");

            roomInformation[0] = c.getString(idIndex);
            roomInformation[1] = c.getString(titleIndex);
            roomInformation[2] = c.getString(locationIndex);
            roomInformation[3] = c.getString(majorindex);
            roomInformation[4] = c.getString(imageIndex);
            roomInformation[5] = c.getString(description1Index);
            roomInformation[6] = c.getString(description2Index);
            roomInformation[7] = c.getString(description3Index);
        } else {
            System.out.println("There was an error with the cursor");
        }
        return roomInformation;
    }
}
