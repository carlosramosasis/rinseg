package rinseg.asistp.com.services;

import rinseg.asistp.com.models.ROP;

/**
 * Created by Carlos Ramos on 20/10/2016.
 */
public class Services {

    public static final String PROTOCOL = "http://";
    //  public static final String IP_PUERTO = "192.168.2.36";
    public static final String IP_PUERTO = "190.81.47.196";
    public static final String PROY = "/rinseg/public/api/";
    public static final String SECURITY = "security/";
    public static final String ROPS = "rops/";

    public static final String URL = PROTOCOL + IP_PUERTO + PROY;
    public static final String URL_SECURITY = PROTOCOL + IP_PUERTO + PROY + SECURITY;
    public static final String URL_ROPS = PROTOCOL + IP_PUERTO + PROY + ROPS;

    //Métodos
    public static final String LOGIN = "login";
    public static final String SYNC = "sync";
    public static final String ROP_CLOSE = "close";
    public static final String ADD_IMAGEN = "add-image";

}
