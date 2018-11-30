package co.edu.udea.compumovil.gr02_20182.proyecto.Firebase;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.udea.compumovil.gr02_20182.proyecto.Constant.Constantes;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Agendar;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.User;

public class AgendarFirebase {

    static DatabaseReference mDatabase; //Referencia a la base de datos
    static StorageReference mStorageRef; // para referenciar la foto a guardar Storage


    public static List<Agendar> agendarList = new ArrayList<>();

    public AgendarFirebase(Activity activity) {
        inicilizarFirebase(activity);
    }


    void inicilizarFirebase(Activity activity)
    {
        FirebaseApp.initializeApp(activity);
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Inicia la referencia con la base de datos en el nodo principal 'mRootReference'
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    public void limpiarLista() {
        if (agendarList != null)
        {
            agendarList.clear();
        }
    }

    public final List<Agendar> getListaAgendar() {

        return agendarList;
    }


    public void inserAgendar(final String date, final String activity, final String lote, final String observacion, final String estado)
    {
          Agendar datos = new Agendar();
          datos.setId(UUID.randomUUID().toString()); //id automatico random
          datos.setDate(date);
          datos.setActivity(activity);
          datos.setLote(lote);
          datos.setObservacion(observacion);
          datos.setEstado(estado);
          mDatabase.child(Constantes.TABLA_AGENDAR).child(datos.getId()).setValue(datos);

    }


    public void cargarListAgendar() {
        mDatabase.child(Constantes.TABLA_AGENDAR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                agendarList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    try{
                        Agendar agendar = objSnaptshot.getValue(Agendar.class); //Obtenemos los valores que solo estan declarado en Usuario models
                        agendarList.add(agendar);
                    }catch (Exception e){

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public void deleteAgendar(String id) {
        mDatabase.child(Constantes.TABLA_AGENDAR).child(id).removeValue();
    }

    public void updateAgendar(final String id, final String date, final String activity, final String lote, final String observacion, final String estado)
    {
        Agendar datos = new Agendar();
        datos.setId(id);
        datos.setDate(date);
        datos.setActivity(activity);
        datos.setLote(lote);
        datos.setObservacion(observacion);
        datos.setEstado(estado);
        mDatabase.child(Constantes.TABLA_AGENDAR).child(id).setValue(datos);
    }




}
