package rinseg.asistp.com.utils;

/**
 * Created by Usuario on 28/09/2016.
 */
public class Constants {
    //Tiempos en la aplicación
    public static final long SPLASH_SCREEN_DELAY = 1500;

    //codigo de mensajes en el servicio web - login
    public static int LOGIN_OK = 200;
    public static int LOGIN_FAIL = 422;

    //tag tablas para secuencial
    public static String tagROP_Pendiente = "RP";

    public static int ANIO = 1990;
    public static int MES = 1;
    public static int DIA = 1;

    public static String SUCCESS = "success";

    public static String PATH_IMAGE_GALERY = "image_galery_rop/";

    public static char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    public static int LEN_IMAGEN = 10;

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static String KEY_INTENT_SERV_ACTIVO = "intentServActivo";

    public static String NAME_EVENT_ACTO_INSEGURO = "acto_inseguro";
    public static String NAME_EVENT_CONDICION_INSEGURA = "condicion_insegura";
    public static String NAME_EVENT_ACTO_SUBESTANDAR = "acto_subestandar";
    public static String NAME_EVENT_CONDICION_SUBESTANDAR = "condicion_subestandar";
    public static String NAME_EVENT_CUASI_ACCIDENTE = "cuasi_accidente";
    public static String NAME_EVENT_ACTO_POSITIVO = "acto_positivo";

}
