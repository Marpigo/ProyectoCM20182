package co.edu.udea.compumovil.gr02_20182.proyecto.Adapter;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.ActivityNavigationDrawer;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.InsumoDetalleFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.InsumoGestionarFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Insumo;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;


public class AdapterDataRecycler_insumo extends RecyclerView.Adapter<AdapterDataRecycler_insumo.ViewHolderDatos>
        implements View.OnClickListener, Filterable {



    List<Insumo> insumoList;
    List<Insumo> insumoListfull;
    private View.OnClickListener listener;
    Context contesto;



    public AdapterDataRecycler_insumo(List<Insumo> insumoList, Context context) {

        this.insumoList = insumoList;
        this.insumoListfull = new ArrayList<>(insumoList);
        this.contesto = context;

    }

    /*Este metodo nos enlaza el adaptador con item_list.xml*/
    @Override
    public AdapterDataRecycler_insumo.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insumo_list,null,false);



        /*evento click en las insumo*/
        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    /*establece la comunicacion entre el adaptador y el ViewHolderDatos*/
    @Override
    public void onBindViewHolder(AdapterDataRecycler_insumo.ViewHolderDatos holder, int position) {
        holder.id_in.setText(insumoList.get(position).getId());
        holder.referencia.setText(insumoList.get(position).getSupply());
        holder.presentacion.setText(insumoList.get(position).getPresentation());
        holder.saldo.setText(insumoList.get(position).getBalance());
        holder.linea.setText(insumoList.get(position).getLine());


    }


    @Override
    public int getItemCount() {
        return insumoList.size();
    }


    public class ViewHolderDatos extends RecyclerView.ViewHolder{
        TextView id_in, referencia, presentacion, saldo, linea;


        public ViewHolderDatos(View itemView) {
            super(itemView);
            id_in = (TextView) itemView.findViewById(R.id.txtIDIns);
            referencia = (TextView) itemView.findViewById(R.id.txtreferencia_item_insumo);
            presentacion = (TextView) itemView.findViewById(R.id.txtPresentacion_item_insumo);
            saldo = (TextView) itemView.findViewById(R.id.txtsaldo_item_insumo);
            linea = (TextView) itemView.findViewById(R.id.txtlinea_item_insumo);


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
            List<Insumo> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(insumoListfull);
            } else {
                String filterPattern = constraint.toString().toUpperCase().trim();

                for (Insumo item : insumoListfull) {
                    if (item.getSupply().toUpperCase().contains(filterPattern)) {
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
            insumoList.clear();
            insumoList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


}
