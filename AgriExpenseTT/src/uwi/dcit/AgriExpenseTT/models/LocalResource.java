package uwi.dcit.AgriExpenseTT.models;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import uwi.dcit.AgriExpenseTT.helpers.DbHelper;
import uwi.dcit.AgriExpenseTT.helpers.Manageable;

/**
 * Created by kersc on 11/22/2017.
 */

@SuppressLint("ParcelCreator")
public class LocalResource implements Parcelable, Manageable {
    private int id;
    private String name;
    private String type;


    public LocalResource(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    public LocalResource(int id, String name, String type){
        super();
        this.id = id;
        this.name = name;
        this.type = type;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    @Override
    public ContentValues convertToContentValues(SQLiteDatabase db, DbHelper dbh) {
        ContentValues cv=new ContentValues();
        cv.put(ResourceContract.ResourceEntry.RESOURCES_NAME, name);
        cv.put(ResourceContract.ResourceEntry.RESOURCES_TYPE, type);

        return cv;
    }

    @Override
    public String getTableName() {
        return ResourceContract.ResourceEntry.TABLE_NAME;
    }

    @Override
        public int describeContents () {
            return 0;
        }

        @Override
        public void writeToParcel (Parcel parcel,int i){
            parcel.writeString(name);
            parcel.writeString(type);
        }
}
