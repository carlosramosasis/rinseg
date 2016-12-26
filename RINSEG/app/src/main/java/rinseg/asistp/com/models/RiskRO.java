package rinseg.asistp.com.models;

import io.realm.RealmObject;

/**
 * Created by Carlos Ramos on 27/10/2016.
 */
public class RiskRO extends RealmObject{
    private int id ;
    private String displayName;
    private int minValue;
    private int maxValue;

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

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public String toString() {
        return this.displayName; // Value to be displayed in the Spinner
    }

}
