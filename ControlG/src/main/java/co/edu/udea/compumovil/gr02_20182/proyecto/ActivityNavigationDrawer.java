package co.edu.udea.compumovil.gr02_20182.proyecto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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

import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.InsumoFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Firebase.LevanteFirebase;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.AcercadeFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.AgendarFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.ControlMenuFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.FragmentListInsumoRecycler;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.FragmentListLevanteRecycler;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.InsumoGestionarFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.LevanteGestionarFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.ConfigUsuarioFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Insumo;
import co.edu.udea.compumovil.gr02_20182.proyecto.Model.Levante;

public class ActivityNavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentListLevanteRecycler.OnFragmentInteractionListener, FragmentListInsumoRecycler.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {


    public static ActivityNavigationDrawer activityNavigationDrawer;

    static List<Levante> recibirListLevante;
    static List<Insumo> recibirListInsumo;


    public static Toolbar toolbar;
    public static boolean menu_visible = false;
    public static int opcion = 0;

    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    static boolean visible_menu = false; //ocultar icono del menu

    FloatingActionsMenu fabgrupo;

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        activity = this;


        fabgrupo = (FloatingActionsMenu) findViewById(R.id.fabGrupo);
        com.getbase.floatingactionbutton.FloatingActionButton fablevante = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabLevante);
        com.getbase.floatingactionbutton.FloatingActionButton fabinsumo = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabInsumo);
        com.getbase.floatingactionbutton.FloatingActionButton fabagendar = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAgendear);

        botonFlotanteMenu(true);

        fablevante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragmentRecyclerLevante();
                botonFlotanteMenu(false);
                iconoMenuGestionActivar(true);
                TituloToolbar(getString(R.string.s_levante));
                fabgrupo.collapse();
            }
        });

        fabinsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragmentRecyclerInsumo();
                botonFlotanteMenu(false);
                iconoMenuGestionActivar(true);
                TituloToolbar(getString(R.string.s_insumo_detalle));
                fabgrupo.collapse();
            }
        });


        fabagendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragmentAgendar();
                botonFlotanteMenu(false);
                iconoMenuGestionActivar(true);
                TituloToolbar(getString(R.string.s_menu_agenda));
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

       iniciarFirebaseListLevante();
        iniciarFirebaseListInsumo();
        openFragmentControlMenu();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewLogo:
                openFragmentControlMenu();/*Presionar el logo muestra el menu*/
                break;

            case R.id.fabNuevoLevante: //editar levante
                LevanteGestionarFragment.modo = 0;//Modo nuevo
                editarAnimalLevanteNuevo();
                break;

            case R.id.fabNuevoInsumo: //editar levante
                InsumoGestionarFragment.modo = 0;//Modo nuevo
                editarInsumoNuevo();
                break;

        }
    }

    void iniciarFirebaseListLevante() {
        LevanteFirebase levanteFirebase = new LevanteFirebase();
        levanteFirebase.limpiarLista();
        levanteFirebase.cargarListLevante();
        recibirListLevante = LevanteFirebase.levanteList;
    }

    void iniciarFirebaseListInsumo() {
        InsumoFirebase insumoFirebase = new InsumoFirebase();
        insumoFirebase.limpiarLista();
        insumoFirebase.cargarListInsumo();
        recibirListInsumo = InsumoFirebase.insumoList;
    }


    public void autenticadoUser() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this) //el 1 es para enurar el fragmente
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    //dentro de el evento no pones nada para que no genere ninguna acción en el momento que presionan el botón físico de atrás.
    @Override
    public void onBackPressed() {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation_drawer, menu);

        MenuItem menuItem;
        menuItem = menu.findItem(R.id.action_inicio);

        getMenuInflater().inflate(R.menu.menu_gestionar, menu);
        menuItem = menu.findItem(R.id.action_gestionar_guardar);
        menuItem.setVisible(menu_visible);

        menuItem = menu.findItem(R.id.action_gestionar_nuevo);
        menuItem.setVisible(menu_visible);

        return true;
    }

    public void iconoMenuGestionActivar(boolean visibles) {
        //Titele del toolbar
        //Toast.makeText(this, "ACTIVAR O DESACTIVAR", Toast.LENGTH_SHORT).show();
        menu_visible = visibles; //mostrar el icono de gestion
        this.invalidateOptionsMenu(); // este llamano ejecuta nuevamente onCreateOptionsMenu()
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_inicio) {
            openFragmentControlMenu();
            botonFlotanteMenu(true);
            iconoMenuGestionActivar(false);
            TituloToolbar(getString(R.string.app_name));
        }

        return super.onOptionsItemSelected(item);
    }

    public void botonFlotanteMenu(boolean mostrar) {
        if (mostrar) {
            fabgrupo.setVisibility(View.VISIBLE); //Mostrar o Ocultar el boton flotante
            fabgrupo.expand();

        } else if (!mostrar) {
            fabgrupo.setVisibility(View.INVISIBLE); //Ocultar o Ocultar el boton flotante
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_levante) {
            openFragmentRecyclerLevante();
            botonFlotanteMenu(false);
            iconoMenuGestionActivar(true);

        } else if (id == R.id.nav_insumo) {
            openFragmentRecyclerInsumo();
            botonFlotanteMenu(false);
            iconoMenuGestionActivar(true);
        } else if (id == R.id.nav_agendar) {
            openFragmentAgendar();
            botonFlotanteMenu(false);
            iconoMenuGestionActivar(true);
        } else if (id == R.id.nav_profile) {
            //openFragmentPerfil();
            botonFlotanteMenu(false);
            iconoMenuGestionActivar(true);//mostrar el menu de gestion el toolbar configuracionAppTabbed
            openFragmentUsuario();

            TituloToolbar(getString(R.string.s_users));//Titulo de Toolbar

        } else if (id == R.id.nav_configuration) {
            openFragmentConfigurationTabbed();
        } else if (id == R.id.nav_Sing_off) {
            singOff();
        } else if (id == R.id.nav_about) {
            openFragmentAcercaDe();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void TituloToolbar(String menu) {
        toolbar.setTitle(menu); //Titulo de Toolbar
    }

    private void singOff() {
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

    private void openFragmentConfigurationTabbed() {
        Intent miIntent = new Intent(ActivityNavigationDrawer.this, ConfiguracionAppTabbed.class);
        startActivity(miIntent);
        // finish();
    }


    private void openFragmentRecyclerLevante() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new FragmentListLevanteRecycler()).commit();
    }

    private void openFragmentRecyclerInsumo() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new FragmentListInsumoRecycler()).commit();
    }


    private void openFragmentAgendar() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new AgendarFragment()).commit();
    }


    private void openFragmentUsuario() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new ConfigUsuarioFragment()).commit();
    }


    private void openFragmentAcercaDe() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new AcercadeFragment()).commit();
    }

    public void editarAnimalLevanteNuevo() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new LevanteGestionarFragment()).commit();
    }

    public void editarInsumoNuevo(){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new InsumoGestionarFragment()).commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
