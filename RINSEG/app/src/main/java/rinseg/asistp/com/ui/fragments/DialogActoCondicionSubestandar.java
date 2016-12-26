package rinseg.asistp.com.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rinseg.asistp.com.adapters.AccionCondicionSubestandarAdapter;
import rinseg.asistp.com.adapters.RopAdapter;
import rinseg.asistp.com.listener.ListenerClickActoCondicion;
import rinseg.asistp.com.models.EventItemsRO;
import rinseg.asistp.com.models.EventRO;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.utils.RinsegModule;

/**
 * Created by Carlos Ramos  on 25/11/2016.
 */
public class DialogActoCondicionSubestandar extends Dialog implements View.OnClickListener,ListenerClickActoCondicion {

    ///// TODO: ::::::::::::::::::::::::::::::::::::::::::::::::::::: VARIABLES ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public Activity activity;
    public Dialog d;
    public Button btnAceptar, btnCancelar;
    public TextView title;
    private RecyclerView recyclerViewItems;
    private AccionCondicionSubestandarAdapter itemsAdapter;
    private RecyclerView.LayoutManager lManager;


    public String nameActoCondicion;
    private List<EventItemsRO> listaItems = new ArrayList<>();
    RealmConfiguration myConfig;

    ///// TODO: ::::::::::::::::::::::::::::::::::::::::::::::::::::: CONSTRUCTOR ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public DialogActoCondicionSubestandar(Activity a, String pNameActoCondicion) {
        super(a, R.style.CustomDialogTheme);
        // TODO Auto-generated constructor stub
        this.activity = a;
        this.nameActoCondicion = pNameActoCondicion;

    }

    ///// TODO: ::::::::::::::::::::::::::::::::::::::::::::::::::::: EVENTOS ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_accion_condicion_subestandar);
        btnCancelar = (Button) findViewById(R.id.btn_dialog_cancelar);
        btnAceptar = (Button) findViewById(R.id.btn_dialog_aceptar);
        title = (TextView) findViewById(R.id.txt_title_dialog);
        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        //configuracion para el recicler
        recyclerViewItems= (RecyclerView) findViewById(R.id.recycler_view_acto_condicion_subestandar);
        recyclerViewItems.setHasFixedSize(true);
        // usar administrador para linearLayout
        lManager = new LinearLayoutManager(this.getContext());
        recyclerViewItems.setLayoutManager(lManager);
        // Crear un nuevo Adaptador
        itemsAdapter = new AccionCondicionSubestandarAdapter(listaItems,this);
        recyclerViewItems.setAdapter(itemsAdapter);

        //configuramos Realm
        Realm.init(getContext());
        myConfig = new RealmConfiguration.Builder()
                .name("rinseg.realm")
                .schemaVersion(2)
                .modules(new RinsegModule())
                .deleteRealmIfMigrationNeeded()
                .build();



        CargarAccionesCondiciones();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_aceptar:
                Log.e("btnAceptar", "ACEPTAR");
                break;
            case R.id.btn_cancelar:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }


    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }


    ///// TODO: ::::::::::::::::::::::::::::::::::::::::::::::::::::: METODOS ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void setTitle(String pTitle) {
        title.setText(pTitle);
    }

    public void setTextBtnAceptar(String pTexte) {
        btnAceptar.setText(pTexte);
    }

    public void setTextBtnCancelar(String pBody) {
        btnCancelar.setText(pBody);
    }

    public void CargarAccionesCondiciones(){
        Realm realm = Realm.getInstance(myConfig);
        try {
            EventRO event = realm.where(EventRO.class).equalTo("name",nameActoCondicion).findFirst();

            for (int i = 0; i < event.eventItems.size(); i++) {
                EventItemsRO eventItem = event.eventItems.get(i);
                listaItems.add(eventItem);
                itemsAdapter.notifyDataSetChanged();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            realm.close();
        }
    }

    @Override
    public void onItemClicked(AccionCondicionSubestandarAdapter.AccionViewHolder holder, int position) {

    }
}
