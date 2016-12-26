package rinseg.asistp.com.listener;

import rinseg.asistp.com.adapters.AccionCondicionSubestandarAdapter;

/**
 * @author Carlos Ramos
 * @version 1.0
 * Created on 14/10/2016
 * Interface para el onCLickListener del Recycler
 */
public interface ListenerClickActoCondicion {
    /**
     * @param holder The ViewHolder for the clicked kitten
     * @param position The position in the grid of the kitten that was clicked
     */
    void onItemClicked(AccionCondicionSubestandarAdapter.AccionViewHolder holder, int position);

}