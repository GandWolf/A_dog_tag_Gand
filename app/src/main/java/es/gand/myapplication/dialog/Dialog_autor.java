package es.gand.myapplication.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import es.gand.myapplication.R;

/**
 * Created by Gand on 03/12/16.
 */

/**
 * Clase para mostrar un AlertDialog personalizado con los datos del autor
 * Layout asociada dialog_autor.xml
 */

public class Dialog_autor extends DialogFragment {

    public Dialog_autor() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialogo();
    }

    public AlertDialog createDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_autor, null);

        builder.setView(v);

        Button aceptar = (Button) v.findViewById(R.id.btnAceptar);
        aceptar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss(); //Cierra la ventana (Tambien se puede cancelar pinchando fuera)
                    }
                }
        );

        return builder.create();
    }
}//Fin Dialog_autor