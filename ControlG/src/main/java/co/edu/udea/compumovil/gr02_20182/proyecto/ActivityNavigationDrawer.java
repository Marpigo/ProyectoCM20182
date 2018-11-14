package co.edu.udea.compumovil.gr02_20182.proyecto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.LevanteFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.ConfigurationFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.ControlMenuFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.FragmentListLevanteRecycler;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.LevanteGestionarFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.PerfilFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;

public class ActivityNavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentListLevanteRecycler.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {



    static List<Levante> recibirListLevante;
   // static List<Comida> recibirListFood;



    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final FloatingActionsMenu fabgrupo = (FloatingActionsMenu) findViewById(R.id.fabGrupo);
        com.getbase.floatingactionbutton.FloatingActionButton fablevante = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabLevante);
        com.getbase.floatingactionbutton.FloatingActionButton fabinsumo  = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabInsumo);
        com.getbase.floatingactionbutton.FloatingActionButton fabagendar  = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAgendear);


        fablevante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragmentLevanteGestionar();
                fabgrupo.collapse();
            }
        });

        fabinsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragmentInsumoGestionar();
                fabgrupo.collapse();
            }
        });


        fabagendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //     openActividadFood();
                fabgrupo.collapse();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        autenticadoUser();
        iniciarFirebaseListDrink();
    }


    void iniciarFirebaseListDrink()
    {
        LevanteFirebase levanteFirebase = new LevanteFirebase();
        levanteFirebase.limpiarLista();
        levanteFirebase.cargarListLevante();
        recibirListLevante = LevanteFirebase.levanteList;
    }

    public void autenticadoUser()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation_drawer, menu);

        openFragmentControlMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_diary) {
            // Handle the camera action
        } else if (id == R.id.nav_levante) {
            openFragmentRecyclerLevante();
        } else if (id == R.id.nav_inventory) {

        } else if (id == R.id.nav_product) {


        } else if (id == R.id.nav_profile) {
            openFragmentPerfil();

        } else if (id == R.id.nav_configuration) {
        openFragmentConfiguration();
        } else if (id == R.id.nav_Sing_off) {
            singOff();
        } else if (id == R.id.nav_about) {
            openFragmentAcercaDe();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgProfileEdition:
                UsuarioAtivity.modo = 1;/*Modo edicion*/
                openUsuarioActividad();
                break;
            case R.id.imageViewLogo:
                openFragmentControlMenu();/*Presionar el logo muestra el menu*/
                break;
        }
    }


    private void singOff()
    {
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {

                    openLoguin();

                } else {
                    Toast.makeText(getApplicationContext(), "No se pudo cerrar la session", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void openLoguin() {
        Intent miIntent = new Intent(ActivityNavigationDrawer.this, LoguinTabbed.class);
        startActivity(miIntent);
        finish();
    }

    private void openFragmentControlMenu() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new ControlMenuFragment()).commit();
    }

    private void openFragmentConfiguration() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new ConfigurationFragment()).commit();
    }
    private void openFragmentPerfil() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new PerfilFragment()).commit();
    }
    private void openUsuarioActividad() {
        Intent miIntent = new Intent(ActivityNavigationDrawer.this, UsuarioAtivity.class);
        startActivity(miIntent);
    }

    private void openFragmentLevanteGestionar() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new LevanteGestionarFragment()).commit();
    }

    private void openFragmentRecyclerLevante() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new FragmentListLevanteRecycler()).commit();
    }

    private void openFragmentInsumoGestionar() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new InsumoGestionarFragment()).commit();
    }


    private void openFragmentAcercaDe() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new AcercadeFragment()).commit();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}