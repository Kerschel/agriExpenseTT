package uwi.dcit.AgriExpenseTT.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import uwi.dcit.AgriExpenseTT.R;

/**
 * Created by kersc on 11/7/2017.
 */

public class MaterialsHelper {
    private JSONObject materials;

    public MaterialsHelper(Context context, SQLiteDatabase db){
        materials = getJson(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void populateMaterialList(SQLiteDatabase db, DbHelper dbh){
        Iterator<String> iterator = materials.keys(); // Gets the name of all categories of materials
        while (iterator.hasNext()){
            String materialType = iterator.next();
            insertMaterial(db,dbh,materialType);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void insertMaterial(SQLiteDatabase db, DbHelper dbh,String materialType){
        try{
            JSONObject currentObject = materials.getJSONObject(materialType);
            JSONArray material = new JSONArray(currentObject.get("list"));
            // Material Name
            for (int i=0;i<= material.length();i++) {
                DbQuery.insertResource(db, materialType, material.getString(i));
            }
        } catch(JSONException exception){
        }
    }

    public JSONObject getJson(Context context) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.materials);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
            return new JSONObject(json);
        }
            catch (IOException | JSONException ex){
            ex.printStackTrace();
            return null;
        }
    }




}