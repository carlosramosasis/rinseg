package rinseg.asistp.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import rinseg.asistp.com.listener.ListenerClick;
import rinseg.asistp.com.models.CompanyRO;
import rinseg.asistp.com.models.EventRO;
import rinseg.asistp.com.models.ROP;
import rinseg.asistp.com.models.SettingsRopRO;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.utils.Generic;
import rinseg.asistp.com.utils.RinsegModule;

/**
 * Created by Carlos Ramos on 30/09/2016.
 */
public class RopAdapter extends RecyclerView.Adapter<RopAdapter.RopViewHolder> {
    private List<ROP> ListaRops;
    private final ListenerClick mListener;
    private RealmList<EventRO> mEvents = new RealmList<>();
    private RealmList<CompanyRO> mCompanies = new RealmList<>();

    public static class RopViewHolder extends RecyclerView.ViewHolder {
        //Campos respectivos del item
        public TextView txtCodigo;
        public TextView txtFechaCrea;
        public TextView txtHoraCrea;
        public TextView txtTipoEvento;
        public TextView txtEmrpesa;

        public final RelativeLayout vLayout;

        public RopViewHolder(View v) {
            super(v);

            txtCodigo = (TextView) v.findViewById(R.id.txt_card_view_cod_rop);
            txtFechaCrea = (TextView) v.findViewById(R.id.txt_card_view_fec_crea_rop);
            txtHoraCrea = (TextView) v.findViewById(R.id.txt_card_view_hora_crea_rop);
            txtTipoEvento = (TextView) v.findViewById(R.id.txt_card_view_tipo_evento_rop);
            txtEmrpesa = (TextView) v.findViewById(R.id.txt_card_view_empresa_rop);
            vLayout = (RelativeLayout) v.findViewById(R.id.linearlayout_card_rop);

        }
    }

    public RopAdapter(List<ROP> rops, Context context, ListenerClick listener) {
        this.ListaRops = rops;
        mListener = listener;
        Realm.init(context);
        RealmConfiguration myConfig = new RealmConfiguration.Builder()
                .name("rinseg.realm")
                .schemaVersion(2)
                .modules(new RinsegModule())
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm realm = Realm.getInstance(myConfig);
        SettingsRopRO settingsRopRO = realm.where(SettingsRopRO.class).findFirst();
        if (settingsRopRO != null) {
            this.mEvents = settingsRopRO.events;
            this.mCompanies = settingsRopRO.companies;
        }

    }

    @Override
    public int getItemCount() {
        return ListaRops.size();
    }

    @Override
    public RopViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_rop, viewGroup, false);
        return new RopViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RopViewHolder viewHolder, final int i) {
        if (ListaRops.get(i).esCerrado() == true) {
            if (ListaRops.get(i).getId() != 0) {
                viewHolder.txtCodigo.setText(("" + ListaRops.get(i).getId()));
            } else {
                viewHolder.txtCodigo.setText(R.string.pendiente_envio);
            }

        } else {
            viewHolder.txtCodigo.setText(ListaRops.get(i).getTmpId());
        }

        viewHolder.txtFechaCrea.setText(Generic.dateFormatter.format(ListaRops.get(i).getEventDate()));
        viewHolder.txtHoraCrea.setText(Generic.timeFormatter.format(ListaRops.get(i).getEventDate()));

        int idEvent = ListaRops.get(i).getEventId();
        int idCompany = ListaRops.get(i).getCompanyId();

        for (int j = 0; j < mEvents.size(); j++) {
            EventRO evnt = mEvents.get(j);
            if (idEvent == evnt.getId()) {
                viewHolder.txtTipoEvento.setText(evnt.getDisplayName());
            }
        }

        for (int j = 0; j < mCompanies.size(); j++) {
            CompanyRO cmp = mCompanies.get(j);
            if (idCompany == cmp.getId()) {
                viewHolder.txtEmrpesa.setText(cmp.getDisplayName());
            }
        }


        viewHolder.vLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClicked(viewHolder, i);
            }
        });
    }

}
