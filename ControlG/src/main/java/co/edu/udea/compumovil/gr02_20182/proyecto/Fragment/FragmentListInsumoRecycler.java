package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

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

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Adapter.AdapterDataRecycler_insumo;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.InsumoFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Insumo;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;

public class FragmentListInsumoRecycler extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static AdapterDataRecycler_insumo adapterInsumo;
    RecyclerView recycler;
    List<Insumo> insumoList;

    public FragmentListInsumoRecycler() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_list_insumo_recycler, container, false);
        generarDatosRecycler(view);

        return view;
    }

    public void generarDatosRecycler(View vista)
    {
        recycler= (RecyclerView) vista.findViewById(R.id.recyclerInsumo);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //recycler.setLayoutManager(new GridLayoutManager(this, 2)); ver en dos columna la informacion
        insumoList = InsumoFirebase.insumoList; //recibir lista

        //  Toast.makeText(getContext(), "SIZE bebida : " + databasesqlitedrink.getListBebida().size(), Toast.LENGTH_SHORT).show();
        adapterInsumo = new AdapterDataRecycler_insumo(insumoList, getContext());//llenar el adaptador con la lista
        recycler.setAdapter(adapterInsumo);

        adapterInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDetalleInsumo(insumoList.get(recycler.getChildAdapterPosition(view)));
            }
        });

    }

    public void fragmentDetalleInsumo(Insumo insumo){
        String idlevante = insumo.getId();//enviamos por Bundle el id del animal a modificar
        Bundle args = new Bundle();
        args.putString("idinsumo", idlevante);

        Fragment frag2 = new InsumoDetalleFragment();
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
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
