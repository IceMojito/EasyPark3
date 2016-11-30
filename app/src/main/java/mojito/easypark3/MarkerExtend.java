package mojito.easypark3;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Point;
import android.graphics.Rect;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by mohor on 23/06/2016.
 * marker extend to enable button GOTO and import data...
 */
public class MarkerExtend extends Marker {
    public static Button ButtonGoToMethod;

    //needed for: public MarkerExtend(MapView mapView, final Context resourceProxy)
    //this was added to change original MarkerInfoWindowExtend mDefaultInfoWindow from library Marker.java TO MarkerInfoWindowExtend mDefaultInfoWindow
    Resources resource;
    protected static MarkerInfoWindowExtend mDefaultInfoWindow = null;



    public MarkerExtend (MapView mapView, Button ButtonGoTo)
    {
        this(mapView, (mapView.getContext()));
        //to include button in hitTest
        ButtonGoToMethod = ButtonGoTo;

    }

    //this was added to change original MarkerInfoWindowExtend mDefaultInfoWindow from library Marker.java TO MarkerInfoWindowExtend mDefaultInfoWindow
    public MarkerExtend(MapView mapView, final Context resourceProxy) {
        super(mapView, resourceProxy);
        resource = mapView.getContext().getResources();
        mBearing = 0.0f;
        mAlpha = 1.0f; //opaque
        mPosition = new GeoPoint(0.0, 0.0);
        mAnchorU = ANCHOR_CENTER;
        mAnchorV = ANCHOR_CENTER;
        mIWAnchorU = ANCHOR_CENTER;
        mIWAnchorV = ANCHOR_TOP;
        mDraggable = false;
        mIsDragged = false;
        mPositionPixels = new Point();
        mPanToView = true;
        mFlat = false; //billboard
        mOnMarkerClickListener = null;
        mOnMarkerDragListener = null;
        if (mDefaultIcon == null)
            mDefaultIcon = resourceProxy.getResources().getDrawable(org.osmdroid.library.R.drawable.marker_default);
        mIcon = mDefaultIcon;
        if (mDefaultInfoWindow == null || mDefaultInfoWindow.getMapView() != mapView){
            //build default bubble, that will be shared between all markers using the default one:
			/* pre-aar version
			Context context = mapView.getContext();
			String packageName = context.getPackageName();
			int defaultLayoutResId = context.getResources().getIdentifier("bonuspack_bubble", "layout", packageName);
			if (defaultLayoutResId == 0)
				Log.e(BonusPackHelper.LOG_TAG, "Marker: layout/bonuspack_bubble not found in "+packageName);
			else
				mDefaultInfoWindow = new MarkerInfoWindow(defaultLayoutResId, mapView);
			*/
            //get the default layout now included in the aar library
            mDefaultInfoWindow = new MarkerInfoWindowExtend(org.osmdroid.library.R.layout.bonuspack_bubble, mapView);
        }
        setInfoWindow(mDefaultInfoWindow);
    }



    @Override
    public boolean hitTest(final MotionEvent event, final MapView mapView) {

        //to make visible GO TO button
        ButtonGoToMethod.setVisibility(View.VISIBLE);
        //to provide location of endpoint for the road
        MainActivity.endPoint = this.getPosition();

        //repeat from original method
        final Projection pj = mapView.getProjection();
        pj.toPixels(mPosition, mPositionPixels);
        final Rect screenRect = pj.getIntrinsicScreenRect();
        int x = -mPositionPixels.x + screenRect.left + (int) event.getX();
        int y = -mPositionPixels.y + screenRect.top + (int) event.getY();
        boolean hit = mIcon.getBounds().contains(x, y);
        return hit;
    }

/*    @Override
    public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView){
        boolean touched = hitTest(event, mapView);
        if (touched){
            if (mOnMarkerClickListener == null){
                return onMarkerClickDefault(this, mapView);
            } else {
                return mOnMarkerClickListener.onMarkerClick(this, mapView);
            }
        } else
            return touched;
    }*/


}
