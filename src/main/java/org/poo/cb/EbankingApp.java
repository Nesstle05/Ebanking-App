package org.poo.cb;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class EbankingApp {
    //clasa aplicatiei noastre de ebanking
    public static ArrayList<Utilizator> utilizatori;
    public static ArrayList<Actiuni>  listaStocks;
    //lista de stocuri disponibile pentru a putea accesa mai usor pretul lor

    private static EbankingApp instance;
    //utilizam Singleton pentru a ne asigura ca avem o singura instanta a aplicatiei
    public EbankingApp() {
        //initializam listele de utilizatori si stocuri
        utilizatori = new ArrayList<Utilizator>();
        listaStocks = new ArrayList<Actiuni>();
    }

    public static EbankingApp getInstance() {
        if (instance == null) {
            instance = new EbankingApp();
        }
        return instance;
    }
    public void clean() {
        //metoda care sterge toate datele din aplicatie pentru a ne asigura ca nu avem date vechi
        utilizatori = null;
        instance = null;
        listaStocks = null;
    }

    public Utilizator existaUtilizator(String email) {
        //verificam daca exista utilizatorul cu emailul dat
        for(Utilizator utilizator : utilizatori) {
            if(utilizator.getEmail().equals(email)) {
                return utilizator;
            }
        }
        return null;
    }
    public void adaugaUtilizator(Utilizator utilizator) {
        //adaugam noi utilizatori in ArrayList
        utilizatori.add(utilizator);
    }
    public boolean areCont(String email, String tipValuta) {
        //verificam daca utilizatorul cu emailul dat are un cont in valuta data
        for (Cont cont : existaUtilizator(email).getConturi()) {
            if (cont.getTipValuta().equals(tipValuta)) {
                return true;
            }
        }
        return false;
    }
    public void adaugaBani(String email, String tipValuta, double suma) {
        //metoda care adauga bani in contul utilizatorului cu emailul dat
        for (Cont cont : existaUtilizator(email).getConturi()) {
            if (cont.getTipValuta().equals(tipValuta)) {
                cont.setSuma(cont.getSuma() + suma);
            }
        }
    }

    public void adaugaInListaStocks(Actiuni actiune) {
        //metoda care adauga un nou stoc in lista de stocuri
        listaStocks.add(actiune);
    }

}
