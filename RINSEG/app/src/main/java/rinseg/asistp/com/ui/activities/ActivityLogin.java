package rinseg.asistp.com.ui.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.ui.fragments.FragmentSingIn;

public class ActivityLogin extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Seteamos el fragment de Logueo
        replaceFragment(new FragmentSingIn(), false);
    }


    /**
     * Created on 28/09/2016
     * Método para reemplazar el fragmento
     *
     * @param fragment       Fragmento que reemplazará al anfitrión.
     * @param addToBackStack indicador de apilamiento
     */
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String tag = fragment.getClass().getSimpleName();
        transaction.replace(R.id.frame_login_content, fragment, tag);
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }






    /**Created on 28/09/2016
     * Método para lanzar la actividad principal
     */
   /* public void launchMain(){
        Intent mainIntent = new Intent().setClass(ActivityLogin.this, ActivityMain.class);
        startActivity(mainIntent);
        finish();
    }*/




}
