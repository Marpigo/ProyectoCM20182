package co.edu.udea.compumovil.gr02_20182.proyecto.SQLiteconexion;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Constant.Constantes;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.User;

public class DatabaseSQLiteUser {

    private static DatabaseSQLite databasesqlit;


    public List<User> getListUsuario() {
        User users = null;
        List<User> usuarioList = new ArrayList<>();

        try{

            Cursor cursor = databasesqlit.database.rawQuery("SELECT * FROM usuario", null);
            while (cursor.moveToNext()){
                users=new User();
                users.setName(cursor.getString(0));
                users.setEmail(cursor.getString(1));
                users.setPassword(cursor.getString(2));
                users.setPhoto(cursor.getBlob(3));
                usuarioList.add(users);
            }
        }catch (Exception e){
            //Toast.makeText(getApplicationContext(), "Notificacion  " + e, Toast.LENGTH_SHORT).show();
        }
        return usuarioList;
    }

    public  int insertUser(int id, String name, String email, String password, byte[] photo) {
        int registro  =0;
        try
        {
            SQLiteDatabase bd = databasesqlit.database ;

            ContentValues values=new ContentValues();
            values.put(Constantes.CAMPO_ID, id);
            values.put(Constantes.CAMPO_NAME, name);
            values.put(Constantes.CAMPO_EMAIL,email);
            values.put(Constantes.CAMPO_PASSWORD, password);
            values.put(Constantes.CAMPO_PHOTO,photo);

            long idResultante = bd.insert(Constantes.TABLA_USER,null,values);
            registro = (int) idResultante;

        }catch (Exception e){

        }
        return registro;
    }

    public void deleteUsers() {
        SQLiteDatabase bd = databasesqlit.database;
        bd.execSQL("DELETE FROM " + Constantes.TABLA_USER);
        //bd.execSQL("DELETE FROM " + Constantes.TABLA_USUARIO+ " WHERE "+ Constantes.CAMPO_ID+"='"+value+"'");
    }


    public int updateUser(String nameFirst, String name, String email, String password, byte[] photo) {
        int registro  =0;
        SQLiteDatabase bd = databasesqlit.database;

        String[] parametros = {nameFirst};
        ContentValues values = new ContentValues();
        values.put(Constantes.CAMPO_NAME, name);
        values.put(Constantes.CAMPO_EMAIL, email);
        values.put(Constantes.CAMPO_PASSWORD, password);
        values.put(Constantes.CAMPO_PHOTO, photo);
        long idResultante = bd.update(Constantes.TABLA_USER, values, Constantes.CAMPO_NAME + "=?", parametros);
        registro = (int) idResultante;
        return registro;
    }



        public List<User> getUser(String user, String password) {
        User users = null;
        List<User> usuarioList = new ArrayList<>();

        try{

            Cursor cursor = databasesqlit.database.rawQuery("SELECT name, email, password, photo FROM usuario WHERE name ='"+ user +"' AND password='" + password +"'", null);
            //Cursor cursor = databasesqlit.database.rawQuery("SELECT name, email, password, photo FROM usuario WHERE name = ?", new String[]{user});
            while (cursor.moveToNext()){
                users=new User();
                users.setName(cursor.getString(0));
                users.setEmail(cursor.getString(1));
                users.setPassword(cursor.getString(2));
                users.setPhoto(cursor.getBlob(3));
                usuarioList.add(users);
            }
        }catch (Exception e){
            //Toast.makeText(getApplicationContext(), "Notificacion  " + e, Toast.LENGTH_SHORT).show();
        }
        return usuarioList;
    }



}
