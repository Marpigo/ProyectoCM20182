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

import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;


public class AdapterDataRecycler_levante extends RecyclerView.Adapter<AdapterDataRecycler_levante.ViewHolderDatos>
        implements View.OnClickListener, Filterable {



    List<Levante> levanteList;
    List<Levante> levanteListfull;
    private View.OnClickListener listener;
    Context contesto;

    public AdapterDataRecycler_levante(List<Levante> levanteList, Context context) {

        this.levanteList = levanteList;
        this.levanteListfull = new ArrayList<>(levanteList);
         this.contesto = context;

    }

    /*Este metodo nos enlaza el adaptador con item_list.xml*/
    @Override
    public AdapterDataRecycler_levante.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_levante_list,null,false);



        /*evento click en las comida*/
        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    /*establece la comunicacion entre el adaptador y el ViewHolderDatos*/
    @Override
    public void onBindViewHolder(AdapterDataRecycler_levante.ViewHolderDatos holder, int position) {
        holder.lote.setText(levanteList.get(position).getLote());
        holder.nombre.setText(levanteList.get(position).getName());
        holder.genero.setText(levanteList.get(position).getGender());
        holder.fecha.setText(levanteList.get(position).getDateI());

    }


    @Override
    public int getItemCount() {
        return levanteList.size();
    }


    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView lote, nombre, genero, fecha;



        public ViewHolderDatos(View itemView) {
            super(itemView);
            lote = (TextView) itemView.findViewById(R.id.txtLote_item_levante);
            nombre = (TextView) itemView.findViewById(R.id.txtName_item_levante);
            genero = (TextView) itemView.findViewById(R.id.txtGenero_item_levante);
            fecha = (TextView) itemView.findViewById(R.id.txtFecha_item_levante);

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
        return levanteFilter;
    }


    private Filter levanteFilter = new Filter() {
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Levante> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(levanteListfull);
            } else {
                String filterPattern = constraint.toString().toUpperCase().trim();

                for (Levante item : levanteListfull) {
                    if (item.getName().toUpperCase().contains(filterPattern)) {
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
            levanteList.clear();
            levanteList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


}
