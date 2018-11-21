package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Adapter.AdapterDataRecycler_insumo;
import co.edu.udea.compumovil.gr02_20182.proyecto.Adapter.AdapterDataRecycler_levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.InsumoFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.LevanteFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Insumo;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;

public class InsumoDetalleFragmentDialogo extends DialogFragment {

    Activity activity;
    static List<Insumo> recibirListInsumo;


    TextView tex_insumdtit;
    TextView tex_insumo;
    TextView tex_present;
    TextView tex_linea;
    TextView tex_marca;
    TextView tex_especie;

    TextView tex_infoins;
    TextView tex_intosaldo;

    Button but_cancelar;


    public InsumoDetalleFragmentDialogo() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insumo_detalle, container, false);

        activity = getActivity();

        init(view);
        iniciarFirebaseListLevante();
        cargarDetalleLevante();


        but_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });


        return  view;
    }

    void init(View view){
        tex_insumdtit = (TextView) view.findViewById(R.id.texInsumdt);
        tex_insumo= (TextView) view.findViewById(R.id.texInsumod);
        tex_present= (TextView) view.findViewById(R.id.texPresentacionId);
        tex_linea= (TextView) view.findViewById(R.id.texLineaId);
        tex_marca= (TextView) view.findViewById(R.id.texMarcaId);
        tex_especie= (TextView) view.findViewById(R.id.texSpecieId);

        tex_infoins= (TextView) view.findViewById(R.id.texInfoinsumo);
        tex_intosaldo= (TextView) view.findViewById(R.id.texinfosaldoI);

        but_cancelar = (Button) view.findViewById(R.id.butCancelarDetaI);
    }

    void iniciarFirebaseListLevante()
    {
         recibirListInsumo = InsumoFirebase.insumoList;
    }

    public void cargarDetalleLevante()
    {
        String id= AdapterDataRecycler_insumo.id_insumo;
        String lote ="";
        int cantidadAnimal = 0;

        int i = 0;


        for(i = 0; i < recibirListInsumo.size(); i++)
        {
            if(recibirListInsumo.get(i).getId().equals(id))
            {
                tex_insumdtit.setText(recibirListInsumo.get(i).getSupply());
                tex_insumo.setText(recibirListInsumo.get(i).getSupply());
                tex_present.setText(recibirListInsumo.get(i).getPresentation());
                tex_linea.setText(recibirListInsumo.get(i).getLine());
                tex_marca.setText(recibirListInsumo.get(i).getBrand());
                tex_especie.setText(recibirListInsumo.get(i).getSpecies());

                tex_infoins.setText(recibirListInsumo.get(i).getSupply());
                tex_intosaldo.setText(recibirListInsumo.get(i).getBalance());
             }
        }
    }


}

