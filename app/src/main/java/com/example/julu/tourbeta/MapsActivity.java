package com.example.julu.tourbeta;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.analytics.ExceptionParser;
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
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import static android.R.attr.id;
import static android.R.attr.tag;
import static com.example.julu.tourbeta.R.drawable.be105;
import static com.example.julu.tourbeta.R.id.bottomsheet;
import static com.example.julu.tourbeta.R.id.bottomtest;
import static com.example.julu.tourbeta.R.id.list;
import static com.example.julu.tourbeta.R.id.textViewMajor;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private LocationManager locationManager;
    private BottomSheetLayout bs;
    private SQLiteDatabase myDB = null;
    private String TableName = "testdb4";
    private ExpandableTextView expandableText;
    private String[] majorArray = {"Graduate Division", "Computer Science", "CS: Game Design",
            "Computer Engineering", "Electrical Engineering",
            "Technology Information Management", "Bioengineering"};

    private ArrayList<String> majorsToDisplay = new ArrayList<>();
    private FloatingActionsMenu rightLabels;
    private ArrayList<Marker> listOfMarkers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setupBottomSheet();
        setupDatabase();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Getting LocationManager object from System Service LOCATION_SERVICE
        //LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        rightLabels = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        setupButtons();




    }
    /*

        private String[] majorArray = {"Computer Science", "Computer Science: GD",
            "Computer Engineering", "Electrical Engineering",
            "Technology Information Management", "Bioengineering"};
     */

    //go through all majorArray where each index is contained within majorsToDisplay

    public void addAllMajors() {
        for(int i = 0; i < majorArray.length; i++) {
            //majorsToDisplay.add(majorArray[i]);
        }
    }

    public void initializeChosenMajorsToDisplay() {
        mMap.clear();
        for(int i = 0; i < majorArray.length; i++) {

            String major = majorArray[i];

            if(majorsToDisplay.contains(major)) {
                System.out.println("We will display " + major);
                generateMarkersFromDatabaseUsingMajor(major);
            }
        }
    }

    public void setupButtons() {
        for(int i=0;i<majorArray.length;i++) {

            FloatingActionButton fab = new FloatingActionButton(this);
            fab.setTitle(majorArray[i]);
            fab.setSize(FloatingActionButton.SIZE_MINI);
            final String major = fab.getTitle();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(majorsToDisplay.contains(major)) {
                        System.out.println(major + " is contained so I will remove it now");
                        majorsToDisplay.remove(major);
                    } else {
                        System.out.println(major + " is not here so i will add it now");
                        majorsToDisplay.add(major);
                    }
                    initializeChosenMajorsToDisplay();
                }
            });
            rightLabels.addButton(fab);
        }
    }



    /*
        roomInformation[0] = c.getString(idIndex);
        roomInformation[1] = c.getString(titleIndex);
        roomInformation[2] = c.getString(locationIndex);
        roomInformation[3] = c.getString(majorindex);
        roomInformation[4] = c.getString(imageIndex);
        roomInformation[5] = c.getString(descriptionIndex);
        roomInformation[8] = c.getString(latitude);
        roomInformation[9] = c.getString(longitude);
     */

    @Override
    public boolean onMarkerClick(final Marker marker) {
        View parentView;
        TextView markerTitle, markerLocation, markerMajor;
        RelativeLayout peekLayout;
        ImageView markerImage;
        // Retrieve the data from the marker.
        Integer tag = (Integer) marker.getTag();
        String[] roomInformation = retrieveRoomInformation(myDB, TableName, tag);

        bs.showWithSheetView(LayoutInflater.from(this).inflate(R.layout.bottom_test, bs, false));
        parentView = bs.getSheetView();


        markerTitle = (TextView) parentView.findViewById(R.id.textViewTitle);
        markerLocation = (TextView) parentView.findViewById(R.id.textViewLocation);
        markerMajor = (TextView) parentView.findViewById(R.id.textViewMajor);
        markerImage = (ImageView) parentView.findViewById(R.id.imageViewImage);

        //peekLayout = (RelativeLayout) findViewById(R.id.peekLayout);

        //peekLayout.setBackgroundDrawable(Drawable d);
        //peekLayout.setBackgroundResource(int resid);

        //String color = getMarkerColor(markerMajor.getText().toString());
        //peekLayout.setBackgroundColor(Color.parseColor());

        String imageFileName = roomInformation[4];
        String imageFilePath = "com.example.julu.tourbeta:drawable/" + imageFileName;

        int id = getResources().getIdentifier(imageFilePath, null, null);
        markerImage.setImageResource(id);
        System.out.println("After image " + markerImage.getDrawable());

        markerTitle.setText(roomInformation[1]);
        markerLocation.setText(roomInformation[2]);
        markerMajor.setText(roomInformation[3]);

        // sample code snippet to set the text content on the ExpandableTextView
        expandableText = (ExpandableTextView) parentView.findViewById(R.id.expand_text_view)
                .findViewById(R.id.expand_text_view);

        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        expandableText.setText(roomInformation[5]);



        return false;
    }


    public void setupDatabase() {
        try {
            myDB = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);
            myDB.execSQL("DROP TABLE " + TableName);
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TableName
                    + " (id INT(2), title VARCHAR(30), location VARCHAR(50), major VARCHAR(30), " +
                    "image VARCHAR(200), description VARCHAR(1000), latitude VARCHAR(15), longitude VARCHAR(15));");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (0,'BE 105: Computer Lab', 'Baskin Engineering 1 Room 105', 'Computer Science', " +
                    "'be105', '105 description1', 37.0002225, -122.063148);");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (1,'BE 368: Game Design Lab', 'Baskin Engineering 1 Room 368', 'CS: Game Design', " +
                    "'defaultimage'," +
                    " 'The Game Lab is among the most distinctive teaching labs at UC Santa Cruz. Here, undergraduate game majors work in teams of 4 to 15 students to create a substantial computer game over their entire senior year. " +
                    "\n\nGame Lab games have won awards at game festivals such as IndieCade and the Google Play Indie Games awards, and are publicly released on platforms such as Itch.io, the Apple App Store, and the Google Play Store. " +
                    "\n\nThe lab features high end computer workstations with VR-capable graphics cards and dual monitors, virtual reality headsets, game prototyping supplies, a sound studio, an extensive library of books on game topics, and multiple team meeting spaces.'," +
                    " '37.000419', '-122.062715');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (2,'BE 115: Mechatronics Lab', 'Baskin Engineering 1 Room 115', 'Computer Engineering', " +
                    "'defaultimage', '115 description1', '37.000189', '-122.063545');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (3,'BE 150: Electrical Engineering 101 Lab', 'Baskin Engineering 1 Room 150', 'Electrical Engineering', " +
                    "'be150'," +
                    "'The circuits lab (Baskin 150) is used for beginning instruction in analog electronics, both for EE 101/L (Circuits) and BME 51A+B (Applied Electronics for Bioengineers). " +
                    "\n\nThe class shown here is one section of BME 51A, doing their first amplifier lab: building an instrumentation amplifier for a pressure sensor for measuring air pressure in a blood-pressure cuff. " +
                    "\n\nAll the bioengineering concentrations are required to do BME 51, as it provides a relatively quick way to gain practical engineering design experience and to understand the basics of interfacing biological systems to computers.'," +
                    "'37.000358', '-122.063413');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (4,'Graduate Advising Office', 'Oakes Academic Building, Room 221', 'Graduate Division', " +
                    "'defaultimage', 'Graduate description1', '36.9896204', '-122.0649923');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (5,'Graduate Student Housing', 'Redwood Grove', 'Graduate Division', " +
                    "'graduatehousing'," +
                    "'Graduate Student Housing is an intimate community housing just 82 students, and is home to a diverse group, including students from all over the United States and the world. " +
                    "\n\nThe apartments are set in a beautifully landscaped natural environment conveniently located adjacent to Science Hill, home to many of UCSCs main academic facilities. " +
                    "\n\nEach apartment has four single bedrooms, living room, kitchen, dining room, and bathroom. " +
                    "\n\nGround floor apartments have decks, while upper apartments have private balconies. '," +
                    " '37.0000333', '-122.0642744');");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (6,'BE 301A: Computer Networks Lab', 'Baskin Engineering 1 Room 301A', 'Computer Engineering', " +
                    "'be301', 'BE301A – Computer Networks Lab: This lab is used by the University’s Computer Networking and Electrical Engineering courses. " +
                    "Students in these courses receive hands-on experience with real-world networking equipment and concepts to prepare them for careers as network engineers. " +
                    "\n\nThey use software such as Wireshark to observe packets as they travel through a computer network to understand how communication between routers, switches, and computers occur on Local Area Networks as well as the Internet. " +
                    "\n\nAdditionally, the students are introduced to emerging concepts, such as Software Defined Networking, to prepare them for the Internet of the future.'," +
                    " '37.000271', '-122.063057');");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (7,'E2 507: Computational Genomics Lab', 'Baskin Engineering 2 Room 507', 'Bioengineering', " +
                    "'computationalgenomics', 'Computational Genomics Default Description', '37.000919', '-122.063080');");

            System.out.println("Done inserting");
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }



    public void generateMarkersFromDatabaseUsingMajor(String requestedMajor) {
        Cursor c = myDB.rawQuery("SELECT * FROM " + TableName + " WHERE  major = " + "'"+requestedMajor+"'", null);

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


            Marker marker = mMap.addMarker(new MarkerOptions().position(latLong).title(title).icon(BitmapDescriptorFactory.defaultMarker(colorOfMarker)));
            marker.setTag(tag);

            listOfMarkers.add(marker);
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
                System.out.println("HUE_BLUE: " + BitmapDescriptorFactory.HUE_BLUE);
                return BitmapDescriptorFactory.HUE_BLUE;
            case "Computer Engineering":
                System.out.println("HUE_AZURE: " + BitmapDescriptorFactory.HUE_AZURE);
                return HUE_AZURE;
            case "Electrical Engineering":
                return BitmapDescriptorFactory.HUE_GREEN;
            case "Computer Science Game Design":
                return BitmapDescriptorFactory.HUE_MAGENTA;
            case "Graduate Division":
                return BitmapDescriptorFactory.HUE_YELLOW;
            case "Bioengineering":
                return BitmapDescriptorFactory.HUE_CYAN;
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
            int descriptionIndex = c.getColumnIndex("description");
            int latitudeIndex = c.getColumnIndex("latitude");
            int longitudeIndex = c.getColumnIndex("longitude");

            roomInformation[0] = c.getString(idIndex);
            roomInformation[1] = c.getString(titleIndex);
            roomInformation[2] = c.getString(locationIndex);
            roomInformation[3] = c.getString(majorindex);
            roomInformation[4] = c.getString(imageIndex);
            roomInformation[5] = c.getString(descriptionIndex);
            roomInformation[6] = c.getString(latitudeIndex);
            roomInformation[7] = c.getString(longitudeIndex);

        } else {
            System.out.println("There was an error with the cursor");
        }
        return roomInformation;
    }

    public void setupBottomSheet() {
        bs = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        bs.setShouldDimContentView(false);

        //We are taking 80, which is the height of peekLayout in dp.
        //Convert dp into pixels and use that as the sheet translation
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics());
        bs.setPeekSheetTranslation(px);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng defaultPoint = new LatLng(37.000225, -122.063148);
        addAllMajors();
        initializeChosenMajorsToDisplay();
        //generateMarkersFromDatabase();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((defaultPoint), 18.01f));

        mMap.setOnMarkerClickListener(this);

    }
}
