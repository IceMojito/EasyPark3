package mojito.easypark3;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
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





public class DetailActivity extends Activity {

    //definition of tag for razdalja
    private static final String TAG_RAZDALJA = "razdalja";

    private static final String TAG_IME = "Ime";
    private static final String TAG_PROSTA_M = "P_kratkotrajniki";

    AlertDialog aDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //intent?
        Intent in = getIntent();
        //String razdalja = in.getStringExtra(("razdalja"));//gets name from intent
        HashMap<String, String> selectedPark = (HashMap<String, String>)in.getSerializableExtra("selectedPark");


        //return error message if json is empty, otherwise proceed with json to listview
        if (selectedPark == null) {
            aDialog = new AlertDialog.Builder(DetailActivity.this).create();
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

            //to populate Textview in activity_details
//            TextView text = (TextView) findViewById(R.id.ime);
//            text.setText(selectedPark.get("Ime"));
            populateTW(R.id.ime,"Ime",selectedPark);
            populateTW(R.id.prostaM,"P_kratkotrajniki",selectedPark);
            populateTW(R.id.razdalja, "razdalja", selectedPark);
            populateTW(R.id.X,"KoordinataX_wgs",selectedPark);
            populateTW(R.id.Y,"KoordinataY_wgs",selectedPark);
            //TODO: DODAJ VSE MOŽNE VREDNOSTI, TO MORAŠ NAREDIT ŽE UBISTVU NA ZAČETKU KO PARSA....
        }



    }


    //method to populate textview
    private void populateTW (Integer Rid, String nameS, HashMap<String, String> itemInfo) {
        TextView text = (TextView) findViewById(Rid);
        text.setText(itemInfo.get(nameS));
    }


}
