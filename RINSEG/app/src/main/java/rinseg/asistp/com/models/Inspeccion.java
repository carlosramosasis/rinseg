package rinseg.asistp.com.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Carlos on 30/09/2016.
 */
public class Inspeccion  implements Serializable{
    private String codigo;
    private Date fechaCreacion;

    public Inspeccion(String codigo, Date fechaCreacion) {
        this.codigo = codigo;
        this.fechaCreacion = fechaCreacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
