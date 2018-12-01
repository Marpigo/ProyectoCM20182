package co.edu.udea.compumovil.gr02_20182.proyecto.Firebase;

import android.app.Activity;
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
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.User;

public class UserFirebase {

    static DatabaseReference mDatabase; //Referencia a la base de datos
    static StorageReference mStorageRef; // para referenciar la foto a guardar Storage

    public static List<User> usuarioList = new ArrayList<>();
    public static int logueado = 0; //1.Gollge, 2.Usuario

    public UserFirebase(Activity activity) {
        inicilizarFirebase(activity);
    }


    void inicilizarFirebase(Activity activity)
    {
        FirebaseApp.initializeApp(activity);
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Inicia la referencia con la base de datos en el nodo principal 'mRootReference'
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void limpiarLista() {
        if (usuarioList != null)
        {
            usuarioList.clear();
        }
    }

    public final List<User> getListaUsuarios() {

        return usuarioList;
    }





    public void insertUser(final String name, final String email, final String password, Uri filePath, final String tipo)
    {
        final boolean registro = false;
        //Subimos la imagen un direcotorio Fotos, nombre de la foto filepath
        final StorageReference fotoRef = mStorageRef.child("Fotos").child(Constantes.TABLA_USER).child(filePath.getLastPathSegment());
        //final StorageReference fotoRef = mStorageRef.child("Fotos").child(firebaseAuth.getCurrentUser().getUid()).child(filePath.getLastPathSegment());
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

                    User datosuser = new User();
                    datosuser.setId(UUID.randomUUID().toString()); //id automatico random
                    datosuser.setName(name);
                    datosuser.setEmail(email);
                    datosuser.setPassword(password);
                    datosuser.setImagen(downloadLink.toString());
                    datosuser.setAutenticado(0);
                    datosuser.setTipo(tipo);
                    mDatabase.child(Constantes.TABLA_USER).child(datosuser.getId()).setValue(datosuser);

                }
            }
        });
    }


    public void updateUserAutenticado(final String id, final String name, final String email, final String password, final String imangeurl, final String tipo, final int autenticado)
    {
        User datosuser = new User();
        datosuser.setId(id); //id actualizar
        datosuser.setName(name);
        datosuser.setEmail(email);
        datosuser.setPassword(password);
        datosuser.setImagen(imangeurl);
        datosuser.setAutenticado(autenticado);
        datosuser.setTipo(tipo);
        mDatabase.child(Constantes.TABLA_USER).child(id).setValue(datosuser);
    }


    public void cargarListUsuario() {
        mDatabase.child(Constantes.TABLA_USER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//dataSnapshot: Nos devuelve  un solo valor de los tipos de usuarios
                usuarioList.clear();
                for (DataSnapshot objSnaptshot : dataSnapshot.getChildren()){ //getChildren: obtiene los datos de cada nodo de dataSnapshot, lo almacena en snapshot
                    try{
                        User user = objSnaptshot.getValue(User.class); //Obtenemos los valores que solo estan declarado en Usuario models
                        usuarioList.add(user);
                    }catch (Exception e){
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    public void deleteUsers(String id) {
        mDatabase.child(Constantes.TABLA_USER).child(id).removeValue();
    }


    public void updateUser(final String id, final String name, final String email, final String password, Uri filePath, final String tipo)
    {
        final boolean registro = false;
        //Subimos la imagen un direcotorio Fotos, nombre de la foto filepath
        final StorageReference fotoRef = mStorageRef.child("Fotos").child(Constantes.TABLA_USER).child(filePath.getLastPathSegment());
        //final StorageReference fotoRef = mStorageRef.child("Fotos").child(firebaseAuth.getCurrentUser().getUid()).child(filePath.getLastPathSegment());
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

                    User datosuser = new User();
                    datosuser.setId(id); //id actualizar
                    datosuser.setName(name);
                    datosuser.setEmail(email);
                    datosuser.setPassword(password);
                    datosuser.setImagen(downloadLink.toString());
                    datosuser.setAutenticado(1);
                    datosuser.setTipo(tipo);
                    mDatabase.child(Constantes.TABLA_USER).child(id).setValue(datosuser);

                }
            }
        });
    }



}
