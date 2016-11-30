package mojito.easypark3;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by mohor on 10/07/2016.
 */
public class IconManeuver {
    Drawable icon;
    Integer Nicon;

    //for buble popup on marker
    public IconManeuver (RoadNode Rnode, Marker nMarker, Context activity) {

        switch (Rnode.mManeuverType) {
            //check table here to see what ID tells you http://open.mapquestapi.com/guidance/#maneuvertypes
/*            case 0:
            case 2:
            case 9:
            case 10:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
                icon = activity.getResources().getDrawable(R.drawable.ic_empty);
                break;*/
            case 1:
            case 2:
            case 11: icon = activity.getResources().getDrawable(R.drawable.ic_continue);
                break;
            case 3:  icon = activity.getResources().getDrawable(R.drawable.ic_slight_left);
                break;
            case 4:  icon = activity.getResources().getDrawable(R.drawable.ic_turn_left);
                break;
            case 5:  icon = activity.getResources().getDrawable(R.drawable.ic_sharp_left);
                break;
            case 6:  icon = activity.getResources().getDrawable(R.drawable.ic_slight_right);
                break;
            case 7:  icon = activity.getResources().getDrawable(R.drawable.ic_turn_right);
                break;
            case 8:  icon = activity.getResources().getDrawable(R.drawable.ic_sharp_right);
                break;
            case 12:
            case 13:icon = activity.getResources().getDrawable(R.drawable.ic_u_turn);
                break;
            case 24:
            case 25:
            case 26:icon = activity.getResources().getDrawable(R.drawable.ic_arrived);
                break;
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:icon = activity.getResources().getDrawable(R.drawable.ic_roundabout);
                break;
            default: icon = activity.getResources().getDrawable(R.drawable.ic_empty);
                break;
        }

        nMarker.setImage(icon);

    }

    //for Tbt listview with directions
    public IconManeuver (Integer ManeuverType) {

        switch (ManeuverType) {
            //check table here to see what ID tells you http://open.mapquestapi.com/guidance/#maneuvertypes
            /*case 0:
            case 2:
            case 9:
            case 10:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
                icon = activity.getResources().getDrawable(R.drawable.ic_empty);
                break;*/
            case 1:
            case 2:
            case 11: Nicon = 0;
                //icon = activity.getResources().getDrawable(R.drawable.ic_continue);
                break;
            case 3:  Nicon = 1;
            //icon = activity.getResources().getDrawable(R.drawable.ic_slight_left);
                break;
            case 4:  Nicon = 2;
                //icon = activity.getResources().getDrawable(R.drawable.ic_turn_left);
                break;
            case 5:  Nicon = 3;
                //icon = activity.getResources().getDrawable(R.drawable.ic_sharp_left);
                break;
            case 6: Nicon = 4;
                //icon = activity.getResources().getDrawable(R.drawable.ic_slight_right);
                break;
            case 7:  Nicon = 5;
                //icon = activity.getResources().getDrawable(R.drawable.ic_turn_right);
                break;
            case 8:  Nicon = 6;
                //icon = activity.getResources().getDrawable(R.drawable.ic_sharp_right);
                break;
            case 12:
            case 13: Nicon = 7;
                //icon = activity.getResources().getDrawable(R.drawable.ic_u_turn);
                break;
            case 24:
            case 25:
            case 26: Nicon = 8;
                //icon = activity.getResources().getDrawable(R.drawable.ic_arrived);
                break;
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34: Nicon = 9;
                //icon = activity.getResources().getDrawable(R.drawable.ic_roundabout);
                break;
            default: Nicon = 10;
            //icon = activity.getResources().getDrawable(R.drawable.ic_empty);
                break;
        }

    }


    public Integer getNicon() {
        return Nicon;
    }
}
