package uwi.dcit.AgriExpenseTT.helpers.PlantingHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.api.client.json.Json;

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
import java.util.Iterator;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.DbHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbQuery;

/**
 * Created by kersc on 11/23/2017.
 */

public abstract class Helper {

    private JSONObject materials;
    private int filename;
    private String type;

    public Helper (String type, int filename){
    this.type = type;
    this.filename = filename;
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

    public void populate(Context context, SQLiteDatabase db, DbHelper dbh){
        materials = getJson(context);
        JSONArray array = null;
        try {
            array = materials.getJSONArray("list"); // Gets the name of all categories of materials
            for(int i = 0; i < array.length(); i++){
                DbQuery.insertResource(db, dbh, type, array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
