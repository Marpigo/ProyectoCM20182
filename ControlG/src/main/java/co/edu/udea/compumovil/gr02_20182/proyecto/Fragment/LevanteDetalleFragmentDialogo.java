package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Adapter.AdapterDataRecycler_levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.LevanteFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.FragmentListLevanteRecycler;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;

public class LevanteDetalleFragmentDialogo extends DialogFragment {

    Activity activity;
    static List<Levante> recibirListLevante;

    TextView tex_nombre;
    TextView tex_lote;
    TextView tex_genero;
    TextView tex_raza;
    TextView tex_ingreso;
    TextView tex_fecha;
    TextView tex_chapeta;
    TextView tex_observacion;

    TextView tex_infolote;
    TextView tex_intocantidad;

    ImageView img_Levanted;
    Button but_cancelar;


    public LevanteDetalleFragmentDialogo() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_levante_detalle, container, false);

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
        tex_nombre = (TextView) view.findViewById(R.id.texiNombreLd);
        tex_lote = (TextView) view.findViewById(R.id.texLoteLd);
        tex_genero = (TextView) view.findViewById(R.id.texGeneroLd);
        tex_raza = (TextView) view.findViewById(R.id.texRazaLd);
        tex_ingreso = (TextView) view.findViewById(R.id.texIngresoLd);
        tex_fecha = (TextView) view.findViewById(R.id.texFechaLd);
        tex_chapeta = (TextView) view.findViewById(R.id.texChapetaLd);
        tex_observacion = (TextView) view.findViewById(R.id.texObservacionLd);

        tex_infolote = (TextView) view.findViewById(R.id.texLoteInfo);
        tex_intocantidad = (TextView) view.findViewById(R.id.texLevanteCantidadInfo);
        img_Levanted = (ImageView) view.findViewById(R.id.imgLevanted);

        but_cancelar = (Button) view.findViewById(R.id.butCancelarDetaL);

    }

    void iniciarFirebaseListLevante()
    {
         recibirListLevante = LevanteFirebase.levanteList;
    }

    public void cargarDetalleLevante()
    {
        String id= AdapterDataRecycler_levante.id_levante;
        String lote ="";
        int cantidadAnimal = 0;

        int i = 0;


        for(i = 0; i < recibirListLevante.size(); i++) {
            if(recibirListLevante.get(i).getId().equals(id))
            {lote = recibirListLevante.get(i).getLote();}
        }

        for(i = 0; i < recibirListLevante.size(); i++) {
            if (recibirListLevante.get(i).getLote().equals(lote)){
                cantidadAnimal++;
            }
        }

        for(i = 0; i < recibirListLevante.size(); i++)
        {
            if(recibirListLevante.get(i).getId().equals(id))
            {
                tex_nombre.setText(recibirListLevante.get(i).getName());
                tex_lote.setText(recibirListLevante.get(i).getLote());
                tex_genero.setText(recibirListLevante.get(i).getGender());
                tex_raza.setText(recibirListLevante.get(i).getRace());
                tex_ingreso.setText(recibirListLevante.get(i).getType());
                tex_fecha.setText(recibirListLevante.get(i).getDateI());
                tex_chapeta.setText(recibirListLevante.get(i).getNumberPin());
                tex_observacion.setText(recibirListLevante.get(i).getObservation());
                tex_infolote.setText(lote);
                tex_intocantidad.setText(cantidadAnimal +"");
                //Glide.with(activity).load(recibirListLevante.get(i).getImagen()).into(img_Levanted);
            }

        }
    }


}

