package rinseg.asistp.com.listener;

import rinseg.asistp.com.adapters.IncidenciaAdapter;
import rinseg.asistp.com.adapters.InspeccionAdapter;
import rinseg.asistp.com.adapters.RopAdapter;
import rinseg.asistp.com.models.Incidencia;

/**
 * @author Carlos Ramos
 * @version 1.0
 * Created on 14/10/2016
 * Interface para el onCLickListener del Recycler
 */
public interface ListenerClick {
    /**
     * @param holder The ViewHolder for the clicked kitten
     * @param position The position in the grid of the kitten that was clicked
     */
    void onItemClicked(InspeccionAdapter.InspeccionViewHolder holder, int position);

    void onItemClicked(RopAdapter.RopViewHolder holder, int position);

    void onItemClicked(IncidenciaAdapter.IncidenciaViewHolder holder, int position);
}