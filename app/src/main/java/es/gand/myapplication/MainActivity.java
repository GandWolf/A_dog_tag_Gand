package es.gand.myapplication;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


/**
 * Activity Main, sólo como pantalla de carga de 2 seg
 */
public class MainActivity extends AppCompatActivity {

    static boolean inicio=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Intent i = new Intent(getApplicationContext(), PerroListActivity.class);
        if (!inicio){ //Para que sólo lo muestre la primera vez y no se pueda volver
            final Handler handler = new Handler(); //Runnable para lanzar acción con Delay
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    inicio=true;
                }
            }, 2000);
        }else {
            startActivity(i);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }

    }//Fin onResume

}//Fin MainActivity