package rinseg.asistp.com.ui.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rinseg.asistp.com.models.FotoModel;
import rinseg.asistp.com.models.ImagenRO;
import rinseg.asistp.com.models.ROP;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.utils.Constants;
import rinseg.asistp.com.utils.Generic;
import rinseg.asistp.com.utils.Messages;
import rinseg.asistp.com.utils.RinsegModule;

public class ActivityFotoComentario extends AppCompatActivity {
    public Toolbar toolbarFotoComentario;
    public ImageView imageFoto;
    public EditText txtComentario;
    private ActivityFotoComentario thiss = (ActivityFotoComentario) this;
    private FotoModel fotoModel;
    Bitmap bitmap;

    ImageView imageView;
    ImageButton btnGuardar;

    ROP mRop;
    ImagenRO imagenRO;
    String tmpIdRop = null;
    RealmConfiguration myConfig;

    View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_comentario);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                fotoModel = (FotoModel) extras.getSerializable("imagen_rop");
                tmpIdRop = extras.getString("ROPtmpId", null);
            }
        }

        setUpElements();

        LoadRopPendiente();
        MostrarImagen();

        setUpActions();

    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbarFotoComentario.setTitle(R.string.title_add_imagen);
        txtComentario.clearFocus();
    }

    //Proceso para cargar las vistas
    private void setUpElements() {
        rootLayout = findViewById(R.id.coordinator_foto_comentario);
        toolbarFotoComentario = (Toolbar) findViewById(R.id.toolbarFotoComentario);
        toolbarFotoComentario.setNavigationIcon(R.drawable.ic_arrow_left);
        imageFoto = (ImageView) findViewById(R.id.image_foto_comentario);
        txtComentario = (EditText) findViewById(R.id.txt_comentario);
        imageView = (ImageView) findViewById(R.id.image_foto_comentario);
        btnGuardar = (ImageButton) findViewById(R.id.btn_guardar_foto_comentario);

        //configuramos Realm
        Realm.init(this.getApplicationContext());
        myConfig = new RealmConfiguration.Builder()
                .name("rinseg.realm")
                .schemaVersion(2)
                .modules(new RinsegModule())
                .deleteRealmIfMigrationNeeded()
                .build();

    }

    //cargamos los eventos
    private void setUpActions() {
        toolbarFotoComentario.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thiss.finish();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtComentario.getText().toString().trim().length() == 0){
                    Messages.showToast(rootLayout, getString(R.string.error_comentario));
                    return;
                }

                SaveImagenComentario();
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


    private void MostrarImagen() {
        if (fotoModel.uri != null) {
            Uri uri = fotoModel.uri;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void LoadRopPendiente() {

        //ROP tmpRop = new ROP();
        if (tmpIdRop != null) {
            final Realm realm = Realm.getInstance(myConfig);
            try {
                mRop = realm.where(ROP.class).equalTo("tmpId", tmpIdRop).findFirst();
                if (mRop == null) {
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                realm.close();
            }
        }
    }

    private void SaveImagenComentario() {
        String nombreImagen = Generic.randomString(Constants.CHARSET_AZ_09, Constants.LEN_IMAGEN);
        Boolean result = Generic.GuardarImagenCarpeta(this.getApplicationContext(), mRop.getTmpId(), bitmap, nombreImagen);
        if (result) {
            Realm realm = Realm.getInstance(myConfig);
            try {
                realm.beginTransaction();

                imagenRO = realm.createObject(ImagenRO.class);
                imagenRO.setName(nombreImagen +".jpg");
                imagenRO.setDescripcion(txtComentario.getText().toString().trim());
                mRop.listaImgComent.add(imagenRO);

                realm.commitTransaction();
                Messages.showToast(rootLayout, getString(R.string.guardo_ok));
                this.finish();
            } catch (Exception e) {
                e.printStackTrace();
                Messages.showToast(rootLayout, getString(R.string.guardo_error));
            } finally {
                realm.close();
            }
        } else {
            Messages.showToast(rootLayout, getString(R.string.guardo_error));
        }
    }


}
