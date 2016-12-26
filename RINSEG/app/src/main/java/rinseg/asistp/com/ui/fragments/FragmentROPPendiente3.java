package rinseg.asistp.com.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rinseg.asistp.com.models.AccionPreventiva;
import rinseg.asistp.com.models.FotoModel;
import rinseg.asistp.com.models.ROP;
import rinseg.asistp.com.models.SettingsRopRO;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.ui.activities.ActivityFotoComentario;
import rinseg.asistp.com.ui.activities.ActivityMain;
import rinseg.asistp.com.utils.RinsegModule;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentROPPendiente3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentROPPendiente3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentROPPendiente3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView txtBody;
    TextView txtNota;

    RealmConfiguration myConfig;

    private SettingsRopRO sRop;

    ROP mRop;

    Bundle bundle;


    ActivityMain activityMain;
    Button btnContinuar;

    public FragmentROPPendiente3() {
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
    public static FragmentROPPendiente3 newInstance(String param1, String param2) {
        FragmentROPPendiente3 fragment = new FragmentROPPendiente3();
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
        View view = inflater.inflate(R.layout.fragment_rop_pendiente3, container, false);


        setUpElements(view);
        setUpActions();

        LoadFormDefault(view);

        LoadRopPendiente();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        activityMain.toolbar.setTitle(R.string.title_rop);
        activityMain.ShowButtonsBottom(true);
        activityMain.ShowNumPagina();
        activityMain.btnFabMenu.setVisibility(View.VISIBLE);
        activityMain.btnFabMenu.collapseImmediately();
        activityMain.MostrarCantidadImagenesRop(mRop.getTmpId());
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
        super.onDestroyView();
        activityMain.btnFabMenu.setVisibility(View.GONE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == activityMain.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            launchActivityFotoComentario(data.getData());
        }
    }



    //Proceso para cargar las vistas
    private void setUpElements(View v) {
        bundle = getArguments();
        activityMain = ((ActivityMain) getActivity());
        txtBody = (TextView) v.findViewById(R.id.txt_compromiso_body);
        txtNota = (TextView) v.findViewById(R.id.txt_compromiso_nota);

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
        activityMain.btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMain.actualPaginaRop -= 1;
                Fragment fRopPendiente2 = new FragmentROPPendiente2();
                if(!activityMain.mostrarPagAccionRealizada){
                    fRopPendiente2 = new FragmentROPPendiente1();
                }
                Bundle args = new Bundle();
                args.putString("ROPtmpId", mRop.getTmpId());
                fRopPendiente2.setArguments(args);
                activityMain.replaceFragment(fRopPendiente2, true, R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left);
            }
        });
        activityMain.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMain.actualPaginaRop += 1;
                Fragment fRopPendiente4 = new FragmentROPPendiente4();
                Bundle args = new Bundle();
                args.putString("ROPtmpId", mRop.getTmpId());
                fRopPendiente4.setArguments(args);
                activityMain.replaceFragment(fRopPendiente4, true, R.anim.enter_from_left, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_right);
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
                  /*  count++;
                    String file = count + ".jpg";
                    File newfile = new File(file);
                    try {
                        newfile.createNewFile();
                    } catch (IOException e) {
                    }

                    Uri outputFileUri = Uri.fromFile(newfile);*/

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

    private void LoadFormDefault(View v) {
        Realm realm = Realm.getInstance(myConfig);
        try {
            sRop = realm.where(SettingsRopRO.class).findFirst();

            txtBody.setText(sRop.getBody());
            txtNota.setText(sRop.getNote());

        } catch (Exception ex) {

        } finally {
            realm.close();
        }

    }

    private void LoadRopPendiente() {

        String tmpIdRop = null;
        if (bundle != null) {
            tmpIdRop = bundle.getString("ROPtmpId", null);

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

    public void launchActivityFotoComentario(Uri uri) {
        FotoModel fotoModel =  new FotoModel();
        fotoModel.uri = uri ;
        Intent FotoComentarioIntent = new Intent().setClass(activityMain, ActivityFotoComentario.class);
        FotoComentarioIntent.putExtra("imagen_rop", fotoModel);
        FotoComentarioIntent.putExtra("ROPtmpId", mRop.getTmpId());
        startActivity(FotoComentarioIntent );
    }


}
