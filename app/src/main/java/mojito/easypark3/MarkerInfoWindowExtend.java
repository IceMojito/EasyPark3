package mojito.easypark3;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.osmdroid.bonuspack.utils.BonusPackHelper;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

/**
 * Created by mohor on 09/07/2016.
 */
public class MarkerInfoWindowExtend extends MarkerInfoWindow {

    public MarkerInfoWindowExtend(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);



/*        //default behavior: close it when clicking on the bubble:
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP)
                    //to make visible GO TO button
                    MarkerExtend.ButtonGoToMethod.setVisibility(View.INVISIBLE);
                    close();
                return true;
            }
        });*/

    }


    //BETTER TO USE THIS METHOD TO HIDE
    @Override public void onClose() {
        super.onClose();
        mMarkerRef = null;
        //to make visible GO TO button
        //MarkerExtend.ButtonGoToMethod.setVisibility(View.INVISIBLE);
    }



}
