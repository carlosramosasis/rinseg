package rinseg.asistp.com.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Carlos Ramos on 27/10/2016.
 */
public class EventRO  extends RealmObject{
    private int id ;
    private String displayName;
    private String name;

    public RealmList<EventItemsRO> eventItems;

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        id = _id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String _displayName) {
        displayName = _displayName;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}



    @Override
    public String toString() {
        return this.displayName; // Value to be displayed in the Spinner
    }



}
