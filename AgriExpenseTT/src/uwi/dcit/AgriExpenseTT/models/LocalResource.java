package uwi.dcit.AgriExpenseTT.models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kersc on 11/22/2017.
 */

@SuppressLint("ParcelCreator")
public class LocalResource implements Parcelable {
    private String name;
    private String type;


    public LocalResource(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}




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
