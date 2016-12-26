package rinseg.asistp.com.utils;

import io.realm.annotations.RealmModule;
import rinseg.asistp.com.models.AccionPreventiva;
import rinseg.asistp.com.models.AreaRO;
import rinseg.asistp.com.models.CompanyRO;
import rinseg.asistp.com.models.EventItemsRO;
import rinseg.asistp.com.models.EventRO;
import rinseg.asistp.com.models.FrecuencieRO;
import rinseg.asistp.com.models.ImagenRO;
import rinseg.asistp.com.models.ManagementRO;
import rinseg.asistp.com.models.ROP;
import rinseg.asistp.com.models.RacRO;
import rinseg.asistp.com.models.RiskRO;
import rinseg.asistp.com.models.SecuencialRO;
import rinseg.asistp.com.models.SettingsInspectionRO;
import rinseg.asistp.com.models.SettingsRopRO;
import rinseg.asistp.com.models.SeveritiesRO;
import rinseg.asistp.com.models.TargetRO;
import rinseg.asistp.com.models.TypesRO;
import rinseg.asistp.com.models.User;

/**
 * Created by Carlos Ramos on 25/10/2016.
 */
@RealmModule(classes = {
          SettingsInspectionRO.class
        , CompanyRO.class
        , FrecuencieRO.class
        , SettingsRopRO.class
        , SeveritiesRO.class
        , RiskRO.class
        , EventRO.class
        , EventItemsRO.class
        , ManagementRO.class
        , TargetRO.class
        , TypesRO.class
        , RacRO.class
        , AreaRO.class
        , User.class
        , ROP.class
        , SecuencialRO.class
        , AccionPreventiva.class
        , ImagenRO.class
})

public class RinsegModule {

}
