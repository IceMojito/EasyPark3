package mojito.easypark3;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;

import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


//for osmdroid
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;


//*import org.osmdroid.DefaultResourceProxyImpl;
//*import org.osmdroid.ResourceProxy;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;




// extends AppCompatActivity
public class MainActivity extends Activity implements MapEventsReceiver {
    /**
     * Variables
     * <p/>
     * osmdroid
     *
     * @osm - map view instance
     * @mc- map controller instance, for example for zoom buttons
     * <p/>
     * JSON
     */

    public static MapView osm;
    private MapController mc;
    private MyLocationNewOverlay mLocationOverlay;
    private GpsMyLocationProvider GPS;
    private GeoPoint point;
    //*public ResourceProxy mResourceProxy;

    //TODO: VERJETNO TELE SPREMENJLJIVKE LAHKO DAS POD RESOURCES???
    ////VARIABLES FOR JSON
    // URL to get contacts JSON
    private static String url = "http://opendata.si/promet/parkirisca/lpt/";

    // JSON Node names
    //basic information
    private static final String TAG_PARKIRISCA = "Parkirisca";
    private static final String TAG_ID = "ID_Parkirisca";
    private static final String TAG_IME = "Ime";
    private static final String TAG_ZASEDENOST = "zasedenost";
    private static final String TAG_PROSTA_M = "P_kratkotrajniki";
    private static final String TAG_ST_MEST = "St_mest";

    //coordinates
    private static final String TAG_KOO_X = "KoordinataX_wgs";
    private static final String TAG_KOO_Y = "KoordinataY_wgs";

    //definition of tag for razdalja
    private static final String TAG_RAZDALJA = "razdalja";

    //variable to share parkirisce list to ListActivityC
    public static ArrayList<HashMap<String, String>> parkirisceList;

    //make overlay
    ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();


    //definition of MarkerPoint -  place of the markerpoint
    private static GeoPoint MarkerPoint;

    //definition of endpoint
    public static GeoPoint endPoint;

    //needed for GoTo
    public static Polyline roadOverlay;
    public static Road road;
    public static Marker nodeMarker;
    public static Integer countNodes = 0;

    //button is accesible
    public static Button ButtonGoTo;

    //for Tbt
    public static  ListView listView=null;

    ///to get icons
    int[] icons = new int[]{
            R.drawable.ic_continue,
            R.drawable.ic_slight_left,
            R.drawable.ic_turn_left,
            R.drawable.ic_sharp_left,
            R.drawable.ic_slight_right,
            R.drawable.ic_turn_right,
            R.drawable.ic_sharp_right,
            R.drawable.ic_u_turn,
            R.drawable.ic_arrived,
            R.drawable.ic_roundabout,
            R.drawable.ic_empty
    };



    ///
    //current location variable
    public static GeoPoint startPoint;

    //varible to check if getting current location is possible
    public static boolean testLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Button ButtonGoTo = (Button) findViewById(R.id.buttonGoTo);

        ////to create layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ////PARSE DATA
        // Calling async task to get json
        new GetParkirisca().execute();

        //had to move osmdroid to postexecute!


        ///only for making toast when map is clicked?? maybe could implement deselection of selected parkirisce?
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        //for MapRecieverOverlay
        ////for MapEventReceiver

        osm.getOverlays().add(0, mapEventsOverlay);


    }


    //////////////
    //ACTIVITY LIST METHOD CALL
    /////////////
    // to go to listview of the data
    public void activitylist(View view) {
        Intent intent = new Intent(MainActivity.this, ListActivityC.class);
        startActivity(intent);
    }

    // to refresh data
    public void activityrefresh(View view) {

        //close ALL info windows (MarkerInfoWindow)
        MarkerInfoWindowExtend.closeAllInfoWindowsOn(osm);
        //to ensure that buttongoto does not reappear, do not know why otherwise reapears
        ButtonGoTo.setVisibility(View.INVISIBLE);
        //remove roads and node markers
        new RemoveRoadsNmarkers(osm, roadOverlay, countNodes);
        ////PARSE DATA
        // Calling async task to get json
        new GetParkirisca().execute();
    }

    // when to go to button is clicked
    public void activitygoto(View view) {
        //// <SAMO KOPIJA IMPLEMENTACIJE, kar se zares izvede je pod AsyncForGoTo
        /*//get road manager
        RoadManager roadManager = new OSRMRoadManager(MainActivity.this);

        //set up start and endpoint
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        //TODO get GPS point coordinates to GeoPoint
        //to get coordinates of GPS an add as waypoint
        waypoints.add(mLocationOverlay.getMyLocation());
        //add endpoint coordinates (clicked parkirisce)
        waypoints.add(endpoint);

        //retrieve the road between points
        //TODO: NEKI SE ZATAKNE OR ROAD?? KAJ NAROBI Z WAYPOINTS ALI MAINACTIVITY.THIS??
        Road road = roadManager.getRoad(waypoints);
        //build polyline
        roadOverlay = RoadManager.buildRoadOverlay(road, MainActivity.this);
        //add polyline to overlays of the map
        osm.getOverlays().add(roadOverlay);*/
        //// />

        //pozene metodo
        new AsyncForGoTo().execute();







        //hides buttongoto (pot)
        ButtonGoTo.setVisibility(View.INVISIBLE);
        osm.invalidate();




    }


    //////////////
    //TO PARSE DATA
    /////////////

    /**
     * Async task class to get json by making HTTP call
     */
    //TODO: A JE TO RES PUBLIC=???
    public class GetParkirisca extends AsyncTask<Void, Void, Void> {


        // Hashmap for ListView
        ArrayList<HashMap<String, String>> parkirisceList;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Prosimo počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();

            //genereta map and controls - zoom
            osm = (MapView) findViewById(R.id.mapView);
            osm.setTileSource(TileSourceFactory.MAPNIK);
            osm.setBuiltInZoomControls(true);
            osm.setMultiTouchControls(true);

            ////to see current location
            // has to be at the beginning, so it is possible to calculate distance
            GPS = new GpsMyLocationProvider(osm.getContext());
            mLocationOverlay = new MyLocationNewOverlay(GPS, osm);
            mLocationOverlay.enableMyLocation();

            osm.getOverlays().add(mLocationOverlay);

        }


        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);

            Log.d("Response: ", "> " + jsonStr);

            //two variables for ParseJSON
            testLocation = mLocationOverlay.getMyLocation() == null;
            startPoint = mLocationOverlay.getMyLocation();

            parkirisceList = ParseJSON(jsonStr, MainActivity.this);
            //to share parkirisceList to ListActivityC
            //parkirisceListShare = parkirisceList;

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


            ////was only checkinh for overlay item, TODO: now check if Marker?
            AlertDialog aDialog;
            //return error message if json is empty
            if (osm.getOverlays().size() <= 0) {
                aDialog = new AlertDialog.Builder(MainActivity.this).create();
                aDialog.setTitle("Napaka!");
                aDialog.setMessage("Aplikaciji ni uspelo pridobiti podatke. Preverite, če imate vklopljen WIFI oziroma prenos podatkov!");
                aDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "V redu",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                aDialog.show();
            }

            //// TO GENERATE OSMDROID MAP, ZOOM TO CENTER

            //*only for osmdroid 5.1
            //*mResourceProxy = new DefaultResourceProxyImpl(getApplicationContext());

            ////road manager needed for routing
            //TODO


            //OTHER STUFF FOR MAP
            mc = (MapController) osm.getController();
            mc.setZoom(13);

            GeoPoint center = new GeoPoint(46.0511, 14.5050);
            mc.animateTo(center);


            //// EXAMPLE to make icon for geopoint, SINGLE!, generate overlay, manually define icon
/*        point = new GeoPoint(46.0511, 14.5050);
        OverlayItem olItemS = new OverlayItem("Here", "Descr", point);
        //make custom icon
        Drawable newMarker = this.getResources().getDrawable(R.mipmap.ic_launcher);
        olItemS.setMarker(newMarker);
        //make and add to overlay
        ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();
        mItems.add(olItemS);*/

/*        ////EXAMPLE: In case of no click for details, no need for waiting till mItems is defined
          //to create overlay items
        ItemizedIconOverlay newLocationOverlay;
        newLocationOverlay = new ItemizedIconOverlay<OverlayItem>(mItems, null, mResourceProxy);*/

            ////to enable tap on overlay item, details displayed    WORKS ONLY WITH OSMDROID 5.1, RATHER ADDED MARKER WITH OSMDROID 5.2
/*            ItemizedOverlayWithFocus<OverlayItem> newLocationOverlay = new ItemizedOverlayWithFocus<OverlayItem>(mItems,
                    new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                        @Override
                        public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                            //do something
                            return true;
                        }
                        @Override
                        public boolean onItemLongPress(final int index, final OverlayItem item) {
                            return false;
                        }
                    }, mResourceProxy);
            newLocationOverlay.setFocusItemsOnTap(true);

            osm.getOverlays().add(newLocationOverlay);*/


            //to update the MapView so as to see the changes we made to it
            osm.invalidate();


/*            Context context = getApplicationContext();
            CharSequence text = "Hello toast!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();*/


        }

    }


    ////ASYNC FOR DRAWING THE ROAD! DID NOT KNOW HOW TO DO IT IN SEPARATE CLASS (PROBLEM WAS importing MainActivity.this, app crashed)
    public class AsyncForGoTo extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Prosimo počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();


            //REMOVE PREVIOUS ROADS AND NODE MARKERS

            new RemoveRoadsNmarkers(osm, roadOverlay, countNodes);
        }


        @Override
        protected Void doInBackground(Void... arg0) {

            //NOTE: had to change from:
            //RoadManager roadManager = new OSRMRoadManager(MainActivity.this);
            //to code below, had to register for API! Now I have it.... https://developer.mapquest.com/
            RoadManager roadManager = new MapQuestRoadManager("qxbiBsURBXimHW9l3hhqa9GkxGl6kCh3");

            roadManager.addRequestOption("routeType=shortest");

            //to clear, useful in case GPS location is not defined (GPS off)
            roadOverlay = null;


            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            //get current location from GPS
            startPoint = mLocationOverlay.getMyLocation();
            //check if GPS location was retrieved
            if (startPoint == null) {
                //has to be seperated from aDialog!
                // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            } else {
                waypoints.add(startPoint);
                //endpoint is filled by method hittest in the class MarkerExtend
                waypoints.add(endPoint);

                //MainActivity.thisPolyline.clearPath();
                road = roadManager.getRoad(waypoints);
                roadOverlay = RoadManager.buildRoadOverlay(road, MainActivity.this);


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //add overlay and cancel dialog only if startPoint was found from GPS, therefore roadOverlay should not be null
            if (roadOverlay != null) {

                //first add polyline road so it does not cover markers
                osm.getOverlays().add(roadOverlay);
                //add road nodes for route steps


                Drawable nodeIcon = getResources().getDrawable(R.drawable.marker_node);
                countNodes = road.mNodes.size();
                for (int i = 0; i < road.mNodes.size(); i++) {
                    RoadNode node = road.mNodes.get(i);
                    nodeMarker = new Marker(osm);
                    nodeMarker.setPosition(node.mLocation);
                    nodeMarker.setIcon(nodeIcon);
                    nodeMarker.setTitle("Step " + i);
                    osm.getOverlays().add(nodeMarker);

                    //set the bubble snippet with the instructions - add description to road nodes -steps
                    nodeMarker.setSnippet(node.mInstructions);
                    //Set the bubble sub-description with the length and duration of the step
                    nodeMarker.setSubDescription(Road.getLengthDurationText(MainActivity.this, node.mLength, node.mDuration));
                    //set icons
                    new IconManeuver(node, nodeMarker, MainActivity.this);
                }

                ////
                ////generate listview with instructions TODO: do it when button clicked!!! + TODO: new button show/hide (Show instructions)
                ////

                //listView has to be defined
                listView=new ListView(MainActivity.this);
                //generate listview with instructions
                listView.setAdapter(new TbTactivity(road, MainActivity.this, icons).getAdapter());

                //to show the listview with instructions
                android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setPositiveButton("OK",null);
                builder.setView(listView);
                android.app.AlertDialog dialog=builder.create();
                dialog.show();

                // Perform action when an item is clicked
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ViewGroup vg = (ViewGroup) view;
                        TextView txt = (TextView) vg.findViewById(R.id.instructions);
                        Toast.makeText(MainActivity.this, txt.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                });
                ////***
                ////generate listview with instructions
                ////***


                // Dismiss the progress dialog
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            } else {
                //pDialog already canceled in doInBackground
                AlertDialog aDialog;

                aDialog = new AlertDialog.Builder(MainActivity.this).create();
                aDialog.setTitle("Napaka!");
                aDialog.setMessage("Aplikaciji ni uspelo pridobiti podatke o trenutni lokaciji. Preverite, če imate vklopljen GPS!");
                aDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "V redu",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                aDialog.show();

            }
        }

    }

    //opomba: context se potrebuje le za getResources() v metodi DrawMarker
    public static ArrayList<HashMap<String, String>> ParseJSON(String json, Context context) {
        if (json != null) {
            try {
                // Hashmap for ListView
                parkirisceList = new ArrayList<HashMap<String, String>>();

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray parkirisca = jsonObj.getJSONArray(TAG_PARKIRISCA);

                // looping through All Parkirisca
                for (int i = 0; i < parkirisca.length(); i++) {
                    JSONObject c = parkirisca.getJSONObject(i);

                    //GET INFO FROM JSON
                    String id = c.getString(TAG_ID);
                    String ime = c.getString(TAG_IME);


                    // Zasedenost node is JSON Object
                    //beacuse not all parkirisce have zasedenost object
                    String prostaM;
                    if (c.has(TAG_ZASEDENOST)) {
                        JSONObject zasedenost = c.getJSONObject(TAG_ZASEDENOST);
                        prostaM = zasedenost.getString(TAG_PROSTA_M);
                        // if prostaM is negative, change to 0
                        if (Integer.parseInt(prostaM) < 0) prostaM = "0";
                    } else {
                        prostaM = "N/A";
                    }

                    //koordinate, ker Parkomati nimajo koordinat je potrebno narediti if, spodaj krajsi zapis if else
                    String kooX = ((c.has(TAG_KOO_X)) ? c.getString(TAG_KOO_X) : "N/A");
                    String kooY = ((c.has(TAG_KOO_Y)) ? c.getString(TAG_KOO_Y) : "N/A");

                    //get stevilo mest
                    String stMest = ((c.has(TAG_ST_MEST)) ? c.getString(TAG_ST_MEST) : "N/A");


                    // tmp hashmap for single parkirisce
                    HashMap<String, String> parkirisce = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    parkirisce.put(TAG_ID, id);
                    parkirisce.put(TAG_IME, ime);
                    parkirisce.put(TAG_PROSTA_M, prostaM);
                    //koordinate NE DELA
                    parkirisce.put(TAG_KOO_X, kooX);
                    parkirisce.put(TAG_KOO_Y, kooY);

                    ////calculate AIR distance (NOT ROAD - the road distance takes a lot of time
                    //have to check if it is possible to calculate distance: have to have coordinates of parkirisce and GPS coordinates
                    if (kooX == "N/A" || testLocation) {
                        parkirisce.put(TAG_RAZDALJA, "neznana");
                    } else {

                        //current location already retrieved

                        float[] razdalja = new float[1];
                        Location.distanceBetween(startPoint.getLatitude(), startPoint.getLongitude(), Double.parseDouble(kooY), Double.parseDouble(kooX), razdalja);
                        // za izračunano razdaljo
                        //TODO: DAJ NA KM IN ZAOKROZI!
                        parkirisce.put(TAG_RAZDALJA, String.valueOf(Math.round(razdalja[0])) + " m");
                        //////
                    }

                    // adding student to students list
                    parkirisceList.add(parkirisce);

                    ////////
                    //to make Markers (overlayItems was for osmdroid5.1) for osmdroid (info for icons on map of parkirisc)
                    ////////

                    if (c.has(TAG_KOO_X)) {
                        DrawMarker(c, osm, context);
                    }

                }
                return parkirisceList;


            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }

    //method to get information about single point, opomba: context se potrebuje le za getResources()
    private static void DrawMarker(JSONObject parkiriscaJSON, MapView IMmapview, Context context) {
        try {
            //declare button variable
            ButtonGoTo = (Button) IMmapview.findViewById(R.id.buttonGoTo);

            //to make icon for geopoint
            Double Long = Double.parseDouble(parkiriscaJSON.getString(TAG_KOO_X));
            Double Lat = Double.parseDouble(parkiriscaJSON.getString(TAG_KOO_Y));

            MarkerPoint = new GeoPoint(Lat, Long);
            String Title = parkiriscaJSON.getString(TAG_IME);
            //TODO: dodaj nekaj v description.... ALI SE DA DODAT NEK GUMB???


            String prostaM = parkiriscaJSON.getJSONObject(TAG_ZASEDENOST).getString(TAG_PROSTA_M);
            String stMest = parkiriscaJSON.getString(TAG_ST_MEST);

            String Description = "Št. prostih parkirnih mest: " + prostaM + " / " + stMest;

            //izracun % zasedenosti
            Double ratio;
            ratio = 1 - (Double.parseDouble(prostaM) / Double.parseDouble(stMest));


            ////*working with overlay item and resourceproxy in osmdroid 5.1, rather marker!
            //*OverlayItem ONEpoint = new OverlayItem(Title, Description, point);

            MarkerExtend ONEpoint = new MarkerExtend(IMmapview, ButtonGoTo);
            ONEpoint.setPosition(MarkerPoint);
            ONEpoint.setTitle(Title);
            ONEpoint.setSubDescription(Description);

            ONEpoint.setAnchor(MarkerExtend.ANCHOR_CENTER, MarkerExtend.ANCHOR_BOTTOM);
            IMmapview.getOverlays().add(ONEpoint);


            //make custom icon depending on ratio; TODO: if ratio cannot be calculated (no stMest)
            if (ratio > 0.9) {
                Drawable newMarker = context.getResources().getDrawable(R.drawable.marker_default_red);
                ONEpoint.setIcon(newMarker);
            } else if (ratio > 0.7) {
                Drawable newMarker = context.getResources().getDrawable(R.drawable.marker_default_orange);
                ONEpoint.setIcon(newMarker);
            } else if (ratio > 0.5) {
                Drawable newMarker = context.getResources().getDrawable(R.drawable.marker_default_yellow);
                ONEpoint.setIcon(newMarker);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //for TOAST message on single tap on map.
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        //Toast.makeText(this, "Tapped", Toast.LENGTH_SHORT).show();

        //close ALL info windows (MarkerInfoWindow)
        MarkerInfoWindowExtend.closeAllInfoWindowsOn(osm);

        //to ensure that buttongoto does not reappear, do not know why otherwise reapears
        ButtonGoTo.setVisibility(View.INVISIBLE);
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        //DO NOTHING FOR NOW:
        return false;
    }


}
