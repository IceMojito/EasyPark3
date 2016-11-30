package mojito.easypark3;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

/**
 * Created by mohor on 10/07/2016.
 */
public class RemoveRoadsNmarkers {

    public RemoveRoadsNmarkers (MapView mapView, Polyline roadPoly, Integer Nnodes) {
        //remove previous road (polyline)
        mapView.getOverlays().remove(roadPoly);
        //remove previous node markers for steps of the road
        //firstly needs to count how many overlay, than deletes the n of them (countNodes) - deleting with names does not work
        Integer countOverlays = mapView.getOverlays().size() - 1;
        for (int i = 0; i < Nnodes; i++) {
            mapView.getOverlays().remove(countOverlays-i);
        }

    }
}
