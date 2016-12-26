package rinseg.asistp.com.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import rinseg.asistp.com.listener.ListenerClickAccionPreventiva;
import rinseg.asistp.com.models.AccionPreventiva;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.utils.Generic;

/**
 * Created by Carlos Ramos on 30/09/2016.
 */
public class AccionPreventivaAdapter extends RecyclerView.Adapter<AccionPreventivaAdapter.AccionViewHolder> {
    private List<AccionPreventiva> ListaAcciones;
    private final ListenerClickAccionPreventiva mListener;

    public static class AccionViewHolder extends RecyclerView.ViewHolder {
        //Campos respectivos del item
        public TextView txtResponsable;
        public TextView txtAccion;
        public TextView txtFecha;
        public final ImageButton btnEliminar;



        public AccionViewHolder(View v) {
            super(v);

            txtResponsable = (TextView) v.findViewById(R.id.txt_card_view_accion_responsable);
            txtAccion = (TextView) v.findViewById(R.id.txt_card_view_accion_accion);
            txtFecha = (TextView) v.findViewById(R.id.txt_card_view_accion_fecha);

            btnEliminar = (ImageButton) v.findViewById(R.id.btn_card_view_delete);

        }
    }

    public AccionPreventivaAdapter(List<AccionPreventiva> accionesPreventivas ,ListenerClickAccionPreventiva listener) {
        this.ListaAcciones = accionesPreventivas;
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return ListaAcciones.size();
    }

    @Override
    public AccionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_acciones_preventivas, viewGroup, false);
        return new AccionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AccionViewHolder viewHolder, final int i) {
        viewHolder.txtResponsable.setText(ListaAcciones.get(i).getResponsable());
        viewHolder.txtAccion.setText(ListaAcciones.get(i).getAccion());
        viewHolder.txtFecha.setText(Generic.dateFormatter.format(ListaAcciones.get(i).getFecha()));

        viewHolder.btnEliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mListener.onItemClicked(viewHolder, i);
            }
        });

    }


}
