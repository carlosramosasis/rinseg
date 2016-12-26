package rinseg.asistp.com.models;

import java.util.Date;

/**
 * Created by Carlos on 10/10/2016.
 */
public class Incidencia {
    private String nombre;
    private String detalle;

    public Incidencia(String nombre, String detalle) {
        this.nombre = nombre;
        this.detalle = detalle;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
