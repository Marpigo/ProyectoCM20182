package co.edu.udea.compumovil.gr02_20182.proyecto.Fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.LevanteFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;

public class LevanteGestionarFragment extends Fragment  implements View.OnClickListener{

    static List<Levante> recibirListLevante;
    LevanteFirebase levanteFirebase = new LevanteFirebase();


    public static int modo = 0; /*0.Nuevo, 1.Modificar*/


    ImageButton imgbfecha;
    private int dia, mes, ano;
    private Uri filePath;


    Spinner spinner_lote;
    EditText name_animal;
    Spinner spinner_genero;
    Spinner spinner_raza;
    Spinner spinner_tipo_ingreso;
    EditText edi_fecha;
    EditText numer_chapeta;
    EditText observacion;

    Activity activity;
    ArrayList<String> comboLote = new ArrayList<>();
    ArrayList<String> comboGnero = new ArrayList<>();
    ArrayList<String> comboRaza = new ArrayList<>();
    ArrayList<String> comboIngreso = new ArrayList<>();
    public static final int NUMERO_LOTES = 10;

    ImageView campoPhoto;
    Bitmap bitmaphoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        activity = getActivity();

        View view;
        view = inflater.inflate(R.layout.fragment_levante_gestionar, container, false);

        init(view);

        llenarLote();
        llenarGenero();
        llenarRaza();
        llenarIngreso();


        final FloatingActionButton fabfoto = (FloatingActionButton) view.findViewById(R.id.fabFotoLevante);
        fabfoto.setOnClickListener(this);

        imgbfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirFecha();
            }
        });



        return view;
    }


    //implements View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabFotoLevante:
                imagenGallery();
                break;
        }
    }



    public void guardarLevante() {
        final String lote = spinner_lote.getSelectedItem().toString();
        final String name = name_animal.getText().toString();
        final String genero = spinner_genero.getSelectedItem().toString();
        final String raza = spinner_raza.getSelectedItem().toString();
        final String tipoingreso = spinner_tipo_ingreso.getSelectedItem().toString();
        final String fecha = edi_fecha.getText().toString();
        final String chapeta = numer_chapeta.getText().toString();
        final String observa = observacion.getText().toString();

        boolean requerimientos = false;
        requerimientos = validateCampo(name_animal.getText().toString(), edi_fecha.getText().toString(), numer_chapeta.getText().toString());
        if (requerimientos) {
            if (modo == 0) { //Nuevo
                levanteFirebase.insertLevante(lote, name, genero, raza, tipoingreso, fecha, chapeta, observa);
                Toast.makeText(activity, getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                limpiar();
            } else if (modo == 1) { //Modificar
                //levanteFirebase.updateLevante(lote. name, genero, raza, tipoingreso, fecha, chapeta, observa);
                Toast.makeText(activity, getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                limpiar();
            }

        }
    }

    void init(View view) {
        spinner_lote = (Spinner) view.findViewById(R.id.spinnerLote);
        name_animal = (EditText) view.findViewById(R.id.ediNombreL);
        spinner_genero = (Spinner) view.findViewById(R.id.spinnerGenero);
        spinner_raza = (Spinner) view.findViewById(R.id.spinnerRaza);
        spinner_tipo_ingreso = (Spinner) view.findViewById(R.id.spinnerIngreso);
        edi_fecha = (EditText) view.findViewById(R.id.ediFechaL);
        numer_chapeta = (EditText) view.findViewById(R.id.ediChapetaL);
        observacion = (EditText) view.findViewById(R.id.ediObservacionL);

        imgbfecha = (ImageButton) view.findViewById(R.id.imabFechaL);

        campoPhoto = (ImageView) view.findViewById(R.id.imgLevante);


    }

    void llenarLote() {
        /*LLenado del Spinner lote*/
        comboLote.add(getString(R.string.s_lote_detalle));
        int i = 0;
        for (i = 1; i <= NUMERO_LOTES; i++) {
            comboLote.add("Lote " + i);
        }
        comboLLenado(spinner_lote, comboLote);
    }

    void llenarGenero() {
        /*LLenado del Spinner Genero*/
        comboGnero.add(getString(R.string.s_genero_detalle));
        comboGnero.add("Macho");
        comboGnero.add("Hembra");
        comboLLenado(spinner_genero, comboGnero);
    }

    void llenarRaza() {
        /*LLenado del Spinner Genero*/
        comboRaza.add(getString(R.string.s_raza_detalle));
        comboRaza.add("Brahman");
        comboRaza.add("Caqueteño");
        comboRaza.add("Casanareño");
        comboRaza.add("Cebú");
        comboRaza.add("Chino");
        comboRaza.add("Hartón");
        comboRaza.add("Lucerna");
        comboRaza.add("Red poll");
        comboRaza.add("Romosinuano");
        comboLLenado(spinner_raza, comboRaza);
    }


    void llenarIngreso() {
        /*LLenado del Spinner Genero*/
        comboIngreso.add(getString(R.string.s_tipo_ingreso_detalle));
        comboIngreso.add("Consignación");
        comboIngreso.add("Compra");
        comboIngreso.add("Nacimiento");
        comboIngreso.add("Trueque");
        //Collections.sort(comboIngreso);/*Ordenar lista*/
        comboLLenado(spinner_tipo_ingreso, comboIngreso);
    }


    void comboLLenado(Spinner spinner, ArrayList<String> combobox) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, combobox);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String xx = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void abrirFecha() {

        Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("Europe/Madrid"));
        dia = calendarNow.get(Calendar.DAY_OF_MONTH);
        mes = calendarNow.get(Calendar.MONTH);
        ano = calendarNow.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                edi_fecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }
                , dia, mes, ano);
        datePickerDialog.show();

    }

    void eliminarLevante(String id) {
        levanteFirebase.deleteLevante(id);
    }

    /*
      Validar campos: Vacios o nulo
     */
    boolean validateCampo(String name, String fecha, String chapeta) {
        int vericar = 0;


        name_animal.setError(null);
        edi_fecha.setError(null);
        numer_chapeta.setError(null);

        ConnectivityManager con = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();


        if (TextUtils.isEmpty(name)) {
            name_animal.setError(getString(R.string.s_requerimiento).toString());
            vericar = 1;
        }

        if (TextUtils.isEmpty(fecha)) {
            edi_fecha.setError(getString(R.string.s_requerimiento).toString());
            vericar += 1;
        }

        if (TextUtils.isEmpty(chapeta)) {
            numer_chapeta.setError(getString(R.string.s_requerimiento).toString());
            vericar += 1;
        }


        if (networkinfo == null && !networkinfo.isConnected()) {
            //Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
            vericar += 1;
        }
        return vericar > 0 ? false : true;
    }


    private void limpiar() {
        name_animal.setText("");
        edi_fecha.setText("");
        numer_chapeta.setText("");
        observacion.setText("");

    }


    private void imagenGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccionar la aplicación"), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int RESULT_OK = -1;

        if (resultCode == RESULT_OK) {
            filePath = data.getData();

            try {
                bitmaphoto = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), filePath);
                campoPhoto.setImageBitmap(bitmaphoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
   }




}
