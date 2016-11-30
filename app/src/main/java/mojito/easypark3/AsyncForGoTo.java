/*
package mojito.easypark3;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;



*/
/**
 * Created by mohor on 27/06/2016.
 *//*

public class AsyncForGoTo extends AsyncTask<Void, Void, Void>  {
    public Context contMainImp;
    public static Polyline roadOverlay;
    public MapView osmImp;
    public Road road;

    ProgressDialog pDialog;

    public AsyncForGoTo (Context contMain, MapView mapView)
    {
        contMainImp = contMain;
        osmImp = mapView;

        //add polyline to overlays of the map
        osmImp.getOverlays().add(roadOverlay);

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(contMainImp);
        pDialog.setMessage("Prosimo počakajte...");
        pDialog.setCancelable(false);
        pDialog.show();
    }



    @Override
    protected Void doInBackground(Void... arg0) {

*/
/*        RoadManager roadManager = new OSRMRoadManager(contMainImp);


        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        GeoPoint startPoint = new GeoPoint(46.04738246, 14.49534416);
        waypoints.add(startPoint);
        GeoPoint endPoint = new GeoPoint(46.04540166, 14.48922873);
        waypoints.add(endPoint);


        road = roadManager.getRoad(waypoints);


        roadOverlay = RoadManager.buildRoadOverlay(road, contMainImp);*//*


        return null;

    }

    @Override
    protected void onPostExecute(Void result) {
        //super.onPostExecute(result);
    }
}
*/
