package rinseg.asistp.com.models;

import io.realm.RealmObject;

/**
 * Created by Carlos Ramos on 28/10/2016.
 */
public class EventItemsRO  extends RealmObject {
    private int Id;
    private String Code;
    private String Name;
    private boolean checked;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isChecked() {return checked;}

    public void setChecked(boolean checked) {this.checked = checked;}
}
