package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Adapter.AdapterDataRecycler_agendar;
import co.edu.udea.compumovil.gr02_20182.proyecto.Adapter.AdapterDataRecycler_levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.AgendarFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.LevanteFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Agendar;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;


public class FragmentListAgendarRecycler extends Fragment{

    private OnFragmentInteractionListener mListener;

    AdapterDataRecycler_agendar adapterAgendar;
    RecyclerView recycler;
    List<Agendar> agendarList;

    List<Agendar> agendarListAuxi = new ArrayList<>();//lista auxiliar contiene solo las actividades sin realizar

    String idagendar;
    Activity activity;
    Context mContext;


    public FragmentListAgendarRecycler() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_list_agendar_recycler, container, false);

        activity = getActivity();
        mContext = getActivity();


        generarDatosRecycler(view);


        return view;
    }

    public void generarDatosRecycler(View vista)
    {

        recycler= (RecyclerView) vista.findViewById(R.id.recyclerAgendar);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //recycler.setLayoutManager(new GridLayoutManager(this, 2)); ver en dos columna la informacion

        agendarList = new ArrayList<>(AgendarFirebase.agendarList); //clonar la lista

        activiadesPorRealizar();
        llenarEncabezadoConsulta(vista);

        adapterAgendar = new AdapterDataRecycler_agendar(agendarListAuxi, getContext());//llenar el adaptador con la lista
        recycler.setAdapter(adapterAgendar);
        //adapterAgendar.notifyDataSetChanged();




        adapterAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentAgendaActiviadRealizada(agendarListAuxi.get(recycler.getChildAdapterPosition(view)));
            }
        });
    }

    //Activiades pendintes por realizar
    public void activiadesPorRealizar(){

        for (Agendar agendar : agendarList) {
            if(Integer.parseInt(agendar.getEstado()) == 0) {
                agendarListAuxi.add(agendar);
            }
        }
    }

    public void llenarEncabezadoConsulta(View view)
    {
        TextView cantidadactivo, ultimaagendfecha;
        cantidadactivo = (TextView) view.findViewById(R.id.txtActividadCantidadA);
        ultimaagendfecha = (TextView) view.findViewById(R.id.texActividadtFechaultima);

        int i=0;
        int cantidad=0;
        String fech="x";
        for(i=0; i<agendarList.size(); i++)
        {
            if(Integer.parseInt(agendarList.get(i).getEstado()) == 0){

                cantidad++;
                fech = agendarList.get(i).getDate();
            }
        }
        cantidadactivo.setText(cantidad +" Actividad");
        ultimaagendfecha.setText(fech +"");

    }

    public void fragmentAgendaActiviadRealizada(Agendar agendar){


        String id_agenda = agendar.getId();//enviamos por Bundle el id del animal a modificar
        Bundle args = new Bundle();
        args.putString("id_agenda", id_agenda);

        Fragment frag2 = new AgendarFragmentRealizada();
        frag2.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainers, frag2);
        ft.commit();
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
