package rinseg.asistp.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import rinseg.asistp.com.listener.ListenerClickAccionPreventiva;
import rinseg.asistp.com.listener.ListenerClickActoCondicion;
import rinseg.asistp.com.models.AccionPreventiva;
import rinseg.asistp.com.models.EventItemsRO;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.utils.Generic;

/**
 * Created by Carlos Ramos on 30/09/2016.
 */
public class AccionCondicionSubestandarAdapter extends RecyclerView.Adapter<AccionCondicionSubestandarAdapter.AccionViewHolder> {
    private List<EventItemsRO> ListaActosCondiciones;
    private final ListenerClickActoCondicion mListener;
    private int selectedPosition = -1;

    public static class AccionViewHolder extends RecyclerView.ViewHolder {
        //Campos respectivos del item
        public CheckBox chkActoCondicion;
        public TextView txtActoCondicion;



        public AccionViewHolder(View v) {
            super(v);

            chkActoCondicion = (CheckBox) v.findViewById(R.id.chk_card_view_accion_condicion);
            txtActoCondicion = (TextView) v.findViewById(R.id.txt_card_view_accion_condicion);

        }
    }

    public AccionCondicionSubestandarAdapter(List<EventItemsRO> actosCondiciones,ListenerClickActoCondicion listener) {
        this.ListaActosCondiciones = actosCondiciones;
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return ListaActosCondiciones.size();
    }

    @Override
    public AccionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_accion_condicion_subestandar, viewGroup, false);
        return new AccionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AccionViewHolder viewHolder, final int i) {
        //viewHolder.txtResponsable.setText(ListaAcciones.get(i).getResponsable());
        viewHolder.txtActoCondicion.setText(ListaActosCondiciones.get(i).getName());
        //viewHolder.chkActoCondicion.setChecked(selectedPosition == i);
        if(selectedPosition == i){
            viewHolder.chkActoCondicion.setChecked(true);}
        else{
            viewHolder.chkActoCondicion.setChecked(false);
        }
        viewHolder.chkActoCondicion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectedPosition = viewHolder.getAdapterPosition();
            }
        });
    }
}