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

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.R.attr.id;
import static android.R.attr.tag;
import static com.example.julu.tourbeta.R.drawable.be105;
import static com.example.julu.tourbeta.R.drawable.be301;
import static com.example.julu.tourbeta.R.drawable.danser;
import static com.example.julu.tourbeta.R.drawable.graduatehousing;
import static com.example.julu.tourbeta.R.drawable.mechatronics;
import static com.example.julu.tourbeta.R.id.bottomsheet;
import static com.example.julu.tourbeta.R.id.bottomtest;
import static com.example.julu.tourbeta.R.id.list;
import static com.example.julu.tourbeta.R.id.map;
import static com.example.julu.tourbeta.R.id.textViewMajor;
import static com.example.julu.tourbeta.R.string.be105Desciption;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private LocationManager locationManager;
    private BottomSheetLayout bs;
    private SQLiteDatabase myDB = null;
    private String TableName = "testdb4";
    private ExpandableTextView expandableText;
    //If you wish to add a new category, you'll have to add it to majorArray as well
    private String[] majorArray = {"Graduate Division", "Computer Science", "CS: Game Design",
            "Computer Engineering", "Electrical Engineering",
            "Technology Information Management", "Bioengineering", "Organizations"};

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
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


        // Getting LocationManager object from System Service LOCATION_SERVICE



        rightLabels = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        setupButtons();




    }
    //go through all majorArray where each index is contained within majorsToDisplay

    public void addAllMajors() {
        for(int i = 0; i < majorArray.length; i++) {
            majorsToDisplay.add(majorArray[i]);
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
/*
be105Desciption
gameLabDescription
mechatronicsDescription
ee101Description
graduateAdvisingDescription
graduateHousingDescription
danserDescription
opticsLabDescription
genomicsDescription
be301Description
mepDescription
nanoporeLabDescription
humanGenomeDescription
computerVisionDescription

String be105Desciption = getResources().getString(R.string.be105Desciption);
            String be105Desciption = getResources().getString(R.string.be105Desciption);
            String be105Desciption = getResources().getString(R.string.be105Desciption);

            <string name="mepDescription"></string>
    <string name="nanoporeLabDescription"></string>
    <string name="humanGenomeDescription"></string>
    <string name="computerVisionDescription"></string>
 */
            String be105Description = getResources().getString(be105Desciption);
            String gameLabDescription = getResources().getString(R.string.gameLabDescription);
            String mechatronicsDescription = getResources().getString(R.string.mechatronicsDescription);
            String ee101Description = getResources().getString(R.string.ee101Description);
            String graduateAdvisingDescription = getResources().getString(R.string.graduateAdvisingDescription);
            String graduateHousingDescription = getResources().getString(R.string.graduateHousingDescription);
            String danserDescription = getResources().getString(R.string.danserDescription);
            String opticsLabDescription = getResources().getString(R.string.opticsLabDescription);
            String computationalGenomicsDescription = getResources().getString(R.string.computationalGenomicsDescription);
            String be301Description = getResources().getString(R.string.be301Description);
            String mepDescription = getResources().getString(R.string.mepDescription);
            String nanoporeLabDescription = getResources().getString(R.string.nanoporeLabDescription);
            String humanGenomeDescription = getResources().getString(R.string.humanGenomeDescription);
            String computerVisionDescription = getResources().getString(R.string.computerVisionDescription);



            myDB = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);
            myDB.execSQL("DROP TABLE " + TableName);
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TableName
                    + " (id INT(2), title VARCHAR(30), location VARCHAR(50), major VARCHAR(30), " +
                    "image VARCHAR(200), description VARCHAR(1000), latitude VARCHAR(15), longitude VARCHAR(15));");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (0,'Linux Lab', 'Baskin Engineering 1 Room 105', 'Computer Science', " +
                    "'be105', " + be105Description + ", 37.0002225, -122.063148);");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (1,'Game Design Lab', 'Baskin Engineering 1 Room 368', 'CS: Game Design', " +
                    "'defaultimage'," + gameLabDescription +
                    ", '37.000419', '-122.062715');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (2,'Mechatronics Lab', 'Baskin Engineering 1 Room 115', 'Computer Engineering', " +
                    "'mechatronics'," + mechatronicsDescription + ", '37.000189', '-122.063545');");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (3,'Electrical Engineering 101 Lab', 'Baskin Engineering 1 Room 150', 'Electrical Engineering', " +
                    "'be150'," + ee101Description +
                    ", '37.000358', '-122.063413');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (4,'Graduate Advising Office', 'Oakes Academic Building, Room 221', 'Graduate Division', " +
                    "'defaultimage'," + graduateAdvisingDescription +", '36.9896204', '-122.0649923');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (5,'Graduate Student Housing', 'Redwood Grove', 'Graduate Division', " +
                    "'graduatehousing'," + graduateHousingDescription + ", '37.0000333', '-122.0642744');");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (6,'Computer Networks Lab', 'Baskin Engineering 1 Room 301A', 'Computer Engineering', " +
                    "'be301'," + be301Description +
                    ", '37.000271', '-122.063057');");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (7,'Computational Genomics Lab', 'Baskin Engineering 2 Room 507', 'Bioengineering', " +
                    "'defaultimage', " + computationalGenomicsDescription + ", '37.000901', '-122.063175');");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (8,'Applied Optics Lab', 'Baskin Engineering 1 Room 268', 'Electrical Engineering', " +
                    "'defaultimage'," + opticsLabDescription + ", '37.000058', '-122.063369');");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (9,'DANSER Lab', 'Baskin Engineering 1 Room XXX', 'Electrical Engineering', " +
                    "'danser'," + danserDescription + ", '37.000095', '-122.063183');");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (10,'MESA Engineering Program', 'Baskin Engineering 1 Room 399', 'Organizations', " +
                    "'mep', 'The MESA Engineering Program, also known as the Multicultural Engineering Program at the University of California Santa Cruz campus, is the university level component of the statewide Mathematics, Engineering, Science Achievement (MESA) - a program of the University of California Office of the President." +
                    "\n\nAt UC Santa Cruz, MEP is supported by the Baskin School of Engineering with its goal to promote diversity and facilitate the retention and graduation of a diverse population of students, especially groups which continue to remain the most underrepresented in engineering studies. The program received the 2004 UC Santa Cruz Excellence Through Diversity Award which is presented to programs or individuals for efforts which promote a diverse and inclusive environment." +
                    "\n\nThe School of Engineering (SoE) strives to maintain an environment that stimulates excellence in scholarship and service along with a commitment to diversity through the coordinated efforts and services of the Multicultural Engineering Program (MEP). Also known as the MESA Engineering Program, MEP is the university level component of the statewide system Mathematics, Engineering, Science Achievement (MESA) program of the University of California Office of the President." +
                    "\n\nAt UC Santa Cruz, MEP is an integral part of the Undergraduate Advising Unit–providing advice in academic, career, social and cultural areas for students in the SoE community." + " MEP provides a variety of academic and personal support services to encourage the academic success and graduation of aspiring engineering students who are first in their family to attend college or first to enroll in computer science or engineering studies, or from family backgrounds of limited financial resources and less educational opportunities. The MEP learning community augments students’ undergraduate experience by engaging their full participation in services and activities which ensure that students gain access and achieve success, and by leveraging a strong support network that encourages their matriculation into graduate school.'," +
                    " '37.000258', '-122.062930');");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (11,'Nanopore Lab', 'Baskin Engineering 1 Room 217', 'Bioengineering', " +
                    "'nanopore', 'The Nanopore project at UC Santa Cruz has pioneered the use of ion channels for the analysis of single RNA and DNA molecules. Nanopore technology makes it possible to measure DNA structure and dynamics with precision at the angstrom level. Thus, it is possible to rapidly discriminate between nearly identical strands of DNA and investigate their physical properties. Nanopore technology is well suited to analysis of the terminal ends of double-stranded DNA, and it is amenable to high throughput experiments." +
                    "\n\nIn the future, it may be possible to develop a durable solid-state or protein-based nanopore device that would allow the measurement of several different genomic factors from one cell without amplification:" +
                    "\n\nGene expression" +
                    "\n\nSingle-nucleotide polymorphisms (SNPs), common, minute variations in genes that can be used to track familial inheritance\n\nPoint mutations in single RNA or DNA molecules" +
                    "\n\nUsing machine learning tools developed at UCSC, each molecule could be identified in real time and in less than 50 milliseconds. Such a device would have broad clinical utility in diagnosing inherited traits such as hearing disorders and drug sensitivity and in tracking disease progression at the level of single cells.', '37.000309', '-122.063426');");

            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (12,'Human Genomics Institute', 'Baskin Engineering 2 Room 501', 'Bioengineering', " +
                    "'defaultimage', 'On June 22, 2000, UCSC and the other members of the International Human Genome Project consortium completed the first working draft of the human genome assembly, forever ensuring free public access to the genome and the information it contains. A few weeks later, on July 7, 2000, the newly assembled genome was released on the web at http://genome.ucsc.edu, along with the initial prototype of a graphical viewing tool, the UCSC Genome Browser." +
                    "\n\nUC Santa Cruz possesses particular strength in bioinformatics--the myriad ways to probe and analyze biological data by using computational, mathematical, and statistical approaches.\n\nThe largest product of this expertise is the UCSC Genome Browser, which serves as an interactive web-based microscope that allows researchers to view all 23 chromosomes of the human genome at any scale, from a full chromosome down to an individual nucleotide. The genome sequences on display have been analyzed and annotated, and they are aligned with the genomes of dozens of other species that are also displayed on the UCSC browser. Biomedical researchers throughout the world use this browser extensively as they seek to understand the vast amount of information contained in the genome sequences, to probe them with new experimental and informatics methodologies, and ultimately to decode the genetic program of life.\n\nFar from simply displaying the genetic code, the UCSC browser brings the code to life by aligning relevant areas with experimental and computational data and images. It also links to international databases, giving researchers instant access to deeper information about the genome. An experienced user can form a hypothesis and verify it in minutes using this tool.\n\nThe browser platform has multiple potential uses that can improve diagnosis, prevention, and cures for disease. Spin-offs include the following:" +
                    "\n\nThe HIV Data Browser" +
                    "\n\nThe UCSC Cancer Genomics Browser" +
                    "\n\nThe data collection center for the international ENCODE project', '37.000919', '-122.063080');");
            myDB.execSQL("INSERT INTO "
                    + TableName
                    + " (id, title, location, major, image, description, latitude, longitude)"
                    + " VALUES (13,'Computer Vision Lab', 'Baskin Engineering 2 Room XXX', 'Computer Science', " +
                    "'computervision', 'We do research on different aspects of computer vision,  sensor signal processing, and human-machine interface. Most of our projects are guided by applications in assistive technology for people who are blind or have a visual impairment.\n\nThe laboratory, formed in 2001 by R. Manduchi and H. Tao, is part of the Department of Computer Engineering at UC Santa Cruz. Our research is or has been supported by grants from NSF, NIH, DARPA, NASA, Research to Prevent Blindness, Transportation Research Board, CITRIS, HP Labs, SAIC, Honda Research, Imimtek, NEC, Nokia Research, CITRIS, VW, Microsoft, Toyota. We are particularly indebted to the late Mr. Larry Bock, who generously contributed to the mission of our lab.', '37.000885', '-122.062830');");


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
                return BitmapDescriptorFactory.HUE_GREEN;
            case "Computer Engineering":
                System.out.println("HUE_AZURE: " + BitmapDescriptorFactory.HUE_AZURE);
                return BitmapDescriptorFactory.HUE_ORANGE;
            case "Electrical Engineering":
                return BitmapDescriptorFactory.HUE_BLUE;
            case "CS: Game Design":
                return BitmapDescriptorFactory.HUE_MAGENTA;
            case "Graduate Division":
                return BitmapDescriptorFactory.HUE_YELLOW;
            case "Bioengineering":
                return BitmapDescriptorFactory.HUE_ROSE;
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
        //addAllMajors();
        majorsToDisplay = getIntent().getStringArrayListExtra("majorsToDisplay");
        majorsToDisplay.add("Organizations");
        initializeChosenMajorsToDisplay();
        //generateMarkersFromDatabase();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((defaultPoint), 18.01f));

        mMap.setOnMarkerClickListener(this);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION}, 1);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //System.out.println("This is my location: Lat: " + location.getLatitude() + "Long: " + location.getLongitude());
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            System.out.println("Error: " + e.getLocalizedMessage());

        }

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
