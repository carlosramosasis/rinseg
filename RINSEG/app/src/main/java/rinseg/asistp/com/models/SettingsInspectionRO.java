package rinseg.asistp.com.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Carlos Ramos on 25/10/2016.
 */
public class SettingsInspectionRO extends RealmObject {
    public RealmList<CompanyRO> companies;
    public RealmList<FrecuencieRO> frecuencies;
    public RealmList<SeveritiesRO> severities;
    public RealmList<RiskRO> risks;
    public RealmList<EventRO> events;
    public RealmList<ManagementRO> managements;
    public RealmList<TargetRO> targets;
    public RealmList<TypesRO> types;
    public RealmList<RacRO> racs;
}
