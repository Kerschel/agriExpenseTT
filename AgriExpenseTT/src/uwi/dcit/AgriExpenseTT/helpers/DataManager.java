package uwi.dcit.AgriExpenseTT.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import uwi.dcit.AgriExpenseTT.cloud.CloudInterface;
import uwi.dcit.AgriExpenseTT.models.CycleContract;
import uwi.dcit.AgriExpenseTT.models.CycleResourceContract;
import uwi.dcit.AgriExpenseTT.models.LocalCycle;
import uwi.dcit.AgriExpenseTT.models.LocalCycleUse;
import uwi.dcit.AgriExpenseTT.models.LocalResourcePurchase;
import uwi.dcit.AgriExpenseTT.models.ResourceContract;
import uwi.dcit.AgriExpenseTT.models.ResourcePurchaseContract;
import uwi.dcit.agriexpensesvr.accountApi.model.Account;
import uwi.dcit.agriexpensesvr.cycleApi.model.Cycle;
import uwi.dcit.agriexpensesvr.resourcePurchaseApi.model.ResourcePurchase;



public abstract class DataManager {
	SQLiteDatabase db;
	DbHelper dbh;
	Context context;
	TransactionLog tL;
	Account acc;
	public DataManager(Context context){
		dbh= new DbHelper(context);
//		db=dbh.getReadableDatabase();
        db = dbh.getWritableDatabase();
		this.context=context;
		tL=new TransactionLog(dbh,db,context);
		acc=DbQuery.getUpAcc(db);
	}
	public DataManager(Context context,SQLiteDatabase db,DbHelper dbh){
		this.dbh= dbh;
		this.db=db;
		this.context=context;
		tL=new TransactionLog(dbh,db,context);
		acc=DbQuery.getUpAcc(db);
	}


    public int insert(Manageable manageable){
        ContentValues cv = manageable.convertToContentValues(db, dbh);
        String tableName = manageable.getTableName();
        int id = DbQuery.insert(db, dbh, cv, tableName, tL);
        CloudInterface c= new CloudInterface(context,db,dbh);// new CloudInterface(context);
        long time = System.currentTimeMillis()/1000;
        DbQuery.updateAccount(db,time);
        return id;

    }





	public void delete(int id){
	    return;
	}


	public boolean update(int id, ContentValues cv){
		return true;
	}



}
