package co.edu.udea.compumovil.gr02_20182.proyecto.Firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    public AgendarFirebase() {
        inicilizarFirebase();
    }


    void inicilizarFirebase()
    {
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


    public  void cargarListAgendar() {

        //estamos dentro del nodo usuario
        mDatabase.child(Constantes.TABLA_AGENDAR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot: Nos devuelve  un solo valor de los tipos de usuarios

                for(final DataSnapshot snapshot: dataSnapshot.getChildren()) //getChildren: obtiene los datos de cada nodo de dataSnapshot, lo almacena en snapshot
                {
                    //Itero dentro de cada uno de los push o key subido de usuarios
                    mDatabase.child(Constantes.TABLA_AGENDAR).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            try{
                                Agendar agendar = snapshot.getValue(Agendar.class); //Obtenemos los valores que solo estan declarado en Usuario models
                                agendarList.add(agendar);
                            }catch (Exception e){

                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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


    public void updateAgendar(final String id, final String date, final String activity, final String lote, final String observacion,final String estado)
    {
        Agendar datos = new Agendar();
        datos.setId(id); //id automatico random
        datos.setDate(date);
        datos.setActivity(activity);
        datos.setLote(lote);
        datos.setObservacion(observacion);
        datos.setEstado(estado);
        mDatabase.child(Constantes.TABLA_AGENDAR).child(id).setValue(datos);
    }



}
