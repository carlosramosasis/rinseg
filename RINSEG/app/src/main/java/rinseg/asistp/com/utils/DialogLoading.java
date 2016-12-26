package rinseg.asistp.com.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import rinseg.asistp.com.rinseg.R;

/**
 * Created by Carlos Ramos on 25/10/2016.
 */
public class DialogLoading extends Dialog {

    public Activity c;

    public DialogLoading(Activity a) {
        super(a, R.style.CustomDialogTheme);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        setCancelable(false);
    }
}
