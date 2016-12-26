package rinseg.asistp.com.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.ui.activities.ActivityMain;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInspeccionNuevo1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInspeccionNuevo1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInspeccionNuevo1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    ActivityMain activityMain;

    public FragmentInspeccionNuevo1() {
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
    public static FragmentInspeccionNuevo1 newInstance(String param1, String param2) {
        FragmentInspeccionNuevo1 fragment = new FragmentInspeccionNuevo1();
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
        View view = inflater.inflate(R.layout.fragment_inspeccion_nuevo1, container, false);


        setUpElements(view);
        setUpActions();

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        activityMain.toolbar.setTitle(R.string.title_inspectores);
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
    public void onDestroyView() {
        activityMain.ButtonBottomSetDefault();
        super.onDestroyView();
    }


    //Proceso para cargar las vistas
    private void setUpElements(View v) {
        activityMain = ((ActivityMain) getActivity());
        activityMain.btnLeft.setText(R.string.btn_cancelar);

    }

    //cargamos los eventos
    private void setUpActions() {

        activityMain.btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMain.replaceFragment(new FragmentInspecciones(), true, 0, 0, 0, 0);
            }
        });
        activityMain.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMain.replaceFragment(new FragmentInspeccionNuevo2(), true, R.anim.enter_from_left, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_right);
            }
        });
    }
}
