package rinseg.asistp.com.models;

import io.realm.RealmObject;

/**
 * Created by Carlos Ramos on 26/10/2016.
 */
public class FrecuencieRO extends RealmObject {
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
