package rinseg.asistp.com.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import rinseg.asistp.com.models.AreaRO;
import rinseg.asistp.com.models.CompanyRO;
import rinseg.asistp.com.models.EventRO;
import rinseg.asistp.com.models.ROP;
import rinseg.asistp.com.models.RiskRO;
import rinseg.asistp.com.models.SecuencialRO;
import rinseg.asistp.com.models.SettingsRopRO;
import rinseg.asistp.com.models.TargetRO;
import rinseg.asistp.com.rinseg.R;
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
 * {@link FragmentROPPendiente1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentROPPendiente1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentROPPendiente1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    FragmentROPPendiente1 thiss = this;

    ActivityMain activityMain;
    Spinner spinnerPotencialPerdida;
    Spinner spinnerTipoEvento;
    Spinner spinnerBlanco;
    Spinner spinnerAreaResp;
    EditText txtLugarExacto;
    Spinner spinnerEmpresa;
    EditText txtDesc;
    TextView txtFecha;
    TextView txtHora;
    ImageButton btnCalendar;
    ImageButton btnHora;
    Calendar newCalendar;

    private SettingsRopRO sRop;

    RealmConfiguration myConfig;

    ArrayAdapter<RiskRO> adapterPotencialPerdida;
    ArrayAdapter<EventRO> adapterTipoEvento;
    ArrayAdapter<TargetRO> adapterTargets;
    ArrayAdapter<AreaRO> adapteArea;
    ArrayAdapter<CompanyRO> adapteCompanie;

    ROP mRop;

    Calendar newDateForFecha = Calendar.getInstance();
    Calendar newDateForHora = Calendar.getInstance();
    Calendar newDateForROP;

    private DialogLoading dialogLoading;



    private String nameEvent;

    Bundle bundle;


    public FragmentROPPendiente1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentROPPendiente1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentROPPendiente1 newInstance(ROP pRop) {
        FragmentROPPendiente1 fragment = new FragmentROPPendiente1();
        Bundle args = new Bundle();
       /* args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
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
        View view = inflater.inflate(R.layout.fragment_rop_pendiente1, container, false);


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
        activityMain.ShowButtonsBottom(true);

        ValidarSgtePagina();
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
    }


    //Proceso para cargar las vistas
    private void setUpElements(View v) {
        bundle = getArguments();

        activityMain = ((ActivityMain) getActivity());
        activityMain.btnLeft.setText(R.string.btn_cancelar);
        spinnerPotencialPerdida = (Spinner) v.findViewById(R.id.spinner_rop_p1_potencial_perdida);
        spinnerTipoEvento = (Spinner) v.findViewById(R.id.spinner_rop_p1_tipo_evento);
        spinnerBlanco = (Spinner) v.findViewById(R.id.spinner_rop_p1_blanco);
        spinnerAreaResp = (Spinner) v.findViewById(R.id.spinner_rop_p1_area_resp);
        txtLugarExacto = (EditText) v.findViewById(R.id.txt_rop_p1_lugar_exacto);
        spinnerEmpresa = (Spinner) v.findViewById(R.id.spinner_rop_p1_empresa);
        txtDesc = (EditText) v.findViewById(R.id.txt_rop_p1_desc);
        txtFecha = (TextView) v.findViewById(R.id.txt_rop_p1_fecha);
        txtHora = (TextView) v.findViewById(R.id.txt_rop_p1_hora);
        btnCalendar = (ImageButton) v.findViewById(R.id.btn_rpo_p1_calendar);
        btnHora = (ImageButton) v.findViewById(R.id.btn_rop_p1_hora);
        newCalendar = Calendar.getInstance();

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

        txtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker();
            }
        });
        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker();
            }
        });


        activityMain.btnLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mRop == null) {
                    activityMain.replaceFragment(new FragmentTabRops(), true, 0, 0, 0, 0);
                    return;
                }

                if (mRop.puedeElimianrse()) {

                    final DialogRINSEG dialogRINSEG = new DialogRINSEG(getActivity());
                    dialogRINSEG.show();
                    dialogRINSEG.setBody(getString(R.string.dialog_p1_body));
                    dialogRINSEG.btnAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Realm realm = Realm.getInstance(myConfig);
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

                        }
                    });
                } else {
                    activityMain.replaceFragment(new FragmentTabRops(), true, 0, 0, 0, 0);
                }


            }
        });
        activityMain.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidarFormulario()) {
                    return;
                }

                try {
                    dialogLoading.show();
                    SaveRop();

                    activityMain.actualPaginaRop += 1;

                    Fragment fRopPendiente2 = new FragmentROPPendiente2();
                    if (!activityMain.mostrarPagAccionRealizada) {
                        fRopPendiente2 = new FragmentROPPendiente3();
                    }

                    Bundle args = new Bundle();
                    args.putString("ROPtmpId", mRop.getTmpId());
                    args.putString("nameEvent", nameEvent);
                    fRopPendiente2.setArguments(args);
                    activityMain.replaceFragment(fRopPendiente2, true, R.anim.enter_from_left, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_right);
                    dialogLoading.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        spinnerTipoEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ValidarSgtePagina();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void LoadFormDefault(View v) {
        final Realm realm = Realm.getInstance(myConfig);
        try {

            sRop = realm.where(SettingsRopRO.class).findFirst();
            adapterPotencialPerdida = new ArrayAdapter<RiskRO>(getActivity(), R.layout.spinner_item, sRop.risks);
            adapterPotencialPerdida.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPotencialPerdida.setAdapter(adapterPotencialPerdida);

            //cargar Tipo de evento  (Events)
            adapterTipoEvento = new ArrayAdapter<EventRO>(getActivity(), R.layout.spinner_item, sRop.events);
            adapterTipoEvento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTipoEvento.setAdapter(adapterTipoEvento);

            //cargar Blanco (Targets)
            adapterTargets = new ArrayAdapter<TargetRO>(getActivity(), R.layout.spinner_item, sRop.targets);
            adapterTargets.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBlanco.setAdapter(adapterTargets);

            //cargar Blanco (Area)
            adapteArea = new ArrayAdapter<AreaRO>(getActivity(), R.layout.spinner_item, sRop.areas);
            adapteArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAreaResp.setAdapter(adapteArea);

            //cargar Empresa (Companies)
            adapteCompanie = new ArrayAdapter<CompanyRO>(getActivity(), R.layout.spinner_item, sRop.companies);
            adapteCompanie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEmpresa.setAdapter(adapteCompanie);


        } catch (Exception e) {
            Messages.showSB(v, e.getMessage(), "ok");
        } finally {
            realm.close();
        }
    }

    private void LoadRopPendiente() {

        String tmpIdRop = null;
        if (bundle != null) {
            tmpIdRop = bundle.getString("ROPtmpId", null);
            boolean vieneDeListado = bundle.getBoolean("vieneDeListado", false);
            //ROP tmpRop = new ROP();
            if (tmpIdRop != null) {
                final Realm realm = Realm.getInstance(myConfig);
                try {
                    mRop = realm.where(ROP.class).equalTo("tmpId", tmpIdRop).findFirst();
                    if (mRop == null) {
                        return;
                    }

                    if (vieneDeListado) {
                        realm.beginTransaction();
                        mRop = realm.where(ROP.class).equalTo("tmpId", tmpIdRop).findFirst();
                        mRop.setPuedeElimianrse(false);
                        realm.commitTransaction();
                    }


                    // recuperar potencial de perdida
                    for (int i = 0; i < sRop.risks.size(); i++) {
                        RiskRO tmpRisk = sRop.risks.get(i);
                        if (tmpRisk.getId() == mRop.getRiskId()) {
                            spinnerPotencialPerdida.setSelection(i);
                            break;
                        }
                    }

                    // recuperar tipo de evento
                    for (int i = 0; i < sRop.events.size(); i++) {
                        EventRO tmpEvent = sRop.events.get(i);
                        if (tmpEvent.getId() == mRop.getEventId()) {
                            spinnerTipoEvento.setSelection(i);
                            break;
                        }
                    }

                    // recuperar Blanco
                    for (int i = 0; i < sRop.targets.size(); i++) {
                        TargetRO tmpTarget = sRop.targets.get(i);
                        if (tmpTarget.getId() == mRop.getTargetId()) {
                            spinnerBlanco.setSelection(i);
                            break;
                        }
                    }

                    // recuperar area responsable
                    for (int i = 0; i < sRop.areas.size(); i++) {
                        AreaRO tmpArea = sRop.areas.get(i);
                        if (tmpArea.getId() == mRop.getAreaId()) {
                            spinnerAreaResp.setSelection(i);
                            break;
                        }
                    }

                    // recuperar lugar exacto
                    txtLugarExacto.setText(mRop.getEventPlace());

                    // recuperar Empresa
                    for (int i = 0; i < sRop.companies.size(); i++) {
                        CompanyRO tmpCompany = sRop.companies.get(i);
                        if (tmpCompany.getId() == mRop.getCompanyId()) {
                            spinnerEmpresa.setSelection(i);
                            break;
                        }
                    }

                    //recuperar fecha y hora
                    newDateForROP = Calendar.getInstance();
                    newDateForROP.setTime(mRop.getEventDate());
                    newDateForFecha = new GregorianCalendar(newDateForROP.get(Calendar.YEAR), newDateForROP.get(Calendar.MONTH), newDateForROP.get(Calendar.DAY_OF_MONTH));
                    newDateForHora = new GregorianCalendar(Constants.ANIO, Constants.MES, Constants.DIA, newDateForROP.get(Calendar.HOUR_OF_DAY), newDateForROP.get(Calendar.MINUTE));
                    txtFecha.setText(Generic.dateFormatter.format(newDateForROP.getTime()));
                    txtHora.setText(Generic.timeFormatter.format(newDateForROP.getTime()));


                    // recuperar descripcion
                    txtDesc.setText(mRop.getEventDescription());


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    realm.close();
                }
            }
            if (mRop != null) {


            }
        }
    }

    private void ShowDatepicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDateForFecha = Calendar.getInstance();
                newDateForFecha.set(year, monthOfYear, dayOfMonth);
                txtFecha.setText(Generic.dateFormatter.format(newDateForFecha.getTime()));
                txtFecha.setError(null);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    private void ShowTimePicker() {
        newCalendar = Calendar.getInstance();
        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                // String am_pm = "AM";

                newDateForHora = new GregorianCalendar(Constants.ANIO, Constants.MES, Constants.DIA, selectedHour, selectedMinute);

               /* if (selectedHour >= 12) {
                    am_pm = "PM";
                    if (selectedHour > 12) {
                        selectedHour = selectedHour - 12;
                    }
                }*/

                //txtHora.setText(selectedHour + ":" + selectedMinute + " " + am_pm);
                txtHora.setText(Generic.timeFormatter.format(newDateForHora.getTime()));
                txtHora.setError(null);
            }
        }, hour, minute, false);//True 24 hour time format
        mTimePicker.show();
    }

    private boolean ValidarFormulario() {


        boolean resu = true;

        if (txtDesc.getText().length() == 0) {
            txtDesc.setError(getString(R.string.error_desc_rp1));
            resu = false;
        }

        if (txtHora.getText().length() == 0) {
            txtHora.setError(getString(R.string.error_hora_rp1));
            resu = false;
        }

        if (txtFecha.getText().length() == 0) {
            txtFecha.setError(getString(R.string.error_fecha_rp1));
            resu = false;
        }

        CompanyRO companySelect;
        companySelect = ((CompanyRO) spinnerEmpresa.getSelectedItem());
        if (companySelect.getId() == 0) {
            TextView txtEmpresa = (TextView) spinnerEmpresa.getSelectedView();
            txtEmpresa.setError("");
            resu = false;
        }

        if (txtLugarExacto.getText().length() == 0) {
            txtLugarExacto.setError(getString(R.string.error_lugar_exacto_rp1));
            resu = false;
        }

        AreaRO areaSelect;
        areaSelect = ((AreaRO) spinnerAreaResp.getSelectedItem());
        if (areaSelect.getId() == 0) {
            TextView txtArea = (TextView) spinnerAreaResp.getSelectedView();
            txtArea.setError("");
            resu = false;
        }

        TargetRO targetSelect;
        targetSelect = ((TargetRO) spinnerBlanco.getSelectedItem());
        if (targetSelect.getId() == 0) {
            TextView txtBlanco = (TextView) spinnerBlanco.getSelectedView();
            txtBlanco.setError("");
            resu = false;
        }


        EventRO eventSelect;
        eventSelect = ((EventRO) spinnerTipoEvento.getSelectedItem());
        if (eventSelect.getId() == 0) {
            TextView txtEvent = (TextView) spinnerTipoEvento.getSelectedView();
            txtEvent.setError("");
            resu = false;
        } else {
            nameEvent = eventSelect.getName();
        }

        RiskRO potencialPerdidaSelect;
        potencialPerdidaSelect = ((RiskRO) spinnerPotencialPerdida.getSelectedItem());
        if (potencialPerdidaSelect.getId() == 0) {
            TextView txtPotencialPerdida = (TextView) spinnerPotencialPerdida.getSelectedView();
            txtPotencialPerdida.setError("");
            resu = false;
        }

        return resu;
    }


    private void SaveRop() {
        Realm realm = Realm.getInstance(myConfig);
        try {
            RiskRO potencialPerdida = ((RiskRO) spinnerPotencialPerdida.getSelectedItem());
            EventRO tipoEvento = ((EventRO) spinnerTipoEvento.getSelectedItem());
            TargetRO blanco = ((TargetRO) spinnerBlanco.getSelectedItem());
            AreaRO areaResp = ((AreaRO) spinnerAreaResp.getSelectedItem());
            CompanyRO empresa = ((CompanyRO) spinnerEmpresa.getSelectedItem());


            realm.beginTransaction();


            if (mRop != null) {   // recuperar para actualizar
                mRop = realm.where(ROP.class).equalTo("tmpId", mRop.getTmpId()).findFirst();

            } else {  //creamos uno rop nuevo
                mRop = realm.createObject(ROP.class);

                boolean puedeEliminarse = bundle.getBoolean("puedeEliminarse", false);
                mRop.setPuedeElimianrse(puedeEliminarse);

                mRop.setUserId(activityMain.usuarioLogueado.getId());

                // Obtener id Secuencial para id temporal
                int codigoSecuencial = 0;
                RealmResults<SecuencialRO> resultsSecuancial = realm.where(SecuencialRO.class).equalTo("tagTabla", Constants.tagROP_Pendiente).findAll();
                if (resultsSecuancial.isEmpty() && resultsSecuancial.size() < 1) {
                    codigoSecuencial += 1;
                    mRop.setTmpId(Generic.FormatTmpId(codigoSecuencial));
                } else {
                    codigoSecuencial = (resultsSecuancial.max("codigo").intValue() + 1);
                    mRop.setTmpId(Generic.FormatTmpId(codigoSecuencial));
                }
                SecuencialRO mSecuencial = realm.createObject(SecuencialRO.class);
                mSecuencial.setCodigo(codigoSecuencial);
                mSecuencial.setTagTabla(Constants.tagROP_Pendiente);
            }


            mRop.setRiskId(potencialPerdida.getId());
            mRop.setEventId(tipoEvento.getId());
            mRop.setTargetId(blanco.getId());
            mRop.setAreaId(areaResp.getId());
            mRop.setCompanyId(empresa.getId());
            mRop.setEventPlace(txtLugarExacto.getText().toString().trim());
            mRop.setEventDescription(txtDesc.getText().toString().trim());

            //Obtener Fecha y Hora Seleccionadas
            int year, month, day, hour, minute;
            year = newDateForFecha.get(Calendar.YEAR);
            month = newDateForFecha.get(Calendar.MONTH);
            day = newDateForFecha.get(Calendar.DAY_OF_MONTH);
            hour = newDateForHora.get(Calendar.HOUR_OF_DAY);
            minute = newDateForHora.get(Calendar.MINUTE);
            newDateForROP = new GregorianCalendar(year, month, day, hour, minute);
            mRop.setEventDate(newDateForROP.getTime());
            mRop.setEventDateString(Generic.dateFormatterMySql.format(mRop.getEventDate()));

            realm.commitTransaction();

            //Creamos la carpeta que contendra las imagenes
            boolean createdImageGalery = Generic.CrearCarpetaImagenesPorRop(getActivity().getApplicationContext(), mRop.getTmpId());

        } catch (Exception e) {
            Messages.showSB(getView(), e.getMessage(), "ok");
        } finally {
            realm.close();
        }
    }

    private void ValidarSgtePagina() {
        activityMain.actualPaginaRop = 1;
        EventRO eventSelect;
        eventSelect = ((EventRO) spinnerTipoEvento.getSelectedItem());

        if (eventSelect.getName().isEmpty()) {
            return;
        }

        if (eventSelect.getName().equals(Constants.NAME_EVENT_ACTO_SUBESTANDAR) || eventSelect.getName().equals(Constants.NAME_EVENT_CONDICION_SUBESTANDAR)) {
            activityMain.totalPaginasRop = 4;
            activityMain.mostrarPagAccionRealizada = true;
        } else {
            activityMain.totalPaginasRop = 3;
            activityMain.mostrarPagAccionRealizada = false;
        }

        activityMain.ShowNumPagina();

    }

}
