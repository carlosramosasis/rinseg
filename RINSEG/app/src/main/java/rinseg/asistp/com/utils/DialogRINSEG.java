package rinseg.asistp.com.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import rinseg.asistp.com.rinseg.R;

/**
 * Created by Carlos Ramos  on 25/11/2016.
 */
public class DialogRINSEG extends Dialog implements android.view.View.OnClickListener{

    public Activity activity;
    public Dialog d;
    public Button btnAceptar, btnCancelar;
    public TextView title, body;

    public DialogRINSEG(Activity a) {
        super(a, R.style.CustomDialogTheme);
        // TODO Auto-generated constructor stub
        this.activity = a;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rinseg);
        btnCancelar = (Button) findViewById(R.id.btn_dialog_cancelar);
        btnAceptar = (Button) findViewById(R.id.btn_dialog_aceptar);
        title = (TextView) findViewById(R.id.txt_title_dialog);
        body = (TextView) findViewById(R.id.txt_body_dialog);
        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_aceptar:
                Log.e("btnAceptar","ACEPTAR");
                break;
            case R.id.btn_cancelar:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public void setTitle(String pTitle){
        title.setText(pTitle);
    }

    public void setBody(String pBody){
        body.setText(pBody);
    }

    public void setTextBtnAceptar(String pTexte){
        btnAceptar.setText(pTexte);
    }

    public void setTextBtnCancelar(String pBody){
        btnCancelar.setText(pBody);
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }
}
