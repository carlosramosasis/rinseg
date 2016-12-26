package rinseg.asistp.com.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rinseg.asistp.com.listener.ListenerClick;
import rinseg.asistp.com.models.Incidencia;
import rinseg.asistp.com.models.Inspeccion;
import rinseg.asistp.com.rinseg.R;

/**
 * Created by Usuario on 30/09/2016.
 */
public class IncidenciaAdapter extends RecyclerView.Adapter<IncidenciaAdapter.IncidenciaViewHolder> {
    private List<Incidencia> ListaIncidencias;
    private final ListenerClick mListener;

    public static class IncidenciaViewHolder extends RecyclerView.ViewHolder {
        //Campos respectivos del item
        public TextView txtNombre;
        public TextView txtDesc;
        public final LinearLayout vLayout;


        public IncidenciaViewHolder(View v){
            super(v);

            txtNombre  = (TextView) v.findViewById(R.id.txt_card_view_incidencia_nombre);
            txtDesc  = (TextView) v.findViewById(R.id.txt_card_view_incidencia_desc);
            vLayout = (LinearLayout) v.findViewById(R.id.linearlayout_card_incidencia);
        }
    }

    public IncidenciaAdapter(List<Incidencia> incidencias,ListenerClick listener){
        this.ListaIncidencias = incidencias;
        mListener = listener;
    }

    @Override
    public int getItemCount(){
        return ListaIncidencias.size();
    }

    @Override
    public IncidenciaViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_incidencia,viewGroup,false);
        return new IncidenciaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final IncidenciaViewHolder viewHolder,final int i){
        viewHolder.txtNombre.setText(ListaIncidencias.get(i).getNombre());
        viewHolder.txtDesc.setText( ListaIncidencias.get(i).getDetalle().toString() );

        viewHolder.vLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mListener.onItemClicked(viewHolder, i);
            }
        });
    }

}
