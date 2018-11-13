package co.edu.udea.compumovil.gr02_20182.proyecto.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = "Notificación: ";


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

       String token = FirebaseInstanceId.getInstance().getToken();


        registerTokenServidor(token);



    }

    //http://restauranteudeafirebase.000webhostapp.com/REST/register.php?
    //https://restauranteudea.000webhostapp.com/REST/wsJSONRegistroB.php?


    public void registerTokenServidor(String token) {

        Log.d(TAG, "Token:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx " + token);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .build();

        Request request = new Request.Builder()
                .url("http://restauranteudeafirebase.000webhostapp.com/REST/register.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
