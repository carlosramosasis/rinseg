package rinseg.asistp.com.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.AtomicFile;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rinseg.asistp.com.adapters.IncidenciaAdapter;
import rinseg.asistp.com.adapters.RopAdapter;
import rinseg.asistp.com.listener.ListenerClick;
import rinseg.asistp.com.models.Inspeccion;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.ui.activities.ActivityInspeccionDetalle;
import rinseg.asistp.com.ui.activities.ActivityMain;
import rinseg.asistp.com.adapters.InspeccionAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInspecciones.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInspecciones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInspecciones extends Fragment implements ListenerClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;


    private FloatingActionButton btnAgregar;
    ActivityMain activityMain;

    private RecyclerView recyclerInspecciones;
    private RecyclerView.Adapter inspeccionAdapter;
    private RecyclerView.LayoutManager lManager;
    private List<Inspeccion> listaInspeccinoes = new ArrayList<>();

    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    String dir;

    public FragmentInspecciones() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentROPsPendientes.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentInspecciones newInstance(String param1, String param2) {
        FragmentInspecciones fragment = new FragmentInspecciones();
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
        View view = inflater.inflate(R.layout.fragment_inspecciones, container, false);


        listaInspeccinoes.add(new Inspeccion("Inspección 0001", new Date()));
        listaInspeccinoes.add(new Inspeccion("Inspección 0001", new Date()));
        listaInspeccinoes.add(new Inspeccion("Inspección 0001", new Date()));
        listaInspeccinoes.add(new Inspeccion("Inspección 0001", new Date()));

        setUpElements(view);
        setUpActions();

        Permissions();

        // Here, we are making a folder named picFolder to store
        // pics taken by the camera using this application.
        dir = Environment.getExternalStorageDirectory() + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();


        return view;

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE ) {
            Log.d("CameraDemo", "Pic saved");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityMain.toolbar.setTitle(R.string.title_inspecciones);
        activityMain.ShowButtonsBottom(false);
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
    public void onItemClicked(InspeccionAdapter.InspeccionViewHolder holder,int position){
      //  FragmentInspeccionDetalle1 inspeccionDetalle1 = FragmentInspeccionDetalle1.newInstance(listaInspeccinoes.get(position));
        launchActivityInspeccionDetalle(listaInspeccinoes.get(position));

        /*activityMain.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_content,inspeccionDetalle1)
                .addToBackStack(null)
                .commit();*/
    }
    @Override
    public void onItemClicked(RopAdapter.RopViewHolder holder, int position){}
    @Override
    public void onItemClicked(IncidenciaAdapter.IncidenciaViewHolder holder, int position){}


    //Proceso para cargar las vistas
    private void setUpElements(View v) {
        btnAgregar = (FloatingActionButton) v.findViewById(R.id.btn_agregar_inspeccion);
        activityMain = ((ActivityMain) getActivity());

        //configuracion para el recicler
        recyclerInspecciones = (RecyclerView) v.findViewById(R.id.recycler_view_inspecciones);
        recyclerInspecciones.setHasFixedSize(true);
        // usar administrador para linearLayout
        lManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        recyclerInspecciones.setLayoutManager(lManager);
        // Crear un nuevo Adaptador
        inspeccionAdapter = new InspeccionAdapter(listaInspeccinoes,this);
        recyclerInspecciones.setAdapter(inspeccionAdapter);
    }

    private void setUpActions() {
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, the counter will be incremented each time, and the
                // picture taken by camera will be stored as 1.jpg,2.jpg
                // and likewise.    CODIGO PARA LLAMAR A CAMARA
               /* count++;
                String file = dir + count + ".jpg";
                File newfile = new File(file);
                try {
                    newfile.createNewFile();
                } catch (IOException e) {
                }

                Uri outputFileUri = Uri.fromFile(newfile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);*/

                activityMain.ShowButtonsBottom(true);
                activityMain.replaceFragment(new FragmentInspeccionNuevo1(), true,0,0,0,0);

            }
        });
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
            this.requestPermissions(permisos, TAKE_PHOTO_CODE);
        }

    }

    /**
     * Created on 28/09/16
     * Módulo encargado de lanzar la actividad
     */
    public void launchActivityInspeccionDetalle(Inspeccion insp) {

        Intent InspeccionDetalleIntent = new Intent().setClass(activityMain, ActivityInspeccionDetalle.class);
        InspeccionDetalleIntent.putExtra("Inspeccion", insp);
        startActivity(InspeccionDetalleIntent );
    }
}
