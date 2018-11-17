package co.edu.udea.compumovil.gr02_20182.proyecto;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.ConfigInsumoFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.ConfigLevanteFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.ConfigurationFragment;
import co.edu.udea.compumovil.gr02_20182.proyecto.Fragment.ConfigUsuarioFragment;


public class ConfiguracionAppTabbed extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    ImageView Photo_edicion_usuario ;

    boolean menu_visible = false;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_app_tabbed);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.s_configuration));

        init();
        setupActionBarRegresar(); //volver Configuracion sistema

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.containerC);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    private void setupActionBarRegresar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onClick(View view) {

        openFragmentUsuairoGestionar();

    }



    public  void init()
    {
        Photo_edicion_usuario = (ImageView) findViewById(R.id.imgProfileEdition);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuracion_app_tabbed, menu);


        getMenuInflater().inflate(R.menu.menu_gestionar, menu);
        MenuItem menuItem;
        menuItem = menu.findItem(R.id.action_gestionar_guardar);
        menuItem.setVisible(menu_visible);

        menuItem = menu.findItem(R.id.action_gestionar_nuevo);
        menuItem.setVisible(menu_visible);

        // searchItem = menu.findItem(R.id.action_search_menu).setVisible(false);


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


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_configuracion_app_tabbed, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    ConfigurationFragment configurationFragment = new ConfigurationFragment();
                    return  configurationFragment;

                case 1:
                    ConfigLevanteFragment configLevanteFragment = new ConfigLevanteFragment();
                    return  configLevanteFragment;

                case 2:
                    ConfigInsumoFragment configInsumoFragment = new ConfigInsumoFragment();
                    return  configInsumoFragment;

            }


            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }



    ///sin uso
    public void iconoMenuGestionActivar(boolean visibles)
    {
        Toast.makeText(this, "PRUEBA XXXX", Toast.LENGTH_SHORT).show();
        menu_visible = visibles; //mostrar el icono de gestion
        this.invalidateOptionsMenu(); // este llamano ejecuta nuevamente onCreateOptionsMenu()
    }


    //visible_menu = true; //mostrar el icono de guardar
    // this.invalidateOptionsMenu(); // este llamano ejecuta nuevamente onCreateOptionsMenu()

    private void openFragmentUsuairoGestionar() {

        //Titele del toolbar
        toolbar.setTitle(getString(R.string.s_users));

        //Ocultar el contenedor de los elementos en el fragmentconfiguracion
        TableLayout tabLayout = (TableLayout) findViewById(R.id.tableLayoutConfiguracion);
        tabLayout.setVisibility(View.INVISIBLE);

        iconoMenuGestionActivar(true);//mostrar el menu de gestion el toolbar configuracionAppTabbed

        //Cargar el fragment Usuario gestion
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container_configuracion, new ConfigUsuarioFragment()).commit();


        // < ----------------Selecionar una pagina del tabbed ------------------------>
        //final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.containerC);
        //viewPager.setCurrentItem(1);

    }


}
