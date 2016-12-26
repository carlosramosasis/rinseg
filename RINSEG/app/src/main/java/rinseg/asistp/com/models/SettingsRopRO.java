package rinseg.asistp.com.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Carlos Ramos on 27/10/2016.
 */
public class SettingsRopRO extends RealmObject {
    public RealmList<CompanyRO> companies;
    public RealmList<RiskRO> risks;
    public RealmList<EventRO> events;
    public RealmList<TargetRO> targets;
    public RealmList<AreaRO> areas;

    private String body;
    private String note;

    public String getBody() {
        return body;
    }

    public void setBody(String _body) {
        this.body = _body;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String _note) {
        this.note = _note;
    }
}
