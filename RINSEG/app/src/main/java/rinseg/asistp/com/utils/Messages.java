package rinseg.asistp.com.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import rinseg.asistp.com.rinseg.R;

/**
 * @author Carlos Ramos
 * @version 1.0
 * Created on 25/10/2016
 * Clase para administrar los mensajes de la aplicación
 */

public class Messages {

    /**
     * Created on 28/10/2016
     * @param view vista.
     * @param message Mensaje a mostrarse.
     * @param messageAction Mensaje de la acción a realizarse.
     */
    public static void showSB(View view, String message,
                              String messageAction){
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG)
                .setAction(messageAction, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {}
                });
        snackbar.show();
    }


    public static void showToast(View view, String message){
        Toast.makeText(view.getContext(),message,Toast.LENGTH_LONG).show();
    }


    /**
     * Created on 23/06/2016
     * @param coordinatorLayout Layout que contiene a las vistas.
     * @param error Código de error.
     */
    public static void handleError(CoordinatorLayout coordinatorLayout, int error){

    }
}
