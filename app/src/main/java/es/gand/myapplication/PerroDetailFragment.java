package es.gand.myapplication;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import es.gand.myapplication.perro.PerroContent;


/**
 * Fragmento que muestra los detalles de un objeto Perro
 * Acoplado en modo Master Details junto a la lista de elementos en tablets
 * o modo detalles en la actividad {@link PerroDetailActivity}
 */
public class PerroDetailFragment extends Fragment {

    /**
     * Activity que llama al fragmento
     */
    Activity activity;

    private MediaPlayer mMediaPlayer;
    /**
     * Animatos para el zoom de la foto
     */
    private Animator mCurrentAnimator;
    /**
     * Argumento que le pasa la activity para localizar el Perro a mostrar
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Objeto Perro actual a mostrar
     */
    public static PerroContent.Perro mItem;


    public PerroDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            //Para lo único que uso el Mapa de Perros, podría sustituirlo por una busqueda en el array
            mItem = PerroContent.PERRO_MAP.get(getArguments().getString(ARG_ITEM_ID));

            activity = this.getActivity();
            final CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            //Imagen del DinamicScroll en el coordinatos layout
            final ImageView fotoPerrete=(ImageView)activity.findViewById(R.id.colapsinFoto);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getNombre());
                Typeface puppy=Typeface.createFromAsset(activity.getAssets(),"fonts/puppy.ttf");
                appBarLayout.setExpandedTitleTypeface(puppy);
            }
            if (!PerroListActivity.mTwoPane){//Modo movil
                fotoPerrete.setImageResource(mItem.getFoto());
                fotoPerrete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zoomImage(fotoPerrete, mItem.getFoto(), activity.findViewById(R.id.coordinator));
                    }
                });
            }
        }


    }

    //Datos del objeto Perro, como si fuera un adapter
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.perro_detail, container, false);

        if (mItem != null) {
            final ImageView foto=(ImageView) rootView.findViewById(R.id.imgFotoDt);
            foto.setImageResource(mItem.getFoto());
            foto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zoomImage(foto, mItem.getFoto(), rootView);
                    }

                });

            ((TextView) rootView.findViewById(R.id.tvNombreDt)).setText(mItem.getNombre());
            ((TextView) rootView.findViewById(R.id.tvRazaDt)).setText(mItem.getRaza());
            ((TextView) rootView.findViewById(R.id.tvSexoDt)).setText(mItem.isSexo());
            ((TextView) rootView.findViewById(R.id.tvEdadDt)).setText(mItem.getEdad());
            ((CheckBox) rootView.findViewById(R.id.cbVacunaDt)).setChecked(mItem.isVacuna());
            ((TextView) rootView.findViewById(R.id.tvDetallesDt)).setText(mItem.getDetalles());

            final ImageButton contacto = (ImageButton)rootView.findViewById(R.id.ibContacto);
            contacto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(rootView, "\"Guau\", \"Guau\" (Sonido de ladrido)", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    releaseMediaPlayer();
                    mMediaPlayer = MediaPlayer.create(activity.getApplicationContext(), R.raw.perro_ladrar);
                    mMediaPlayer.start();

                    Animation llamada = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.rotate2);
                    contacto.setImageResource(R.drawable.ic_phone_in_talk_black_24dp);
                    contacto.startAnimation(llamada);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contacto.setImageResource(R.drawable.ic_call_black_24dp);
                        }
                    },3000);

                    Toast.makeText(activity.getApplicationContext(),"¡Gracias por usar A-Dog-TaG by Gand!\n" +
                            "Contacte con nosotros tlf:950999999", Toast.LENGTH_LONG).show();
                }
                private void releaseMediaPlayer() {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }
                }
            });
        }//Fin mItem!=null

        return rootView;
    }//Fin onCreateView


    //Método para zoom de imagen
    //Calcula la posición origen, calcula trayectorio de origen a destino y amplía la imagen
    private void zoomImage(final View imageOrigen, int imageResId, View rootView) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) rootView.findViewById(R.id.imgFotoDtG);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail, and the
        // final bounds are the global visible rectangle of the container view. Also
        // set the container view's offset as the origin for the bounds, since that's
        // the origin for the positioning animation properties (X, Y).
        imageOrigen.getGlobalVisibleRect(startBounds);
        rootView.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
        // "center crop" technique. This prevents undesirable stretching during the animation.
        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
        // it will position the zoomed-in view in the place of the thumbnail.
        imageOrigen.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
        // the zoomed-in view (the default is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and scale properties
        // (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(750);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down to the original bounds
        // and show the thumbnail instead of the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel, back to their
                // original values.
                AnimatorSet set = new AnimatorSet();
                set
                        .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(300);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        imageOrigen.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        imageOrigen.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }//Fin Zoom
}
