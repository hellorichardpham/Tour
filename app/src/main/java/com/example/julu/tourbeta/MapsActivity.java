package com.example.julu.tourbeta;

import android.content.Context;
import android.content.pm.PackageManager;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private LocationManager locationManager;
    private BottomSheetLayout bs;
    public View descriptionView;
    public LayoutInflater inflater;
    public TextView tv;

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
        View description = inflater.inflate(R.layout.bottom_test, null);
        System.out.println(description.getHeight());
        bs.setShouldDimContentView(false);
        System.out.println(bs.getPeekSheetTranslation());
        bs.setPeekSheetTranslation(300);

        System.out.println("contentView: " + bs.getContentView().findViewById(bottomsheet));
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        descriptionView = inflater.inflate(R.layout.bottom_test, null); //log.xml is your file.
        tv = (TextView)descriptionView.findViewById(R.id.textViewLocation); //get a reference to the textview on the log.xml file.
        System.out.printf("Before tv: %s\n", tv.getText());
        tv.setText("why aren't you setting?");
        System.out.printf("After tv: %s\n", tv.getText());

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
        tv.setText("why aren't you setting?");

    }
    @Override
    public boolean onMarkerClick(final Marker marker) {
        View parentView;
        TextView markerTitle, markerLocation, markerMajor;
        // Retrieve the data from the marker.
        Integer tag = (Integer) marker.getTag();

        switch(tag) {
            case 0:
                bs.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.bottom_test, bs, false));
                parentView = bs.getSheetView();
                markerTitle = (TextView) parentView.findViewById(R.id.textViewTitle);
                markerTitle.setText(marker.getTitle());

                markerLocation = (TextView) parentView.findViewById(R.id.textViewLocation);
                //markerTitle.setText(marker.get());

                break;
            case 1:
                bs.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.bottom_test, bs, false));
                parentView = bs.getSheetView();
                markerTitle = (TextView) parentView.findViewById(R.id.textViewTitle);
                markerTitle.setText(marker.getTitle());
                break;
            case 2:
                bs.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.bottom_test, bs, false));
                parentView = bs.getSheetView();
                markerTitle = (TextView) parentView.findViewById(R.id.textViewTitle);
                markerTitle.setText(marker.getTitle());
                break;
            case 3:
                bs.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.bottom_test, bs, false));
                parentView = bs.getSheetView();
                markerTitle = (TextView) parentView.findViewById(R.id.textViewTitle);
                markerTitle.setText(marker.getTitle());
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
}
