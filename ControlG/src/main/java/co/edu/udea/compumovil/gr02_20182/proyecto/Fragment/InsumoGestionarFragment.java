package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Adapter.AdapterDataRecycler_insumo;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.InsumoFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Insumo;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;

public class InsumoGestionarFragment extends Fragment {

    static List<Insumo> recibirListInsumo;





    public static int modo = 0; /*0.Nuevo, 1.Modificar*/


    EditText insumo_edi;
    EditText presentacion_edi;
    EditText marca_edi;
    Spinner spinner_especies;
    Spinner spinner_linea;
    EditText saldo;
    EditText id_insumo;

    Activity activity;
    ArrayList<String> comboSpecie =new ArrayList<>();
    ArrayList<String> comboLinea =new ArrayList<>();

    ImageView img_guardar;
    ImageView img_cancelar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();

        String idInsumo = getArguments() != null ? getArguments().getString("idinsumo") : "000000";

        View view;
        view = inflater.inflate(R.layout.fragment_insumo_gestionar, container, false);

        init(view);

        llenarEspecie();
        cargarPreferencias(); //carga las lineas desde parametros de referencia
        iniciarFirebaseListLevante();
        setHasOptionsMenu(true);//nos permite ejecutar icono del menu toobar onOptionsItemSelected

        if(modo == 1) //Insumo modificar
        {
            cargarDetalleLevante(view, idInsumo);

        }else if(modo == 0){ //Insumo nuevo
            saldo.setText("0");
        }

        img_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarInsumo();
                iniciarFirebaseListLevante();
            }
        });


        img_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragmentRecyclerInsumo();
                modo = 0;
            }
        });

        return  view;
    }


    void init(View view)
    {

        id_insumo = (EditText) view.findViewById(R.id.ediIDins);
        insumo_edi = (EditText) view.findViewById(R.id.ediInsumoI);
        presentacion_edi= (EditText) view.findViewById(R.id.ediPresentacionI);
        spinner_linea = (Spinner) view.findViewById(R.id.spinnerLineaI);
        marca_edi = (EditText) view.findViewById(R.id.ediMarcaI);
        spinner_especies = (Spinner) view.findViewById(R.id.spinnerEspecieI);
        saldo = (EditText) view.findViewById(R.id.ediBalanceI);

        img_cancelar = (ImageView) view.findViewById(R.id.imaCancelarIG);
        img_guardar = (ImageView) view.findViewById(R.id.imaGuardarIG);

    }

    void iniciarFirebaseListLevante()
    {
        recibirListInsumo = InsumoFirebase.insumoList;
    }


    public void guardarInsumo() {
        java.util.Date fecha = new Date();

        final String insum = insumo_edi.getText().toString();
        final String presenta = presentacion_edi.getText().toString();
        final String marc = marca_edi.getText().toString();
        final String line = spinner_linea.getSelectedItem().toString();
        final String espec = spinner_especies.getSelectedItem().toString();
        final String cantidad = saldo.getText().toString(); //cantidad del insumo solo se gestiona
        final String fecha_actual = fecha.toString();
        final String id = id_insumo.getText().toString();

        InsumoFirebase insumoFirebase = new InsumoFirebase(activity);

        boolean requerimientos = false;
        requerimientos = validateCampo(insumo_edi.getText().toString(), presentacion_edi.getText().toString(), marca_edi.getText().toString());
        if (requerimientos) {
            if (modo == 0 ){ //Nuevo
                insumoFirebase.insertInsumo(insum, presenta, line, marc, espec, cantidad, fecha_actual);
                Toast.makeText(activity, getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                limpiar();
            }else if(modo == 1){ //Modificar
                insumoFirebase.updateInsumo(id, insum, presenta, line, marc, espec, cantidad, fecha_actual);
                Toast.makeText(activity, getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                limpiar();
            }
        }
    }


    public void cargarDetalleLevante(View view, String idIns)
    {
        String id= idIns;

        int i = 0;

        for(i = 0; i < recibirListInsumo.size(); i++)
        {
            if(recibirListInsumo.get(i).getId().equals(id))
            {
                id_insumo.setText(recibirListInsumo.get(i).getId());
                insumo_edi.setText(recibirListInsumo.get(i).getSupply());
                presentacion_edi.setText(recibirListInsumo.get(i).getPresentation());
                spinner_linea.setSelection(0);
                marca_edi.setText(recibirListInsumo.get(i).getBrand());
                saldo.setText(recibirListInsumo.get(i).getBalance());
                spinner_especies.setSelection(0);
            }
        }
    }


    void llenarEspecie(){
        /*LLenado del Spinner Genero*/
        comboSpecie.add("Bovino");
        comboSpecie.add("Equino");
        comboLLenado(spinner_especies, comboSpecie);
    }



    public  void cargarPreferencias(){
        SharedPreferences preferences = activity.getSharedPreferences("configuracionInsumo", Context.MODE_PRIVATE);
        String linea = preferences.getString("grupolinea", "Agro insumos, Medicamentos, Sales, Ferreteria"); //segundo parametro es informacion por defecto dado el caso que el archivo preferecnias no exita
        comboLinea = tokeNizer(linea, comboLinea);
        comboLLenado(spinner_linea, comboLinea);


    }

    ArrayList<String>  tokeNizer(String cadenagrupo, ArrayList<String> combobox){
        String[] grupo = cadenagrupo.split(", ");
        int i=0;
        for (i=0; i<grupo.length; i++){

            combobox.add(grupo[i]);
        }
        return  combobox;
    }



    void comboLLenado(Spinner spinner, ArrayList<String> combobox)
    {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, combobox);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String xx = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    /*
      Validar campos: Vacios o nulo
     */
    boolean validateCampo (String insumo, String presentacion, String marca){
        int vericar = 0;


        insumo_edi.setError(null);
        presentacion_edi.setError(null);
        marca_edi.setError(null);

        ConnectivityManager con = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();


        if (TextUtils.isEmpty(insumo))
        {insumo_edi.setError(getString(R.string.s_requerimiento).toString());vericar = 1;}

        if (TextUtils.isEmpty(presentacion))
        {presentacion_edi.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if (TextUtils.isEmpty(marca))
        {marca_edi.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}


        if(networkinfo == null && !networkinfo.isConnected()){
            Toast.makeText(activity, getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
            vericar += 1;
        }
        return vericar>0?false:true;
    }


    private void limpiar() {
        id_insumo.setText("");
        insumo_edi.setError(null);
        presentacion_edi.setError(null);
        marca_edi.setError(null);

        insumo_edi.setText("");
        presentacion_edi.setText("");
        marca_edi.setText("");
        saldo.setText("0");
    }


    private void openFragmentRecyclerInsumo() {
        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new FragmentListInsumoRecycler()).commit();

    }


    }
