package rinseg.asistp.com.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rinseg.asistp.com.models.User;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.services.RestClient;
import rinseg.asistp.com.services.Services;
import rinseg.asistp.com.ui.activities.ActivityMain;
import rinseg.asistp.com.utils.DialogLoading;
import rinseg.asistp.com.utils.Messages;
import rinseg.asistp.com.utils.RinsegModule;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSingIn.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSingIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSingIn extends Fragment {

    private User usuario;
    private Button mBtnIngresar;
    private TextInputEditText txtUsu;
    private TextInputEditText txtPass;

    private DialogLoading dialogLoading;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RealmConfiguration myConfig;

    public FragmentSingIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSingIn.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSingIn newInstance(String param1, String param2) {
        FragmentSingIn fragment = new FragmentSingIn();
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

        View view = inflater.inflate(R.layout.fragment_sing_in, container, false);

        setUpElements(view);
        setUpActions();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




  /*  @Override
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
     * <p>
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
        mBtnIngresar = (Button) v.findViewById(R.id.btnIngresar);
        txtUsu = (TextInputEditText) v.findViewById(R.id.text_input_usuario);
        txtPass = (TextInputEditText) v.findViewById(R.id.text_input_contraseña);
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

    /**
     * Created on 23/05/2016
     * Proceso para iniciar las acciones:
     */
    private void setUpActions() {
        mBtnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(txtUsu.getText().toString(), txtPass.getText().toString(), v);
                /*
                Intent myIntent = new Intent(getActivity(), ActivityMain.class);
                getActivity().startActivity(myIntent);
                getActivity().finish();*/
            }
        });
    }


    private void Login(String usu, String pass, final View v) {
        /*if(usu.length() == 0 || pass.length() == 0) {
            Messages.showSB(v,  getString(R.string.msg_login_incomplete) , "ok");
            return;
        }
*/
        dialogLoading.show();
        RestClient restClient = new RestClient(Services.URL_SECURITY);
        Call<ResponseBody> call = restClient.iServices.setLogin("carlosc", "123456");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.e("jsonObject",jsonObject.toString());
                        JSONObject usur = jsonObject.getJSONObject("user");
                        Gson gson = new Gson();
                        User u = gson.fromJson(usur.toString(), User.class);
                        SaveUserInRealm(u);
                        launchActivityMain(u);
                        dialogLoading.dismiss();
                    } catch (Exception e) {
                        dialogLoading.dismiss();
                        e.printStackTrace();
                        Messages.showSB(v, e.getMessage(), "ok");
                    }
                } else {
                    dialogLoading.dismiss();
                    Messages.showSB(v, getString(R.string.msg_login_fail), "ok");
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialogLoading.dismiss();
                Messages.showSB(v, getString(R.string.msg_servidor_inaccesible), "ok");
            }
        });
    }

    /**
     * Created on 28/09/16
     * Módulo encargado de lanzar la actividad Main
     */
    private void launchActivityMain(User User) {
        Intent MainIntent = new Intent().setClass(getContext(), ActivityMain.class);
        //  MainIntent.putExtra("User", User);

        MainIntent.putExtra("Sincronizar", true);
        startActivity(MainIntent);
        getActivity().finish();
    }

    private void SaveUserInSharedPreferences(User user) {
        SharedPreferences appSettings = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());

        StringBuilder usuarioString = new StringBuilder();

        usuarioString.append(user.getId()).append(",");
        usuarioString.append(user.getName()).append(",");
        usuarioString.append(user.getLastname()).append(",");
        usuarioString.append(user.getUsername()).append(",");
        usuarioString.append(user.getDni()).append(",");
        usuarioString.append(user.getEmail()).append(",");
        usuarioString.append(user.getPhoto()).append(",");
        usuarioString.append(user.getApi_token()).append(",");
        usuarioString.append(user.getCompany_id()).append(",");
        usuarioString.append(user.getManagement_id()).append(",");


        SharedPreferences.Editor prefsEditor = appSettings.edit();
        prefsEditor.putString("userShared", usuarioString.toString());
        prefsEditor.commit();
    }

    private void SaveUserInRealm(User user) {
        Realm realm = Realm.getInstance(myConfig);
        try {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(User.class);
                }
            });

            realm.beginTransaction();
            User userLogued = realm.createObject(User.class);
            userLogued.setId(user.getId());
            userLogued.setName(user.getName());
            userLogued.setLastname(user.getLastname());
            userLogued.setUsername(user.getUsername());
            userLogued.setDni(user.getDni());
            userLogued.setEmail(user.getEmail());
            userLogued.setPhoto(user.getPhoto());
            userLogued.setApi_token(user.getApi_token());
            userLogued.setCompany_id(user.getCompany_id());
            userLogued.setManagement_id(user.getManagement_id());

            realm.commitTransaction();

        } catch (Exception e) {

        }finally {
            realm.close();
        }
    }

}
