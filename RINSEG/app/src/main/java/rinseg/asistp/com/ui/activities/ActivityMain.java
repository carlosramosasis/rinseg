package rinseg.asistp.com.ui.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rinseg.asistp.com.models.AreaRO;
import rinseg.asistp.com.models.CompanyRO;
import rinseg.asistp.com.models.EventItemsRO;
import rinseg.asistp.com.models.EventRO;
import rinseg.asistp.com.models.FrecuencieRO;
import rinseg.asistp.com.models.ManagementRO;
import rinseg.asistp.com.models.ROP;
import rinseg.asistp.com.models.RacRO;
import rinseg.asistp.com.models.RiskRO;
import rinseg.asistp.com.models.SettingsInspectionRO;
import rinseg.asistp.com.models.SettingsRopRO;
import rinseg.asistp.com.models.SeveritiesRO;
import rinseg.asistp.com.models.TargetRO;
import rinseg.asistp.com.models.TypesRO;
import rinseg.asistp.com.models.User;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.services.RestClient;
import rinseg.asistp.com.services.Services;
import rinseg.asistp.com.ui.fragments.FragmentInspecciones;
import rinseg.asistp.com.ui.fragments.FragmentLevantarIncidencia;
import rinseg.asistp.com.ui.fragments.FragmentTabRops;
import rinseg.asistp.com.utils.DialogLoading;
import rinseg.asistp.com.utils.Generic;
import rinseg.asistp.com.utils.Messages;
import rinseg.asistp.com.utils.RinsegModule;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ///// TODO: ::::::::::::::::::::::::::::::::::::::::::::::: VARIABLES ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    private DialogLoading dialogLoading;

    ActivityMain thiss = this;
    public DrawerLayout drawer;
    private TextView txtNombreUsuario;
    private TextView txtCorreoUsuario;

    private NavigationView navigationView;
    View header;
    public Toolbar toolbar;
    Dialog syncDialog;

    Dialog cerrarSesionDialog;
    Button btnCerrarSesion;
    Button btnCancelarSerrarSesion;

    LinearLayout spaceButtonBottom;

    public LinearLayout linearButtonsBottom;
    public Button btnLeft;
    public Button btnRight;

    private static final String TAG_ROP_PENDIENTE = "ropsPendientes";
    private static final String TAG_ROP_CERRADO = "ropsCerrados";
    private static final String TAG_INSPECCIONES = "inspeccioens";
    public static String CURRENT_TAG = TAG_ROP_PENDIENTE;

    public boolean buttonButtomIsVisible = false;

    public static User usuarioLogueado;

    private Boolean sincronizar;

    public SettingsRopRO settingRop;
    public SettingsInspectionRO settingInsp;

    RealmConfiguration myConfig;

    public short totalPaginasRop = 0;
    public short actualPaginaRop = 0;
    public TextView txtNumeroPagina;
    public boolean mostrarPagAccionRealizada = false;

    public FloatingActionsMenu btnFabMenu;
    public FloatingActionButton btnGaleriaFotos;
    public FloatingActionButton btnImportarFotos;
    public FloatingActionButton btnTomarFoto;
    public FloatingActionButton btnGenerarPDF;

    //import foto
    public int PICK_IMAGE_REQUEST = 1;
    //tomar foto
    public int REQUEST_IMAGE_CAPTURE = 1;


    ///// TODO: ::::::::::::::::::::::::::::::::::::::::::::::: EVENTOS ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Pintamos como seleccionado el primer item de nuestro NavigationView
        navigationView.getMenu().getItem(0).setChecked(true);

        setUpElements();
        setUpActions();

        //Llamamos al fragment ROPS pendientes para que sea el primero que se muestre
        replaceFragment(new FragmentTabRops(), true, 0, 0, 0, 0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        dialogLoading.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialogLoading.hide();

        LoadUserFromRealm();

        sincronizar = this.getIntent().getBooleanExtra("Sincronizar", false);
        if (sincronizar && usuarioLogueado != null) {
            Sincronizar(usuarioLogueado.getApi_token());
        }

    }


    @Override
    public void onBackPressed() {
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/


    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rops) {
            ButtonBottomSetDefault();
            replaceFragment(new FragmentTabRops(), true, 0, 0, 0, 0);
        } else if (id == R.id.nav_inspecciones) {
            ButtonBottomSetDefault();
            replaceFragment(new FragmentInspecciones(), true, 0, 0, 0, 0);
        } else if (id == R.id.nav_sincronizar) {
            Sincronizar(usuarioLogueado.getApi_token());
        } else if (id == R.id.nav_acerca_de) {
            ButtonBottomSetDefault();
            replaceFragment(new FragmentLevantarIncidencia(), true, 0, 0, 0, 0);
        } else if (id == R.id.nav_cerrar_sesion) {
            CerrarSesion();
        }


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    ///// TODO: ::::::::::::::::::::::::::::::::::::::::::::::: METODOS ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


    //Proceso para cargar las vistas
    private void setUpElements() {

        dialogLoading = new DialogLoading(this);

        header = navigationView.getHeaderView(0);

        //instanciamos el dialog para sincronizar
        syncDialog = new Dialog(this, R.style.CustomDialogTheme);

        //instanciamos el dialog para cerrar sesion
        cerrarSesionDialog = new Dialog(this, R.style.CustomDialogTheme);

        spaceButtonBottom = (LinearLayout) this.findViewById(R.id.espacio_button_bottom);

        linearButtonsBottom = (LinearLayout) this.findViewById(R.id.linear_buttons_bottom);
        linearButtonsBottom.setY(linearButtonsBottom.getHeight());

        txtNumeroPagina = (TextView) findViewById(R.id.txt_numero_pagina);

        btnLeft = (Button) this.findViewById(R.id.btn_left);
        btnRight = (Button) this.findViewById(R.id.btn_right);

        btnFabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu_rop);
        btnGaleriaFotos = (FloatingActionButton) findViewById(R.id.fab_set_dir);
        btnImportarFotos = (FloatingActionButton) findViewById(R.id.fab_import_foto);
        btnTomarFoto = (FloatingActionButton) findViewById(R.id.fab_tomar_foto);
        btnGenerarPDF = (FloatingActionButton) findViewById(R.id.fab_generar_pdf);

        //configuramos Realm
        Realm.init(thiss.getApplicationContext());
        myConfig = new RealmConfiguration.Builder()
                .name("rinseg.realm")
                .schemaVersion(2)
                .modules(new RinsegModule())
                .deleteRealmIfMigrationNeeded()
                .build();

    }

    private void setUpActions(){

    }

    private void LoadUserFromRealm() {
        try {
            Realm realm = Realm.getInstance(myConfig);
            realm.beginTransaction();
            User usuario = realm.where(User.class).findFirst();
            usuarioLogueado = usuario;
            realm.commitTransaction();

            if (usuarioLogueado != null) {
                String nombreUsu = (usuarioLogueado.getName() != null && usuarioLogueado.getLastname() != null) ? (Generic.Mayus(usuarioLogueado.getName()) + " " + Generic.Mayus(usuarioLogueado.getLastname())) : "";
                txtNombreUsuario = (TextView) header.findViewById(R.id.txt_nombre_usuario);
                txtNombreUsuario.setText(nombreUsu);
                String correoUsu = (usuarioLogueado.getEmail() != null) ? usuarioLogueado.getEmail() : "";
                txtCorreoUsuario = (TextView) header.findViewById(R.id.txt_correo_usuario);
                txtCorreoUsuario.setText(correoUsu);
            }

        } catch (Exception e) {

        }

    }

    public void Sincronizar(String api_token) {
        View parentLAyout = findViewById(R.id.layout_content_main);

        if (!Generic.IsOnRed(this)) {
            Messages.showSB(parentLAyout, getString(R.string.no_internet), getString(R.string.ok));
            return;
        }


        syncDialog.show();
        syncDialog.setContentView(R.layout.dialog_sincroniza_datos);
        syncDialog.setCancelable(false);


        RestClient restClient = new RestClient(Services.URL);
        Call<ResponseBody> call = restClient.iServices.getSync(api_token);

        call.enqueue(new Callback<ResponseBody>() {
            View rootLayout = findViewById(R.id.coordinator_activity_main);

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Realm realm = Realm.getInstance(myConfig);
                    try {
                        View parentLAyout = findViewById(R.id.layout_content_main);

                        int code = response.code();
                        String body = response.body().string();

                        if (body.charAt(0) != '{') {
                            Messages.showSB(parentLAyout, getString(R.string.sincronizando_error), getString(R.string.ok));
                            return;
                        }


                        // limpiamos la bd local
                        clearRealmDB(realm);

                        JSONObject jsonObject = new JSONObject(body);

                        ////////////////////////////Inspections//////////////////////////////////////////////
                        // recuperamos la configuracion inspeccions del resultado recibido del WS
                        JSONObject inspectionsJSON = jsonObject.getJSONObject("inspections");
                        JSONArray iCompaniesJSON = inspectionsJSON.getJSONArray("companies");
                        JSONArray iFecuenciesJSON = inspectionsJSON.getJSONArray("frequencies");
                        JSONArray iSeveritiesJSON = inspectionsJSON.getJSONArray("severities");
                        JSONArray iRisksJSON = inspectionsJSON.getJSONArray("risks");
                        JSONArray iEventsJSON = inspectionsJSON.getJSONArray("event_items");
                        JSONArray iManagementsJSON = inspectionsJSON.getJSONArray("managements");
                        JSONArray iTargentsJSON = inspectionsJSON.getJSONArray("targets");
                        JSONArray iTypesJSON = inspectionsJSON.getJSONArray("types");
                        JSONArray iRacsJSON = inspectionsJSON.getJSONArray("racs");

                        // creamos una SettingsInspectionRO para almacenar los valores para seleccion en los formularios en nuestra BD local
                        realm.beginTransaction();
                        SettingsInspectionRO inspections = realm.createObject(SettingsInspectionRO.class);
                        // poblamos las tablas para la inspeccion
                        PopulateCompaniesForInspection(inspections, iCompaniesJSON, realm);
                        PopulateFrecuenciesForInspection(inspections, iFecuenciesJSON, realm);
                        PopulateSeveritiesForInspection(inspections, iSeveritiesJSON, realm);
                        PopulateRisksForInspection(inspections, iRisksJSON, realm);
                        PopulateEventsForInspection(inspections, iEventsJSON, realm);
                        PopulateManagementsForInspection(inspections, iManagementsJSON, realm);
                        PopulateTargetsForInspection(inspections, iTargentsJSON, realm);
                        PopulateTypesForInspection(inspections, iTypesJSON, realm);
                        PopulateRacsForInspection(inspections, iRacsJSON, realm);

                        ////////////////////////////ROPS//////////////////////////////////////////////
                        // recuperamos el la configuracion ROP del resultado recibido del WS
                        JSONObject ropJSON = jsonObject.getJSONObject("rops");
                        JSONArray rCompaniesJSON = ropJSON.getJSONArray("companies");
                        JSONArray rRisksJSON = ropJSON.getJSONArray("risks");
                        JSONArray rEventsJSON = ropJSON.getJSONArray("event_items");
                        JSONArray rTargetsJSON = ropJSON.getJSONArray("targets");
                        JSONArray rAreasJSON = ropJSON.getJSONArray("areas");
                        // creamos un SettingsRopRO para almacenar los valores para seleccion en los formularios en nuestra BD local
                        SettingsRopRO rops = realm.createObject(SettingsRopRO.class);
                        // poblamos las tablas para el ROP
                        PopulateCompaniesForROP(rops, rCompaniesJSON, realm);
                        PopulateRisksForROP(rops, rRisksJSON, realm);
                        PopulateEventsForROP(rops, rEventsJSON, realm);
                        PopulateTargetsForROP(rops, rTargetsJSON, realm);
                        PopulateAreasForROP(rops, rAreasJSON, realm);

                        //////////////////////////////////Settings///////////////////////////////////////
                        JSONObject settingsJSON = jsonObject.getJSONObject("settings");
                        PopulateSettings(rops, settingsJSON);


                        realm.commitTransaction();

                        /*settingInsp = realm.where(SettingsInspectionRO.class).findFirst();
                        settingRop = realm.where(SettingsRopRO.class).findFirst();*/
                        settingInsp = realm.copyFromRealm(inspections);
                        settingRop = realm.copyFromRealm(rops);

                        syncDialog.dismiss();
                        Messages.showToast(rootLayout, getString(R.string.sincronizando_ok));

                        replaceFragment(new FragmentTabRops(), true, 0, 0, 0, 0);

                        //Pintamos como seleccionado el primer item de nuestro NavigationView
                        navigationView.getMenu().getItem(0).setChecked(true);

                        sincronizar = false;
                        thiss.getIntent().putExtra("Sincronizar", sincronizar);

                        //// TODO: 28/10/2016  Eliminar esta ultima parte
                        SettingsInspectionRO insp = realm.where(SettingsInspectionRO.class).findFirst();
                        SettingsRopRO rop = realm.where(SettingsRopRO.class).findFirst();
                        RealmResults<FrecuencieRO> f = realm.where(FrecuencieRO.class).findAll();
                        RealmResults<CompanyRO> c = realm.where(CompanyRO.class).findAll();
                        RealmList<CompanyRO> comp = insp.companies;
                        Log.e("INSP", insp.toString());
                        Log.e("ROP", rop.toString());
                        for (CompanyRO cm : comp) {
                            Log.e("INSP_COMP", cm.getId() + " " + cm.getDisplayName());
                        }
                        Log.e("FREC", f.toString());
                        Log.e("COMP", c.toString());

                    } catch (Exception e) {
                        syncDialog.dismiss();
                        e.printStackTrace();
                        Messages.showSB(rootLayout, e.getMessage(), "ok");
                    } finally {
                        realm.close();
                        syncDialog.dismiss();
                    }

                } else {
                    syncDialog.dismiss();
                    Messages.showSB(rootLayout, getString(R.string.sincronizando_error), "ok");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                syncDialog.dismiss();
                Messages.showSB(rootLayout, getString(R.string.sincronizando_error), "ok");
            }
        });


    }

    public void clearRealmDB(Realm r) {
        r.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(CompanyRO.class);
                realm.delete(FrecuencieRO.class);
                realm.delete(SeveritiesRO.class);
                realm.delete(RiskRO.class);
                realm.delete(EventRO.class);
                realm.delete(EventItemsRO.class);
                realm.delete(ManagementRO.class);
                realm.delete(TargetRO.class);
                realm.delete(TypesRO.class);
                realm.delete(RacRO.class);
                realm.delete(SettingsInspectionRO.class);

                realm.delete(AreaRO.class);
                realm.delete(SettingsRopRO.class);

                //realm.delete(ROP.class);
            }
        });
    }

    public void PopulateCompaniesForInspection(SettingsInspectionRO insp, JSONArray companies, Realm realm) {
        for (int i = 0; i < companies.length(); i++) {
            try {
                JSONObject c = companies.getJSONObject(i);
                // CompanyRO companie = realm.createObjectFromJson (CompanyRO.class,c);
                CompanyRO companie = realm.createObject(CompanyRO.class);
                companie.setId(c.getInt("id"));
                companie.setDisplayName(c.getString("display_name"));
                insp.companies.add(companie);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateFrecuenciesForInspection(SettingsInspectionRO insp, JSONArray frecuencies, Realm realm) {
        for (int i = 0; i < frecuencies.length(); i++) {
            try {

                JSONObject f = frecuencies.getJSONObject(i);
                FrecuencieRO realmFrecuencie = realm.createObject(FrecuencieRO.class);
                realmFrecuencie.setId(f.getInt("id"));
                realmFrecuencie.setDisplayName(f.getString("display_name"));
                insp.frecuencies.add(realmFrecuencie);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateSeveritiesForInspection(SettingsInspectionRO insp, JSONArray severities, Realm realm) {
        for (int i = 0; i < severities.length(); i++) {
            try {

                JSONObject f = severities.getJSONObject(i);
                SeveritiesRO realmSeverities = realm.createObject(SeveritiesRO.class);
                realmSeverities.setId(f.getInt("id"));
                realmSeverities.setDisplayName(f.getString("display_name"));
                insp.severities.add(realmSeverities);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateRisksForInspection(SettingsInspectionRO insp, JSONArray risks, Realm realm) {

        for (int i = 0; i < risks.length(); i++) {
            try {

                JSONObject f = risks.getJSONObject(i);
                RiskRO realmRisks = realm.createObject(RiskRO.class);
                realmRisks.setId(f.getInt("id"));
                realmRisks.setDisplayName(f.getString("display_name"));
                realmRisks.setMinValue(f.getInt("min_value"));
                realmRisks.setMaxValue(f.getInt("max_value"));
                insp.risks.add(realmRisks);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateEventsForInspection(SettingsInspectionRO insp, JSONArray events, Realm realm) {
        for (int i = 0; i < events.length(); i++) {
            try {

                JSONObject f = events.getJSONObject(i);
                EventRO realmEvents = realm.createObject(EventRO.class);
                realmEvents.setId(f.getInt("id"));
                realmEvents.setDisplayName(f.getString("display_name"));

                JSONArray eventItems = f.getJSONArray("items");
                for (int j = 0; j < eventItems.length(); j++) {
                    JSONObject item = eventItems.getJSONObject(j);
                    EventItemsRO itemRO = realm.createObject(EventItemsRO.class);
                    itemRO.setId(item.getInt("id"));
                    itemRO.setCode(item.getString("code"));
                    itemRO.setName(item.getString("name"));
                    realmEvents.eventItems.add(itemRO);
                }

                insp.events.add(realmEvents);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateManagementsForInspection(SettingsInspectionRO insp, JSONArray managements, Realm realm) {
        for (int i = 0; i < managements.length(); i++) {
            try {
                JSONObject m = managements.getJSONObject(i);
                ManagementRO realmManagement = realm.createObject(ManagementRO.class);
                realmManagement.setId(m.getInt("id"));
                realmManagement.setDisplayName(m.getString("display_name"));
                insp.managements.add(realmManagement);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateTargetsForInspection(SettingsInspectionRO insp, JSONArray targets, Realm realm) {
        for (int i = 0; i < targets.length(); i++) {
            try {
                JSONObject t = targets.getJSONObject(i);
                TargetRO realmTargetRO = realm.createObject(TargetRO.class);
                realmTargetRO.setId(t.getInt("id"));
                realmTargetRO.setDisplayName(t.getString("display_name"));
                insp.targets.add(realmTargetRO);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateTypesForInspection(SettingsInspectionRO insp, JSONArray types, Realm realm) {
        for (int i = 0; i < types.length(); i++) {
            try {
                JSONObject t = types.getJSONObject(i);
                TypesRO realmTypeRO = realm.createObject(TypesRO.class);
                realmTypeRO.setId(t.getInt("id"));
                realmTypeRO.setDisplayName(t.getString("display_name"));
                insp.types.add(realmTypeRO);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateRacsForInspection(SettingsInspectionRO insp, JSONArray racs, Realm realm) {
        for (int i = 0; i < racs.length(); i++) {
            try {
                JSONObject r = racs.getJSONObject(i);
                RacRO realmRacsRO = realm.createObject(RacRO.class);
                realmRacsRO.setId(r.getInt("id"));
                realmRacsRO.setDisplayName(r.getString("display_name"));
                insp.racs.add(realmRacsRO);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void PopulateCompaniesForROP(SettingsRopRO rops, JSONArray companies, Realm realm) {
        for (int i = 0; i < companies.length(); i++) {
            try {
                if (i == 0) {
                    CompanyRO companySelect = realm.createObject(CompanyRO.class);
                    companySelect.setDisplayName(getString(R.string.spinner_default));
                    rops.companies.add(companySelect);
                }
                JSONObject c = companies.getJSONObject(i);
                CompanyRO companie = realm.createObject(CompanyRO.class);
                companie.setId(c.getInt("id"));
                companie.setDisplayName(c.getString("display_name"));
                rops.companies.add(companie);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateRisksForROP(SettingsRopRO rops, JSONArray risks, Realm realm) {
        for (int i = 0; i < risks.length(); i++) {
            try {
                if (i == 0) {
                    RiskRO riskSelect = realm.createObject(RiskRO.class);
                    riskSelect.setDisplayName(getString(R.string.spinner_default));
                    rops.risks.add(riskSelect);
                }

                JSONObject r = risks.getJSONObject(i);
                RiskRO riskRO = realm.createObject(RiskRO.class);
                riskRO.setId(r.getInt("id"));
                riskRO.setDisplayName(r.getString("display_name"));
                rops.risks.add(riskRO);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateEventsForROP(SettingsRopRO rops, JSONArray events, Realm realm) {
        for (int i = 0; i < events.length(); i++) {
            try {
                if (i == 0) {
                    EventRO eventSelect = realm.createObject(EventRO.class);
                    eventSelect.setDisplayName(getString(R.string.spinner_default));
                    eventSelect.setName(getString(R.string.spinner_default));
                    rops.events.add(eventSelect);
                }

                JSONObject f = events.getJSONObject(i);
                EventRO realmEvents = realm.createObject(EventRO.class);
                realmEvents.setId(f.getInt("id"));
                realmEvents.setDisplayName(f.getString("display_name"));
                realmEvents.setName(f.getString("name"));

                JSONArray eventItems = f.getJSONArray("items");
                for (int j = 0; j < eventItems.length(); j++) {
                    JSONObject item = eventItems.getJSONObject(j);
                    EventItemsRO itemRO = realm.createObject(EventItemsRO.class);
                    itemRO.setId(item.getInt("id"));
                    itemRO.setCode(item.getString("code"));
                    itemRO.setName(item.getString("name"));
                    realmEvents.eventItems.add(itemRO);
                }

                rops.events.add(realmEvents);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateTargetsForROP(SettingsRopRO rops, JSONArray targets, Realm realm) {
        for (int i = 0; i < targets.length(); i++) {
            try {
                if (i == 0) {
                    TargetRO targetSelect = realm.createObject(TargetRO.class);
                    targetSelect.setDisplayName(getString(R.string.spinner_default));
                    rops.targets.add(targetSelect);
                }

                JSONObject t = targets.getJSONObject(i);
                TargetRO targetRO = realm.createObject(TargetRO.class);
                targetRO.setId(t.getInt("id"));
                targetRO.setDisplayName(t.getString("display_name"));
                rops.targets.add(targetRO);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void PopulateAreasForROP(SettingsRopRO rops, JSONArray areas, Realm realm) {
        for (int i = 0; i < areas.length(); i++) {
            try {
                if (i == 0) {
                    AreaRO areaSelect = realm.createObject(AreaRO.class);
                    areaSelect.setDisplayName(getString(R.string.spinner_default));
                    rops.areas.add(areaSelect);
                }

                JSONObject a = areas.getJSONObject(i);
                AreaRO areaRO = realm.createObject(AreaRO.class);
                areaRO.setId(a.getInt("id"));
                areaRO.setDisplayName(a.getString("display_name"));
                rops.areas.add(areaRO);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void PopulateSettings(SettingsRopRO rop, JSONObject settings) {
        try {
            JSONObject ropSettingsJSON = settings.getJSONObject("rops");
            rop.setBody(ropSettingsJSON.getString("body"));
            rop.setNote(ropSettingsJSON.getString("note"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void CerrarSesion() {
        cerrarSesionDialog.show();
        cerrarSesionDialog.setContentView(R.layout.dialog_cerrar_sesion);
        btnCerrarSesion = (Button) cerrarSesionDialog.findViewById(R.id.btn_cerrar_sesion_salir);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getInstance(myConfig);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(User.class);
                    }
                });
                thiss.finishAffinity();

            }
        });

        btnCancelarSerrarSesion = (Button) cerrarSesionDialog.findViewById(R.id.btn_cerrar_sesion_cancelar);
        btnCancelarSerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesionDialog.dismiss();
            }

        });

    }

    //seteamos los valores e iconos por defecto de la barra de botones inferior
    public void ButtonBottomSetDefault() {
        btnLeft.setText(R.string.btn_atras);
        btnLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_chevron_left_black_24dp, 0, 0, 0);
        btnRight.setText(R.string.btn_ccontinuar);
        btnRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_black_24dp, 0);

    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack,
                                int animIdIn1,
                                int animIdOut1,
                                int animIdIn2,
                                int animIdOut2) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String tag = fragment.getClass().getSimpleName();
        tag = "Tag_" + tag;
        transaction.setCustomAnimations(animIdIn1, animIdOut1, animIdIn2, animIdOut2);
        transaction.replace(R.id.frame_main_content, fragment, tag);
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }

    public void ShowButtonsBottom(boolean bolean) {
        if (bolean) {
            if (buttonButtomIsVisible) {
                return;
            }


            TranslateAnimation animate = new TranslateAnimation(0, 0, linearButtonsBottom.getHeight(), 0);
            animate.setDuration(200);
            animate.setFillAfter(true);
            linearButtonsBottom.startAnimation(animate);
            linearButtonsBottom.setVisibility(View.VISIBLE);
            buttonButtomIsVisible = true;
            btnLeft.setClickable(true);
            btnRight.setClickable(true);

            spaceButtonBottom.setVisibility(View.VISIBLE);
        } else {
            if (!buttonButtomIsVisible) {
                return;
            }

            TranslateAnimation animate = new TranslateAnimation(0, 0, 0, linearButtonsBottom.getHeight());
            animate.setDuration(200);
            animate.setFillAfter(true);
            linearButtonsBottom.startAnimation(animate);
            linearButtonsBottom.setVisibility(View.GONE);
            buttonButtomIsVisible = false;
            btnLeft.setClickable(false);
            btnRight.setClickable(false);

            spaceButtonBottom.setVisibility(View.GONE);
        }

    }

    public void ShowNumPagina() {
        txtNumeroPagina.setText("" + actualPaginaRop + "/" + totalPaginasRop);
        txtNumeroPagina.setVisibility(View.VISIBLE);
    }

    public void HideNumPagina() {
        txtNumeroPagina.setVisibility(View.GONE);
    }


    public void MostrarCantidadImagenesRop(String nombreCarpeta) {
        int cant = Generic.CantidadImagenesPorRop(this.getApplicationContext(), nombreCarpeta);
        this.btnGaleriaFotos.setTitle(getString(R.string.label_fotos) + " (" + cant + ")");
    }

}
