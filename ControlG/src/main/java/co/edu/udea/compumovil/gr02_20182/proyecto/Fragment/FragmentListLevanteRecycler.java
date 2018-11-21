package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Adapter.AdapterDataRecycler_levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.LevanteFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;


public class FragmentListLevanteRecycler extends Fragment{



    private OnFragmentInteractionListener mListener;

    public static AdapterDataRecycler_levante adapterlevat;
    RecyclerView recycler;
    List<Levante> levaneList;
    String idlevante;
    Activity activity;
    Context mContext;
    public static String id_levante  ="";

    public FragmentListLevanteRecycler() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_list_levante_recycler, container, false);
        generarDatosRecycler(view);
        activity = getActivity();
        mContext = getActivity();

        return view;
    }

    public void generarDatosRecycler(View vista)
    {

        //recycler.setRefreshing(false);


        recycler= (RecyclerView) vista.findViewById(R.id.recyclerLevante);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //recycler.setLayoutManager(new GridLayoutManager(this, 2)); ver en dos columna la informacion

        levaneList = LevanteFirebase.levanteList; //recibir lista

        //recycler.removeAllViewsInLayout();//removes all the views

        //  Toast.makeText(getContext(), "SIZE bebida : " + databasesqlitedrink.getListBebida().size(), Toast.LENGTH_SHORT).show();
        adapterlevat = new AdapterDataRecycler_levante(levaneList, getContext());//llenar el adaptador con la lista
        recycler.setAdapter(adapterlevat);
        adapterlevat.notifyDataSetChanged();
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
