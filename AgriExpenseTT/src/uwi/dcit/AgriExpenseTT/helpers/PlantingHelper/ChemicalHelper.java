package uwi.dcit.AgriExpenseTT.helpers.PlantingHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.DHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbHelper;

/**
 * Created by kersc on 11/24/2017.
 */

public class ChemicalHelper extends Helper {

    public ChemicalHelper(Context context, SQLiteDatabase db, DbHelper dbh) {
        super(DHelper.cat_plantingMaterial, R.raw.plantingmat);
        super.populate(context,db,dbh);
    }

}
