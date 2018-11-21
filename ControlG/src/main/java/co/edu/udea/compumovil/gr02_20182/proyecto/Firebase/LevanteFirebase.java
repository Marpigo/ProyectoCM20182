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
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;

public class LevanteFirebase {

    static DatabaseReference mDatabase; //Referencia a la base de datos
    static StorageReference mStorageRef; // para referenciar la foto a guardar Storage

    public static List<Levante> levanteList = new ArrayList<>();


    public LevanteFirebase() {
        inicilizarFirebase();
    }


    void inicilizarFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Inicia la referencia con la base de datos en el nodo principal 'mRootReference'
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void limpiarLista() {
        if (levanteList != null)
        {
            levanteList.clear();
        }
    }

    public final List<Levante> getListaLevante() {

        return levanteList;
    }


    public void insertLevante(final String lote, final String name, final String gender, final String race, final String type, final String dateI, final String numberPin, final String observation, final Uri filePath)
    {
        final boolean registro = false;
        //Subimos la imagen un direcotorio Fotos, nombre de la foto filepath
        final StorageReference fotoRef = mStorageRef.child("Fotos").child(Constantes.TABLA_LEVANTE).child(filePath.getLastPathSegment());
        fotoRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception { //subimos la foto al Storage con fotoRef.putFile
                if(!task.isSuccessful()){
                    throw new Exception();

                }

                return fotoRef.getDownloadUrl(); //una vez que suba todo, devuelve el link de descarga
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){ //si se pudo devolver el link, gurdo el resultado en dowloadLink
                    Uri downloadLink = task.getResult();

                    Levante datos = new Levante();
                    datos.setId(UUID.randomUUID().toString()); //id automatico random
                    datos.setLote(lote);
                    datos.setName(name);
                    datos.setGender(gender);
                    datos.setRace(race);
                    datos.setType(type);
                    datos.setDateI(dateI);
                    datos.setNumberPin(numberPin);
                    datos.setObservation(observation);
                    datos.setImagen(downloadLink.toString());
                    mDatabase.child(Constantes.TABLA_LEVANTE).child(datos.getId()).setValue(datos);
                }
            }
        });
    }


    public  void cargarListLevante() {

        //estamos dentro del nodo usuario
        mDatabase.child(Constantes.TABLA_LEVANTE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot: Nos devuelve  un solo valor de los tipos de usuarios

                for(final DataSnapshot snapshot: dataSnapshot.getChildren()) //getChildren: obtiene los datos de cada nodo de dataSnapshot, lo almacena en snapshot
                {
                    //Itero dentro de cada uno de los push o key subido de usuarios
                    mDatabase.child(Constantes.TABLA_LEVANTE).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try{
                                Levante levante = snapshot.getValue(Levante.class); //Obtenemos los valores que solo estan declarado en Usuario models
                                levanteList.add(levante);
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


    public void deleteLevante(String id) {
        mDatabase.child(Constantes.TABLA_LEVANTE).child(id).removeValue();
    }

    public void updateLevante(final String id, final String lote, final String name, final String gender, final String race, final String type, final String dateI, final String numberPin, final String observation)
    {
        Levante datos = new Levante();
        datos.setId(id); //id actualizar
        datos.setLote(lote);
        datos.setName(name);
        datos.setGender(gender);
        datos.setRace(race);
        datos.setType(type);
        datos.setDateI(dateI);
        datos.setNumberPin(numberPin);
        datos.setObservation(observation);
        mDatabase.child(Constantes.TABLA_LEVANTE).child(id).setValue(datos);
    }
}
