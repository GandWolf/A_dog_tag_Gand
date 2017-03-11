package es.gand.myapplication;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import es.gand.myapplication.dialog.Dialog_autor;
import es.gand.myapplication.dialog.Dialog_licencias;
import es.gand.myapplication.perro.PerroContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity principal, muestra una lista de Perros en un GridView
 * Carga de fragment en panel lateral en dispositivos tablet para detalles.
 * Implementa menú de navegación lateral.
 */
public class PerroListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Determina si la activity está o no en modo dos paneles para cargar fragment
     */
    static boolean mTwoPane;
    View gridView;

    /**
     * Para controlar la visualizacion de los botones fab
     */
    boolean fabClik=false;
    FloatingActionButton fab, fabT, fabM, fabH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_perro_list); //Activity sin menú lateral
        setContentView(R.layout.activity_perro_list_menu);

        //Inicializa la bbdd sólo una vez
        if (PerroContent.PERROST.size() == 0)
            PerroContent.iniciarBBDD();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        botonesFAB();

        lanzarGrid(PerroContent.PERROST);

        if (findViewById(R.id.perro_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        //Menú de navegación
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }//Fin OnCreate

    private void botonesFAB() {
        fabT = (FloatingActionButton) findViewById(R.id.fabTodo);
        fabT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarGrid(PerroContent.PERROST);
                clikFAB();
            }
        });//Fin FABT
        fabM = (FloatingActionButton) findViewById(R.id.fabMacho);
        fabM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarGrid(PerroContent.PERROSM);
                clikFAB();
            }
        });//Fin FABM
        fabH = (FloatingActionButton) findViewById(R.id.fabHembra);
        fabH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarGrid(PerroContent.PERROSH);
                clikFAB();
            }
        });//Fin FABH

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clikFAB();
            }
        });//Fin FAB
    }//Fin botonesFAB

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /**
     * Menú de navegación lateral con seleccion de Perros por tamaño
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_grande) {
            lanzarGrid(PerroContent.PERROSg);
        } else if (id == R.id.nav_medio) {
            lanzarGrid(PerroContent.PERROSm);
        } else if (id == R.id.nav_peque) {
            lanzarGrid(PerroContent.PERROSp);
        } else if (id == R.id.nav_todo) {
            lanzarGrid(PerroContent.PERROST);
        } else if (id == R.id.nav_autor) {
            Dialog_autor dialogo= new Dialog_autor();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialogo.show(ft, "Autor");
        }else if (id == R.id.nav_licencias) {
            Dialog_licencias dialogo= new Dialog_licencias();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialogo.show(ft, "Licencias");
        }
            ViewGroup list = (ViewGroup) findViewById(android.R.id.content);
            Slide slideR = new Slide();
            slideR.setSlideEdge(Gravity.RIGHT);
            TransitionManager.beginDelayedTransition(list, slideR);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void clikFAB() {
        Slide slideT = new Slide();
        Slide slideB = new Slide();
        slideT.setSlideEdge(Gravity.TOP);
        slideT.setSlideEdge(Gravity.BOTTOM);

        ViewGroup list = (ViewGroup) findViewById(android.R.id.content);
        if (!fabClik) {
            TransitionManager.beginDelayedTransition(list, slideB);
            fabT.setVisibility(View.VISIBLE);
            fabM.setVisibility(View.VISIBLE);
            fabH.setVisibility(View.VISIBLE);
            fab.setImageResource(R.drawable.ic_close_black_24dp);
            fabClik = true;
        } else {
            TransitionManager.beginDelayedTransition(list, slideT);
            fabT.setVisibility(View.GONE);
            fabM.setVisibility(View.GONE);
            fabH.setVisibility(View.GONE);
            fab.setImageResource(R.drawable.ic_add_black_24dp);
            fabClik = false;
        }
        Animation rotateAnime = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate);
        fab.setAnimation(rotateAnime);
        fab.animate();
    }

    public void lanzarGrid(List<PerroContent.Perro> perros) {
        gridView = findViewById(R.id.perro_list);
        assert gridView != null;
        setupGridView((GridView) gridView, perros);
    }

    private void setupGridView(@NonNull GridView gridView, List<PerroContent.Perro> perros) {
        gridView.setAdapter( new ImageAdapter(this, perros));
    }

    /**
     * Adapter para el GridView con la lista de Objetos Perro
     */
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private List<PerroContent.Perro> PERROS=new ArrayList<>();

        public ImageAdapter(Context c, List<PerroContent.Perro> perros) {
            mContext = c;
            PERROS=perros;
        }
        public int getCount() {
            return PERROS.size();
        }
        public PerroContent.Perro getItem(int position) {
            return PERROS.get(position);
        }
        public long getItemId(int position) {
            return (long)Integer.parseInt(PERROS.get(position).getId());
        }
        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View vista, ViewGroup parent) {
            if (vista==null){
                vista= LayoutInflater.from(mContext).inflate(R.layout.perro_list_content,null);
            }
            final PerroContent.Perro currentPerro= PERROS.get(position);

            ImageView foto=(ImageView)vista.findViewById(R.id.imgFoto);
            foto.setImageResource(currentPerro.getFoto());

            //Si hay dos paneles lanza detalles al panel lateral, si no, llama al fragmento como
            //nueva activity
            foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(PerroDetailFragment.ARG_ITEM_ID, currentPerro.getId());
                        PerroDetailFragment fragment = new PerroDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.perro_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent i = new Intent(context, PerroDetailActivity.class);
                        i.putExtra(PerroDetailFragment.ARG_ITEM_ID, currentPerro.getId());

                        context.startActivity(i);
                    }
                }
            });

            TextView nombre=(TextView)vista.findViewById(R.id.tvNombre);
            nombre.setText(currentPerro.getNombre());
            Typeface puppy=Typeface.createFromAsset(getAssets(),"fonts/puppy.ttf");
            nombre.setTypeface(puppy);

            return vista;
        }
    }//Fin ImageAdapter

}//Fin Activity
