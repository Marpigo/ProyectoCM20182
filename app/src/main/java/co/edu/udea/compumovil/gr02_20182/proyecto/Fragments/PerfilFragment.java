package co.edu.udea.compumovil.gr02_20182.proyecto.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Model.User;
import co.edu.udea.compumovil.gr02_20182.proyecto.R;
import co.edu.udea.compumovil.gr02_20182.proyecto.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.proyecto.SQLiteconexion.DatabaseSQLiteUser;


public class PerfilFragment extends Fragment {

    public static String user_login, user_pass;

    TextView name_profil, email_profil;
    ImageView photo_profil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_perfil, container, false);

       init(view);
       userQuery();
        return  view;
    }

    public  void init(View view)
    {
        name_profil = (TextView)view.findViewById(R.id.txtNameProfil);
        email_profil = (TextView)view.findViewById(R.id.txtEmailProfil);
        photo_profil = (ImageView)view.findViewById(R.id.imgPhotoProfile);
    }

    public void  userQuery()
    {
        Boolean uservalido = false;
        List<User> userList;
        DatabaseSQLiteUser databasesqliteduser = new DatabaseSQLiteUser();
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(getContext());
        databasesqlit.open();

        String name;
        String password;

        userList = databasesqliteduser.getUser(user_login, user_pass);
        name_profil.setText(userList.get(0).getName());
        email_profil.setText(userList.get(0).getEmail());

        byte[] data = userList.get(0).getPhoto();
        Bitmap image = toBitmap(data);
        photo_profil.setImageBitmap(image);

        databasesqlit.close();
    }


    public static Bitmap toBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
