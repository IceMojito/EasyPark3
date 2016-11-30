package mojito.easypark3;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class ListActivityC extends ListActivity {

    // URL to get contacts JSON
    private static String url = "http://opendata.si/promet/parkirisca/lpt/";

    // JSON Node names
    //basic information
    private static final String TAG_PARKIRISCA = "Parkirisca";
    private static final String TAG_ID = "ID_Parkirisca";
    private static final String TAG_IME = "Ime";
    private static final String TAG_ZASEDENOST = "zasedenost";
    private static final String TAG_PROSTA_M = "P_kratkotrajniki";

    //coordinates
    private static final String TAG_KOO_X = "KoordinataX_wgs";
    private static final String TAG_KOO_Y = "KoordinataY_wgs";

    //definition of tag for razdalja
    private static final String TAG_RAZDALJA = "razdalja";

    //DELETEpublic static ArrayList<HashMap<String, String>> parkirisceListMain = MainActivity.parkirisceList;


    //variable to flip order of sorting
    public static boolean flip = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Calling async task to get json
        new GetParkirisca().execute();



    }


    // to refresh data
    public void activityrefresh(View view)
    {
        ////PARSE DATA
        // Calling async task to get json
           //to clear parsed data from the mainactivity: HAVE NO IDEA IF THIS IS REALLY NECESSARY
        MainActivity.parkirisceList = null;
        new GetParkirisca().execute();
    }

    public void activitysortABC(View view)
    {
        //// to sort data (items) by the Ime
        sorting("Ime");
    }

    public void activitysortZas(View view)
    {
        //// to sort data (items) by the free parking spaces
        sorting("P_kratkotrajniki");
    }

    public void activitysortRazd(View view)
    {
        //// to sort data (items) by the free parking spaces
        sorting("razdalja");
    }


    // to go to activity details of the data
    // UNDER forDetailsViewListener()



    /**
     * Async task class to get json by making HTTP call
     */

    public class GetParkirisca extends AsyncTask<Void, Void, Void> {


        // Hashmap for ListView
        //DELETE ArrayList<HashMap<String, String>> parkirisceList;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ListActivityC.this);
            pDialog.setMessage("Prosimo počakajte...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);

            Log.d("Response: ", "> " + jsonStr);


            //two variables for ParseJSON
            //testLocation = mLocationOverlay.getMyLocation() == null;
            //startPoint = mLocationOverlay.getMyLocation();
            //V tem activityu, se lahko kot context da ListActivity.this namesto MainActivity.this, ker se context potrebuje samo za getResources() (ikone)
            MainActivity.parkirisceList = MainActivity.ParseJSON(jsonStr, ListActivityC.this);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            AlertDialog aDialog;
            //return error message if json is empty, otherwise proceed with json to listview
            //TODO: naredi da prenese preko intenta parkirisce list in da ni treba kopirat iz public mainactivity
            if (MainActivity.parkirisceList == null) {
                aDialog = new AlertDialog.Builder(ListActivityC.this).create();
                aDialog.setTitle("Napaka!");
                aDialog.setMessage("Aplikaciji ni uspelo pridobiti podatke. Preverite, če imate vklopljen WIFI oziroma prenos podatkov!");
                aDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "V redu",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                aDialog.show();
            } else {


                /**
                 * Updating parsed JSON data into ListView
                 * */

                // prej je blo ListAdapter adapter = new SimpleAdapter( + ni bila definirana spremenljivka v MainActivity
                ListAdapter adapter = new SimpleAdapter(
                        ListActivityC.this, MainActivity.parkirisceList,
                        R.layout.list_item, new String[]{TAG_IME, TAG_PROSTA_M,
                        TAG_RAZDALJA}, new int[]{R.id.ime,
                        R.id.prostaM, R.id.razdalja});

                setListAdapter(adapter);

                //needed for detailed listview
                forDetailsViewListener();

            }

        }

    }


    /*private ArrayList<HashMap<String, String>> ParseJSON(String json) {


        if (json != null) {
            try {
                // Hashmap for ListView
                ArrayList<HashMap<String, String>> parkirisceList = new ArrayList<HashMap<String, String>>();

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




                    //TO DO: NAREDI CLASS, KI IZRAČUNA RAZDALJO IZ TRENUTNE LOKACIJE DO TRENUTNO OBRAVNAVANEGA PARKIRIŠČA, INPUTS: X Y KOORDINATA!
                    String razdalja = "neznana";

                    // tmp hashmap for single parkirisce
                    HashMap<String, String> parkirisce = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    parkirisce.put(TAG_ID, id);
                    parkirisce.put(TAG_IME, ime);
                    parkirisce.put(TAG_PROSTA_M, prostaM);
                    //koordinate NE DELA
                    parkirisce.put(TAG_KOO_X, kooX);
                    parkirisce.put(TAG_KOO_Y, kooY);

                    // za izračunano razdaljo
                    parkirisce.put(TAG_RAZDALJA, razdalja);


                    // adding student to students list
                    parkirisceList.add(parkirisce);
                }

                //NECESSARY for sorting the arraylist
                //DELETE parkirisceListMain = parkirisceList;

                return parkirisceList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }*/

    ////function to sort
    private void sorting (final String sortBy) {
        /**
         * @sortBy- data name of the arraylist hashmap
         */

        //to change order of flipping (from highest to lowest and other way around)
        flip = !flip;

        //// to sort data (items) by the sortBy


        Collections.sort(MainActivity.parkirisceList, new Comparator<HashMap< String,String >>() {

            @Override
            public int compare(HashMap<String, String> first,
                               HashMap<String, String> second) {

                String firstValue = first.get(sortBy);
                String secondValue = second.get(sortBy);

                //to change order of flipping (from highest to lowest and other way around)
                if (flip) {
                    String temp = secondValue;
                    secondValue = firstValue;
                    firstValue = temp;
                }

                //for case when integer comparison is needed. if one of the Values it string, this rule does not apply.
                if (isInteger(first.get(sortBy)) && isInteger(second.get(sortBy))) {
                    Integer firstValueInt = Integer.parseInt(firstValue);
                    Integer secondValueInt = Integer.parseInt(secondValue);
                    //flipped values to sort from largest to smallest
                    return secondValueInt.compareTo(firstValueInt);

                } else {
                    return firstValue.compareTo(secondValue);
                }
            }
        });


        //TODO2: NAREDI METODO!
        ListAdapter adapter = new SimpleAdapter(
                ListActivityC.this, MainActivity.parkirisceList,
                R.layout.list_item, new String[]{TAG_IME, TAG_PROSTA_M,
                TAG_RAZDALJA}, new int[]{R.id.ime,
                R.id.prostaM, R.id.razdalja});

        setListAdapter(adapter);

        //needed for detailed listview
        forDetailsViewListener();
    }

    //to test if string
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    //method which is needed for DetailsActivity

    public void forDetailsViewListener () {
        //for DetailsActivity
        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                HashMap<String, String> selectedPark = (HashMap<String, String>) parent.getItemAtPosition(position);

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                // sending data to new activity
                i.putExtra("selectedPark", selectedPark);
                i.putExtra("razdalja", selectedPark.get("razdalja"));
                startActivity(i);




            }
        });
    }




}
