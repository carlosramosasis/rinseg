package rinseg.asistp.com.ui.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.ui.fragments.FragmentInspeccionDetalle1;

public class ActivityInspeccionDetalle extends AppCompatActivity {
    public Toolbar toolbarInspeccionDet;
    private ActivityInspeccionDetalle thiss = (ActivityInspeccionDetalle) this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspeccion_detalle);

        setUpElements();
        setUpActions();
        //Llamamos al fragment ROPS pendientes para que sea el primero que se muestre
        replaceFragment(new FragmentInspeccionDetalle1(), true, 0, 0, 0, 0);
    }

    @Override
    public void onBackPressed() {

    }

    //Proceso para cargar las vistas
    private void setUpElements() {
        toolbarInspeccionDet = (Toolbar) findViewById(R.id.toolbarInspeccionDetalle);
        toolbarInspeccionDet.setNavigationIcon(R.drawable.ic_arrow_left);
    }

    //cargamos los eventos
    private void setUpActions() {
        toolbarInspeccionDet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thiss.finish();
            }
        });
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
        transaction.replace(R.id.frame_main_content_inspeccion_detalle, fragment, tag);
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }


}
