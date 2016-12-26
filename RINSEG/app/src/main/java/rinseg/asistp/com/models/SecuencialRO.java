package rinseg.asistp.com.models;

import io.realm.RealmObject;

/**
 * Created by Carlos Ramos on 07/11/2016.
 */
public class SecuencialRO extends RealmObject {
    private int codigo;
    private String tagTabla;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int _codigo) {
        this.codigo = _codigo;
    }

    public String getTagTabla() {
        return tagTabla;
    }

    public void setTagTabla(String _tagTabla) {
        this.tagTabla = _tagTabla;
    }
}
