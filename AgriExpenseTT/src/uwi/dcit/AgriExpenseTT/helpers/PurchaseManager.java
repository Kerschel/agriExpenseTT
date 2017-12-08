package uwi.dcit.AgriExpenseTT.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;

import uwi.dcit.AgriExpenseTT.cloud.CloudInterface;
import uwi.dcit.AgriExpenseTT.models.LocalCycleUse;
import uwi.dcit.AgriExpenseTT.models.ResourcePurchaseContract;

/**
 * Created by Gerard Rique on 05/12/2017.
 */

public class PurchaseManager extends DataManager {

    PurchaseManager(Context context){
        super(context);
    }

    public PurchaseManager(Context context, SQLiteDatabase db, DbHelper dbh){
        super(context, db, dbh);
    }

    @Override
    public int insert(Manageable manageable) {
        int id = super.insert(manageable);

        CloudInterface c= new CloudInterface(context,db,dbh);//new CloudInterface(context);
        long time = System.currentTimeMillis()/1000;

        if(acc!=null){
            //insert into redo log table
            DbQuery.insertRedoLog(db, dbh, ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME, id, "ins");
            //try to insert into cloud
            if (acc.getSignedIn() == 1 && NetworkHelper.isWifiAvailable(this.context)) {
                c.insertPurchase();
                c.updateUpAccC(time);
            }
        }

        return id;
    }

    @Override
    public boolean update(int id, ContentValues cv) {
        int result = db.update(ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME, cv, ResourcePurchaseContract.ResourcePurchaseEntry._ID+"="+id,null);
        //update the cloud
        TransactionLog tl=new TransactionLog(dbh, db,context);
        tl.insertTransLog(ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME, id, TransactionLog.TL_UPDATE);
        CloudInterface cloud= new CloudInterface(context,db,dbh);// new CloudInterface(context);
        long time = System.currentTimeMillis()/1000;
        DbQuery.updateAccount(db,time);
        if(acc!=null){
            DbQuery.insertRedoLog(db, dbh, ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME,id, TransactionLog.TL_UPDATE);
//			record in transaction log
            if (acc.getSignedIn() == 1 && NetworkHelper.isWifiAvailable(this.context)) {
                cloud.updatePurchase();
                cloud.updateUpAccC(time);
            }
        }

        return (result != -1);
    }

    @Override
    public void delete(int id) {

        CycleManager cycleManager = new CycleManager(this.context, this.db, this.dbh);
        //get all cycleUse with the purchase's id
        //delete each one using the deleteCycleUse to remove the cost added to each cycle
        //delete the purchase (locally)
        //put the delete in the redo log

        //getting all the cycleUse
        ArrayList<LocalCycleUse> list = new ArrayList<>();
        DbQuery.getCycleUseP(db, dbh, id, list, null);
        Iterator<LocalCycleUse> itr=list.iterator();
        CloudInterface clo= new CloudInterface(context,db,dbh);//new CloudInterface(context);
        while(itr.hasNext()){
            LocalCycleUse l=itr.next();
            cycleManager.deleteCycleUse(l, "Purchase");//already does the recording into the redo log(cloud) and transaction log
//			clo.deleteCycleUse();
        }
        //delete purchase
        db.delete(ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME, ResourcePurchaseContract.ResourcePurchaseEntry._ID+"="+id, null);
        tL.insertTransLog(ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME, id, TransactionLog.TL_DEL);
        if(acc!=null){
            //redo log (cloud)
            DbQuery.insertRedoLog(db, dbh, ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME, id, TransactionLog.TL_DEL);
            if (acc.getSignedIn() == 1 && NetworkHelper.isWifiAvailable(this.context)) {
                CloudInterface c= new CloudInterface(context,db,dbh);//new CloudInterface(context);
                c.deletePurchase();
            }
        }
    }
}
