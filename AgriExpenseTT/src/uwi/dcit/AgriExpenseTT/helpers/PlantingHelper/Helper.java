package uwi.dcit.AgriExpenseTT.helpers.PlantingHelper;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.api.client.json.Json;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.DbHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbQuery;

/**
 * Created by kersc on 11/23/2017.
 */

public abstract class Helper {

    private JSONObject  materials;
    private int filename;
    private String type;
    private String fieldname = "list";

    public Helper (String type, int filename){
    this.type = type;
    this.filename = filename;
    }

// allows the user to specify the name of the field in the json file
    public void setFieldname(String fieldname){
        this.fieldname = fieldname;
    }
    public int getDrawableId(String name){
        try {
            Field fld = R.drawable.class.getField(name);
            return fld.getInt(null);
        } catch (Exception e) {
            Log.d("No Image","No Image for "+ name);
        }
        return -1;
    }
    public JSONObject getMaterials() {
        return materials;
    }

    public JSONObject getAll(){
        return materials;
    }

    public JSONObject getJson(Context context){
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(filename);

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

    // Reads the json file and looks for the fieldname and extracts the list of data from it
    public void populate(Context context, SQLiteDatabase db){
        materials = getJson(context);
        JSONArray array = null;
        try {
            array = materials.getJSONArray(fieldname); // Gets the name of all categories of materials
            for(int i = 0; i < array.length(); i++){
                DbQuery.insertResource(db,type, array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }


    public void addImage(Context context,HashMap<String,Integer> resources){
        JSONObject materials = getJson(context);
        JSONArray array = null;
        try {
            array = materials.getJSONArray(fieldname); // Gets the name of all categories of materials
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


    public Integer getResourceId(String key,HashMap<String,Integer> resources){
        if(resources.containsKey(key))
            return resources.get(key);
        return R.drawable.plant;
    }
}
