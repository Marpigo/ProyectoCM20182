package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.ActivityNavigationDrawer;
import co.edu.udea.compumovil.gr02_20182.proyecto.Adapter.AdapterDataRecycler_levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.LevanteFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.FragmentListLevanteRecycler;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;

public class LevanteDetalleFragment extends Fragment {

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
    ImageView img_cancelar;
    ImageView img_editar;

    static String idlenvate="000";


    public LevanteDetalleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_levante_detalle, container, false);

        activity = getActivity();

        idlenvate = getArguments() != null ? getArguments().getString("idanimal") : "000000";

        init(view);
        iniciarFirebaseListLevante();
        cargarDetalleLevante(idlenvate);


        img_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragmentRecyclerLevante();

            }
        });


        img_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LevanteGestionarFragment.modo = 1;
                openFragmentLevanteGestionar(idlenvate);

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

        img_cancelar = (ImageView) view.findViewById(R.id.imaCancelarL);
        img_editar = (ImageView) view.findViewById(R.id.imaEditarL);

    }

    void iniciarFirebaseListLevante()
    {
         recibirListLevante = LevanteFirebase.levanteList;
    }

    public void cargarDetalleLevante(String idver)
    {
        String id= idver; // AdapterDataRecycler_levante.id_levante
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

                String imag = recibirListLevante.get(i).getImagen();
                Glide.with(activity).load(imag).into(img_Levanted);
            }

        }
    }


    private void openFragmentRecyclerLevante() {
        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new FragmentListLevanteRecycler()).commit();

    }

    private void openFragmentLevanteGestionar(String id) {

        String idlevt = id;//enviamos por Bundle el id del animal a modificar
        Bundle args = new Bundle();
        args.putString("idanimal", idlevt);

        Fragment frag2 = new LevanteGestionarFragment();
        frag2.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainers, frag2);
        ft.commit();

    }





}

