package es.gand.myapplication.perro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.gand.myapplication.R;

/**
 * Created by Gand on 28/11/16.
 */

/**
 * Clase de arrays de la bbdd de la app que contiene a la clase del objeto Perro
 */

public class PerroContent {


    public static final List<Perro> PERROSM = new ArrayList<Perro>();
    public static final List<Perro> PERROSH = new ArrayList<Perro>();
    public static final List<Perro> PERROST = new ArrayList<Perro>();
    public static final List<Perro> PERROSg = new ArrayList<Perro>();
    public static final List<Perro> PERROSm = new ArrayList<Perro>();
    public static final List<Perro> PERROSp = new ArrayList<Perro>();

    /**
     * Mapa por id de los perros
     */
    public static final Map<String, Perro> PERRO_MAP = new HashMap<String, Perro>();


    public static void iniciarBBDD() {
        addItem(new Perro("1", "Anubis", "Norfolk Terrier",true, "2 meses", true,1, R.drawable.perro1 ));
        addItem(new Perro("2", "Bobi", "Bulldog",false, "3 años", true,2, R.drawable.perro2 ));
        addItem(new Perro("3", "Brutus", "Labrador",false, "8 años", false,3, R.drawable.perro3 ));
        addItem(new Perro("4", "Canijo", "Bulldog Francés",false, "2 años", false,1, R.drawable.perro4 ));
        addItem(new Perro("5", "Colin", "West Highland White Terrier",false, "3 meses", true,1, R.drawable.perro5 ));
        addItem(new Perro("6", "Fauno", "Mixto",false, "4 años", true,1, R.drawable.perro6 ));
        addItem(new Perro("7", "Lily", "Jack Russell",true, "5 años", false,2, R.drawable.perro7 ));
        addItem(new Perro("8", "Luna", "Podenco",true, "1 año", true,2, R.drawable.perro9 ));
        addItem(new Perro("9", "Sam", "Cocker Spaniel",true, "6 años", true,2, R.drawable.perro10 ));
        addItem(new Perro("10", "Africa", "Galgo",true, "5 meses", false,3, R.drawable.perro11 ));
        addItem(new Perro("11", "Denver", "Collie",false, "1 año", false,3, R.drawable.perro12 ));
        addItem(new Perro("12", "Felix", "Shar Pei",false, "4 meses", true,3, R.drawable.perro13 ));
        addItem(new Perro("13", "Fuego", "Labrador",false, "3 años", true,3, R.drawable.perro14 ));
        addItem(new Perro("14", "Henry", "Pastor Belga",false, "5 años", false,3, R.drawable.perro15 ));
        addItem(new Perro("15", "Jeff", "Shih Tzu",false, "5 meses", false,1, R.drawable.perro16 ));
        addItem(new Perro("16", "Jenjibre", "Bulldog",false, "8 años", true,2, R.drawable.perro17 ));
        addItem(new Perro("17", "Kathy", "Pug",true, "6 años", true,1, R.drawable.perro18 ));
        addItem(new Perro("18", "Leonor", "Labrador",true, "5 años", false,3, R.drawable.perro19 ));
        addItem(new Perro("19", "Maggi", "American Stafforshire Terrier",true,"8 años", false,2, R.drawable.perro20 ));
        addItem(new Perro("20", "Santa", "Mixto",false, "3 años", true,2, R.drawable.perro21 ));
        addItem(new Perro("21", "Mimo", "American Stafforshire Terrier",false, "2 años", true,2, R.drawable.perro22 ));
        addItem(new Perro("22", "Duende", "Labrador",false, "3 meses", false,3, R.drawable.perro8 ));
        addItem(new Perro("23", "Kia", "Fox Terrier",true, "6 años", false,2, R.drawable.perro23 ));
        addItem(new Perro("24", "Luz", "Mixto (Husky y P. Alemán)",true, "4 años", false,3, R.drawable.perro24 ));
        addItem(new Perro("25", "Nestor", "Pitbull Terrier",false, "1 año", true,3, R.drawable.perro25 ));
        addItem(new Perro("26", "Rolfo", "Pitbull",false, "7 años", true,2, R.drawable.perro26 ));
        addItem(new Perro("27", "Panda", "Dalmata",true, "6 meses", false,3, R.drawable.perro27 ));
        addItem(new Perro("28", "Knut & Betsy", "Springer Spaniel",true, "4 y 6 años", false,3, R.drawable.perro28 ));
        addItem(new Perro("29", "Perdigon", "Pastor Australiano",false, "8 meses", true,2, R.drawable.perro29 ));
        addItem(new Perro("30", "Stich", "Labrador",false, "9 años", true,3, R.drawable.perro30 ));
        addItem(new Perro("31", "Trueno", "Husky",false, "5 años", false,3, R.drawable.perro31 ));
        addItem(new Perro("32", "Willow", "Chihuahua de pelo largo",false, "3 años", false,1, R.drawable.perro32 ));

        for (int i=0; i<PERROST.size();i++){
            if (PERROST.get(i).sexo){
                PERROSH.add(PERROST.get(i));
            }else{
                PERROSM.add(PERROST.get(i));
            }
            if (PERROST.get(i).tamano==1){
                PERROSp.add(PERROST.get(i));
            }if (PERROST.get(i).tamano==2){
                PERROSm.add(PERROST.get(i));
            }if (PERROST.get(i).tamano==3){
                PERROSg.add(PERROST.get(i));
            }
        }
    }//Fin inicar

    private static void addItem(Perro item) {
        PERROST.add(item);
        PERRO_MAP.put(item.getId(), item);
    }


    //Clase perro --------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------
    /**
     * Clase del objeto Perro
     */
    public static class Perro {
        private String id;
        private String nombre;
        private String raza;
        private boolean sexo;
        private String edad;
        private boolean vacuna;
        private String detalles;
        private int foto;
        private int tamano;



        public Perro(String id, String nombre, String raza,boolean sexo, String edad, boolean vacuna,int tamano, int foto ) {
            this.id = id;
            this.nombre = nombre;
            this.raza=raza;
            this.sexo=sexo;
            this.edad=edad;
            this.vacuna=vacuna;
            this.tamano = tamano;
            this.foto=foto;
            this.detalles=generarDetalles(id);
        }



        //Getter------------------------------------------------------------
        public String getId() {            return id;        }
        public String getNombre() {            return nombre;        }
        public String getDetalles() {            return detalles;        }
        public String getEdad() {            return edad;        }
        public int getFoto() {            return foto;        }
        public String getRaza() {            return raza;        }
        public boolean isVacuna() {            return vacuna;        }
        public String isSexo(){ if(!sexo){return "M";} return "H";   }
        public int getTamano() {            return tamano;        }
        //Getter------------------------------------------------------------

        private String generarDetalles(String id) {
            StringBuilder builder = new StringBuilder();
            builder.append("Cosas bonitas del perrete.");
            for (int i = 0; i < Integer.parseInt(id)+10; i++) {
                builder.append("\nCosas bonitas del perrete.");
            }
            return builder.toString();
        }

    }//Fin class Perro

}//Fin class PerroContent
