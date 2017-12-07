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

    public CropPlantHelper(Context context, SQLiteDatabase db){
        super(DHelper.cat_plantingMaterial, R.raw.plantingmat);
    }

    public CropPlantHelper(){
        super(DHelper.cat_plantingMaterial, R.raw.plantingmat);
    }




}
