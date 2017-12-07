package uwi.dcit.AgriExpenseTT.helpers.PlantingHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import uwi.dcit.AgriExpenseTT.R;
import uwi.dcit.AgriExpenseTT.helpers.DHelper;
import uwi.dcit.AgriExpenseTT.helpers.DbHelper;

/**
 * Created by kersc on 11/24/2017.
 */

public class FertilizerHelper extends Helper{

    public FertilizerHelper (Context context, SQLiteDatabase db){
        super(DHelper.cat_fertilizer, R.raw.fertilizers);
    }

}
