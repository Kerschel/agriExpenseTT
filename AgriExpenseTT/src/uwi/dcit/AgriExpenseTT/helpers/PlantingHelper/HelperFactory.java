package uwi.dcit.AgriExpenseTT.helpers.PlantingHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import uwi.dcit.AgriExpenseTT.helpers.DbHelper;

/**
 * Created by kersc on 11/24/2017.
 */

public class HelperFactory {
    private Context context;
    private SQLiteDatabase db;
    private DbHelper dbh;


    public HelperFactory(Context ctx, SQLiteDatabase db){
        this.context = context;
        this.db = this.db;
        this.dbh = dbh;
    }

    public HelperFactory() {

    }


    public Helper getHelper(String helptype) {
        if (helptype == null) return null;
        helptype = helptype.toLowerCase();
        try {
            switch (helptype) {
                case "fertilizes":
                    return new FertilizerHelper(context, db);
                case "chemicals":
                    return new ChemicalHelper(context, db);
                case "crops":
                    return new CropPlantHelper(context, db);
                case "cropsresource":
                    return new CropPlantHelper();
                case "soil":
                    return new SoilHelper(context, db);
                default:
                    return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
