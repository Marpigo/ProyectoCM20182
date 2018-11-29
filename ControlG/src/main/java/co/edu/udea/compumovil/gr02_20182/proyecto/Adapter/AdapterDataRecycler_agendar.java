package co.edu.udea.compumovil.gr02_20182.proyecto.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Agendar;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Insumo;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;


public class AdapterDataRecycler_agendar extends RecyclerView.Adapter<AdapterDataRecycler_agendar.ViewHolderDatos>
        implements View.OnClickListener, Filterable {

    List<Agendar> agendarList;
    List<Agendar> agendarListfull;
    private View.OnClickListener listener;
    Context contesto;



    public AdapterDataRecycler_agendar(List<Agendar> agendarList, Context context) {

        this.agendarList = agendarList;
        this.agendarListfull = new ArrayList<>(agendarList);
        this.contesto = context;

    }

    /*Este metodo nos enlaza el adaptador con item_list.xml*/
    @Override
    public AdapterDataRecycler_agendar.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendar_list,null,false);



        /*evento click en las insumo*/
        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    /*establece la comunicacion entre el adaptador y el ViewHolderDatos*/
    @Override
    public void onBindViewHolder(AdapterDataRecycler_agendar.ViewHolderDatos holder, int position) {
     //   holder.id_ag.setText(agendarList.get(position).getId());
        holder.fecha.setText(agendarList.get(position).getDate());
        holder.actividad.setText(agendarList.get(position).getActivity());
        holder.lote.setText(agendarList.get(position).getLote());


    }





    @Override
    public int getItemCount() {
        return agendarList.size();
    }


    public class ViewHolderDatos extends RecyclerView.ViewHolder{
        TextView id_ag, fecha, actividad, lote;



        public ViewHolderDatos(View itemView) {
            super(itemView);
          //  id_ag = (TextView) itemView.findViewById(R.id.txtIDIns);
            fecha = (TextView) itemView.findViewById(R.id.txtFechaA);
            actividad = (TextView) itemView.findViewById(R.id.txtActividadA);
            lote = (TextView) itemView.findViewById(R.id.txtLotaA);

        }
    }


    /*Encargado de escuchar el evento onclik*/
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    /*Implementacion metodo basico de seleccion onclik*/
    @Override
    public void onClick(View view) {
        if (listener != null)
        {
            listener.onClick(view);
        }
    }


    //  implements Filterable
    @Override
    public Filter getFilter() {
        return insumoFilter;
    }


    private Filter insumoFilter = new Filter() {
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Agendar> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(agendarListfull);
            } else {
                String filterPattern = constraint.toString().toUpperCase().trim();

                for (Agendar item : agendarListfull) {
                    if (item.getLote().toUpperCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            agendarList.clear();
            agendarListfull.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


}
