package rinseg.asistp.com.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rinseg.asistp.com.adapters.IncidenciaAdapter;
import rinseg.asistp.com.adapters.InspeccionAdapter;
import rinseg.asistp.com.adapters.RopAdapter;
import rinseg.asistp.com.listener.ListenerClick;
import rinseg.asistp.com.models.Incidencia;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.ui.activities.ActivityInspeccionDetalle;
import rinseg.asistp.com.ui.activities.ActivityMain;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInspeccionDetalle2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInspeccionDetalle2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInspeccionDetalle2 extends Fragment implements ListenerClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerIncidencias;
    private RecyclerView.Adapter incidenciaAdapter;
    private RecyclerView.LayoutManager lManager;
    private List<Incidencia> listaIncidencias = new ArrayList<>();



    ActivityInspeccionDetalle activityMain;

    public FragmentInspeccionDetalle2() {
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
    public static FragmentInspeccionDetalle2 newInstance(String param1, String param2) {
        FragmentInspeccionDetalle2 fragment = new FragmentInspeccionDetalle2();
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
        View view = inflater.inflate(R.layout.fragment_inspeccion_detalle2_incidencias, container, false);



        listaIncidencias.add(new Incidencia("nombre 1","desc"));
        listaIncidencias.add(new Incidencia("nombre 2","descripcion"));
        listaIncidencias.add(new Incidencia("nombre 3","descripcion"));
        listaIncidencias.add(new Incidencia("nombre 4","desc"));

        setUpElements(view);
        setUpActions();

        return view;
    }

    //Proceso para cargar las vistas
    private void setUpElements(View v) {
        activityMain = ((ActivityInspeccionDetalle) getActivity());

        //configuracion para el recicler
        recyclerIncidencias = (RecyclerView) v.findViewById(R.id.recycler_view_inspeccion_detalle_incidencias);
        recyclerIncidencias.setHasFixedSize(true);
        // usar administrador para linearLayout
        lManager =  new LinearLayoutManager(this.getActivity().getApplicationContext());
        recyclerIncidencias.setLayoutManager(lManager);
        // Crear un nuevo Adaptador
        incidenciaAdapter = new IncidenciaAdapter(listaIncidencias,this);
        recyclerIncidencias.setAdapter(incidenciaAdapter);
    }

    //cargamos los eventos
    private void setUpActions() {
        activityMain.toolbarInspeccionDet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMain.replaceFragment(new FragmentInspeccionDetalle1(),true,0,0,0,0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        activityMain.toolbarInspeccionDet.setTitle(getResources().getString( R.string.title_ssma ).toString()+ " " +getResources().getString(R.string.title_incidentes).toString() );

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
    public void onItemClicked(IncidenciaAdapter.IncidenciaViewHolder holder, int position){
        activityMain.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_content_inspeccion_detalle,new FragmentInspeccionIncidenciaDetalle1())
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onItemClicked(InspeccionAdapter.InspeccionViewHolder holder, int position){}
    @Override
    public void onItemClicked(RopAdapter.RopViewHolder holder, int position){}


}
