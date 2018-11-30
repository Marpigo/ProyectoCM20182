package co.edu.udea.compumovil.gr02_20182.proyecto.Firebase;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.firebase.FirebaseApp;
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


    public InsumoFirebase(Activity activity) {
        inicilizarFirebase(activity);
    }


    void inicilizarFirebase(Activity activity)
    {
        FirebaseApp.initializeApp(activity);
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


    public void cargarListInsumo() {
        mDatabase.child(Constantes.TABLA_INSUMO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                insumoList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){
                    try{
                        Insumo insumo = objSnaptshot.getValue(Insumo.class); //Obtenemos los valores que solo estan declarado en Usuario models
                        insumoList.add(insumo);
                    }catch (Exception e){
                    }
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
