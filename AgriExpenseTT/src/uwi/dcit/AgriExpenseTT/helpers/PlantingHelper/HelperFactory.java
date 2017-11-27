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

    public HelperFactory(Context context,SQLiteDatabase db, DbHelper dbh){
        this.context = context;
        this.db = db;
        this.dbh = dbh;
    }

    public HelperFactory(){
        this.context = context;
        this.db = db;
        this.dbh = dbh;
    }


    public Helper getHelper(String helptype) {
        if (helptype == null) return null;
        helptype = helptype.toLowerCase();
        switch (helptype) {
            case "fertilizes":
                return new FertilizerHelper(context,db,dbh);
            case "chemicals":
                return new ChemicalHelper(context,db,dbh);
            case "crops":
                return new CropPlantHelper(context,db,dbh);
            case "cropsresource":
                return new CropPlantHelper();
            case "soil":
                return new SoilHelper(context,db,dbh);
            default:
                return new GeneralHelper(context,db,dbh,helptype);
        }

    }
}
