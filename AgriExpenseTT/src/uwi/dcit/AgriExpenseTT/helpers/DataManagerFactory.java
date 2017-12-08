package uwi.dcit.AgriExpenseTT.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Gerard Rique on 08/12/2017.
 */

public class DataManagerFactory {

    SQLiteDatabase db;
    DbHelper dbh;
    Context context;

    public static String CYCLE_MANAGER = "CycleManager";
    public static String RESOURCE_MANAGER = "ResourceManager";
    public static String PURCHASE_MANAGER = "PurchaseManager";


    public DataManagerFactory(Context context) {
        this.context = context;
        dbh= new DbHelper(context);
//		db=dbh.getReadableDatabase();
        db = dbh.getWritableDatabase();
    }

    public DataManagerFactory(SQLiteDatabase db, DbHelper dbh, Context context) {
        this.db = db;
        this.dbh = dbh;
        this.context = context;
    }

    public DataManager getManager(String managerType){
        if(managerType.equalsIgnoreCase(DataManagerFactory.CYCLE_MANAGER))
            return new CycleManager(context, db, dbh);
        else if(managerType.equalsIgnoreCase(DataManagerFactory.RESOURCE_MANAGER))
            return new ResourceManager(context, db, dbh);
        else if(managerType.equalsIgnoreCase(DataManagerFactory.PURCHASE_MANAGER))
            return new ResourceManager(context, db, dbh);

        return null;
    }


}
