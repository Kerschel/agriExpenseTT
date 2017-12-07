package uwi.dcit.AgriExpenseTT.helpers.PlantingHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.DHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbHelper;

/**
 * Created by kersc on 11/25/2017.
 */

public class SoilHelper extends Helper {
    public SoilHelper (Context context, SQLiteDatabase db) {
        super(DHelper.cat_soilAmendment, R.raw.soilammendments);

    }
}
