package xyz.carlesllobet.pullmarket.Domain;

import java.util.ArrayList;

/**
 * Created by CarlesLlobet on 31/01/2016.
 */
public class Llista {

    //private variables
    private static Llista instancia = null;
    private ArrayList<Product> llista = null;
    private ArrayList<Integer> cantitats = null;

    protected Llista() {
        llista = new ArrayList<Product>();
        cantitats = new ArrayList<Integer>();
        // Exists only to defeat instantiation.
    }

    public static Llista getInstance() {
        if (instancia == null) {
            instancia = new Llista();
        }
        return instancia;
    }

    public void addProduct(Product p) {
        if (llista.contains(p)){
            Integer c = cantitats.get(llista.indexOf(p));
            cantitats.set(llista.indexOf(p),c+1);
        }else{
            llista.add(p);
            cantitats.add(1);
        }
    }

    // getting name
    public ArrayList<Product> getAllProducts(){
        return this.llista;
    }

    public ArrayList<Integer> getAllCants(){
        return this.cantitats;
    }

    public void borraUn(int pos){
        if (cantitats.get(pos)>1){
            Integer c = cantitats.get(pos);
            cantitats.set(pos,c-1);
        }else{
            llista.remove(pos);
            cantitats.remove(pos);
        }
    }
    public void borrarLlista() {llista = new ArrayList<Product>();}

    public Double getPreuTotal(){
        Double res = 0.00;
        for (int i = 0; i < llista.size(); ++i){
            res += llista.get(i).getPreu();
        }
        return res;
    }
}
