package co.edu.udea.compumovil.gr02_20182.proyecto.Adapter;



import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v4.app.FragmentManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.ActivityNavigationDrawer;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.LevanteDetalleFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.LevanteGestionarFragment;
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

        holder.id_le.setText(levanteList.get(position).getId());
        holder.lote.setText(levanteList.get(position).getLote());
        holder.nombre.setText(levanteList.get(position).getName());
        holder.genero.setText(levanteList.get(position).getGender());
        holder.fecha.setText(levanteList.get(position).getDateI());

        String imag = levanteList.get(position).getImagen();
        Glide.with(contesto).load(imag).into(holder.photo);

    }



    @Override
    public int getItemCount() {
        return levanteList.size();
    }




    ///////////////////////////////////////
    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView id_le, lote, nombre, genero, fecha;
        ImageButton imgb_info_editar;
        ImageView photo;

        public ViewHolderDatos(View itemView) {
            super(itemView);
            contesto = itemView.getContext();

            id_le = (TextView) itemView.findViewById(R.id.txtID);
            lote = (TextView) itemView.findViewById(R.id.txtLote_item_levante);
            nombre = (TextView) itemView.findViewById(R.id.txtName_item_levante);
            genero = (TextView) itemView.findViewById(R.id.txtGenero_item_levante);
            fecha = (TextView) itemView.findViewById(R.id.txtFecha_item_levante);

            photo = (ImageView) itemView.findViewById(R.id.imaLevante);


            imgb_info_editar = (ImageButton) itemView.findViewById(R.id.imaEditarL);


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



        public void editarAnimalLevante(){
            FragmentManager fm = ((FragmentActivity) contesto).getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragmentContainers, new LevanteGestionarFragment()).commit();
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
