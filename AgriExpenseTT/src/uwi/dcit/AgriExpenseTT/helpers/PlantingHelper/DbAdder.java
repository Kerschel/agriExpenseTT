package uwi.dcit.AgriExpenseTT.helpers.PlantingHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.DbHelper;

/**
 * Created by kersc on 12/4/2017.
 */

public class DbAdder {
    public static HashMap<String,Integer> resources;
    private Context ctx;
    private SQLiteDatabase db;

    public DbAdder(Context ctx, SQLiteDatabase db) {
        resources = new HashMap<String, Integer>();
        this.ctx = ctx;
        this.db = db;
    }
// Implementation to populate the database with the types of materials
    public void populate(){
        HelperFactory factory = new HelperFactory(ctx,db);
        ArrayList<Helper> helpFactory = new ArrayList<Helper>();
        helpFactory.add(factory.getHelper("chemicals"));
        helpFactory.add(factory.getHelper("soil"));
        helpFactory.add(factory.getHelper("fertilizes"));
        helpFactory.add(factory.getHelper("crops"));

        for(Helper help:helpFactory){
            help.populate(ctx,db);
        }

    }


}
