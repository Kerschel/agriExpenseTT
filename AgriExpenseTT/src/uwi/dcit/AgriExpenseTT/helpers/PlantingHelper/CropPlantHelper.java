package uwi.dcit.AgriExpenseTT.helpers.PlantingHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.DHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbQuery;

/**
 * Created by kersc on 11/24/2017.
 */

public class CropPlantHelper extends Helper {

    public CropPlantHelper(Context context, SQLiteDatabase db, DbHelper dbh){
        super(DHelper.cat_plantingMaterial, R.raw.plantingmat);
        super.populate(context,db,dbh);
    }

    public CropPlantHelper(){
        super(DHelper.cat_plantingMaterial, R.raw.plantingmat);
    }

    @Override
    public void addImage(Context context,HashMap<String,Integer> resources){
        JSONObject materials = getJson(context);
        JSONArray array = null;
        try {
            array = materials.getJSONArray("list"); // Gets the name of all categories of materials
            String strings[] = new String[array.length()];
            for(int i=0;i<strings.length;i++) {
                strings[i] = array.getString(i);
            }
            for(String name:strings){
                 int drawableId = getDrawableId(name.toLowerCase().replace(" ","_"));
                 if(drawableId!=-1)
                 resources.put(name,drawableId);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
