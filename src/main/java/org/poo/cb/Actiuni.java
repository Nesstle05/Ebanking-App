package org.poo.cb;

import java.util.ArrayList;

public class Actiuni {
    String numeCompanie;
    ArrayList<Double> valoriInUltimele10Zile;
    int numar; // numarul de actiuni pe care le detine un client

    public Actiuni(String numeCompanie){
        //constructorul clasei
        this.numeCompanie = numeCompanie;
        valoriInUltimele10Zile = new ArrayList<Double>();
        this.numar = 0;

    }
    //am creat getteri si setteri pentru a putea accesa si modifica atributele private
    public void setNumar(int numar){
        this.numar = numar;
    }
    public int getNumar(){
        return this.numar;
    }

    public String getNumeCompanie(){
        return this.numeCompanie;
    }

    public ArrayList<Double> getValoriInUltimele10Zile(){
        return this.valoriInUltimele10Zile;
    }
    public void setValoriInUltimele10Zile(ArrayList<Double> valoriInUltimele10Zile){
        this.valoriInUltimele10Zile = valoriInUltimele10Zile;
    }
    public double shortTermSMA(){
        //metoda care claculeaza SMA pentru valorile stocului din ultimele 5 zile
        double suma = 0;
        for(int i = 5; i < 10; i++){
            suma += this.valoriInUltimele10Zile.get(i);
        }
        return suma / 5;
    }
    public double longTermSMA(){
        //metoda care claculeaza SMA pentru valorile stocului din ultimele 10 zile
        double suma = 0;
        for(int i = 0; i < 10; i++){
            suma += this.valoriInUltimele10Zile.get(i);
        }
        return suma / 10;
    }
    public double getUltimaValoare(){
        //metoda care ia cea mai recenta valoare a stocului
        return this.valoriInUltimele10Zile.get(9);
    }
}