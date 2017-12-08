package uwi.dcit.AgriExpenseTT.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import uwi.dcit.AgriExpenseTT.models.LocalCycle;
import uwi.dcit.AgriExpenseTT.models.LocalResourcePurchase;
import uwi.dcit.AgriExpenseTT.models.ResourceContract;

/**
 * Created by Gerard Rique on 07/12/2017.
 */

public class ResourceManager extends DataManager {

    public ResourceManager(Context context) {
        super(context);
    }

    public ResourceManager(Context context, SQLiteDatabase db, DbHelper dbh) {
        super(context, db, dbh);
    }

    @Override
    public void delete(int id) {
        DataManager purchaseManager = new PurchaseManager(this.context, this.db, this.dbh);
        //get all purchases of rId
        //delete them all using the deletePurchase above (ready to use version)
        //get all cycles who's cycleId is rId
        //delete them all using the deleteCycle above (ready to use version)
        //delete resource and record in transaction log
        //-----Not Sure If we're having resources in the cloud

        ArrayList<LocalResourcePurchase> pList = new ArrayList<>();
        DbQuery.getResourcePurchases(db, dbh, pList, id);
        for (LocalResourcePurchase aPList : pList) {
            //this.deletePurchase(aPList.toRPurchase());
            purchaseManager.delete(aPList.toRPurchase().getPId());
        }

        ArrayList<LocalCycle> cList = new ArrayList<>();
        DbQuery.getCycles(db, dbh, cList);
        for (LocalCycle c : cList) {
            if (c.getCropId() == id)
                //this.deleteCycle(c);
                this.delete(c.getId());
        }
        //delete resource
        db.delete(ResourceContract.ResourceEntry.TABLE_NAME, ResourceContract.ResourceEntry._ID+"="+id, null);
        //not bothering to record in transaction log if not storing resources in cloud
        //not bothering to store in redo log if res not going to cloud
    }
}
