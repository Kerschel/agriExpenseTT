package uwi.dcit.AgriExpenseTT.helpers.PlantingHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.DHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbHelper;

/**
 * Created by kersc on 11/25/2017.
 */

public class GeneralHelper extends Helper {

    public GeneralHelper(Context context, SQLiteDatabase db, DbHelper dbh,String type) {
        super(DHelper.cat_plantingMaterial, R.raw.plantingmat);
        super.populate(context,db,dbh);
    }
}
