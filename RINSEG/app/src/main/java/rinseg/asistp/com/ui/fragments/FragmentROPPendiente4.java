package rinseg.asistp.com.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rinseg.asistp.com.models.CompanyRO;
import rinseg.asistp.com.models.FotoModel;
import rinseg.asistp.com.models.ImagenRO;
import rinseg.asistp.com.models.ROP;
import rinseg.asistp.com.models.SettingsRopRO;
import rinseg.asistp.com.models.User;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.services.RestClient;
import rinseg.asistp.com.services.Services;
import rinseg.asistp.com.ui.activities.ActivityFotoComentario;
import rinseg.asistp.com.ui.activities.ActivityMain;
import rinseg.asistp.com.utils.Constants;
import rinseg.asistp.com.utils.DialogLoading;
import rinseg.asistp.com.utils.DialogRINSEG;
import rinseg.asistp.com.utils.Generic;
import rinseg.asistp.com.utils.Messages;
import rinseg.asistp.com.utils.RinsegModule;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentROPPendiente4.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentROPPendiente4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentROPPendiente4 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    ActivityMain activityMain;

    RealmConfiguration myConfig;

    private SettingsRopRO sRop;
    ArrayAdapter<CompanyRO> adapteCompanie;

    ROP mRop;

    TextView txtvUsuario, txtvEmpresa;
    EditText txtNombreSupervisor;
    Spinner spinnerEmpresa;
    RadioButton rbtSi;
    RadioButton rbtNo;

    private DialogLoading dialogLoading;

    Bundle bundle;

    int cantImagenesTotal = 0;
    int cantImagenesEnviadas = 0;
    int cantImagenesEnviadasYrecibidos = 0;


    public FragmentROPPendiente4() {
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
    public static FragmentROPPendiente4 newInstance(String param1, String param2) {
        FragmentROPPendiente4 fragment = new FragmentROPPendiente4();
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
        View view = inflater.inflate(R.layout.fragment_rop_pendiente4, container, false);


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
        activityMain.HideNumPagina();
        super.onDestroyView();
        activityMain.btnFabMenu.setVisibility(View.GONE);
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
        activityMain.btnRight.setText(R.string.btn_terminar);
        activityMain.btnRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0);

        dialogLoading = new DialogLoading(getActivity());

        txtvUsuario = (TextView) v.findViewById(R.id.txt_view_rop_p4_usuario);
        txtvEmpresa = (TextView) v.findViewById(R.id.txt_view_rop_p4_empresa);
        txtNombreSupervisor = (EditText) v.findViewById(R.id.txt_rop_p4_nombre_supervisor);
        spinnerEmpresa = (Spinner) v.findViewById(R.id.spinner_rop_p4_empresa);
        rbtSi = (RadioButton) v.findViewById(R.id.rbt_rop4_si);
        rbtNo = (RadioButton) v.findViewById(R.id.rbt_rop4_no);


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
                SaveRop();
                Fragment fRopPendiente3 = new FragmentROPPendiente3();
                Bundle args = new Bundle();
                args.putString("ROPtmpId", mRop.getTmpId());
                fRopPendiente3.setArguments(args);
                activityMain.replaceFragment(fRopPendiente3, true, R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left);
            }
        });
        activityMain.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidarFormulario()) {
                    return;
                }

                activityMain.actualPaginaRop += 1;
                ConfirmarCierreRop(getString(R.string.msg_confirmar_cerrar_rop));
                //activityMain.replaceFragment(new FragmentROPPendiente4(),true,R.anim.enter_from_left, R.anim.exit_to_left,R.anim.enter_from_right,R.anim.exit_to_right);
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
            if (sRop.companies.size() > 0) {
                //cargar Empresa (Companies)
                adapteCompanie = new ArrayAdapter<CompanyRO>(getActivity(), R.layout.spinner_item, sRop.companies);
                adapteCompanie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEmpresa.setAdapter(adapteCompanie);
            }

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


                    if (activityMain.usuarioLogueado != null) {
                        txtvUsuario.setText(activityMain.usuarioLogueado.getName() + " " + activityMain.usuarioLogueado.getName());

                        int idCompanyUsuario = activityMain.usuarioLogueado.getCompany_id();
                        if (idCompanyUsuario != 0) {
                            CompanyRO companyUser = realm.where(CompanyRO.class).equalTo("id", idCompanyUsuario).findFirst();
                            if (companyUser != null) {
                                txtvEmpresa.setText(companyUser.getDisplayName());
                            }
                        }
                    }

                    txtNombreSupervisor.setText(mRop.getSupervisorName());
                    // recuperar Empresa
                    for (int i = 0; i < sRop.companies.size(); i++) {
                        CompanyRO tmpCompany = sRop.companies.get(i);
                        if (tmpCompany.getId() == mRop.getSupervisorIdCompany()) {
                            spinnerEmpresa.setSelection(i);
                            break;
                        }
                    }

                    if (mRop.isResearch_required()) {
                        rbtSi.setChecked(true);
                    } else {
                        rbtNo.setChecked(true);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    realm.close();
                }
            }

        }
    }

    private boolean ValidarFormulario() {
        boolean resu = true;

        if (txtNombreSupervisor.getText().length() == 0) {
            txtNombreSupervisor.setError(getString(R.string.error_nom_rp2));
            resu = false;
        }

        CompanyRO companySelect;
        companySelect = ((CompanyRO) spinnerEmpresa.getSelectedItem());
        if (companySelect.getId() == 0) {
            TextView txtEmpresa = (TextView) spinnerEmpresa.getSelectedView();
            txtEmpresa.setError("");
            resu = false;
        }

        return resu;
    }

    //// TODO: Cierra rop
    private void SaveRop() {


        Realm realm = Realm.getInstance(myConfig);
        try {
            CompanyRO empresaSleccionada = ((CompanyRO) spinnerEmpresa.getSelectedItem());

            realm.beginTransaction();

            mRop.setReporterName(txtvUsuario.getText().toString());
            mRop.setReporterCompany(txtvEmpresa.getText().toString().toString());
            mRop.setSupervisorName(txtNombreSupervisor.getText().toString());
            mRop.setSupervisorIdCompany(empresaSleccionada.getId());
            mRop.setSupervisorCompany(empresaSleccionada.getDisplayName());

            boolean requiere_investigacion = false;
            if (rbtSi.isChecked()) {
                requiere_investigacion = true;
            }
            mRop.setResearch_required(requiere_investigacion);


            realm.commitTransaction();

        } catch (Exception e) {
            Messages.showSB(getView(), e.getMessage(), "ok");
        } finally {
            realm.close();
        }
    }


    private void CerrarYenviarRop(ROP rop) {
        Realm realm = Realm.getInstance(myConfig);
        try {
            // agregamos la fecha de cierre al rop
            realm.beginTransaction();
            Calendar fecha = Calendar.getInstance();
            mRop.setDateClose(fecha.getTime());
            mRop.setDateCloseString(Generic.dateFormatterMySql.format(mRop.getDateClose()));
            mRop.setCerrado(true);
            realm.commitTransaction();
        } catch (Exception e) {
            realm.close();
            Messages.showSB(getView(), getString(R.string.msg_error_cerrar_rop), "ok");
            return;
        } finally {
            realm.close();
        }


        final ROP ropToSend = realm.copyFromRealm(rop);

        String token = activityMain.usuarioLogueado.getApi_token();
        final View v = this.getView();
        dialogLoading.show();
        RestClient restClient = new RestClient(Services.URL_ROPS);

        Call<ResponseBody> call = restClient.iServices.setRopCerrado(ropToSend, token);

        Log.e("rop risk ", "" + rop.getRiskId());
        Log.e("call risk ", "" + call.toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");

                        if (status.equals(Constants.SUCCESS)) {
                            JSONObject messageResult = jsonObject.getJSONObject("message");
                            JSONObject ropResult = messageResult.getJSONObject("rop");

                            String api_token = activityMain.usuarioLogueado.getApi_token();

                            Realm realm = Realm.getInstance(myConfig);
                            realm.beginTransaction();
                            mRop.setId(ropResult.getInt("id"));
                            realm.commitTransaction();

                            int idRop = mRop.getId();

                            if (ropToSend.listaImgComent.size() > 0) {
                                cantImagenesTotal = ropToSend.listaImgComent.size();
                                for (ImagenRO img : ropToSend.listaImgComent) {
                                    new EnviarImagenRop(img, ropToSend.getTmpId(), idRop, api_token).execute("", "", "");
                                }
                            } else {
                                dialogLoading.dismiss();
                                mostrarDialogRopDireccionar(getString(R.string.msg_cerrar_rop_ok));
                            }

                        }

                        Log.e("jsonObject", jsonObject.toString());

                    } catch (Exception e) {
                        dialogLoading.dismiss();
                        e.printStackTrace();
                        mostrarDialogRopDireccionar(getString(R.string.msg_error_guardar_rop));
                    }
                } else {
                    dialogLoading.dismiss();
                    mostrarDialogRopDireccionar(getString(R.string.msg_error_guardar_rop));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialogLoading.dismiss();
                mostrarDialogRopDireccionar(getString(R.string.msg_error_guardar_rop));
            }

        });

    }


    public class EnviarImagenRop extends AsyncTask<String, Integer, Integer> {
        private ImagenRO imagenRop;
        private String tmpIdRop;
        private int idRop;
        private String apitoken;


        EnviarImagenRop(ImagenRO imagen, String pTmpIdRop, int pIdRop, String mApiToken) {
            imagenRop = imagen;
            tmpIdRop = pTmpIdRop;
            idRop = pIdRop;
            apitoken = mApiToken;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int errorValue = 0;
            try {

                RestClient restClient = new RestClient(Services.URL_ROPS);

                File myDir = getActivity().getApplicationContext().getFilesDir();
                File file = new File(myDir, Constants.PATH_IMAGE_GALERY + tmpIdRop + "/" + imagenRop.getName());

                // create RequestBody instance from file
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file_image", file.getName(), requestFile);

                // add another part within the multipart request

                RequestBody description =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), imagenRop.getDescripcion());
                RequestBody rop_id =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), String.valueOf(idRop));


                Call<ResponseBody> call = restClient.iServices.setImageRop(rop_id, description, body, apitoken);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        int code = response.code();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String status = jsonObject.getString("status");
                                if (status.equals(Constants.SUCCESS)) {
                                    JSONObject messageResult = jsonObject.getJSONObject("message");
                                    JSONObject ropImageResult = messageResult.getJSONObject("rop_image");

                                    Realm real = Realm.getInstance(myConfig);

                                    imagenRop = real.where(ImagenRO.class).equalTo("name", ropImageResult.getString("name")).findFirst();
                                    if (imagenRop != null) {
                                        real.beginTransaction();
                                        imagenRop.setId(ropImageResult.getInt("id"));
                                        real.commitTransaction();
                                        cantImagenesEnviadasYrecibidos += 1;
                                    }

                                }

                                Log.e("jsonObject", jsonObject.toString());


                            } catch (Exception e) {
                                // dialogLoading.dismiss();
                                e.printStackTrace();
                                // Messages.showSB(getView(), getString(R.string.msg_error_guardar), "ok");
                            }


                        } else {
                            Log.e("imagen", response.message());
                            Log.e("imagen error", response.errorBody().toString());
                            //dialogLoading.dismiss();
                            //Messages.showSB(getView(), getString(R.string.msg_login_fail), "ok");
                        }

                        cantImagenesEnviadas += 1;
                        postExecute();

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        cantImagenesEnviadas += 1;
                        postExecute();
                        //dialogLoading.dismiss();
                        //Messages.showSB(getView(), getString(R.string.msg_servidor_inaccesible), "ok");
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                cantImagenesEnviadas += 1;
                postExecute();
            }


            return errorValue;
        }

        @Override
        protected void onPostExecute(Integer errorValue) {
           /* if (cantImagenesTotal == cantImagenesEnviadas) {
                dialogLoading.hide();

                mostrarDialogRopDireccionar(getString(R.string.msg_cerrar_rop_ok));

                cantImagenesTotal = 0;
                cantImagenesEnviadas = 0;
                cantImagenesEnviadasYrecibidos = 0;
            }*/
        }


    }

    public void postExecute() {
        if (cantImagenesTotal == cantImagenesEnviadas) {
            dialogLoading.hide();

            mostrarDialogRopDireccionar(getString(R.string.msg_cerrar_rop_ok));

            cantImagenesTotal = 0;
            cantImagenesEnviadas = 0;
            cantImagenesEnviadasYrecibidos = 0;
        }
    }


    private void mostrarDialogRopDireccionar(String msg) {
        final DialogRINSEG dialogRINSEG = new DialogRINSEG(getActivity());
        dialogRINSEG.show();
        dialogRINSEG.setBody(msg);
        dialogRINSEG.btnCancelar.setVisibility(View.GONE);
        dialogRINSEG.btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMain.replaceFragment(new FragmentTabRops(), true, 0, 0, 0, 0);
                dialogRINSEG.dismiss();
            }
        });
    }

    private void ConfirmarCierreRop(String msg) {
        final DialogRINSEG dialogConfirmarCierre = new DialogRINSEG(getActivity());
        dialogConfirmarCierre.show();
        dialogConfirmarCierre.setBody(msg);
        dialogConfirmarCierre.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogConfirmarCierre.dismiss();
            }
        });
        dialogConfirmarCierre.btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogConfirmarCierre.dismiss();
                SaveRop();
                CerrarYenviarRop(mRop);
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
