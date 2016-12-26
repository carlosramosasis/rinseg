package rinseg.asistp.com.intent_services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rinseg.asistp.com.models.FrecuencieRO;
import rinseg.asistp.com.models.ImagenRO;
import rinseg.asistp.com.models.ROP;
import rinseg.asistp.com.models.User;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.services.RestClient;
import rinseg.asistp.com.services.Services;
import rinseg.asistp.com.utils.Constants;
import rinseg.asistp.com.utils.RinsegModule;

/**
 * Created by Carlos Ramos on 16/12/2016.
 */
public class RopIntentServices extends IntentService {
    RopIntentServices thiss = this;
    RealmConfiguration myConfig;
    User usuParaToken;


    public RopIntentServices() {
        super("rop_intent_services");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            Boolean intentActivo = preferences.getBoolean(Constants.KEY_INTENT_SERV_ACTIVO, false);
            if (!intentActivo) {
                preferences.edit().putBoolean(Constants.KEY_INTENT_SERV_ACTIVO, true).commit();
                Log.e("intent", "esto viene del intent");

                //configuramos Realm
                Realm.init(this.getApplicationContext());
                myConfig = new RealmConfiguration.Builder()
                        .name("rinseg.realm")
                        .schemaVersion(2)
                        .modules(new RinsegModule())
                        .deleteRealmIfMigrationNeeded()
                        .build();

                Realm realm = Realm.getInstance(myConfig);

                User user = realm.where(User.class).findFirst();
                if (user != null) {
                    usuParaToken = realm.copyFromRealm(user);
                } else {
                    TerminaProceso();
                    return;
                }

                RealmResults<ROP> ropsCerradosNoEnviados = realm.where(ROP.class).equalTo("cerrado", true).equalTo("id", 0).findAll();

                if (ropsCerradosNoEnviados.size() > 0) {
                    for (int i = 0; i < ropsCerradosNoEnviados.size(); i++) {
                        ROP rop = ropsCerradosNoEnviados.get(i);
                        ROP ropCopy = realm.copyFromRealm(rop);
                        EnviarRop(realm,ropCopy);
                        Log.e("ROP en INTENT",ropCopy.toString());
                    }
                } else {
                    TerminaProceso();
                    return;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            TerminaProceso();
        } finally {

        }
    }


    void TerminaProceso() {
        Log.e("intent", "acabo el intent");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean(Constants.KEY_INTENT_SERV_ACTIVO, false).commit();
    }

    void EnviarRop(Realm realm, final ROP ropCopy) {

       final String token = usuParaToken.getApi_token();

        RestClient restClient = new RestClient(Services.URL_ROPS);

        Call<ResponseBody> call = restClient.iServices.setRopCerrado(ropCopy, token);

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


                            Realm realm = Realm.getInstance(myConfig);
                            realm.beginTransaction();
                            ROP ropCerradoRealm = realm.where(ROP.class).equalTo("tmpId",ropCopy.getTmpId()).findFirst();
                            ropCerradoRealm.setId(ropResult.getInt("id"));
                            realm.commitTransaction();

                            int idRop = ropCerradoRealm.getId();

                            if (ropCopy.listaImgComent.size() > 0) {
                               // cantImagenesTotal = ropCopy.listaImgComent.size();
                                for (ImagenRO img : ropCopy.listaImgComent) {
                                   new EnviarImagenRop(img, ropCopy.getTmpId(), idRop, token).execute("", "", "");
                                }
                                TerminaProceso();
                            } else {
                               TerminaProceso();
                            }

                        }

                        Log.e("jsonObject", jsonObject.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                        TerminaProceso();
                    }
                } else {
                    TerminaProceso();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TerminaProceso();
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

                File myDir = getApplicationContext().getFilesDir();
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

                        //cantImagenesEnviadas += 1;
                        //postExecute();

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //cantImagenesEnviadas += 1;
                        //postExecute();
                        //dialogLoading.dismiss();
                        //Messages.showSB(getView(), getString(R.string.msg_servidor_inaccesible), "ok");
                    }
                });



            } catch (Exception e) {
                e.printStackTrace();
                //cantImagenesEnviadas += 1;
                //postExecute();
            }


            return errorValue;
        }




    }



}
