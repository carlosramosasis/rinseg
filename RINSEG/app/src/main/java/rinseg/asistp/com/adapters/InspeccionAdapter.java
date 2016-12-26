package rinseg.asistp.com.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import rinseg.asistp.com.listener.ListenerClick;
import rinseg.asistp.com.models.Inspeccion;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.ui.fragments.FragmentInspeccionIncidenciaDetalle1;

/**
 * Created by Usuario on 30/09/2016.
 */
public class InspeccionAdapter extends RecyclerView.Adapter<InspeccionAdapter.InspeccionViewHolder> {
    private List<Inspeccion> ListaInspecciones;
    private final ListenerClick mListener;

    public static class InspeccionViewHolder extends RecyclerView.ViewHolder {
        //Campos respectivos del item
        public TextView txtCodigo;
        public TextView txtFechaCrea;
        public final LinearLayout vLayout;


        public InspeccionViewHolder(View v){
            super(v);

            txtCodigo  = (TextView) v.findViewById(R.id.txt_card_view_cod_inspeccion);
            txtFechaCrea  = (TextView) v.findViewById(R.id.txt_card_view_fec_crea_inspeccion);
           vLayout = (LinearLayout) v.findViewById(R.id.linearlayout_card_inspecciones);
        }
    }

    public InspeccionAdapter(List<Inspeccion> inspecciones,ListenerClick listener){
        this.ListaInspecciones = inspecciones;
        mListener = listener;
    }

    @Override
    public int getItemCount(){
        return ListaInspecciones.size();
    }

    @Override
    public InspeccionViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_inspeccion,viewGroup,false);
        return new InspeccionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final InspeccionViewHolder viewHolder,final int i){
        viewHolder.txtCodigo.setText(ListaInspecciones.get(i).getCodigo());
        viewHolder.txtFechaCrea.setText( ListaInspecciones.get(i).getFechaCreacion().toString() );

        viewHolder.vLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mListener.onItemClicked(viewHolder, i);
            }
        });

    }

}
