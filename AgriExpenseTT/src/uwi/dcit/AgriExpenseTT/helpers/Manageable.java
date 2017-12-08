package uwi.dcit.AgriExpenseTT.helpers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Gerard Rique on 05/12/2017.
 */

public interface Manageable {
    public String getTableName();
    public int getId();
    public ContentValues convertToContentValues(SQLiteDatabase db, DbHelper dbh);
}
