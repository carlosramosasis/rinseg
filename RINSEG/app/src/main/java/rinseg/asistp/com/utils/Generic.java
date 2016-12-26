package rinseg.asistp.com.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.SecureRandom;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Usuario on 05/10/2016.
 */
public class Generic {
    public static SimpleDateFormat dateFormatterMySql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    public static SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);

    public static String Mayus(String param) {
        String resu = "";
        resu = param.substring(0, 1).toUpperCase() + param.substring(1);
        return resu;
    }

    public static String FormatTmpId(int id) {
        String resu = "";
        resu = String.format("tmp%06d", id);
        return resu;
    }


    //Validar Acceso a la red
    public static boolean IsOnRed(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    public static boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean IsJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    public static boolean CrearCarpetaImagenesPorRop(Context context, String carpertaRop) {
        boolean result = false;

        File myDir = context.getFilesDir();
        File folder = new File(myDir, Constants.PATH_IMAGE_GALERY + carpertaRop + "/");

        if (!folder.exists()) {
            result = folder.mkdirs();
        }
        Log.e("resultSaveImageGalery", String.valueOf(result));

        return result;
    }

    public static boolean GuardarImagenCarpeta(Context context, String carpertaRop, Bitmap bmp, String nombreImagen) {
        boolean result = false;

        File myDir = context.getFilesDir();
        File path = new File(myDir, Constants.PATH_IMAGE_GALERY + carpertaRop + "/" + nombreImagen+".jpg");

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return result;
    }

    public static int CantidadImagenesPorRop(Context context, String carpertaRop) {
        boolean existeCarpeta = false;
        int resu = 0;

        File myDir = context.getFilesDir();
        File folder = new File(myDir, Constants.PATH_IMAGE_GALERY + carpertaRop + "/");


        if (folder.exists()) {
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                resu += 1;
            }
        }

        return resu;
    }

    public static String randomString(char[] characterSet, int length) {
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }


}


