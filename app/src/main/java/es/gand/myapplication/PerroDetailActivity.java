package es.gand.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Activity que representa los detalles de un único Perro
 * Activity sólo para pantallas estrechas, en tablets se carga junto a la lista de item mediante
 * fragmentos
 */
public class PerroDetailActivity extends AppCompatActivity {

    CoordinatorLayout coordinator;
    private MediaPlayer mMediaPlayer; //Para reproducir sonido de ladridos


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perro_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        coordinator=(CoordinatorLayout)findViewById(R.id.coordinator);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "\"Guau\", \"Guau\" (Sonido de ladrido)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); //Por si no está activado el sonido

                releaseMediaPlayer(); //Para reproducción anterior si la hay
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.perro_ladrar);
                mMediaPlayer.start();

                final Animation zoomAnime = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);//Animación de zoom
                final Animation zoomLatido = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.latido);
                final Animation llamada = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate2);
                final ImageView fotop=(ImageView) findViewById(R.id.adog);

                fab.setImageResource(R.drawable.ic_phone_in_talk_black_24dp);
                fab.startAnimation(llamada);

                fotop.startAnimation(zoomAnime);
                fotop.setVisibility(View.VISIBLE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fotop.startAnimation(zoomLatido);
                        final Handler handler = new Handler();//Lanzamos la animacion de Fade al terminar el zoom
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Fade fade =  new Fade();
                                fade.setDuration(500);
                                TransitionManager.beginDelayedTransition(coordinator,fade);
                                fotop.setVisibility(View.GONE);
                                fab.setImageResource(R.drawable.ic_call_black_24dp);
                            }
                        },1600);
                    }
                },1800);

                Toast.makeText(getApplicationContext(),"¡Gracias por usar A-Dog-TaG by Gand!\n" +
                        "Contacte con nosotros tlf:950999999", Toast.LENGTH_LONG).show();
            }
        });

        // Para mostrar el botón volver en la toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html

        //Lo de arriba es muy bonito, pero si cambias la rotación de la pantalla casi siempre
        //da fallo
        if (savedInstanceState == null) {
            // En caso de no estar en una tablet y no tener datos anteriores de fragmentos,
            //creamos una nueva instancia del fragmento y lo lanzamos cmo una activity
            //mediante un Transaction()
            Bundle arguments = new Bundle();
            arguments.putString(PerroDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PerroDetailFragment.ARG_ITEM_ID));
            PerroDetailFragment fragment = new PerroDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.perro_detail_container, fragment)
                    .commit();
        }
    }//Fin onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, PerroListActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.from(this)
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}//Fin PerroDetailActivity
