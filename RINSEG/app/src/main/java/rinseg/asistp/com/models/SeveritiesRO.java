package rinseg.asistp.com.models;

import io.realm.RealmObject;

/**
 * Created by Carlos Ramos on 27/10/2016.
 */
public class SeveritiesRO extends RealmObject {
    private int id ;
    private String displayName;

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
}
