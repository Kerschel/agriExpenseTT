package uwi.dcit.AgriExpenseTT.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import uwi.dcit.AgriExpenseTT.cloud.CloudInterface;
import uwi.dcit.AgriExpenseTT.models.CycleContract;
import uwi.dcit.AgriExpenseTT.models.CycleResourceContract;
import uwi.dcit.AgriExpenseTT.models.LocalCycleUse;
import uwi.dcit.AgriExpenseTT.models.ResourcePurchaseContract;

import uwi.dcit.agriexpensesvr.accountApi.model.Account;
import uwi.dcit.agriexpensesvr.cycleApi.model.Cycle;
import uwi.dcit.agriexpensesvr.resourcePurchaseApi.model.ResourcePurchase;

/**
 * Created by Gerard Rique on 05/12/2017.
 */

public class CycleManager extends DataManager {

    CycleManager(Context context){
        super(context);
    }

    public CycleManager(Context context, SQLiteDatabase db, DbHelper dbh){
        super(context, db, dbh);
    }

    @Override
    public int insert(Manageable manageable) {
        int id =  super.insert(manageable);
        CloudInterface c= new CloudInterface(context,db,dbh);// new CloudInterface(context);
        long time = System.currentTimeMillis()/1000;
        if(acc!=null){
            //insert into transaction table
            DbQuery.insertRedoLog(db, dbh, CycleContract.CycleEntry.TABLE_NAME, id, "ins");
            //try insert into cloud
            if (acc.getSignedIn() == 1 && NetworkHelper.isWifiAvailable(this.context)) {
                Log.i("IINNSSEERRTT", "Going to insert into cloud!");
                c.insertCycle();
                c.updateUpAccC(time);
            }
        }

        return id;
    }

    @Override
    public void delete(int id) {
        ArrayList<LocalCycleUse> list = new ArrayList<>();
        DbQuery.getCycleUse(db, dbh, id, list, null);

        for (LocalCycleUse l : list) {
            Log.i("Cycle Removal", ":" + l);
            this.deleteCycleUse(l, "Cycle");//already does the recording into the redo log(cloud) and transaction log
        }
        //delete cycle
//		db.delete(CycleContract.CycleEntry.TABLE_NAME, CycleContract.CycleEntry._ID+"="+c.getId(), null);
        try {
            DbQuery.deleteRecord(db, dbh, CycleContract.CycleEntry.TABLE_NAME, id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        tL.insertTransLog(CycleContract.CycleEntry.TABLE_NAME, id, TransactionLog.TL_DEL);
        if(acc!=null){
            //insert into redo log (cloud)
            DbQuery.insertRedoLog(db, dbh, CycleContract.CycleEntry.TABLE_NAME, id, TransactionLog.TL_DEL);
            if (acc.getSignedIn() == 1 && NetworkHelper.isWifiAvailable(this.context)) {
                CloudInterface cloud= new CloudInterface(context,db,dbh);//new CloudInterface(context);
                cloud.deleteCycle();
            }
        }
        DbQuery.getTransactionLog(db);
    }

    @Override
    public boolean update(int id, ContentValues cv) {

        int result = db.update(CycleContract.CycleEntry.TABLE_NAME, cv, CycleContract.CycleEntry._ID+"="+id, null);
        //update the cloud
        TransactionLog tl = new TransactionLog(dbh, db,context);
        tl.insertTransLog(CycleContract.CycleEntry.TABLE_NAME, id,TransactionLog.TL_UPDATE);
        CloudInterface cloud= new CloudInterface(context,db,dbh);// new CloudInterface(context);
        long time = System.currentTimeMillis()/1000;
        DbQuery.updateAccount(db,time);
        if(acc!=null){
            DbQuery.insertRedoLog(db, dbh, CycleContract.CycleEntry.TABLE_NAME, id, TransactionLog.TL_UPDATE);
            //record in transaction log
            if (acc.getSignedIn() == 1 && NetworkHelper.isWifiAvailable(this.context)) {
                cloud.updateCycle();
                cloud.updateUpAccC(time);
            }
        }
        return (result != -1);
    }

    public void insertCycleUse(int cycleId, int resPurchaseId, double qty, String type, String quantifier, double useCost) {
        //insert into database
        int id=DbQuery.insertResourceUse(db, dbh, cycleId, type, resPurchaseId, qty,quantifier,useCost, tL);
        //insert into redo log table
        DbQuery.insertRedoLog(db, dbh, CycleResourceContract.CycleResourceEntry.TABLE_NAME, id, "ins");
        //try to insert into cloud
        CloudInterface cloud = new CloudInterface(context, db, dbh);//new CloudInterface(context);
        long time = System.currentTimeMillis()/1000;
        DbQuery.updateAccount(db,time);
        if(acc!=null ){
            if (acc.getSignedIn() == 1 && NetworkHelper.isWifiAvailable(this.context)) {
                cloud.insertCycleUseC();
                cloud.updateUpAccC(time);
            }
        }
        Log.i("SPIT TRANSCARION LOG","SPIT");
        DbQuery.getTransactionLog(db);
    }

    public void deleteCycleUse(LocalCycleUse l, String type){
        //update the Purchase that was used (local) add back the amount that was used
        //update cloud, record it in the redo Log purchase Id and the table
        //update the Cycle's total spent (local) subtract the usage cost from the cycle's total spent
        //update cloud, record the update in the redo log
        //finally delete cycleUse (locally)
        //delete cycleUse in cloud, by recording the delete in the redo log
        CloudInterface clo= new CloudInterface(context,db,dbh);// new CloudInterface(context);
        Log.i("Printing Local Use!",">>"+l);
//		//PURCHASE
//		//updating local Purchase
        if(type.equals("Cycle")) {
            ResourcePurchase p = DbQuery.getARPurchase(db, dbh, l.getPurchaseId());
            ContentValues cv = new ContentValues();
            cv.put(ResourcePurchaseContract.ResourcePurchaseEntry.RESOURCE_PURCHASE_REMAINING, (l.getAmount() + p.getQtyRemaining()));
            db.update(ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME, cv, ResourcePurchaseContract.ResourcePurchaseEntry._ID + "=" + l.getPurchaseId(), null);
            //record transaction in log
            tL.insertTransLog(ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME, l.getPurchaseId(), TransactionLog.TL_UPDATE);
            if (acc != null) {
                //redo log (cloud)
                DbQuery.insertRedoLog(db, dbh, ResourcePurchaseContract.ResourcePurchaseEntry.TABLE_NAME, l.getPurchaseId(), TransactionLog.TL_UPDATE);
                if (acc.getSignedIn() == 1 && NetworkHelper.isWifiAvailable(this.context)) {
                    //NOTE
                    clo.updatePurchase();
                }
            }
        }

        //CYCLE
        //updating local Cycle
        if(type.equals("Purchase")) {
            Log.i("CYCLE UPDATE", ">><<<::" + l.getCycleid());
            Cycle c = DbQuery.getCycle(db, dbh, l.getCycleid());
            ContentValues cv = new ContentValues();
            cv.put(CycleContract.CycleEntry.CROPCYCLE_TOTALSPENT, (c.getTotalSpent() - l.getUseCost()));
            db.update(CycleContract.CycleEntry.TABLE_NAME, cv, CycleContract.CycleEntry._ID + "=" + l.getCycleid(), null);
            //record transaction in log
            tL.insertTransLog(CycleContract.CycleEntry.TABLE_NAME, l.getCycleid(), TransactionLog.TL_UPDATE);
            if (acc != null) {
                //redo log (cloud)
                DbQuery.insertRedoLog(db, dbh, CycleContract.CycleEntry.TABLE_NAME, l.getCycleid(), TransactionLog.TL_UPDATE);
                if (acc.getSignedIn() == 1 && NetworkHelper.isWifiAvailable(this.context)) {
                    //NOTE
                    clo.updateCycle();
                }
            }
        }

        //CYCLEUSE
        //Delete CycleUse
        //db.delete(CycleResourceEntry.TABLE_NAME, DbHelper.CYCLE_RESOURCE_ID+"="+l.getId(), null);
        try {
            DbQuery.deleteRecord(db, dbh, CycleResourceContract.CycleResourceEntry.TABLE_NAME, l.getId());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        tL.insertTransLog(CycleResourceContract.CycleResourceEntry.TABLE_NAME, l.getId(), TransactionLog.TL_DEL);
        if(acc!=null){
            //redo log (cloud)
            DbQuery.insertRedoLog(db, dbh, CycleResourceContract.CycleResourceEntry.TABLE_NAME, l.getId(), TransactionLog.TL_DEL);
            if (acc.getSignedIn() == 1 && NetworkHelper.isWifiAvailable(this.context)) {
                //NOTE
                clo.deleteCycleUse();
            }
        }

    }
}
