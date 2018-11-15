package co.edu.udea.compumovil.gr02_20182.proyecto.Firebase;


import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.udea.compumovil.gr02_20182.proyecto.Constant.Constantes;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Insumo;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;

public class InsumoFirebase {

    static DatabaseReference mDatabase; //Referencia a la base de datos
    static StorageReference mStorageRef; // para referenciar la foto a guardar Storage

    public static List<Insumo> insumoList = new ArrayList<>();


    public InsumoFirebase() {
        inicilizarFirebase();
    }


    void inicilizarFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Inicia la referencia con la base de datos en el nodo principal 'mRootReference'
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void limpiarLista() {
        if (insumoList != null)
        {
            insumoList.clear();
        }
    }

    public final List<Insumo> getListaInsumo() {

        return insumoList;
    }

    public void insertInsumo(final String supply, final String presentation, final String line, final String brand, final String species, final String cantidad, final String fecha)
    {
        Insumo datos = new Insumo();
        datos.setId(UUID.randomUUID().toString()); //id automatico random
        datos.setSupply(supply);
        datos.setPresentation(presentation);
        datos.setLine(line);
        datos.setBrand(brand);
        datos.setSpecies(species);
        datos.setBalance(cantidad);
        datos.setDatei(fecha);
        mDatabase.child(Constantes.TABLA_INSUMO).child(datos.getId()).setValue(datos);
    }

    public  void cargarListInsumo() {

        //estamos dentro del nodo usuario
        mDatabase.child(Constantes.TABLA_INSUMO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot: Nos devuelve  un solo valor de los tipos de usuarios

                for(final DataSnapshot snapshot: dataSnapshot.getChildren()) //getChildren: obtiene los datos de cada nodo de dataSnapshot, lo almacena en snapshot
                {
                    //Itero dentro de cada uno de los push o key subido de usuarios
                    mDatabase.child(Constantes.TABLA_INSUMO).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Insumo insumo = snapshot.getValue(Insumo.class); //Obtenemos los valores que solo estan declarado en Usuario models
                            insumoList.add(insumo);
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


    public void deleteInsumo(String id) {
        mDatabase.child(Constantes.TABLA_INSUMO).child(id).removeValue();
    }

    public void updateInsumo(final String id, final String supply, final String presentation, final String line, final String brand, final String species, final String cantidad, final String fecha)
    {
        Insumo datos = new Insumo();
        datos.setId(id); //id actualizar
        datos.setSupply(supply);
        datos.setPresentation(presentation);
        datos.setLine(line);
        datos.setBrand(brand);
        datos.setSpecies(species);
        datos.setBalance(cantidad);
        datos.setDatei(fecha);
        mDatabase.child(Constantes.TABLA_INSUMO).child(id).setValue(datos);
    }

}
