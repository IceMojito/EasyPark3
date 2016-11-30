package mojito.easypark3;



import android.content.Context;


import android.widget.ListAdapter;


import android.widget.SimpleAdapter;


import org.osmdroid.bonuspack.routing.Road;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mohor on 08/08/2016.
 */

//getting information and preparing informatin for generating listview with instructions
public class TbTactivity {
    public ListAdapter adapterTbt;

    public TbTactivity() {

    }


    public TbTactivity (Road road, Context context, int[] icons) {

        ////get informations for generating listview with instructions

        //whole list with TbT instructions
        ArrayList TbTlist=new ArrayList<HashMap<String,String>>();

        // tmp hashmap for single TbT information
        HashMap<String, String> TbT1 = new HashMap<String, String>();

        // Add data to the ListView
        // looping through TbT
        for (int i = 0; i < road.mNodes.size(); i++) {
            Integer TbTicon = new IconManeuver(road.mNodes.get(i).mManeuverType).getNicon();

            TbT1 = new HashMap<String, String>();
            TbT1.put("instruction", road.mNodes.get(i).mInstructions);
            TbT1.put("distance", String.valueOf(String.format("%.2f", road.mNodes.get(i).mLength)));  //get value from road, round double, double to string
            //TbT1.put("pic", String.valueOf(road.mNodes.get(i).mManeuverType));
            TbT1.put("icon", String.valueOf(icons[TbTicon]));

            TbTlist.add(TbT1);
        }

        adapterTbt = new SimpleAdapter(
                context, TbTlist,
                R.layout.tbt_list_item, new String[]{"instruction", "distance",
                "icon"}, new int[]{R.id.instructions,
                R.id.distance, R.id.icon_way});



    }

    public ListAdapter getAdapter() {
        return adapterTbt;
    }

}
