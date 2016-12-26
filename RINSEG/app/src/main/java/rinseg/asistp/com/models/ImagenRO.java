package rinseg.asistp.com.models;

import io.realm.RealmObject;

/**
 * Created by Carlos Ramos on 28/10/2016.
 */
public class ImagenRO extends RealmObject{

    private int id;

    private String name;

    private String descripcion;


    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
