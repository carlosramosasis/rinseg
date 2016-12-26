package rinseg.asistp.com.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rinseg.asistp.com.listener.ListenerClickAccionPreventiva;
import rinseg.asistp.com.models.AccionPreventiva;
import rinseg.asistp.com.models.AreaRO;
import rinseg.asistp.com.models.CompanyRO;
import rinseg.asistp.com.models.EventRO;
import rinseg.asistp.com.models.FotoModel;
import rinseg.asistp.com.models.Inspeccion;
import rinseg.asistp.com.models.ROP;
import rinseg.asistp.com.models.RiskRO;
import rinseg.asistp.com.models.TargetRO;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.ui.activities.ActivityFotoComentario;
import rinseg.asistp.com.ui.activities.ActivityInspeccionDetalle;
import rinseg.asistp.com.ui.activities.ActivityMain;
import rinseg.asistp.com.adapters.AccionPreventivaAdapter;
import rinseg.asistp.com.utils.Constants;
import rinseg.asistp.com.utils.DialogLoading;
import rinseg.asistp.com.utils.DialogRINSEG;
import rinseg.asistp.com.utils.Generic;
import rinseg.asistp.com.utils.Messages;
import rinseg.asistp.com.utils.RinsegModule;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentROPPendiente2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentROPPendiente2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentROPPendiente2 extends Fragment implements ListenerClickAccionPreventiva {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    ActivityMain activityMain;
    ImageButton btnActoCondicion;
    EditText txtResponsable;
    EditText txtAccion;
    TextView txtFecha;
    ImageButton btnCalendar;
    Button btnAgregar;
    ImageButton btnDelete;
    Calendar newCalendar;

    private List<AccionPreventiva> listaAccionPreventiva = new ArrayList<>();
    RecyclerView recyclerAcciones;
    private RecyclerView.LayoutManager lManager;
    private RecyclerView.Adapter accionAdapter;

    ROP mRop;

    RealmConfiguration myConfig;

    private DialogLoading dialogLoading;

    public static int count = 0;

    private String nameEvent;

    Bundle bundle;

    public FragmentROPPendiente2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentROPPendiente1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentROPPendiente2 newInstance(String param1, String param2) {
        FragmentROPPendiente2 fragment = new FragmentROPPendiente2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rop_pendiente2, container, false);


        setUpElements(view);
        setUpActions();

        Permissions();

        LoadRopPendiente();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        activityMain.toolbar.setTitle(R.string.title_rop);
        activityMain.ShowButtonsBottom(true);
        activityMain.ShowNumPagina();
        activityMain.MostrarCantidadImagenesRop(mRop.getTmpId());
        activityMain.btnFabMenu.setVisibility(View.VISIBLE);
        activityMain.btnFabMenu.collapseImmediately();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        activityMain.HideNumPagina();
        activityMain.btnFabMenu.setVisibility(View.GONE);
        super.onDestroyView();

    }

    @Override
    public void onItemClicked(AccionPreventivaAdapter.AccionViewHolder holder, int position) {
        Realm realm = Realm.getInstance(myConfig);
        try {
            realm.beginTransaction();
            mRop.listaAccionPreventiva.get(position).deleteFromRealm();
            realm.commitTransaction();
            listaAccionPreventiva.remove(position);
            accionAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        } finally {
            realm.close();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == activityMain.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            launchActivityFotoComentario(data.getData());
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //Proceso para cargar las vistas
    private void setUpElements(View v) {
        bundle = getArguments();

        activityMain = ((ActivityMain) getActivity());

        newCalendar = Calendar.getInstance();

        btnActoCondicion = (ImageButton) v.findViewById(R.id.btn_rop2_acto_condicion_subestandar);

        txtResponsable = (EditText) v.findViewById(R.id.txt_rop2_responsable);
        txtAccion = (EditText) v.findViewById(R.id.txt_rop2_accion);
        txtFecha = (TextView) v.findViewById(R.id.txt_rop2_fecha);
        btnCalendar = (ImageButton) v.findViewById(R.id.btn_rop2_calendar);

        btnAgregar = (Button) v.findViewById(R.id.btn_rop2_agregar);
        btnDelete = (ImageButton) v.findViewById(R.id.btn_card_view_delete);

        //configuracion para el recicler
        recyclerAcciones = (RecyclerView) v.findViewById(R.id.recycler_rop2_acciones);
        recyclerAcciones.setHasFixedSize(true);
        // usar administrador para linearLayout
        lManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        recyclerAcciones.setLayoutManager(lManager);
        // Crear un nuevo Adaptador
        accionAdapter = new AccionPreventivaAdapter(listaAccionPreventiva, this);
        recyclerAcciones.setAdapter(accionAdapter);

        dialogLoading = new DialogLoading(getActivity());

        //configuramos Realm
        Realm.init(this.getActivity().getApplicationContext());
        myConfig = new RealmConfiguration.Builder()
                .name("rinseg.realm")
                .schemaVersion(2)
                .modules(new RinsegModule())
                .deleteRealmIfMigrationNeeded()
                .build();


    }

    //cargamos los eventos
    private void setUpActions() {
        btnActoCondicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarAccionCondicionSubestandar();
            }
        });

        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatepicker();
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatepicker();
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarAccion();
            }
        });

        activityMain.btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogLoading.show();
                    //SaveRop();
                    activityMain.actualPaginaRop -=1;
                    Fragment fRopPendiente1 = new FragmentROPPendiente1();
                    Bundle args = new Bundle();
                    args.putString("ROPtmpId", mRop.getTmpId());
                    fRopPendiente1.setArguments(args);
                    activityMain.replaceFragment(fRopPendiente1, true, R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left);
                    dialogLoading.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        activityMain.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!listaAccionesValida()){
                    Validarformulario();
                    Messages.showSB(v, getString(R.string.msg_lista_acciones), "ok");
                    return;
                }

                activityMain.actualPaginaRop +=1;
                Fragment fRopPendiente3 = new FragmentROPPendiente3();
                Bundle args = new Bundle();
                args.putString("ROPtmpId", mRop.getTmpId());
                fRopPendiente3.setArguments(args);
                activityMain.replaceFragment(fRopPendiente3, true, R.anim.enter_from_left, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_right);
            }
        });

        activityMain.btnImportarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), activityMain.PICK_IMAGE_REQUEST);
            }
        });

        activityMain.btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int permissionCheckCamera = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                if (permissionCheckCamera != PackageManager.PERMISSION_GRANTED) {
                    Permissions();
                }

                if (permissionCheckCamera == PackageManager.PERMISSION_GRANTED){
                    // Here, the counter will be incremented each time, and the
                    // picture taken by camera will be stored as 1.jpg,2.jpg
                    // and likewise.    CODIGO PARA LLAMAR A CAMARA
                    count++;
                    String file = count + ".jpg";
                    File newfile = new File(file);
                    try {
                        newfile.createNewFile();
                    } catch (IOException e) {
                    }

                    Uri outputFileUri = Uri.fromFile(newfile);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(cameraIntent, activityMain.REQUEST_IMAGE_CAPTURE);
                    }

                }

            }


        });

        activityMain.btnGenerarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Messages.showToast(getView(),String.valueOf(Generic.isOnline()));
            }
        });

    }

    private boolean Validarformulario(){
        boolean resu = true;
        if (txtAccion.getText().length() == 0) {
            txtAccion.setError(getString(R.string.error_accion_rp2));
            resu = false;
        }

        if (txtResponsable.getText().length() == 0) {
            txtResponsable.setError(getString(R.string.error_resp_rp2));
            resu = false;
        }

        if (txtFecha.getText().length() == 0) {
            txtFecha.setError(getString(R.string.error_fecha_rp2));
            resu = false;
        }


        return  resu;

    }


    private void AgregarAccion() {

        if (!Validarformulario()) {
            return;
        }

        Realm realm = Realm.getInstance(myConfig);
        try {
            realm.beginTransaction();
            AccionPreventiva mAccion = realm.createObject(AccionPreventiva.class);
            mAccion.setResponsable(txtResponsable.getText().toString());
            mAccion.setAccion(txtAccion.getText().toString());
            mAccion.setFecha(newCalendar.getTime());
            mAccion.setFechaString();

            mRop.listaAccionPreventiva.add(mAccion);
            realm.commitTransaction();

            listaAccionPreventiva.add(0, mAccion);
            accionAdapter.notifyDataSetChanged();
            txtResponsable.setText("");
            txtAccion.setText("");
            txtFecha.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            realm.close();
        }


    }


    public void ShowDatepicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtFecha.setText(Generic.dateFormatter.format(newDate.getTime()));
                txtFecha.setError(null);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }


    private void LoadRopPendiente() {

        String tmpIdRop = null;
        if (bundle != null) {
            tmpIdRop = bundle.getString("ROPtmpId", null);
            nameEvent = bundle.getString("nameEvent", null);

            //ROP tmpRop = new ROP();
            if (tmpIdRop != null) {
                final Realm realm = Realm.getInstance(myConfig);
                try {
                    mRop = realm.where(ROP.class).equalTo("tmpId", tmpIdRop).findFirst();
                    if (mRop == null) {
                        return;
                    }

                    // recuperar accion preventiva
                    for (int i = 0; i < mRop.listaAccionPreventiva.size(); i++) {
                        AccionPreventiva ap = mRop.listaAccionPreventiva.get(i);
                        listaAccionPreventiva.add(ap);
                        accionAdapter.notifyDataSetChanged();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    realm.close();
                }
            }

        }
    }

//    private void MostrarCantidadImagenesRop() {
//        int cant = Generic.CantidadImagenesPorRop(getActivity().getApplicationContext(), mRop.getTmpId());
//        activityMain.btnGaleriaFotos.setTitle(getString(R.string.label_fotos) + " (" + cant + ")");
//    }


    public void launchActivityFotoComentario(Uri uri) {
        FotoModel fotoModel =  new FotoModel();
        fotoModel.uri = uri ;
        Intent FotoComentarioIntent = new Intent().setClass(activityMain, ActivityFotoComentario.class);
        FotoComentarioIntent.putExtra("imagen_rop", fotoModel);
        FotoComentarioIntent.putExtra("ROPtmpId", mRop.getTmpId());
        startActivity(FotoComentarioIntent );
    }

    public void Permissions() {

        ArrayList<String> especificacionPermisos = new ArrayList<String>();

        int permissionCheckCamera = ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA);
        if (permissionCheckCamera != PackageManager.PERMISSION_GRANTED) {
            especificacionPermisos.add(Manifest.permission.CAMERA);
        }

        String[] permisos = new String[especificacionPermisos.size()];
        permisos = especificacionPermisos.toArray(permisos);

        if (especificacionPermisos.size() > 0) {
            this.requestPermissions(permisos, activityMain.REQUEST_IMAGE_CAPTURE);
        }

    }

    private boolean listaAccionesValida(){
        boolean resu = false;
        if (listaAccionPreventiva.size() > 0){
            resu = true;
        }
        return resu;
    }

    private void MostrarAccionCondicionSubestandar(){

        final DialogActoCondicionSubestandar dialogActoCondicion = new DialogActoCondicionSubestandar(getActivity(),nameEvent);
        dialogActoCondicion.show();

        dialogActoCondicion.btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Realm realm = Realm.getInstance(myConfig);
                try {
                    realm.beginTransaction();
                    mRop.deleteFromRealm();
                    realm.commitTransaction();
                    dialogRINSEG.dismiss();
                    activityMain.replaceFragment(new FragmentTabRops(), true, 0, 0, 0, 0);
                } catch (Exception e) {

                } finally {
                    realm.close();
                }
*/
            }
        });
    }

}
